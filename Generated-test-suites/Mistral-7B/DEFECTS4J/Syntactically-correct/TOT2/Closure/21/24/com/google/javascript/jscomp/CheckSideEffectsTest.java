import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CheckSideEffects checkSideEffects;
 private Node emptyNode;
 private Node expressionResultNode;
 private Node blockNode;
 private Node commaNode;
 private Node stringNode;
 private Node simpleOpNode;
 private Node parentNode;
 private Node forNode;

 @Before
 public void setUp() {
 compiler = mock(Compiler.class);
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 emptyNode = Node.createEmpty();
 expressionResultNode = Node.createExpressionResult();
 blockNode = Node.createBlock();
 commaNode = Node.createComma();
 stringNode = Node.createString("test");
 simpleOpNode = Node.createBinaryOp(Token.OP_ADD, Node.createString("test"), Node.createString("test"));
 parentNode = Node.createBlock();
 forNode = Node.createFor(Node.createIdentifier("i"), Node.createExpression(Node.createIntLiteral(0)), Node.createExpression(Node.createIntLiteral(1)), Node.createBlock());
 }

 // Test for empty node
 @Test
 public void testEmptyNode() {
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), emptyNode, parentNode);
 // No error expected
 }

 // Test for expression result node
 @Test
 public void testExpressionResultNode() {
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), expressionResultNode, parentNode);
 // No error expected
 }

 // Test for block node
 @Test
 public void testBlockNode() {
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), blockNode, parentNode);
 // No error expected
 }

 // Test for comma node
 @Test
 public void testCommaNode() {
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), commaNode, parentNode);
 // No error expected
 }

 // Test for string node with missing '+'
 @Test
 public void testStringNodeMissingPlus() {
 Node stringNode1 = Node.createString("test");
 Node stringNode2 = Node.createString("test");
 Node concatenationNode = Node.createBinaryOp(Token.OP_ADD, stringNode1, stringNode2);
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), concatenationNode, parentNode);
 // Warning expected: "Is there a missing '+' on the previous line?"
 }

 // Test for simple operator node
 @Test
 public void testSimpleOpNode() {
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), simpleOpNode, parentNode);
 // Warning expected: "The result of the '+' operator is not being used."
 }

 // Test for parent node with unused expression result
 @Test
 public void testParentNodeWithUnusedExpressionResult() {
 Node unusedExpressionResultNode = Node.createExpressionResult();
 parentNode.addChildToBack(unusedExpressionResultNode);
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), parentNode, null);
 // No error expected
 }

 // Test for parent node with unused result in for loop
 @Test
 public void testParentNodeWithUnusedResultInForLoop() {
 parentNode.addChildToBack(simpleOpNode);
 forNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), forNode, null);
 // Warning expected: "The result of the '+' operator is not being used."
 }

 // Test for parent node with expression result used in for loop
 @Test
 public void testParentNodeWithExpressionResultUsedInForLoop() {
 parentNode.addChildToBack(simpleOpNode);
 forNode.addChildToBack(expressionResultNode);
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), forNode, null);
 // No error expected
 }

 // Test for parent node with expression result used outside of for loop
 @Test
 public void testParentNodeWithExpressionResultUsedOutsideForLoop() {
 parentNode.addChildToBack(simpleOpNode);
 parentNode.addChildToBack(expressionResultNode);
 checkSideEffects.visit(NodeTraversal.getInstance(compiler), parentNode, null);
 // No error expected
 }
}