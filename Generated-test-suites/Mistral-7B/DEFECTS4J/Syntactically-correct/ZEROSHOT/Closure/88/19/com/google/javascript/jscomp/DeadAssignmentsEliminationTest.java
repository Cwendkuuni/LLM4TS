import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;
  private NodeTraversal nodeTraversal;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = mock(ControlFlowGraph.class);
    scope = mock(Scope.class);
    nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getControlFlowGraph()).thenReturn(controlFlowGraph);

    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case for a simple assignment that is dead.
    Node root = Node.newName("root");
    Node assign1 = Node.newAssignment(
        Node.newName("x"), Node.newNumber(1));
    Node assign2 = Node.newAssignment(
        Node.newName("x"), Node.newNumber(2));

    DiGraphNode<Node, Branch> rootNode = new DiGraphNode<>(root);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode));

    when(scope.getVar("x")).thenReturn(new Var("x", 0));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign1, root.getChild(0));

    // Test case for a simple assignment that is not dead.
    Node assign3 = Node.newAssignment(
        Node.newName("x"), Node.newName("y"));
    DiGraphNode<Node, Branch> assign3Node = new DiGraphNode<>(assign3);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(2, root.getChildCount());
    assertEquals(assign1, root.getChild(0));
    assertEquals(assign3, root.getChild(1));

    // Test case for a simple assignment inside a conditional statement that is dead.
    Node ifStatement = Node.newIfStatement(Node.newFalseConstant(), assign1, null);

    DiGraphNode<Node, Branch> ifStatementNode = new DiGraphNode<>(ifStatement);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node, ifStatementNode));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign3, root);

    // Test case for a simple assignment inside a loop that is dead.
    Node forStatement = Node.newForStatement(
        Node.newName("i"), Node.newNumber(0), Node.newNumber(1), assign1, null);

    DiGraphNode<Node, Branch> forStatementNode = new DiGraphNode<>(forStatement);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node, ifStatementNode, forStatementNode));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign3, root);

    // Test case for a simple assignment inside a function that is dead.
    Node function = Node.newFunction("func", new Node[0], null, assign1, null);

    DiGraphNode<Node, Branch> functionNode = new DiGraphNode<>(function);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node, ifStatementNode, forStatementNode, functionNode));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign3, root);

    // Test case for a simple assignment inside a switch statement that is dead.
    Node switchStatement = Node.newSwitchStatement(
        Node.newName("x"), new Node[0], assign1, null);

    DiGraphNode<Node, Branch> switchStatementNode = new DiGraphNode<>(switchStatement);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node, ifStatementNode, forStatementNode, functionNode, switchStatementNode));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign3, root);

    // Test case for a simple assignment inside a while loop that is dead.
    Node whileStatement = Node.newWhileStatement(
        Node.newName("x"), assign1, null);

    DiGraphNode<Node, Branch> whileStatementNode = new DiGraphNode<>(whileStatement);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node, ifStatementNode, forStatementNode, functionNode, switchStatementNode, whileStatementNode));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign3, root);

    // Test case for a simple assignment inside a do-while loop that is dead.
    Node doWhileStatement = Node.newDoWhileStatement(
        assign1, Node.newName("x"));

    DiGraphNode<Node, Branch> doWhileStatementNode = new DiGraphNode<>(doWhileStatement);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        DiGraph.newNodes(rootNode, assign3Node, ifStatementNode, forStatementNode, functionNode, switchStatementNode, whileStatementNode, doWhileStatementNode));

    when(nodeTraversal.getScopeRoot()).thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(assign3, root);
  }
}