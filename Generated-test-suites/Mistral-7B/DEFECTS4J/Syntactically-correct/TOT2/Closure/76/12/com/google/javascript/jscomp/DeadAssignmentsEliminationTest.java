import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.AbstractCompiler.*;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.DeadAssignmentsElimination.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination.Node;
import com.google.javascript.jscomp.DeadAssignmentsElimination.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

 private DeadAssignmentsElimination deadAssignmentsElimination;
 private ControlFlowGraph cfg;
 private Scope scope;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 nodeFactory = NodeFactory.getInitialNodeFactory();
 scope = new Scope();
 deadAssignmentsElimination = new DeadAssignmentsElimination(new AbstractCompiler());
 }

 @Test
 public void testEmptyProgram() {
 Node root = nodeFactory.createRoot();
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 }

 @Test
 public void testSingleAssignment() {
 // Single assignment
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createNumber(1);
 Node n3 = nodeFactory.createAssign(Token.ASSIGN, n1, n2);
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n3);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n3.isPresentInTree());
 }

 @Test
 public void testMultipleAssignments() {
 // Multiple assignments
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createNumber(1);
 Node n3 = nodeFactory.createNumber(2);
 Node n4 = nodeFactory.createAssign(Token.ASSIGN, n1, n2);
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n1, n3);
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n4);
 root.appendChild(n5);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideIfStatement() {
 // Assignment inside if statement
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createNumber(1);
 Node n3 = nodeFactory.createName("y");
 Node n4 = nodeFactory.createBooleanLiteral(true);
 Node n5 = nodeFactory.createIf(n4, null, null);
 Node n6 = nodeFactory.createAssign(Token.ASSIGN, n1, n2);
 Node n7 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createName("x"));
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n5);
 root.appendChild(n6);
 root.appendChild(n7);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n7.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideForStatement() {
 // Assignment inside for statement
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createNumber(1);
 Node n3 = nodeFactory.createName("i");
 Node n4 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createNumber(0));
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n1, n2);
 Node n6 = nodeFactory.createFor(Token.FOR, null, null, null, null, null, null);
 Node n7 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createAdd(nodeFactory.createName("i"), nodeFactory.createNumber(1)));
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n6);
 root.appendChild(n4);
 root.appendChild(n5);
 root.appendChild(n7);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideSwitchStatement() {
 // Assignment inside switch statement
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createNumber(1);
 Node n3 = nodeFactory.createName("y");
 Node n4 = nodeFactory.createName("i");
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createName("x"));
 Node n6 = nodeFactory.createSwitch(null, null);
 Node case1 = nodeFactory.createCase(Token.CASE, nodeFactory.createNumber(1));
 Node case1Content = nodeFactory.createAssign(Token.ASSIGN, n1, nodeFactory.createNumber(2));
 Node case2 = nodeFactory.createCase(Token.DEFAULT);
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n6);
 root.appendChild(n4);
 root.appendChild(n5);
 root.appendChild(case1);
 root.appendChild(case1Content);
 root.appendChild(case2);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideWhileStatement() {
 // Assignment inside while statement
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createBooleanLiteral(true);
 Node n3 = nodeFactory.createName("i");
 Node n4 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createNumber(0));
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n1, n2);
 Node n6 = nodeFactory.createWhile(null, nodeFactory.createName("i"), null, null, null);
 Node n7 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createAdd(nodeFactory.createName("i"), nodeFactory.createNumber(1)));
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n6);
 root.appendChild(n4);
 root.appendChild(n5);
 root.appendChild(n7);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideFunctionExpression() {
 // Assignment inside function expression
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createName("y");
 Node n3 = nodeFactory.createName("i");
 Node n4 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createNumber(0));
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n1, nodeFactory.createNumber(1));
 Node n6 = nodeFactory.createName("f");
 Node funcBody = nodeFactory.createBlock(null);
 Node funcReturn = nodeFactory.createReturnStatement(nodeFactory.createName("x"));
 funcBody.appendChild(n4);
 funcBody.appendChild(n5);
 funcBody.appendChild(funcReturn);
 Node root = nodeFactory.createRoot();
 root.appendChild(n6);
 root.appendChild(funcBody);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideLambdaExpression() {
 // Assignment inside lambda expression
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createName("y");
 Node n3 = nodeFactory.createName("i");
 Node n4 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createNumber(0));
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n1, nodeFactory.createNumber(1));
 Node n6 = nodeFactory.createName("lambda");
 Node lambdaBody = nodeFactory.createBlock(null);
 Node lambdaReturn = nodeFactory.createReturnStatement(nodeFactory.createName("x"));
 lambdaBody.appendChild(n4);
 lambdaBody.appendChild(n5);
 lambdaBody.appendChild(lambdaReturn);
 Node root = nodeFactory.createRoot();
 root.appendChild(n6);
 root.appendChild(lambdaBody);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test
 public void testAssignmentInsideConditionalExpression() {
 // Assignment inside conditional expression
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createNumber(1);
 Node n3 = nodeFactory.createName("y");
 Node n4 = nodeFactory.createAssign(Token.ASSIGN, n3, nodeFactory.createName("x"));
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n1, n2);
 Node n6 = nodeFactory.createConditional(
     null, null, nodeFactory.createAssign(Token.ASSIGN, nodeFactory.createName("i"), nodeFactory.createNumber(1)), null, null);
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n6);
 root.appendChild(n4);
 root.appendChild(n5);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n4.isPresentInTree());
 }

 @Test
 public void testAssignmentInsidePostfixExpression() {
 // Assignment inside postfix expression
 Node n1 = nodeFactory.createName("x");
 Node n2 = nodeFactory.createName("y");
 Node n3 = nodeFactory.createAssign(Token.ASSIGN, n1, nodeFactory.createNumber(1));
 Node n4 = nodeFactory.createName("x");
 Node n5 = nodeFactory.createAssign(Token.ASSIGN, n2, nodeFactory.createName("x"));
 Node n6 = nodeFactory.createPostfixExpression(n4, Token.INC);
 Node root = nodeFactory.createRoot();
 root.appendChild(n1);
 root.appendChild(n3);
 root.appendChild(n2);
 root.appendChild(n5);
 root.appendChild(n6);
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, root);
 assertTrue(deadAssignmentsElimination.isComplete());
 assertFalse(n5.isPresentInTree());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRootNode() {
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, scope);
 deadAssignmentsElimination.process(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullScope() {
 NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null, null);
 deadAssignmentsElimination.process(null, new NodeFactory().createRoot());
 }
}