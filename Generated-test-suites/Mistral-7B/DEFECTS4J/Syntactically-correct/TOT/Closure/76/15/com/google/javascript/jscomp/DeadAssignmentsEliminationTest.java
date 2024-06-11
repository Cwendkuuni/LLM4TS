package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeTraversalFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination eliminator;
  private NodeTraversal nodeTraversal;
  private ControlFlowGraph<Node> controlFlowGraph;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = mock(ControlFlowGraph.class);
    liveVariablesAnalysis = mock(LiveVariablesAnalysis.class);
    eliminator = new DeadAssignmentsElimination(compiler);
    nodeTraversal = new NodeTraversal(compiler, null, null);
    when(compiler.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveVariablesAnalysis);
    when(controlFlowGraph.analyze()).thenReturn(true);
    when(liveVariablesAnalysis.analyze()).thenReturn(true);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    Node root = NodeTraversalFactory.createRootNode("root");
    NodeTraversal.traverse(compiler, root, eliminator);
    // Here we assume that the eliminator has removed all the dead assignments
    // from the node tree, and we simply check the result.
    checkAssignmentsRemoved(root);
  }

  @Test
  public void testDeadAssignmentsInIfStatement() {
    Node ifStatement = NodeTraversalFactory.createIfStatementNode("condition");
    Node assign1 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(1));
    Node assign2 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(2));
    Node assign3 = NodeTraversalFactory.createAssignmentNode("y", Node.newNumber(3));

    ifStatement.addChildToEnd(assign1);
    ifStatement.addChildToEnd(assign2);
    ifStatement.addChildToEnd(assign3);

    Node root = NodeTraversalFactory.createRootNode("root");
    NodeTraversal.traverse(compiler, root, eliminator);

    checkAssignmentsRemoved(root, assign2);
  }

  @Test
  public void testDeadAssignmentsInForStatement() {
    Node forStatement = NodeTraversalFactory.createForStatementNode("condition");
    Node assign1 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(1));
    Node assign2 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(2));
    Node assign3 = NodeTraversalFactory.createAssignmentNode("y", Node.newNumber(3));

    forStatement.addChildToEnd(assign1);
    forStatement.addChildToEnd(assign2);
    forStatement.addChildToEnd(assign3);

    Node root = NodeTraversalFactory.createRootNode("root");
    NodeTraversal.traverse(compiler, root, eliminator);

    checkAssignmentsRemoved(root, assign2);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsInGlobalScope() {
    Node root = NodeTraversalFactory.createRootNode("x = 1");
    NodeTraversal.traverse(compiler, root, eliminator);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsInFunctionScope() {
    Node functionNode = NodeTraversalFactory.createFunctionNode("function() {}");
    Node root = NodeTraversalFactory.createRootNode(functionNode);
    NodeTraversal.traverse(compiler, root, eliminator);
  }

  @Test
  public void testDeadAssignmentsInExpression() {
    Node assign1 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(1));
    Node assign2 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(2));
    Node expr = NodeTraversalFactory.createExpressionNode("x = 1 + 1");

    assign1.addChildToEnd(expr);
    assign1.addChildToEnd(assign2);

    Node root = NodeTraversalFactory.createRootNode("root");
    NodeTraversal.traverse(compiler, root, eliminator);

    checkAssignmentsRemoved(root, assign2);
  }

  @Test
  public void testDeadAssignmentsInExpressionWithFunctionCall() {
    Node assign1 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(1));
    Node assign2 = NodeTraversalFactory.createAssignmentNode("x", Node.newNumber(2));
    Node functionCall = NodeTraversalFactory.createFunctionCallNode("add", Node.newNumber(1), Node.newNumber(1));
    Node expr = NodeTraversalFactory.createExpressionNode("x = add(x, 1)");

    assign1.addChildToEnd(expr);
    assign1.addChildToEnd(assign2);

    Node root = NodeTraversalFactory.createRootNode("root");
    NodeTraversal.traverse(compiler, root, eliminator);

    checkAssignmentsRemoved(root, assign2);
  }

  private void checkAssignmentsRemoved(Node root, Node... assignmentsToRemove) {
    for (Node assignment : assignmentsToRemove) {
      ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
      when(compiler.reportCodeChange()).thenReturn(true);
      eliminator.tryRemoveAssignment(nodeTraversal, assignment, root.getLastChild());
      Node capturedNode = nodeTraversal.getScope().getLast();
      // Even if the eliminator reports success, the node might still be in the tree
      // so we need to check it's actually gone.
      assertThat(capturedNode).isNull();
    }
  }

  private void checkAssignmentsRemoved(Node root) {
    checkAssignmentsRemoved(root, (Node[]) new Node[0]);
  }
}