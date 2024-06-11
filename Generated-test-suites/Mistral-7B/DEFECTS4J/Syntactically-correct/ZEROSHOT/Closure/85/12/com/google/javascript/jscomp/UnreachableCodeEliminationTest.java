import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private UnreachableCodeElimination elimination;

 @Before
 public void setup() {
 compiler = mock(AbstractCompiler.class);
 elimination = new UnreachableCodeElimination(compiler, false);
 }

 @Test
 public void testUnreachableCodeRemovalWhenThereIsNoDeadCode() {
 Node rootNode = createTestNode();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertEquals("function foo() {}\n", NodePrinter.printNode(rootNode));
 }

 @Test
 public void testUnreachableCodeRemovalWhenThereIsDeadCode() {
 Node rootNode = createTestNodeWithDeadCode();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertNull(findNodeWithValue("return; alert(\"unreachable\");", rootNode));
 }

 @Test
 public void testRemoveUnconditionalBranching() {
 Node rootNode = createTestNodeWithUnconditionalBranching();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertEquals("function foo() {\n" +
         "  if (true) {\n" +
         "    break;\n" +
         "  } else {\n" +
         "  }\n" +
         "  break;\n" +
         "}", NodePrinter.printNode(rootNode));
 }

 @Test
 public void testRemoveUnreachableFunctionCall() {
 Node rootNode = createTestNodeWithUnreachableFunctionCall();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertNull(findNodeWithValue("alert(\"unreachable\");", rootNode));
 }

 @Test
 public void testRemoveNoOpStatement() {
 Node rootNode = createTestNodeWithNoOpStatement();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertNull(findNodeWithValue("true;", rootNode));
 }

 @Test
 public void testRemoveDeadBlock() {
 Node rootNode = createTestNodeWithDeadBlock();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertNull(findNodeWithValue("if (false) {\n" +
         "  return;\n" +
         "}", rootNode));
 }

 @Test
 public void testRemoveDeadBlockWithNoOpStatement() {
 Node rootNode = createTestNodeWithDeadBlockAndNoOpStatement();
 NodeTraversal traversal = new NodeTraversal(rootNode, null);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(compiler, rootNode, elimination);
 assertNull(findNodeWithValue("if (false) {\n" +
         "  return;\n" +
         "  true;\n" +
         "}", rootNode));
 }

 private Node createTestNode() {
 return new Node(Token.FUNCTION, "foo", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 }

 private Node createTestNodeWithDeadCode() {
 Node rootNode = createTestNode();
 Node ifNode = new Node(Token.IF, null, null, null, null, rootNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node returnNode = new Node(Token.RETURN, null, null, null, null, ifNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node alertNode = new Node(Token.CALL, "alert", new Node[]{new Node(Token.STRING, "unreachable")}, null, null, rootNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node blockNode = new Node(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node ifCond = new Node(Token.BOOL_LITERAL, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node ifStat = new Node(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node returnStatement = new Node(Token.RETURN, null, null, null, null, ifStat, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node alertStatement = new Node(Token.CALL, "alert", new Node[]{new Node(Token.STRING, "unreachable")}, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node elseStat = new Node(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node emptyStat = new Node(Token.EMPTY, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node elseCond = new Node(Token.BOOL_LITERAL, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node emptyCond = new Node(Token.BOOL_LITERAL, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 blockNode.setChildren(ifNode, elseStat);
 ifNode.setChildren(ifCond, ifStat, alertStatement);
 ifStat.setChildren(returnStatement);
 elseStat.setChildren(emptyStat);
 rootNode.setChildren(blockNode);
 return rootNode;
 }

 private Node createTestNodeWithUnconditionalBranching() {
 Node rootNode = createTestNode();
 Node ifNode = new Node(Token.IF, null, null, null, null, rootNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node ifCond = new Node(Token.BOOL_LITERAL, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node breakNode = new Node(Token.BREAK, null, null, null, null, ifNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node elseNode = new Node(Token.ELSE, null, null, null, null, ifNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node emptyStat = new Node(Token.EMPTY, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 elseNode.setChildren(emptyStat);
 rootNode.setChildren(ifNode);
 return rootNode;
 }

 private Node createTestNodeWithUnreachableFunctionCall() {
 Node rootNode = createTestNode();
 Node callNode = new Node(Token.CALL, "alert", new Node[]{new Node(Token.STRING, "unreachable")}, null, null, rootNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node emptyStat = new Node(Token.EMPTY, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 rootNode.setChildren(callNode, emptyStat);
 return rootNode;
 }

 private Node createTestNodeWithNoOpStatement() {
 Node rootNode = createTestNode();
 Node trueNode = new Node(Token.BOOL_LITERAL, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node emptyStat = new Node(Token.EMPTY, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 rootNode.setChildren(new Node(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, trueNode), emptyStat);
 return rootNode;
 }

 private Node createTestNodeWithDeadBlock() {
 Node rootNode = createTestNode();
 Node ifNode = new Node(Token.IF, null, null, null, null, rootNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node ifCond = new Node(Token.BOOL_LITERAL, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node blockNode = new Node(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 rootNode.setChildren(ifNode);
 ifNode.setChildren(ifCond, blockNode);
 if (logger.isLoggable(Level.FINE)) {
 logger.fine("If condition is false.");
 }
 return rootNode;
 }

 private Node createTestNodeWithDeadBlockAndNoOpStatement() {
 Node rootNode = createTestNodeWithDeadBlock();
 Node trueNode = new Node(Token.BOOL_LITERAL, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Node emptyStat = new Node(Token.EMPTY, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 rootNode.setChildren(new Node(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, trueNode), emptyStat);
 return rootNode;
 }

 private Node findNodeWithValue(String value, Node node) {
 Node currentNode = node;
 while (currentNode != null) {
 if (currentNode.getValue().getText().equals(value)) {
 return currentNode;
 }
 currentNode = currentNode.getNext();
 }
 return null;
 }
}