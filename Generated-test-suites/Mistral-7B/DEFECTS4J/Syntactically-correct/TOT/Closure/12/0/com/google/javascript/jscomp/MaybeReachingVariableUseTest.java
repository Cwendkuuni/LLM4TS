import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleVariableUse() {
    // Arrange
    String jsCode = "function foo() { var x = 1; x = 2; return x; }";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node definition = controlFlowGraph.getEntryNode().getSucc().getSucc();
    Collection<Node> uses = analysis.getUses("x", definition);

    // Assert
    assertEquals(1, uses.size());
    assertTrue(uses.contains(definition));
  }

  @Test
  public void testVariableUseInCondition() {
    // Arrange
    String jsCode = "function foo() { if (x) { x = 2; } return x; }";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node definition = controlFlowGraph.getEntryNode().getSucc();
    Collection<Node> uses = analysis.getUses("x", definition);

    // Assert
    assertEquals(1, uses.size());
    assertTrue(uses.contains(definition));
  }

  @Test
  public void testVariableUseInLoop() {
    // Arrange
    String jsCode = "function foo() { for (var i = 0; i < 10; i++) { i = i + 1; } }";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node definition = controlFlowGraph.getNodes().get(1);
    Collection<Node> uses = analysis.getUses("i", definition);

    // Assert
    assertEquals(1, uses.size());
    assertTrue(uses.contains(definition));
  }

  @Test
  public void testVariableUseInAssignment() {
    // Arrange
    String jsCode = "function foo() { var x = 1; x += 1; }";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node definition = controlFlowGraph.getEntryNode().getSucc();
    Collection<Node> uses = analysis.getUses("x", definition);

    // Assert
    assertEquals(1, uses.size());
    assertTrue(uses.contains(definition));
  }

  @Test
  public void testVariableEscape() {
    // Arrange
    String jsCode = "function foo(x) { var y = x; return y; }";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node definition = controlFlowGraph.getNodes().get(1);
    Collection<Node> uses = analysis.getUses("x", definition);

    // Assert
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableUseInCatchBlock() {
    // Arrange
    String jsCode = "try { throw new Error(); } catch (e) { e = e + 'Error'; }";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node definition = controlFlowGraph.getNodes().get(2);
    Collection<Node> uses = analysis.getUses("e", definition);

    // Assert
    assertEquals(0, uses.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidVariableName() {
    // Arrange
    String jsCode = "function foo() {}";
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>(jsCode);
    Scope scope = new Scope(controlFlowGraph, null, null);

    // Act and Assert
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);
    analysis.getUses("invalid_variable_name", controlFlowGraph.getEntryNode());
  }
}