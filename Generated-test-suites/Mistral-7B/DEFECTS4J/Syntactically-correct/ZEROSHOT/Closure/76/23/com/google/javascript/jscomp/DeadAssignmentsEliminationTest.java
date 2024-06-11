import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope;

import java.util.ArrayList;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  @org.junit.Test
  public void testDeadAssignmentsElimination() {
    // Create mock objects
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    NodeTraversal traverser = mock(NodeTraversal.class);
    Scope scope = mock(Scope.class);

    // Initialize mocks
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getNodeTraversal(true)).thenReturn(traverser);
    when(scope.getLastChild()).thenReturn(Node.newFunction(null, null, null));
    when(scope.getVar("x")).thenReturn(new Var("x", 0, null, null, null));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // Test empty node
    Node emptyNode = Node.newEmpty();
    deadAssignmentsElimination.process(null, emptyNode);
    // No-op as there are no nodes to traverse.

    // Test simple assignment
    Node assignmentNode = Node.newAssign("x", Node.newNumber(5));
    deadAssignmentsElimination.process(null, assignmentNode);
    // No-op as the variable is not dead.

    // Test dead assignment
    Node deadAssignmentNode = Node.newAssign("x", Node.newNumber(5));
    Node deadAssignmentNextNode = Node.newAssign("x", Node.newNumber(7));
    Node deadAssignmentParentNode = Node.newBlock(new ArrayList<>());
    deadAssignmentParentNode.addChild(deadAssignmentNode);
    deadAssignmentParentNode.addChild(deadAssignmentNextNode);
    deadAssignmentsElimination.process(null, deadAssignmentParentNode);
    // The dead assignment should be removed.
    assertTrue(deadAssignmentParentNode.getLastChild() == deadAssignmentNextNode);

    // Test dead assignment within a loop
    Node loopNode = Node.newFor();
    Node loopConditionNode = Node.newBinaryOp(
        Node.newName("i"), Token.LT, Node.newNumber(10));
    Node loopBodyNode = Node.newBlock(new ArrayList<>());
    Node deadAssignmentLoopNode = Node.newAssign("x", Node.newNumber(5));
    Node deadAssignmentLoopNextNode = Node.newAssign("x", Node.newNumber(7));
    loopBodyNode.addChild(deadAssignmentLoopNode);
    loopBodyNode.addChild(deadAssignmentLoopNextNode);
    loopNode.setCondition(loopConditionNode);
    loopNode.setBody(loopBodyNode);
    deadAssignmentsElimination.process(null, loopNode);
    // The dead assignment should be removed.
    assertTrue(loopBodyNode.getLastChild() == loopConditionNode);

    // Test dead assignment within a conditional
    Node conditionalNode = Node.newIf(Node.newBoolean(false));
    Node conditionalBodyNode = Node.newBlock(new ArrayList<>());
    Node deadAssignmentConditionalNode = Node.newAssign("x", Node.newNumber(5));
    Node deadAssignmentConditionalNextNode = Node.newAssign("x", Node.newNumber(7));
    conditionalBodyNode.addChild(deadAssignmentConditionalNode);
    conditionalBodyNode.addChild(deadAssignmentConditionalNextNode);
    conditionalNode.setBody(conditionalBodyNode);
    deadAssignmentsElimination.process(null, conditionalNode);
    // The dead assignment should be removed.
    assertTrue(conditionalBodyNode.getLastChild() == Node.newBoolean(false));

    // Test dead assignment within a function
    Node functionNode = Node.newFunction(null, null, null);
    Node functionBodyNode = Node.newBlock(new ArrayList<>());
    Node deadAssignmentFunctionNode = Node.newAssign("x", Node.newNumber(5));
    Node deadAssignmentFunctionNextNode = Node.newAssign("x", Node.newNumber(7));
    functionBodyNode.addChild(deadAssignmentFunctionNode);
    functionBodyNode.addChild(deadAssignmentFunctionNextNode);
    functionNode.setBody(functionBodyNode);
    deadAssignmentsElimination.process(null, functionNode);
    // The dead assignment should be removed.
    assertTrue(functionBodyNode.getLastChild() == Node.newName("return"));
  }
}