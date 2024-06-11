import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isAssign;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function foo(a, b) {"
        + "  var x = a + b;"
        + "  var y = a;"
        + "  if (x > 0) {"
        + "    var z = y * 2;"
        + "    return z;"
        + "  } else {"
        + "    return 0;"
        + "  }"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0));
    scope.declareVar("a", new Var(0));
    scope.declareVar("b", new Var(1));
    scope.declareVar("x", new Var(2));
    scope.declareVar("y", new Var(3));
    scope.declareVar("z", new Var(4));

    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> flowGraph = compiler.process(NodePrinter.printNode,
        NodePrinter.printToken, jsCode, null, scope, null);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        flowGraph, scope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice latticeEntry = liveVariablesAnalysis
        .createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeInitial = liveVariablesAnalysis
        .createInitialEstimateLattice();

    // Check entry and initial lattices
    assertEquals(Integer.toBinaryString(0), latticeEntry.toString());
    assertEquals(Integer.toBinaryString(0), latticeInitial.toString());

    LiveVariablesAnalysis.LiveVariableLattice latticeAfterA = liveVariablesAnalysis
        .flowThrough(scope.getRootNode(), latticeInitial);
    assertTrue(latticeAfterA.isLive(scope.getVar("a")));
    assertTrue(latticeAfterA.isLive(scope.getVar("b")));
    assertTrue(latticeAfterA.isLive(scope.getVar("x")));
    assertTrue(latticeAfterA.isLive(scope.getVar("y")));
    assertFalse(latticeAfterA.isLive(scope.getVar("z")));

    LiveVariablesAnalysis.LiveVariableLattice latticeAfterAIf = liveVariablesAnalysis
        .flowThrough(flowGraph.getBranchNode(0), latticeAfterA);
    assertTrue(latticeAfterAIf.isLive(scope.getVar("x")));
    assertTrue(latticeAfterAIf.isLive(scope.getVar("y")));
    assertFalse(latticeAfterAIf.isLive(scope.getVar("z")));
    assertTrue(latticeAfterAIf.isLive(scope.getVar("a")));
    assertTrue(latticeAfterAIf.isLive(scope.getVar("b")));

    LiveVariablesAnalysis.LiveVariableLattice latticeAfterB = liveVariablesAnalysis
        .flowThrough(flowGraph.getBranchNode(1), latticeAfterAIf);
    assertFalse(latticeAfterB.isLive(scope.getVar("x")));
    assertFalse(latticeAfterB.isLive(scope.getVar("y")));
    assertFalse(latticeAfterB.isLive(scope.getVar("z")));
    assertFalse(latticeAfterB.isLive(scope.getVar("a")));
    assertFalse(latticeAfterB.isLive(scope.getVar("b")));

    LiveVariablesAnalysis.LiveVariableLattice latticeAfterReturn = liveVariablesAnalysis
        .flowThrough(flowGraph.getBranchNode(2), latticeAfterB);
    assertTrue(latticeAfterReturn.isLive(scope.getVar("z")));

    LiveVariablesAnalysis.LiveVariableLattice latticeAfterElse = liveVariablesAnalysis
        .flowThrough(flowGraph.getBranchNode(3), latticeAfterReturn);
    assertFalse(latticeAfterElse.isLive(scope.getVar("z")));

    LiveVariablesAnalysis.LiveVariableLattice latticeAfterEnd = liveVariablesAnalysis
        .flowThrough(scope.getRootNode(), latticeAfterElse);
    assertEquals(Integer.toBinaryString(0), latticeAfterEnd.toString());

    Set<Var> liveVariables = Sets.newHashSet(scope.getVar("a"), scope.getVar("b"),
        scope.getVar("x"), scope.getVar("y"), scope.getVar("z"));
    List<LiveVariablesAnalysis.LiveVariableLattice> liveVariablesList = Arrays
        .asList(latticeEntry, latticeInitial, latticeAfterA, latticeAfterAIf,
            latticeAfterB, latticeAfterReturn, latticeAfterElse, latticeAfterEnd);
    List<LiveVariablesAnalysis.LiveVariableLattice> expectedLiveVariablesList = Arrays
        .asList(latticeEntry, latticeInitial, latticeAfterA, latticeAfterAIf,
            latticeAfterB, latticeAfterReturn, latticeAfterElse, latticeAfterEnd);
    assertEquals(liveVariablesList, liveVariablesAnalysis.getLattices());
    assertEquals(liveVariables, liveVariablesAnalysis.getLiveVariables());
  }
}