import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String source = "function f() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  if (true) {" +
        "    var d = 4;" +
        "  } else {" +
        "    var e = 5;" +
        "  }" +
        "  a = b;" +
        "  b = c;" +
        "  c = a;" +
        "  return a;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(NodeUtil.parse(source, jsScope), jsScope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = liveVariablesAnalysis.getEntryLattice();
    LiveVariableLattice exitLattice = liveVariablesAnalysis.getResultLattice();

    // Check initial liveness
    assertEquals("{0, 1, 2}", entryLattice.toString());

    // Check final liveness
    ImmutableSet<String> variables = ImmutableSet.of("a", "b", "c");
    BitSet expectedLiveSet = new BitSet();
    for (String variable : variables) {
      expectedLiveSet.set(liveVariablesAnalysis.getVarIndex(variable));
    }
    assertEquals(expectedLiveSet.toString(), exitLattice.toString());

    // Check that all variables are considered live within an if statement
    Scope ifScope = new Scope();
    cfg = new ControlFlowGraph<Node>(NodeUtil.parse("function g() { if (true) {} }", ifScope), ifScope);
    LiveVariablesAnalysis ifLiveVariablesAnalysis = new LiveVariablesAnalysis(cfg, ifScope, null);
    LiveVariableLattice ifEntryLattice = ifLiveVariablesAnalysis.getEntryLattice();
    LiveVariableLattice ifExitLattice = ifLiveVariablesAnalysis.getResultLattice();
    assertEquals("{0}", ifEntryLattice.toString());
    assertEquals("{0}", ifExitLattice.toString());

    // Check that variables within a try block are considered live
    String trySource = "function h() {" +
        "  try {" +
        "    var a = 1;" +
        "  } catch (e) {" +
        "    var c = 3;" +
        "  }" +
        "  return a;" +
        "}";

    Scope tryScope = new Scope();
    ControlFlowGraph<Node> tryCfg = new ControlFlowGraph<Node>(NodeUtil.parse(trySource, tryScope), tryScope);
    LiveVariablesAnalysis tryLiveVariablesAnalysis = new LiveVariablesAnalysis(tryCfg, tryScope, null);
    LiveVariableLattice tryEntryLattice = tryLiveVariablesAnalysis.getEntryLattice();
    LiveVariableLattice tryExitLattice = tryLiveVariablesAnalysis.getResultLattice();
    assertEquals("{0}", tryEntryLattice.toString());
    assertEquals("{0}", tryExitLattice.toString());

    // Check that variables in a loop are considered live
    String loopSource = "function i() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var j = i;" +
        "  }" +
        "  return i;" +
        "}";

    Scope loopScope = new Scope();
    ControlFlowGraph<Node> loopCfg = new ControlFlowGraph<Node>(NodeUtil.parse(loopSource, loopScope), loopScope);
    LiveVariablesAnalysis loopLiveVariablesAnalysis = new LiveVariablesAnalysis(loopCfg, loopScope, null);
    LiveVariableLattice loopEntryLattice = loopLiveVariablesAnalysis.getEntryLattice();
    LiveVariableLattice loopExitLattice = loopLiveVariablesAnalysis.getResultLattice();
    assertEquals("{0, 1}", loopEntryLattice.toString());
    assertEquals("{0, 1}", loopExitLattice.toString());

    // Check that variables in a function call are considered live
    String callSource = "function j() {" +
        "  function k(x) {" +
        "    return x * x;" +
        "  }" +
        "  return k(1);" +
        "}"
        ;

    Scope callScope = new Scope();
    ControlFlowGraph<Node> callCfg = new ControlFlowGraph<Node>(NodeUtil.parse(callSource, callScope), callScope);
    LiveVariablesAnalysis callLiveVariablesAnalysis = new LiveVariablesAnalysis(callCfg, callScope, null);
    LiveVariableLattice callEntryLattice = callLiveVariablesAnalysis.getEntryLattice();
    LiveVariableLattice callExitLattice = callLiveVariablesAnalysis.getResultLattice();
    assertEquals("{0, 1}", callEntryLattice.toString());
    assertEquals("{0, 1}", callExitLattice.toString());
  }

  @Test
  public void testEscapedVariables() {
    String source = "function f() {" +
        "  function g(x) {" +
        "    return x * x;" +
        "  }" +
        "  return g(a);" +
        "  var a = 1;" +
        "}"
        ;

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(NodeUtil.parse(source, jsScope), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    Var a = jsScope.getVar("a");
    assertTrue(escapedLocals.contains(a));
  }
}