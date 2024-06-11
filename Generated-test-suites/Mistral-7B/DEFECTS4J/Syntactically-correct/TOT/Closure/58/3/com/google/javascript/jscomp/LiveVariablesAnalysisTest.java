package com.google.javascript.jscomp.tests;

import static com.google.javascript.jscomp.LiveVariablesAnalysis.ARGUMENT_ARRAY_ALIAS;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  private static final String TEST_SOURCE =
      "function test() {" +
      "  var x = 1;" +
      "  var y = 2;" +
      "  if (true) {" +
      "    var z = 3;" +
      "  } else {" +
      "    var z = 4;" +
      "  }" +
      "  var a = [1, 2, 3];" +
      "  for (var i = 0; i < a.length; i++) {" +
      "    var temp = a[i];" +
      "  }" +
      "  return x + y + z;" +
      "}";

  @org.junit.Test
  public void testLiveVariablesAnalysis() {
    Scope scope = new Scope();
    Node root = Parser.parse(TEST_SOURCE);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(root), scope, null);

    Set<Var> expectedEscaped = ImmutableSet.of(scope.getVar(ARGUMENT_ARRAY_ALIAS));
    Set<Var> actualEscaped = analysis.getEscapedLocals();

    assertThat(actualEscaped).containsExactlyElementsIn(expectedEscaped);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.getEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimate =
        analysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice lattice = entry;

    for (Node node = root; node != null; node = node.getNext()) {
      lattice = analysis.flowThrough(node, lattice);
    }

    // Check that all local variables are live at the end of the function.
    for (Var var : scope.getVars()) {
      assertThat(lattice.isLive(var)).isTrue();
    }

    // Check that arguments are marked as escaped.
    for (Var var : scope.getVars()) {
      if (ARGUMENT_ARRAY_ALIAS.equals(var.name)) {
        continue;
      }
      // Check that local variables are not marked as escaped.
      assertThat(lattice.isLive(var)).isTrue();
      assertThat(analysis.getEscapedLocals().contains(var)).isFalse();
    }
  }
}