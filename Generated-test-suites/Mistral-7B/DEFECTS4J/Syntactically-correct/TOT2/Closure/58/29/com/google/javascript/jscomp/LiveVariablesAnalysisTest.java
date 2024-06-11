import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis lva;

  @Before
  public void setup() {
    // Assuming the setup for ControlFlowGraph and Scope is already done
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
    lva = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testEmptyProgram() {
    // Test for empty program, no variables are live
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    assertFalse(analysis.getEscapedLocals().isEmpty());
    assertEquals(Collections.emptySet(), analysis.getEscapedLocals());
    LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.createInitialEstimateLattice();
    assertNotNull(entry);
    assertNotNull(result);
    assertEquals(entry, result);
  }

  @Test
  public void testSimpleAssignment() {
    Node root = Node.newNode(Token.SCRIPT, 0);
    Node var1 = Node.newName("var1");
    Node value = Node.newNumber(1);
    Node assign = Node.newAssign(Token.ASSIGN, var1, value);
    root.setFirstChild(var1);
    root.setLastChild(assign);

    // Add the node to the CFG
    cfg.addNode(root);
    Node entryNode = cfg.getStartNode();
    List<DiGraphEdge<Node, Branch>> edges = cfg.getOutEdges(entryNode);
    DiGraphEdge<Node, Branch> edge = edges.get(0);
    Branch branch = edge.getValue();

    // Test the flow through
    LiveVariableLattice entryLattice = lva.createEntryLattice();
    LiveVariableLattice initialLattice = lva.createInitialEstimateLattice();
    LiveVariableLattice lattice = lva.flowThrough(entryNode, initialLattice);

    // Check the result
    assertTrue(lattice.isLive(var1));
  }

  @Test
  public void testSimpleForLoop() {
    // Test for a simple for loop
    Node root = Node.newNode(Token.SCRIPT, 0);
    Node var1 = Node.newName("var1");
    Node start = Node.newNumber(0);
    Node end = Node.newNumber(10);
    Node step = Node.newNumber(1);
    Node forNode = Node.newFor(Token.FOR, var1, start, end, step);
    Node body = Node.newName("body");
    root.setFirstChild(forNode);
    forNode.setLastChild(body);

    // Add the node to the CFG
    cfg.addNode(root);
    Node entryNode = cfg.getStartNode();
    List<DiGraphEdge<Node, Branch>> edges = cfg.getOutEdges(entryNode);
    DiGraphEdge<Node, Branch> edge = edges.get(0);
    Branch branch = edge.getValue();

    // Test the flow through
    LiveVariableLattice entryLattice = lva.createEntryLattice();
    LiveVariableLattice initialLattice = lva.createInitialEstimateLattice();
    LiveVariableLattice lattice = lva.flowThrough(entryNode, initialLattice);

    // Check the result
    assertTrue(lattice.isLive(var1));
  }

  @Test
  public void testComplexProgram() {
    // Test for a complex program
    // (Assuming the complex program setup is already done)

    // Test the getEscapedLocals
    Set<Var> escapedLocals = lva.getEscapedLocals();
    assertTrue(escapedLocals.size() > 0);
    assertEquals(escapedLocals, lva.getEscapedLocals());

    // Test the flowThrough method
    LiveVariableLattice entryLattice = lva.createEntryLattice();
    LiveVariableLattice initialLattice = lva.createInitialEstimateLattice();
    LiveVariableLattice lattice = lva.flowThrough(cfg.getStartNode(), initialLattice);

    // Check the result
    List<Var> variables = Arrays.asList(jsScope.getVar("var1"), jsScope.getVar("var2"));
    for (Var var : variables) {
      assertTrue(lattice.isLive(var));
    }
  }

  @Test(expected = AssertionError.class)
  public void testExceptionScenario() {
    // Test for an exception scenario
    Node root = Node.newNode(Token.SCRIPT, 0);
    Node var1 = Node.newName("var1");
    Node value = Node.newNumber(1);
    Node assign = Node.newAssign(Token.ASSIGN, var1, value);
    Node throwNode = Node.newThrow(Token.THROW, Node.newName("Error"));
    root.setFirstChild(var1);
    root.setLastChild(Node.newSequence(assign, throwNode));

    // Add the node to the CFG
    cfg.addNode(root);
    Node entryNode = cfg.getStartNode();
    List<DiGraphEdge<Node, Branch>> edges = cfg.getOutEdges(entryNode);
    DiGraphEdge<Node, Branch> edge = edges.get(0);
    Branch branch = edge.getValue();

    // Test the flow through
    LiveVariableLattice entryLattice = lva.createEntryLattice();
    LiveVariableLattice initialLattice = lva.createInitialEstimateLattice();
    LiveVariableLattice lattice = lva.flowThrough(entryNode, initialLattice);

    // This test case should throw an AssertionError due to the exception
    // scenario
    assertTrue(lattice.isLive(var1));
  }

  @Test
  public void testEdgeCases() {
    // Test for edge cases
    // (Assuming the edge cases setup is already done)

    // Test the getEscapedLocals
    Set<Var> escapedLocals = lva.getEscapedLocals();
    assertTrue(escapedLocals.size() >= 3);
    Set<Var> expectedEscapedLocals = Sets.newHashSet(
        jsScope.getVar("var1"), jsScope.getVar("var2"), jsScope.getVar("var3"));
    assertEquals(expectedEscapedLocals, lva.getEscapedLocals());

    // Test the flowThrough method
    LiveVariableLattice entryLattice = lva.createEntryLattice();
    LiveVariableLattice initialLattice = lva.createInitialEstimateLattice();
    LiveVariableLattice lattice = lva.flowThrough(cfg.getStartNode(), initialLattice);

    // Check the result
    List<Var> variables = Arrays.asList(jsScope.getVar("var1"), jsScope.getVar("var2"), jsScope.getVar("var3"));
    for (Var var : variables) {
      assertTrue(lattice.isLive(var));
    }
  }

  @Test
  public void testErrorScenarios() {
    // Test for error scenarios
    // (Assuming the error scenarios setup is already done)

    // Test the getEscapedLocals
    Set<Var> escapedLocals = lva.getEscapedLocals();
    assertTrue(escapedLocals.size() >= 3);
    Set<Var> expectedEscapedLocals = Sets.newHashSet(
        jsScope.getVar("var1"), jsScope.getVar("var2"), jsScope.getVar("var3"));
    assertEquals(expectedEscapedLocals, lva.getEscapedLocals());

    // Test the flowThrough method
    LiveVariableLattice entryLattice = lva.createEntryLattice();
    LiveVariableLattice initialLattice = lva.createInitialEstimateLattice();
    LiveVariableLattice lattice = lva.flowThrough(cfg.getStartNode(), initialLattice);

    // Check the result
    List<Var> variables = Arrays.asList(jsScope.getVar("var1"), jsScope.getVar("var2"), jsScope.getVar("var3"));
    for (Var var : variables) {
      assertTrue(lattice.isLive(var));
    }

    // Test for a null argument to the constructor
    assertNull(new LiveVariablesAnalysis(cfg, null, null));

    // Test for a null jsScope argument to the constructor
    assertNull(new LiveVariablesAnalysis(cfg, null, null));
  }
}