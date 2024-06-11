import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;

public class MaybeReachingVariableUseTest {

  private static ControlFlowGraph<Node> createSimpleControlFlowGraph() {
    // Simplified control flow graph for testing purposes.
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();

    Node entry = cfg.addNode(new Node(Token.BLOCK, null));
    Node assignment = cfg.addNode(new Node(Token.ASSIGN, null));
    Node use = cfg.addNode(new Node(Token.NAME, "x"));

    Node ifCondition = cfg.addNode(new Node(Token.IF, null));
    Node elseNode = cfg.addNode(new Node(Token.NAME, "else"));
    Node elseBlock = cfg.addNode(new Node(Token.BLOCK, null));

    cfg.addEdge(entry, assignment, new DiGraphEdge(Token.NORMAL, 1));
    cfg.addEdge(assignment, use, new DiGraphEdge(Token.NORMAL, 0));
    cfg.addEdge(assignment, ifCondition, new DiGraphEdge(Token.NORMAL, 1));
    cfg.addEdge(ifCondition, use, new DiGraphEdge(Token.NORMAL, 1));
    cfg.addEdge(ifCondition, elseNode, new DiGraphEdge(Token.BRANCH, 1));
    cfg.addEdge(elseNode, elseBlock, new DiGraphEdge(Token.NORMAL, 1));

    return cfg;
  }

  @org.junit.Test
  public void testSimpleReachingUsesAnalysis() {
    Scope jsScope = new Scope(null, null, null, null, null, null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        createSimpleControlFlowGraph(), jsScope, null);

    Node entry = analysis.getControlFlowGraph().getNode(0);
    Collection<Node> uses = analysis.getUses("x", entry);

    // The use node should be present in the list.
    assertFalse("use node should be present in the list.", uses.isEmpty());
    assertEquals("The list should contain only the use node.", use, uses.iterator().next());
  }

  @org.junit.Test
  public void testLocalVariableWithAssignmentAndConditionalUsage() {
    ControlFlowGraph<Node> cfg = createSimpleControlFlowGraph();
    Scope jsScope = new Scope(null, null, null, null, null, null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, null);

    Node entry = analysis.getControlFlowGraph().getNode(0);
    Collection<Node> uses = analysis.getUses("x", entry);

    // The use node should be present in the list.
    assertFalse("use node should be present in the list.", uses.isEmpty());
    assertEquals("The list should contain only the use node.", use, uses.iterator().next());

    // Test the case when the conditional statement is not executed.
    analysis.setConditionalExecution(entry, false);
    uses = analysis.getUses("x", entry);
    assertTrue("The use node should be present in the list when the condition is not executed.", uses.contains(use));

    // Test the case when the conditional statement is executed.
    analysis.setConditionalExecution(entry, true);
    uses = analysis.getUsages("x", entry);
    assertTrue("The use node should be present in the list when the condition is executed.", uses.contains(use));
  }

  @org.junit.Test
  public void testSameVariableWithMultipleAssignmentsAndUsages() {
    ControlFlowGraph<Node> cfg = createSimpleControlFlowGraph();
    Scope jsScope = new Scope(null, null, null, null, null, null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, null);

    Node entry = analysis.getControlFlowGraph().getNode(0);
    Collection<Node> uses = analysis.getUsages("x", entry);

    // The use node should be present in the list.
    assertFalse("use node should be present in the list.", uses.isEmpty());
    assertEquals("The list should contain only the use node.", use, uses.iterator().next());

    // Test the case when there are multiple assignments and multiple usages
    // for the same variable.
    Node assignment1 = analysis.getControlFlowGraph().getNode(1);
    Node assignment2 = analysis.getControlFlowGraph().getNode(2);
    Node use1 = analysis.getControlFlowGraph().getNode(3);
    Node use2 = analysis.getControlFlowGraph().getNode(4);

    // First, check if the analysis correctly identifies the uses.
    Collection<Node> uses1 = analysis.getUsages("x", entry);
    assertEquals("The list should contain both uses and assignments.",
        2, uses1.size());
    assertTrue("The first use should be present in the list.", uses1.contains(use1));
    assertTrue("The second use should be present in the list.", uses1.contains(use2));

    // Now, check the result of the analysis for each assignment.
    Collection<Node> usesAssignment1 = analysis.getUsages("x", assignment1);
    Collection<Node> usesAssignment2 = analysis.getUsages("x", assignment2);

    // The first assignment should have one use.
    assertEquals("The first assignment should have one use.", 1, usesAssignment1.size());
    assertTrue("The use should be the use1.", use1, usesAssignment1.iterator().next());

    // The second assignment should have one use.
    assertEquals("The second assignment should have one use.", 1, usesAssignment2.size());
    assertTrue("The use should be the use2.", use2, usesAssignment2.iterator().next());
  }

  @org.junit.Test
  public void testEscapedVariable() {
    ControlFlowGraph<Node> cfg = createSimpleControlFlowGraph();
    Scope jsScope = new Scope(null, null, null, null, null, null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, null);

    // Add an escaped variable to the scope.
    Var escapedVar = jsScope.addVar("escaped", VarType.TOP_LEVEL, true);

    // The analysis should not report any upward exposed uses for the escaped variable.
    Node entry = analysis.getControlFlowGraph().getNode(0);
    Collection<Node> uses = analysis.getUsages("escaped", entry);
    assertTrue("The list should be empty.", uses.isEmpty());
  }

  @org.junit.Test
  public void testForLoopVariable() {
    ControlFlowGraph<Node> cfg = createSimpleControlFlowGraph();
    Scope jsScope = new Scope(null, null, null, null, null, null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, null);

    Node entry = analysis.getControlFlowGraph().getNode(0);
    Collection<Node> uses = analysis.getUsages("i", entry);

    // The use node should not be present in the list because the variable is
    // only used in the loop condition.
    assertTrue("The list should be empty.", uses.isEmpty());
  }
}