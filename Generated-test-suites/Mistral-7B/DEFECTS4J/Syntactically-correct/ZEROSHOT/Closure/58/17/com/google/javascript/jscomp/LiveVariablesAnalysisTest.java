import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Create a sample ControlFlowGraph and Scope for testing
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();

    // Add some sample nodes and edges to the ControlFlowGraph
    Node entry = new Node(Token.SCRIPT);
    Node var1 = new Node(Token.VAR, "var1");
    Node var2 = new Node(Token.VAR, "var2");
    Node assign1 = new Node(Token.ASSIGN, var1, new Node(Token.NAME, "1"));
    Node ifStatement = new Node(Token.IF, null, assign1, null);
    Node assign2 = new Node(Token.ASSIGN, var2, var1);
    Node exit = new Node(Token.SCRIPT);

    cfg.addNodes(entry, var1, var2, assign1, ifStatement, assign2, exit);
    cfg.addEdge(entry, assign1);
    cfg.addEdge(assign1, ifStatement);
    cfg.addEdge(ifStatement, assign2);
    cfg.addEdge(assign2, exit);

    // Create an instance of LiveVariablesAnalysis with the sample ControlFlowGraph and Scope
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // Test getEscapedLocals method
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    // Test flowThrough method with entry lattice
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.flowThrough(entry, entryLattice);
    assertEquals(entryLattice, result);

    // Test flowThrough method with assign1 node
    LiveVariableLattice assign1Lattice = result;
    result = analysis.flowThrough(assign1, assign1Lattice);
    Var var1Index = jsScope.getVar("var1").index;
    assertTrue(result.isLive(var1Index));
    assertFalse(result.isLive(var2.index));

    // Test flowThrough method with ifStatement node
    result = analysis.flowThrough(ifStatement, result);
    assertTrue(result.isLive(var1Index));
    assertTrue(result.isLive(var2.index));

    // Test flowThrough method with assign2 node
    result = analysis.flowThrough(assign2, result);
    assertTrue(result.isLive(var2.index));
    assertTrue(result.isLive(var1.index));

    // Test flowThrough method with exit node
    LiveVariableLattice exitLattice = result;
    result = analysis.flowThrough(exit, exitLattice);
    assertEquals(exitLattice, result);
  }
}