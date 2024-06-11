import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class DeadAssignmentsEliminationTest {

  @org.junit.Before
  public void setup() {
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    DataFlowAnalysis liveness = mock(DataFlowAnalysis.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getDataFlowAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
  }

  @org.junit.Test
  public void testDeadAssignments() {
    Scope scope = mock(Scope.class);
    Node root = new Node(Token.BLOCK, null);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    NodeTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);

    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared(mock(String.class), mock(Boolean.class))).thenReturn(true);
    when(scope.getVar(mock(String.class))).thenReturn(new Var("x", 0));

    Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node ifStatement = new Node(Token.IF, null, assign1, null);
    Node returnStatement = new Node(Token.RETURN, null, assign2, null);

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node forStatement = new Node(Token.FOR, null, ifStatement, returnStatement);
    Node block = new Node(Token.BLOCK, null);

    block.addChild(functionNode);
    block.addChild(forStatement);
    root.addChild(block);

    when(cfg.getDirectedGraphNodes()).thenReturn(GraphTraversal.nodeList(new DiGraphNode[0]));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(mock(AbstractCompiler.class));

    deadAssignmentsElimination.process(null, root);

    GraphTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);
    Node deadAssignment = null;
    for (AbstractNode node = nodeTraversal.getScopeRoot().getFirstChild();
         node != null; node = node.getNext()) {
      if (node instanceof Node && node.getType() == Token.ASSIGN) {
        deadAssignment = (Node) node;
        break;
      }
    }
    assertEquals("assign2 should be removed", null, deadAssignment);
  }

  @org.junit.Test
  public void testDeadAssignmentInsideIfStatement() {
    Scope scope = mock(Scope.class);
    Node root = new Node(Token.BLOCK, null);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    NodeTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);

    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared(mock(String.class), mock(Boolean.class))).thenReturn(true);
    when(scope.getVar(mock(String.class))).thenReturn(new Var("x", 0));

    Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node ifStatementCondition = new Node(Token.NAME, "condition");
    Node ifStatement = new Node(Token.IF, ifStatementCondition, assign1, null);
    Node returnStatement = new Node(Token.RETURN, null, null, null);

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node forStatement = new Node(Token.FOR, null, ifStatement, returnStatement);
    Node block = new Node(Token.BLOCK, null);

    block.addChild(functionNode);
    block.addChild(forStatement);
    root.addChild(block);

    when(cfg.getDirectedGraphNodes()).thenReturn(GraphTraversal.nodeList(new DiGraphNode[0]));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(mock(AbstractCompiler.class));

    deadAssignmentsElimination.process(null, root);

    GraphTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);
    Node deadAssignment = null;
    for (AbstractNode node = nodeTraversal.getScopeRoot().getFirstChild();
         node != null; node = node.getNext()) {
      if (node instanceof Node && node.getType() == Token.ASSIGN) {
        deadAssignment = (Node) node;
        break;
      }
    }

    assertEquals("assign1 should not be removed", assign1, deadAssignment);
  }

  @org.junit.Test
  public void testDeadAssignmentInsideLoop() {
    Scope scope = mock(Scope.class);
    Node root = new Node(Token.BLOCK, null);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    NodeTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);

    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared(mock(String.class), mock(Boolean.class))).thenReturn(true);
    when(scope.getVar(mock(String.class))).thenReturn(new Var("x", 0));

    Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node condition = new Node(Token.NAME, "condition");
    Node forStatement = new Node(Token.FOR, condition, assign1, assign2);
    Node returnStatement = new Node(Token.RETURN, null, null, null);

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node block = new Node(Token.BLOCK, null);

    block.addChild(functionNode);
    block.addChild(forStatement);
    root.addChild(block);

    when(cfg.getDirectedGraphNodes()).thenReturn(GraphTraversal.nodeList(new DiGraphNode[0]));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(mock(AbstractCompiler.class));

    deadAssignmentsElimination.process(null, root);

    GraphTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);
    Node deadAssignment = null;
    for (AbstractNode node = nodeTraversal.getScopeRoot().getFirstChild();
         node != null; node = node.getNext()) {
      if (node instanceof Node && node.getType() == Token.ASSIGN) {
        deadAssignment = (Node) node;
        break;
      }
    }

    assertEquals("assign1 should be removed", assign2, deadAssignment);
  }

  @org.junit.Test
  public void testDeadAssignmentInsideFunction() {
    Scope scope = mock(Scope.class);
    Node root = new Node(Token.BLOCK, null);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    NodeTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);

    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared(mock(String.class), mock(Boolean.class))).thenReturn(true);
    when(scope.getVar(mock(String.class))).thenReturn(new Var("x", 0));

    Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node functionNode = new Node(Token.FUNCTION, null, assign1, assign2);
    Node returnStatement = new Node(Token.RETURN, null, null, null);

    Node block = new Node(Token.BLOCK, null);

    block.addChild(functionNode);
    block.addChild(returnStatement);
    root.addChild(block);

    when(cfg.getDirectedGraphNodes()).thenReturn(GraphTraversal.nodeList(new DiGraphNode[0]));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(mock(AbstractCompiler.class));

    deadAssignmentsElimination.process(null, root);

    GraphTraversal.traverse(mock(AbstractCompiler.class), root, nodeTraversal);
    Node deadAssignment = null;
    for (AbstractNode node = nodeTraversal.getScopeRoot().getFirstChild();
         node != null; node = node.getNext()) {
      if (node instanceof Node && node.getType() == Token.ASSIGN) {
        deadAssignment = (Node) node;
        break;
      }
    }

    assertEquals("assign1 should not be removed", assign1, deadAssignment);
  }
}