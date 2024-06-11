package com.google.javascript.jscomp;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.google.javascript.rhino.Token.*;
import static org.mockito.Mockito.*;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compilerMock;
  private ControlFlowGraph<Node> cfgMock;
  private NodeTraversal nodeTraversalMock;
  private Scope scopeMock;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setup() {
    compilerMock = mock(AbstractCompiler.class);
    cfgMock = mock(ControlFlowGraph.class);
    when(cfgMock.getDirectedGraphNodes()).thenReturn(iteratorMock());
    nodeTraversalMock = mock(NodeTraversal.class);
    when(nodeTraversalMock.getControlFlowGraph()).thenReturn(cfgMock);
    when(nodeTraversalMock.getScope()).thenReturn(scopeMock);
    scopeMock = mock(Scope.class);

    deadAssignmentsElimination = new DeadAssignmentsElimination(compilerMock);
  }

  @Test
  public void testDeadAssignmentsEliminationWithNoDeadAssignments() {
    Node root = createNode("function foo() { var x = 1; }");
    deadAssignmentsElimination.process(null, root);
    verifyNoMoreInteractions();
  }

  @Test
  public void testDeadAssignmentsEliminationWithSingleDeadAssignment() {
    Node root = createNode("function foo() { var x = 1; x = 2; }");
    deadAssignmentsElimination.process(null, root);

    Node xAssignmentNode = nodeTraversalMock.getCurrentNode();
    verify(compilerMock).reportCodeChange();
    verify(nodeTraversalMock).removeChild(xAssignmentNode.getParent(), xAssignmentNode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithMultipleDeadAssignments() {
    Node root = createNode("function foo() { var x = 1; x = 2; x = 3; }");
    deadAssignmentsElimination.process(null, root);

    Node xAssignmentNode1 = nodeTraversalMock.getCurrentNode();
    Node xAssignmentNode2 = nodeTraversalMock.getCurrentNode();
    verify(compilerMock, times(2)).reportCodeChange();
    verify(nodeTraversalMock)
        .removeChild(xAssignmentNode1.getParent(), xAssignmentNode1);
    verify(nodeTraversalMock)
        .removeChild(xAssignmentNode2.getParent(), xAssignmentNode2);
  }

  @Test
  public void testDeadAssignmentsEliminationWithDeadAssignmentInsideConditional() {
    Node root = createNode("function foo() { if (x) { x = 1; }}");
    deadAssignmentsElimination.process(null, root);
    verifyNoMoreInteractions();
  }

  @Test
  public void testDeadAssignmentsEliminationWithDeadAssignmentInsideLoop() {
    Node root = createNode("function foo() { for (var i = 0; i < 10; i++) { i = i + 1; }}");
    deadAssignmentsElimination.process(null, root);
    verifyNoMoreInteractions();
  }

  @Test
  public void testDeadAssignmentsEliminationWithDeadAssignmentInsideFunctionCall() {
    Node root = createNode("function foo() { var x = 1; foo(); x = 2; }");
    deadAssignmentsElimination.process(null, root);
    verifyNoMoreInteractions();
  }

  @Test
  public void testDeadAssignmentsEliminationWithDeadAssignmentInsideReturnStatement() {
    Node root = createNode("function foo() { return x = 1; }");
    deadAssignmentsElimination.process(null, root);
    verify(compilerMock).reportCodeChange();
    verify(nodeTraversalMock).removeChild(nodeTraversalMock.getScopeRoot(), root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithDeadAssignmentInsideVariableDeclaration() {
    Node root = createNode("function foo() { var x = 1, y = x; }");
    deadAssignmentsElimination.process(null, root);
    verifyNoMoreInteractions();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeadAssignmentsEliminationWithNullCompiler() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeadAssignmentsEliminationWithNullControlFlowGraph() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilerMock);
    deadAssignmentsElimination.cfg = null;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeadAssignmentsEliminationWithNullNodeTraversal() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilerMock);
    deadAssignmentsElimination.traverse = null;
  }

  private Node createNode(String code) {
    Node externs = new Node(Token.EMPTY);
    Node root = new Node(TOKEN_FUNCTION, externs, null);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    NodeTraversal.traverse(compilerMock, root, nodeTraversal);
    NodeTraversal nodeTraversalSpy = spy(nodeTraversal);
    doReturn(nodeTraversalSpy).when(nodeTraversalMock).getVariableScope();
    doReturn(nodeTraversalSpy).when(nodeTraversalMock).getCurrentNode();
    NodeTraversal.traverse(compilerMock, root, nodeTraversalMock);
    NodeUtil.processNode(code, externs, root);
    return root;
  }

  private Iterator<DiGraphNode<Node, Branch>> iteratorMock() {
    return mock(Iterator.class);
  }
}