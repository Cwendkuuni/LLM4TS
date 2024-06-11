package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterScopeEvent;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ExitScopeEvent;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.VisitEvent;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversalCallback;
import com.google.javascript.jscomp.AbstractTraversalCallback.EventType;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;
  private Node rootNode;
  private DeadAssignmentsElimination elimination;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = mock(ControlFlowGraph.class);
    scope = mock(Scope.class);
    elimination = spy(new DeadAssignmentsElimination(compiler));
    rootNode = new Node(Token.PROGRAM, null, null);
  }

  @Test
  public void testEnterScope() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node root = new Node(Token.PROGRAM, null, null);
    when(traversal.getScopeRoot()).thenReturn(root);
    when(traversal.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(traversal.getScope()).thenReturn(scope);

    elimination.enterScope(traversal);

    verify(compiler).reportCodeChange();
    verify(controlFlowGraph).analyze();
  }

  @Test
  public void testExitScope() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    elimination.exitScope(traversal);
    verify(controlFlowGraph, never()).analyze();
  }

  @Test
  public void testVisit() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node testNode = new Node(Token.ASSIGN, null, null, null, null, null);
    Node parent = new Node(Token.PROGRAM, null, null);

    when(traversal.getScopeRoot()).thenReturn(parent);
    when(traversal.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(traversal.getScope()).thenReturn(scope);

    elimination.visit(traversal, testNode, parent);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> newNodeCaptor = ArgumentCaptor.forClass(Node.class);

    verify(parent).replaceChild(testNode, newNodeCaptor.capture());
    Node newNode = newNodeCaptor.getValue();
    assertEquals(0, newNode.getChildCount());
    assertEquals(Token.VOID, newNode.getType());

    verify(compiler).reportCodeChange();
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    Node root = setupControlFlowGraph();

    elimination.tryRemoveDeadAssignments(mock(NodeTraversal.class), controlFlowGraph);

    verify(controlFlowGraph).getDirectedGraphNodes();
  }

  @Test
  public void testTryRemoveAssignment() {
    Node root = setupControlFlowGraph();
    Node testNode = new Node(Token.ASSIGN, null, null, null, null, null);
    Node parent = new Node(Token.PROGRAM, null, null);

    when(controlFlowGraph.getValue(parent)).thenReturn(testNode);

    elimination.tryRemoveAssignment(mock(NodeTraversal.class), testNode, controlFlowGraph);

    verify(testNode.getParent()).replaceChild(testNode, isA(Node.class));
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    Node root = setupControlFlowGraph();

    elimination.isVariableStillLiveWithinExpression(root, root, "x");

    verify(root, times(1)).replaceChild(isA(Node.class), isA(Node.class));
  }

  @Test
  public void testIsVariableReadBeforeKill() {
    Node root = setupControlFlowGraph();
    Node testNode1 = new Node(Token.ASSIGN, null, null, null, null, null);
    Node testNode2 = new Node(Token.ASSIGN, null, null, null, null, null);
    Node parent = new Node(Token.PROGRAM, null, null);

    when(controlFlowGraph.getValue(parent)).thenReturn(testNode1);
    when(testNode1.getFirstChild()).thenReturn(testNode2);

    elimination.isVariableReadBeforeKill(testNode2, "x");

    verify(testNode2, times(1)).replaceChild(isA(Node.class), isA(Node.class));
  }

  private Node setupControlFlowGraph() {
    Node root = new Node(Token.PROGRAM, null, null);
    controlFlowGraph = new ControlFlowGraph<Node>() {};
    controlFlowGraph.setRoot(root);
    return root;
  }

  private static Predicate<Node> isA(Class<?> clazz) {
    return new Predicate<Node>() {
      @Override
      public boolean apply(Node input) {
        return clazz.isInstance(input);
      }
    };
  }
}