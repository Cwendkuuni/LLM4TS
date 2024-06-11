import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private Scope scope;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    cfg = mock(ControlFlowGraph.class);
    scope = mock(Scope.class);
    root = mock(Node.class);

    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(cfg.getDirectedGraphNodes()).thenReturn(new ArrayList<>());
    when(scope.getVar("x")).thenReturn(new Var("x", 1));
    when(scope.isDeclared("x", false)).thenReturn(true);

    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testBasicFunctionality() {
    Node function = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), function, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), function, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), function, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));

    function.addChildren(assignX, assignY, assignXWithDeadVariable);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversal.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversal);

    assertEquals(1, function.getChildCount());
    assertEquals(assignY, function.getChild(0));
  }

  @Test
  public void testEdgeCases() {
    // Test dead assignment elimination when there are no dead assignments
    Node functionNoDeadAssignments = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), functionNoDeadAssignments, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), functionNoDeadAssignments, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));

    functionNoDeadAssignments.addChildren(assignX, assignY);

    NodeTraversal nodeTraversalNoDeadAssignments = mock(NodeTraversal.class);
    when(nodeTraversalNoDeadAssignments.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalNoDeadAssignments.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalNoDeadAssignments);

    assertEquals(2, functionNoDeadAssignments.getChildCount());

    // Test dead assignment elimination when there is a dead assignment
    Node functionWithDeadAssignment = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), functionWithDeadAssignment, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), functionWithDeadAssignment, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), functionWithDeadAssignment, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));

    functionWithDeadAssignment.addChildren(assignX, assignY, assignXWithDeadVariable);

    NodeTraversal nodeTraversalWithDeadAssignment = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignment.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignment.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignment);

    assertEquals(1, functionWithDeadAssignment.getChildCount());
    assertEquals(assignY, functionWithDeadAssignment.getChild(0));

    // Test dead assignment elimination when there is a dead assignment in a loop
    Node functionWithDeadAssignmentInLoop = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node loop = new Node(Token.FOR, new ArrayList<>(), functionWithDeadAssignmentInLoop);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), loop, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), loop, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), loop, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));

    loop.addChildren(assignX, assignY, assignXWithDeadVariable);
    functionWithDeadAssignmentInLoop.addChildren(loop);

    NodeTraversal nodeTraversalWithDeadAssignmentInLoop = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentInLoop.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentInLoop.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentInLoop);

    assertEquals(1, functionWithDeadAssignmentInLoop.getChildCount());
    assertEquals(loop, functionWithDeadAssignmentInLoop.getChild(0));

    // Test dead assignment elimination when there is a dead assignment in a function call
    Node functionWithDeadAssignmentInFunctionCall = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node call = new Node(Token.CALL, new ArrayList<>(), functionWithDeadAssignmentInFunctionCall,
        new Node(Token.NAME, new ArrayList<>(), null, "functionWithDeadAssignmentInFunctionCall"),
        new Node(new ArrayList<>()));
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), call, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), call, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), call, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));

    call.addChildren(assignX, assignY, assignXWithDeadVariable);
    functionWithDeadAssignmentInFunctionCall.addChildren(call);

    NodeTraversal nodeTraversalWithDeadAssignmentInFunctionCall = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentInFunctionCall.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentInFunctionCall.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentInFunctionCall);

    assertEquals(1, functionWithDeadAssignmentInFunctionCall.getChildCount());
    assertEquals(call, functionWithDeadAssignmentInFunctionCall.getChild(0));

    // Test dead assignment elimination when there is a dead assignment in a conditional statement
    Node functionWithDeadAssignmentInConditional = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node ifNode = new Node(Token.IF, new ArrayList<>(), functionWithDeadAssignmentInConditional);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), ifNode, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), ifNode, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), ifNode, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));
    Node condition = new Node(Token.BOOL, new ArrayList<>(), null, true);

    ifNode.addChildren(new Node(Token.IF, new ArrayList<>(), null, condition), assignX, assignY, assignXWithDeadVariable);
    functionWithDeadAssignmentInConditional.addChildren(ifNode);

    NodeTraversal nodeTraversalWithDeadAssignmentInConditional = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentInConditional.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentInConditional.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentInConditional);

    assertEquals(1, functionWithDeadAssignmentInConditional.getChildCount());
    assertEquals(ifNode, functionWithDeadAssignmentInConditional.getChild(0));

    // Test dead assignment elimination when there is a dead assignment in a switch statement
    Node functionWithDeadAssignmentInSwitch = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node switchNode = new Node(Token.SWITCH, new ArrayList<>(), functionWithDeadAssignmentInSwitch);
    Node caseNode = new Node(Token.CASE, new ArrayList<>(), switchNode, new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), caseNode, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), caseNode, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), caseNode, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));

    switchNode.addChildren(new Node(Token.SWITCH, new ArrayList<>(), null, new Node(Token.NAME, new ArrayList<>(), null, "switchVariable")), caseNode);
    functionWithDeadAssignmentInSwitch.addChildren(switchNode);

    NodeTraversal nodeTraversalWithDeadAssignmentInSwitch = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentInSwitch.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentInSwitch.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentInSwitch);

    assertEquals(1, functionWithDeadAssignmentInSwitch.getChildCount());
    assertEquals(switchNode, functionWithDeadAssignmentInSwitch.getChild(0));
  }

  @Test
  public void testExceptionScenarios() {
    // Test dead assignment elimination when there is a try/catch block
    Node functionWithDeadAssignmentInTryCatch = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node tryNode = new Node(Token.TRY, new ArrayList<>(), functionWithDeadAssignmentInTryCatch);
    Node catchNode = new Node(Token.CATCH, new ArrayList<>(), tryNode);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), tryNode, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node assignY = new Node(Token.ASSIGN, new ArrayList<>(), tryNode, "y",
        new Node(Token.NUMBER, new ArrayList<>(), null, 2));
    Node assignXWithDeadVariable = new Node(Token.ASSIGN, new ArrayList<>(), tryNode, "x",
        new Node(Token.NAME, new ArrayList<>(), null, "y"));

    tryNode.addChildren(assignX, assignY, assignXWithDeadVariable, catchNode);
    functionWithDeadAssignmentInTryCatch.addChildren(tryNode);

    NodeTraversal nodeTraversalWithDeadAssignmentInTryCatch = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentInTryCatch.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentInTryCatch.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentInTryCatch);

    assertEquals(1, functionWithDeadAssignmentInTryCatch.getChildCount());
    assertEquals(tryNode, functionWithDeadAssignmentInTryCatch.getChild(0));

    // Test dead assignment elimination when there is a throw statement
    Node functionWithDeadAssignmentAndThrowStatement = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), functionWithDeadAssignmentAndThrowStatement, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node throwStatement = new Node(Token.THROW, new ArrayList<>(), functionWithDeadAssignmentAndThrowStatement);

    functionWithDeadAssignmentAndThrowStatement.addChildren(assignX, throwStatement);

    NodeTraversal nodeTraversalWithDeadAssignmentAndThrowStatement = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentAndThrowStatement.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentAndThrowStatement.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentAndThrowStatement);

    assertEquals(0, functionWithDeadAssignmentAndThrowStatement.getChildCount());

    // Test dead assignment elimination when there is a return statement
    Node functionWithDeadAssignmentAndReturnStatement = new Node(Token.FUNCTION, new ArrayList<>(), root);
    Node assignX = new Node(Token.ASSIGN, new ArrayList<>(), functionWithDeadAssignmentAndReturnStatement, "x",
        new Node(Token.NUMBER, new ArrayList<>(), null, 1));
    Node returnStatement = new Node(Token.RETURN, new ArrayList<>(), functionWithDeadAssignmentAndReturnStatement);

    functionWithDeadAssignmentAndReturnStatement.addChildren(assignX, returnStatement);

    NodeTraversal nodeTraversalWithDeadAssignmentAndReturnStatement = mock(NodeTraversal.class);
    when(nodeTraversalWithDeadAssignmentAndReturnStatement.getControlFlowGraph()).thenReturn(cfg);
    when(nodeTraversalWithDeadAssignmentAndReturnStatement.getScope()).thenReturn(scope);

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination, nodeTraversalWithDeadAssignmentAndReturnStatement);

    assertEquals(0, functionWithDeadAssignmentAndReturnStatement.getChildCount());
  }
}