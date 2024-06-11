import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Given
    String testCode = "function test() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  if (true) {" +
        "    var c = 3;" +
        "    a = b;" +
        "  } else {" +
        "    var d = 4;" +
        "  }" +
        "  return a;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT), testCode, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    Set<Var> expectedEscapedLocals = new HashSet<>(Arrays.asList(
        scope.getVar("arguments"),
        scope.getVar("a"),
        scope.getVar("b"),
        scope.getVar("c")
    ));
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();

    // Then
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    LiveVariableLattice result = analysis.compute(new Node(Token.SCRIPT), entryLattice, initialEstimateLattice);

    // Basic functionality test
    assertTrue(result.isLive(scope.getVar("a")));
    assertTrue(result.isLive(scope.getVar("b")));
    assertTrue(result.isLive(scope.getVar("c")));

    // Edge cases
    BitSet liveSet = result.liveSet;
    assertTrue(liveSet.get(scope.getVar("a").index)); // a is live
    assertFalse(liveSet.get(scope.getVar("d").index)); // d is not live

    // Exception handling scenarios
    DiGraph<Node, Branch> graph = cfg.getGraph();
    Node exceptionNode = graph.getNodes().get(1); // the if statement node
    LiveVariableLattice exceptionResult = analysis.flowThrough(exceptionNode, initialEstimateLattice);
    assertTrue(exceptionResult.liveSet.equals(entryLattice.liveSet)); // all variables are killed in exception case
  }
}