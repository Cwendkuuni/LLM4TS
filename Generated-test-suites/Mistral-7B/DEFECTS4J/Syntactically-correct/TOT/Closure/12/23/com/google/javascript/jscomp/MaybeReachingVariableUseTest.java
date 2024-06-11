import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analysis;
  private Scope jsScope;
  private ControlFlowGraph<Node> controlFlowGraph;

  @Before
  public void setUp() {
    // Assuming you have a way to create a ControlFlowGraph and a Scope object.
    jsScope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>();

    analysis = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node program = Node.literal("A = 1;");
    analysis.compute(program);

    Collection<Node> uses = analysis.getUses("A", program);
    assertEquals(Collections.singletonList(program), uses);
  }

  @Test
  public void testAssignmentInCondition() {
    Node program = Node.literal("if (A = 1) { alert(A); }");
    analysis.compute(program);

    Collection<Node> uses = analysis.getUses("A", program);
    assertEquals(Collections.singletonList(program), uses);
  }

  @Test
  public void testAssignmentInLoop() {
    Node program = Node.literal(
        "for (var i = 0; i < 10; i++) { A = i; } alert(A);");
    analysis.compute(program);

    Collection<Node> uses = analysis.getUses("A", program);
    assertEquals(Collections.singletonList(program.getFirstChild()), uses);
  }

  @Test
  public void testAssignmentInFunction() {
    Node program = Node.literal("function foo() { A = 1; } A = 2;");
    analysis.compute(program);

    Collection<Node> uses = analysis.getUsuses("A", program);
    assertEquals(Collections.singletonList(program.getChild(0)), uses);
  }

  @Test
  public void testMultipleAssignments() {
    Node program = Node.literal("A = 1; B = A; C = A; alert(B);");
    analysis.compute(program);

    Collection<Node> uses = analysis.getUses("A", program);
    assertEquals(Collections.unmodifiableList(List.of(
        program.getFirstChild(),
        program.getChild(3)
    )), uses);

    uses = analysis.getUses("B", program);
    assertEquals(Collections.singletonList(program.getChild(5)), uses);
  }

  @Test
  public void testAssignmentInNestedFunction() {
    Node program = Node.literal(
        "function foo() { function bar() { A = 1; } A = 2; } A = 3;");
    analysis.compute(program);

    Collection<Node> uses = analysis.getUses("A", program);
    assertEquals(Collections.singletonList(program.getChild(1)), uses);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetUsesWithInvalidName() {
    Node program = Node.literal("A = 1;");
    analysis.compute(program);

    analysis.getUses("X", program);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetUsesWithInvalidNode() {
    Node program = Node.literal("A = 1;");
    analysis.compute(program);

    analysis.getUses("A", Node.literal("X = 1;"));
  }
}