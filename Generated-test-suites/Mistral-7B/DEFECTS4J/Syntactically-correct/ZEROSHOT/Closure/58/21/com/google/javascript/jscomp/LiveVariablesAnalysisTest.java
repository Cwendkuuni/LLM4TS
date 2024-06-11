import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String sourceCode = "function foo(a, b) {" +
        "  var x = a * b;" +
        "  if (x > 10) {" +
        "    x += 5;" +
        "  } else {" +
        "    x *= 2;" +
        "  }" +
        "  return x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node root = compiler.parseTree(sourceCode, null, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals(entryLattice, initialEstimateLattice);

    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(root, entryLattice);

    Set<String> liveVariables = Sets.newHashSet();
    for (Var var : jsScope.getVars()) {
      if (analysis.isLive(var)) {
        liveVariables.add(var.getName());
      }
    }

    Set<String> expectedLiveVariables = Sets.newHashSet("a", "b", "x");
    assertEquals(expectedLiveVariables, liveVariables);
  }

  @Test
  public void testLiveVariablesAnalysisWithEscapedVariables() {
    String sourceCode = "function foo(a, b) {" +
        "  function bar(x) {" +
        "    x += a;" +
        "  }" +
        "  bar(b);" +
        "  return a;" +
        "}" +
        "function main() {" +
        "  foo(1, 2);" +
        "}" +
        "main();";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node root = compiler.parseTree(sourceCode, null, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals(entryLattice, initialEstimateLattice);

    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(root, entryLattice);

    Set<String> liveVariables = Sets.newHashSet();
    for (Var var : jsScope.getVars()) {
      if (analysis.isLive(var)) {
        liveVariables.add(var.getName());
      }
    }

    Set<String> expectedLiveVariables = Sets.newHashSet("a", "b");
    assertEquals(expectedLiveVariables, liveVariables);

    Set<String> escapedVariables = analysis.getEscapedLocals();
    Set<String> expectedEscapedVariables = Sets.newHashSet("b");
    assertEquals(expectedEscapedVariables, escapedVariables);
  }
}