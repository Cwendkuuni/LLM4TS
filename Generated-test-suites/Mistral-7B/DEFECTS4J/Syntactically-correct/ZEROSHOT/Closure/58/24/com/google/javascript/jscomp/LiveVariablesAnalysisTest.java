import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isVar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DataFlowAnalysis.JoinOp;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String code = "function test() {" +
        "var a = 1;" +
        "var b = 2;" +
        "if (true) {" +
        "  var c = 3;" +
        "  a = b;" +
        "}" +
        "return a;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varA = jsScope.declareVar("a", null, null, true, null);
    Scope.Var varB = jsScope.declareVar("b", null, null, true, null);
    Scope.Var varC = jsScope.declareVar("c", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varA, varB, varC)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());
  }

  @Test
  public void testLiveVariablesAnalysisWithAssignment() {
    String code = "function test() {" +
        "var a = 1;" +
        "var b = 2;" +
        "a = b;" +
        "return a;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varA = jsScope.declareVar("a", null, null, true, null);
    Scope.Var varB = jsScope.declareVar("b", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varA, varB)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());

    assertTrue(latticeAtEnd.isLive(varA));
    assertTrue(latticeAtEnd.isLive(varB));
  }

  @Test
  public void testLiveVariablesAnalysisWithAssignmentAndIf() {
    String code = "function test() {" +
        "var a = 1;" +
        "var b = 2;" +
        "if (true) {" +
        "  var c = 3;" +
        "  a = b;" +
        "}" +
        "return a;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varA = jsScope.declareVar("a", null, null, true, null);
    Scope.Var varB = jsScope.declareVar("b", null, null, true, null);
    Scope.Var varC = jsScope.declareVar("c", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varA, varB, varC)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());

    assertTrue(latticeAtEnd.isLive(varA));
    assertTrue(latticeAtEnd.isLive(varB));
    assertFalse(latticeAtEnd.isLive(varC));
  }

  @Test
  public void testLiveVariablesAnalysisWithAssignmentsAndConditional() {
    String code = "function test() {" +
        "var a = 1;" +
        "var b = 2;" +
        "if (condition) {" +
        "  var c = 3;" +
        "  a = b;" +
        "}" +
        "return a;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varA = jsScope.declareVar("a", null, null, true, null);
    Scope.Var varB = jsScope.declareVar("b", null, null, true, null);
    Scope.Var varC = jsScope.declareVar("c", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varA, varB, varC)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());

    // The behavior is undefined if the condition is not provided.
    // In this test, we assume the condition is false.

    assertTrue(latticeAtEnd.isLive(varA));
    assertFalse(latticeAtEnd.isLive(varB));
    assertFalse(latticeAtEnd.isLive(varC));
  }

  @Test
  public void testLiveVariablesAnalysisWithArguments() {
    String code = "function test(x, y) {" +
        "return x + y;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varX = jsScope.declareVar("x", null, null, true, null);
    Scope.Var varY = jsScope.declareVar("y", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varX, varY)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());

    assertTrue(latticeAtEnd.isLive(varX));
    assertTrue(latticeAtEnd.isLive(varY));
  }

  @Test
  public void testLiveVariablesAnalysisWithArgumentsAndAssignments() {
    String code = "function test(x, y) {" +
        "x = y;" +
        "return x + y;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varX = jsScope.declareVar("x", null, null, true, null);
    Scope.Var varY = jsScope.declareVar("y", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varX, varY)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());

    assertTrue(latticeAtEnd.isLive(varX));
    assertTrue(latticeAtEnd.isLive(varY));
  }

  @Test
  public void testLiveVariablesAnalysisWithArgumentsAndConditionalAssignments() {
    String code = "function test(x, y) {" +
        "if (condition) {" +
        "  x = y;" +
        "}" +
        "return x + y;" +
        "}";

    Scope jsScope = new Scope();
    Scope.Var varX = jsScope.declareVar("x", null, null, true, null);
    Scope.Var varY = jsScope.declareVar("y", null, null, true, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = compiler.getControlFlowGraph(code, jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice latticeAtStart = liveVariablesAnalysis.flowThrough(cfg.getStartNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice latticeAtEnd = liveVariablesAnalysis.flowThrough(cfg.getEndNode(), initialEstimateLattice);

    Set<Var> livenessAtStart = Sets.newHashSet();
    Set<Var> livenessAtEnd = Sets.newHashSet();

    for (Var var : Arrays.asList(varX, varY)) {
      livenessAtStart.add(var);
      if (latticeAtStart.isLive(var)) {
        livenessAtEnd.add(var);
      }
    }

    assertEquals(livenessAtStart, latticeAtStart.liveSet.getValues());
    assertEquals(livenessAtEnd, latticeAtEnd.liveSet.getValues());

    // The behavior is undefined if the condition is not provided.
    // In this test, we assume the condition is false.

    assertTrue(latticeAtEnd.isLive(varX));
    assertFalse(latticeAtEnd.isLive(varY));
  }
}