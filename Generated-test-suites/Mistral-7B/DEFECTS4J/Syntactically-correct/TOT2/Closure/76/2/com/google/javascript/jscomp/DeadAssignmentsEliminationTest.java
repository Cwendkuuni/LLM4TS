import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler = mock(AbstractCompiler.class);
  private ControlFlowGraph cfg = mock(ControlFlowGraph.class);
  private LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);

  @org.junit.Before
  public void setup() {
    when(compiler.reportCodeChange()).thenReturn(true);
  }

  @org.junit.Test
  public void testBasicFunctionality() {
    Node node = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.tryRemoveAssignment(new NodeTraversal(compiler, NodeUtil.rootNode(node), null), cfg);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("x = 0", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInCondition() {
    Node condition = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node ifNode = new Node(Token.IF, condition, Node.newBlockStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node rootNode = Node.newBlockStatement(ifNode);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInLoop() {
    Node loopCondition = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node loopBody = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node loop = new Node(Token.FOR, loopCondition, Node.newBlockStatement(loopBody), Node.newBlockStatement(Node.newExpressionStatement(new Node(Token.PRINT, new Node(Token.NAME, "x")))));
    Node rootNode = Node.newBlockStatement(loop);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInFunction() {
    Node functionBody = Node.newBlockStatement(
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.RETURN, new Node(Token.NAME, "x"))));
    Node function = new Node(Token.FUNCTION, null, null, null, functionBody);
    Node rootNode = Node.newBlockStatement(function);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("function() {\n}", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInConditionWithReadBeforeAssignment() {
    Node condition = new Node(Token.OR,
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 2))));
    Node ifNode = new Node(Token.IF, condition, Node.newBlockStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node rootNode = Node.newBlockStatement(ifNode);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("if (x)", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInConditionWithKillBeforeAssignment() {
    Node condition = new Node(Token.OR,
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 2)));
    Node ifNode = new Node(Token.IF, condition, Node.newBlockStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node rootNode = Node.newBlockStatement(ifNode);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(0, eliminator.compiler.getNumCodeChanges());
  }

  @org.junit.Test
  public void testDeadAssignmentInLoopWithReadBeforeAssignment() {
    Node loopCondition = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node loopBody = Node.newBlockStatement(
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 2))),
        Node.newExpressionStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node loop = new Node(Token.FOR, loopCondition, Node.newBlockStatement(loopBody), Node.newBlockStatement());
    Node rootNode = Node.newBlockStatement(loop);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("for (var x = 1; x; x++) {\n x;\n}", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInLoopWithKillBeforeAssignment() {
    Node loopCondition = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node loopBody = Node.newBlockStatement(
        new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 2)),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node loop = new Node(Token.FOR, loopCondition, Node.newBlockStatement(loopBody), Node.newBlockStatement());
    Node rootNode = Node.newBlockStatement(loop);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(0, eliminator.compiler.getNumCodeChanges());
  }

  @org.junit.Test
  public void testDeadAssignmentInFunctionWithReadBeforeAssignment() {
    Node functionBody = Node.newBlockStatement(
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 2))),
        Node.newExpressionStatement(new Node(Token.RETURN, new Node(Token.NAME, "x"))));
    Node function = new Node(Token.FUNCTION, null, null, null, functionBody);
    Node rootNode = Node.newBlockStatement(function);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("function() {\n x;\n return x;\n}", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentInFunctionWithKillBeforeAssignment() {
    Node functionBody = Node.newBlockStatement(
        new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 2)),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.RETURN, new Node(Token.NAME, "x"))));
    Node function = new Node(Token.FUNCTION, null, null, null, functionBody);
    Node rootNode = Node.newBlockStatement(function);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(0, eliminator.compiler.getNumCodeChanges());
  }

  @org.junit.Test
  public void testDeadAssignmentWithinCondition() {
    Node condition = Node.newExpressionStatement(new Node(Token.OR,
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 2)))));
    Node ifNode = new Node(Token.IF, condition, Node.newBlockStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node rootNode = Node.newBlockStatement(ifNode);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("if (x || y)", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentWithinLoop() {
    Node loopCondition = Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node loopBody = Node.newBlockStatement(
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 2))),
        Node.newExpressionStatement(new Node(Token.PRINT, new Node(Token.NAME, "x"))));
    Node loop = new Node(Token.FOR, loopCondition, Node.newBlockStatement(loopBody), Node.newBlockStatement());
    Node rootNode = Node.newBlockStatement(loop);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("for (var x = 1; x; x++) {\n x;\n}", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testDeadAssignmentWithinFunction() {
    Node functionBody = Node.newBlockStatement(
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1))),
        Node.newExpressionStatement(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 2))),
        Node.newExpressionStatement(new Node(Token.RETURN, new Node(Token.NAME, "x"))));
    Node function = new Node(Token.FUNCTION, null, null, null, functionBody);
    Node rootNode = Node.newBlockStatement(function);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(1, eliminator.compiler.getNumCodeChanges());
    assertEquals("function() {\n x;\n return x;\n}", eliminator.compiler.getCurrentCode().toString());
  }

  @org.junit.Test
  public void testNoDeadAssignmentsToRemove() {
    Node rootNode = Node.newBlockStatement(new Node(Token.RETURN, new Node(Token.INT_LITERAL, 42)));

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    eliminator.process(NodeUtil.rootNode(rootNode), rootNode);

    assertEquals(0, eliminator.compiler.getNumCodeChanges());
  }
}