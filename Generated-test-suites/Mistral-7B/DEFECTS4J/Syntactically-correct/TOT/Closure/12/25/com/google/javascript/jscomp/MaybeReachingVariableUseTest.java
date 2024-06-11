import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleVariableUse() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // For simplicity

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node1 = Node.newInitialNode(Token.NAME, "A");
    Node node2 = Node.newInitialNode(Token.ASSIGN, null, node1, Node.newIntLiteral(1));
    Node node3 = Node.newInitialNode(Token.PRINT, null, Node.newName("A"));

    analysis.addNode(node1);
    analysis.addNode(node2);
    analysis.addNode(node3);
    analysis.connectNodes(node1, node2, DiGraphEdge.create(node1, node2));
    analysis.connectNodes(node1, node3, DiGraphEdge.create(node1, node3));
    analysis.connectNodes(node2, node3, DiGraphEdge.create(node2, node3));

    analysis.compute();

    // Act
    Collection<Node> uses = analysis.getUses("A", node3);

    // Assert
    assertEquals(1, uses.size());
    assertSame(node2, uses.iterator().next());
  }

  @Test
  public void testVariableDefinedInsideCondition() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // For simplicity

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node1 = Node.newInitialNode(Token.IF, null, Node.newBooleanLiteral(true));
    Node node2 = Node.newInitialNode(Token.ASSIGN, null, Node.newName("A"), Node.newIntLiteral(1));

    analysis.addNode(node1);
    analysis.addNode(node2);

    analysis.connectNodes(node1, node2, DiGraphEdge.create(node1, node2));

    analysis.compute();

    // Act
    Collection<Node> uses = analysis.getUses("A", node1);

    // Assert
    assertNull(uses);
  }

  @Test
  public void testVariableDefinedInsideLoop() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // For simplicity

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node1 = Node.newInitialNode(Token.FOR, null);
    Node node2 = Node.newInitialNode(Token.ASSIGN, null, Node.newName("A"), Node.newIntLiteral(1));
    Node node3 = Node.newInitialNode(Token.NAME, "A");
    Node node4 = Node.newInitialNode(Token.PRINT, null, node3);

    analysis.addNode(node1);
    analysis.addNode(node2);
    analysis.addNode(node3);
    analysis.addNode(node4);

    analysis.connectNodes(node1, node2, DiGraphEdge.create(node1, node2));
    analysis.connectNodes(node1, node3, DiGraphEdge.create(node1, node3));
    analysis.connectNodes(node1, node4, DiGraphEdge.create(node1, node4));
    analysis.connectNodes(node2, node4, DiGraphEdge.create(node2, node4));

    analysis.compute();

    // Act
    Collection<Node> uses = analysis.getUses("A", node4);

    // Assert
    assertEquals(1, uses.size());
    assertSame(node2, uses.iterator().next());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableNotInScope() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // For simplicity

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node1 = Node.newInitialNode(Token.NAME, "B"); // Not in scope

    analysis.addNode(node1);

    analysis.compute();

    // Act and Assert
    analysis.getUses("B", node1);
  }

  @Test
  public void testVariableEscaped() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // For simplicity

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.escaped.add(jsScope.getVar("foo"));

    Node node1 = Node.newInitialNode(Token.NAME, "foo");
    Node node2 = Node.newInitialNode(Token.ASSIGN, null, node1, Node.newIntLiteral(1));
    Node node3 = Node.newInitialNode(Token.PRINT, null, node1);

    analysis.addNode(node1);
    analysis.addNode(node2);
    analysis.addNode(node3);
    analysis.connectNodes(node1, node2, DiGraphEdge.create(node1, node2));
    analysis.connectNodes(node1, node3, DiGraphEdge.create(node1, node3));

    analysis.compute();

    // Act
    Collection<Node> uses = analysis.getUses("foo", node3);

    // Assert
    assertNull(uses);
  }

  @Test
  public void testVariableReassigned() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // For simplicity

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node1 = Node.newInitialNode(Token.NAME, "A");
    Node node2 = Node.newInitialNode(Token.ASSIGN, null, node1, Node.newIntLiteral(1));
    Node node3 = Node.newInitialNode(Token.ASSIGN, null, node1, Node.newIntLiteral(2));
    Node node4 = Node.newInitialNode(Token.PRINT, null, Node.newName("A"));

    analysis.addNode(node1);
    analysis.addNode(node2);
    analysis.addNode(node3);
    analysis.addNode(node4);

    analysis.connectNodes(node1, node2, DiGraphEdge.create(node1, node2));
    analysis.connectNodes(node1, node3, DiGraphEdge.create(node1, node3));
    analysis.connectNodes(node1, node4, DiGraphEdge.create(node1, node4));

    analysis.compute();

    // Act
    Collection<Node> uses = analysis.getUses("A", node4);

    // Assert
    assertEquals(1, uses.size());
    assertSame(node3, uses.iterator().next());
  }
}