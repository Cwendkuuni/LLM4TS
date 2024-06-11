import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private static class MockControlFlowGraph extends DiGraph<Node, Branch> {
    @Override
    public GraphNode<Node, Branch> addNode(Node n) {
      return super.addNode(n);
    }
  }

  private static ControlFlowGraph<Node> createMockControlFlowGraph() {
    MockControlFlowGraph cfg = new MockControlFlowGraph();
    return cfg;
  }

  private static Scope createMockScope() {
    Scope scope = mock(Scope.class);
    return scope;
  }

  private static AbstractCompiler createMockCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    return compiler;
  }

  @org.junit.Test
  public void testMaybeReachingVariableUse() {
    // Given
    ControlFlowGraph<Node> cfg = createMockControlFlowGraph();
    Scope jsScope = createMockScope();
    AbstractCompiler compiler = createMockCompiler();

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = new Node(Token.NAME, "A");
    Node useNode1 = new Node(Token.PRINT, new Node(Token.NAME, "A"));
    Node useNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "B"), new Node(Token.NAME, "A"));

    // Add nodes to the control flow graph
    cfg.addNode(defNode);
    cfg.addNode(useNode1);
    cfg.addNode(useNode2);

    // Add edges to the control flow graph
    cfg.addEdge(new DiGraphEdge<Node, Branch>(defNode, useNode1, null));
    cfg.addEdge(new DiGraphEdge<Node, Branch>(defNode, useNode2, null));

    // When
    Set<Node> uses = maybeReachingVariableUse.getUses("A", defNode);

    // Then
    assertNotNull(uses);
    assertEquals(2, uses.size());
    assertEquals(useNode1, uses.iterator().next());
    assertEquals(useNode2, uses.iterator().next());
  }

  @org.junit.Test
  public void testMaybeReachingVariableUse_NoUses() {
    // Given
    ControlFlowGraph<Node> cfg = createMockControlFlowGraph();
    Scope jsScope = createMockScope();
    AbstractCompiler compiler = createMockCompiler();

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = new Node(Token.NAME, "A");

    // When
    Set<Node> uses = maybeReachingVariableUse.getUses("B", defNode);

    // Then
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @org.junit.Test
  public void testMaybeReachingVariableUse_EscapedVariable() {
    // Given
    ControlFlowGraph<Node> cfg = createMockControlFlowGraph();
    Scope jsScope = createMockScope();
    AbstractCompiler compiler = createMockCompiler();

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, compiler);
    Set<Var> escaped = new HashSet<>();
    maybeReachingVariableUse.escaped = escaped;
    escaped.add(jsScope.getVar("globalThis"));

    Node defNode = new Node(Token.NAME, "A");

    // When
    Set<Node> uses = maybeReachingVariableUse.getUses("globalThis", defNode);

    // Then
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @org.junit.Test
  public void testMaybeReachingVariableUse_Cycle() {
    // Given
    ControlFlowGraph<Node> cfg = createMockControlFlowGraph();
    Scope jsScope = createMockScope();
    AbstractCompiler compiler = createMockCompiler();

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = new Node(Token.NAME, "A");
    Node useNode = new Node(Token.ASSIGN, new Node(Token.NAME, "A"), new Node(Token.NAME, "A"));

    // Add nodes to the control flow graph
    cfg.addNode(defNode);
    cfg.addNode(useNode);

    // Add edges to the control flow graph
    cfg.addEdge(new DiGraphEdge<Node, Branch>(defNode, useNode, null));
    cfg.addEdge(new DiGraphEdge<Node, Branch>(useNode, defNode, null));

    // When/Then
    Set<Node> uses = maybeReachingVariableUse.getUses("A", defNode);
    assertEquals(1, uses.size());
    assertEquals(useNode, uses.iterator().next());
  }
}