import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {
  @Test
  public void testSimpleVariableUse() {
    // Given a simple class with a local variable "A"
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(new ControlFlowGraph<>(), new Scope(), null);

    // When performing DataFlowAnalysis on the given class
    ReachingUses reachingUses = analysis.compute(new Node(Token.BLOCK, null));

    // Then the reaches map should contain the expected nodes
    Collection<Node> uses = analysis.getUses("A", new Node(Token.BLOCK, null));
    assertTrue(uses.contains(new Node(Token.PRINT, new Node("A"))));
  }

  @Test
  public void testVariableAssignment() {
    // Given a simple class with a local variable "A"
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(new ControlFlowGraph<>(), new Scope(), null);

    // When performing DataFlowAnalysis on the given class
    ReachingUses reachingUses = analysis.compute(new Node(Token.BLOCK, null));

    // Then the reaches map should contain the expected nodes for the assigned value
    Collection<Node> uses = analysis.getUses("A", new Node(Token.BLOCK, null));
    assertTrue(uses.contains(new Node(Token.ASSIGN, new Node("A"), new Node(Token.NUMBER, 1))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullInput() {
    // Given a null input
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(new ControlFlowGraph<>(), new Scope(), null);

    // When trying to perform DataFlowAnalysis on a null input
    analysis.compute(null);
  }
}