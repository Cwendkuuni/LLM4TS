import static com.google.javascript.jscomp.NodeTraversal.JSTYPE.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private Node rootNode;

 @Before
 public void setUp() {
 compiler = new CompilerOptions().createCompiler();

 rootNode = Node.newBlock("test");
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newExpressionStatement(Node.newPrint("unreachable")));
 }

 @Test
 public void testEmptyFunction() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertFalse(rootNode.getLastChild().isNotNull());
 }

 @Test
 public void testRemoveBlockStatement() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNull());
 }

 @Test
 public void testRemoveFunctionStatement() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());
 rootNode.getLastChild().addChild(Node.newFunctionDeclaration("test", null, null, Node.newBlock("test")));

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertTrue(rootNode.getLastChild().getLastChild().isNull());
 }

 @Test
 public void testRemoveVariableDeclaration() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newVariable("x"));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());
 rootNode.getLastChild().addChild(Node.newVariable("x"));

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 }

 @Test
 public void testRemoveVariableDeclarationWithInitializer() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newVariable("x", Node.newNumberLiteral(1)));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());
 rootNode.getLastChild().addChild(Node.newVariable("x", Node.newNumberLiteral(2)));

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 assertFalse(NodeUtil.hasSideEffects(rootNode.getFirstChild()));
 }

 @Test
 public void testRemoveAssignment() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"), Node.newNumberLiteral(1))));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 }

 @Test
 public void testRemoveCall() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newExpressionStatement(Node.newCall(Node.newIdentifier("print"), Node.newStringLiteral("Hello, World!"))));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 }

 @Test
 public void testRemoveStatementWithSideEffects() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newExpressionStatement(Node.newPrint("Hello, World!")));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 assertTrue(NodeUtil.hasSideEffects(rootNode.getFirstChild()));
 }

 @Test
 public void testRemoveConditionalBranch() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newExpressionStatement(Node.newIf(Node.newIdentifier("x").get(0), Node.newExpressionStatement(Node.newPrint("Branch 1")), Node.newExpressionStatement(Node.newPrint("Branch 2")))));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 }

 @Test
 public void testRemoveLoop() {
 rootNode.setType(SCRIPT);
 rootNode.addChild(Node.newLabel("start"));
 rootNode.addChild(Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"), Node.newNumberLiteral(1))));
 rootNode.addChild(Node.newIf(Node.newIdentifier("x").get(0), Node.newExpressionStatement(Node.newBreak("start")), Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"), Node.newAdd(Node.newVariable("x"), Node.newNumberLiteral(1))), Node.newExpressionStatement(Node.newPrint("Loop " + Node.newIdentifier("x").get(0))), Node.newExpressionStatement(Node.newGoto("start")))));
 rootNode.addChild(Node.newExpressionStatement(Node.newReturn(null)));
 rootNode.addChild(Node.newStatementList());

 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
 traversal.traverse(elimination);

 assertTrue(rootNode.isNotNull());
 assertTrue(rootNode.getFirstChild().isNotNull());
 assertTrue(rootNode.getLastChild().isNotNull());
 assertFalse(rootNode.getLastChild().getLastChild().isNotNull());
 }
}