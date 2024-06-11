import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

 private Compiler compiler;
 private UnreachableCodeElimination elimination;

 @Before
 public void setUp() {
 compiler = new Compiler();
 elimination = new UnreachableCodeElimination(compiler, false);
 }

 @Test
 public void testSimpleReturn() {
 Node root = Node.newBlock(null, null);
 root.addChild(Node.newReturn(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(0, root, "After unreachable code elimination");
 }

 @Test
 public void testSimpleNoOpStatement() {
 Node root = Node.newBlock(null, null);
 root.addChild(Node.newExpressionStatement(Node.newName("a"), null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(0, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCode() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 func.addChild(Node.newReturn(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(1, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndReturnStatement() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 func.addChild(Node.newReturnStatement(null, null));
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(1, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndConditionalBranch() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 func.addChild(Node.newIfStatement(Node.newExpressionStatement(Node.newName("false"), null), null, null));
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(1, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithUnreachableBranch() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 func.addChild(Node.newIfStatement(Node.newExpressionStatement(Node.newName("true"), null), null, null));
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(2, root, "After unreachable code elimination");
 Node unreachableBranch = NodeUtil.findChildByName(func, "if");
 assertFalse("Unreachable branch should be removed",
 unreachableBranch.getParent() != null);
 }

 @Test
 public void testFunctionWithDeadCodeAndTryCatchBlock() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 func.addChild(Node.newTryStatement(null, null, null, null, null, null));
 func.addChild(Node.newCatchClause(null, null, null, null, null));
 func.addChild(Node.newReturnStatement(null, null));
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(3, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndSwitchStatement() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 func.addChild(Node.newSwitchStatement(null, null, null));
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(3, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithMultipleDeadCodeAndConditionalBranches() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 Node ifStmt1 = Node.newIfStatement(Node.newExpressionStatement(Node.newName("true"), null), null, null);
 func.addChild(ifStmt1);
 func.addChild(Node.newExpressionStatement(Node.newName("y1"), null));
 Node ifStmt2 = Node.newIfStatement(Node.newExpressionStatement(Node.newName("false"), null), null, null);
 func.addChild(ifStmt2);
 func.addChild(Node.newExpressionStatement(Node.newName("y2"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(3, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndExceptionHandling() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 Node tryStmt = Node.newTryStatement(null, null, null, null, null, null);
 func.addChild(tryStmt);
 Node catchClause = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt = Node.newReturnStatement(null, null);
 catchClause.addChild(returnStmt);
 tryStmt.addChild(catchClause);
 func.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(3, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndExceptionHandlingAndConditionalBranches() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 Node ifStmt1 = Node.newIfStatement(Node.newExpressionStatement(Node.newName("true"), null), null, null);
 func.addChild(ifStmt1);
 Node catchClause = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt = Node.newReturnStatement(null, null);
 Node tryStmt = Node.newTryStatement(null, null, null, null, null, null);
 func.addChild(tryStmt);
 catchClause.addChild(returnStmt);
 tryStmt.addChild(catchClause);
 func.addChild(Node.newExpressionStatement(Node.newName("y1"), null));
 Node ifStmt2 = Node.newIfStatement(Node.newExpressionStatement(Node.newName("false"), null), null, null);
 func.addChild(ifStmt2);
 func.addChild(Node.newExpressionStatement(Node.newName("y2"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(4, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndExceptionHandlingAndNestedTryCatchBlocks() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 Node tryStmt1 = Node.newTryStatement(null, null, null, null, null, null);
 func.addChild(tryStmt1);
 Node catchClause1 = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt1 = Node.newReturnStatement(null, null);
 catchClause1.addChild(returnStmt1);
 tryStmt1.addChild(catchClause1);
 Node tryStmt2 = Node.newTryStatement(null, null, null, null, null, null);
 func.addChild(tryStmt2);
 Node catchClause2 = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt2 = Node.newReturnStatement(null, null);
 catchClause2.addChild(returnStmt2);
 tryStmt2.addChild(catchClause2);
 func.addChild(Node.newExpressionStatement(Node.newName("y1"), null));
 Node catchClause3 = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt3 = Node.newReturnStatement(null, null);
 catchClause3.addChild(returnStmt3);
 tryStmt1.addChild(catchClause3);
 func.addChild(Node.newExpressionStatement(Node.newName("y2"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(4, root, "After unreachable code elimination");
 }

 @Test
 public void testFunctionWithDeadCodeAndExceptionHandlingAndNestedTryCatchBlocksAndConditionalBranches() {
 Node root = Node.newBlock(null, null);
 Node func = Node.newFunction(null, null, null, null, null);
 root.addChild(func);
 func.addChild(Node.newName("x"));
 Node ifStmt1 = Node.newIfStatement(Node.newExpressionStatement(Node.newName("true"), null), null, null);
 func.addChild(ifStmt1);
 Node tryStmt1 = Node.newTryStatement(null, null, null, null, null, null);
 func.addChild(tryStmt1);
 Node catchClause1 = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt1 = Node.newReturnStatement(null, null);
 catchClause1.addChild(returnStmt1);
 tryStmt1.addChild(catchClause1);
 func.addChild(Node.newExpressionStatement(Node.newName("y1"), null));
 Node ifStmt2 = Node.newIfStatement(Node.newExpressionStatement(Node.newName("false"), null), null, null);
 func.addChild(ifStmt2);
 Node tryStmt2 = Node.newTryStatement(null, null, null, null, null, null);
 func.addChild(tryStmt2);
 Node catchClause2 = Node.newCatchClause(null, null, null, null, null);
 Node returnStmt2 = Node.newReturnStatement(null, null);
 catchClause2.addChild(returnStmt2);
 tryStmt2.addChild(catchClause2);
 func.addChild(Node.newExpressionStatement(Node.newName("y2"), null));
 func.addChild(Node.newReturnStatement(null, null));
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 elimination.process(null, root);
 NodeUtil.assertNodeCount(5, root, "After unreachable code elimination");
 }
}