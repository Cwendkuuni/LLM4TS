import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testEmptyFunction() {
    String jsCode = "function foo() {}";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    assertTrue(liveVariables.get(0)); // return value of the function
    assertFalse(liveVariables.get(1)); // no local variables in this case
    assertFalse(escapedLocals.isEmpty()); // the function has a implicit parameter 'this'
  }

  @Test
  public void testFunctionWithVariable() {
    String jsCode = "function foo(x) { var y = x; }";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertTrue(liveVariables.get(x.index));
    assertTrue(liveVariables.get(y.index));
    assertFalse(escapedLocals.contains(x));
    assertFalse(escapedLocals.contains(y));
  }

  @Test
  public void testFunctionWithAssignment() {
    String jsCode = "function foo(x) { x = 5; }";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    Var x = scope.getVar("x");
    assertTrue(liveVariables.get(x.index));
    assertFalse(escapedLocals.contains(x));
  }

  @Test
  public void testFunctionWithConditional() {
    String jsCode = "function foo(x) { if (x > 0) { x = 5; } }";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    Var x = scope.getVar("x");
    assertTrue(liveVariables.get(x.index));
    assertFalse(escapedLocals.contains(x));
  }

  @Test
  public void testFunctionWithLoop() {
    String jsCode = "function foo(x) { for (var i = 0; i < 10; i++) { x = x + i; } }";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    Var x = scope.getVar("x");
    Var i = scope.getVar("i");
    assertTrue(liveVariables.get(x.index));
    assertTrue(liveVariables.get(i.index));
    assertFalse(escapedLocals.contains(x));
    assertFalse(escapedLocals.contains(i));
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithException() {
    String jsCode = "function foo(x) { try { x = 5; } catch (e) { } }";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    Var x = scope.getVar("x");
    assertTrue(liveVariables.get(x.index));
    assertFalse(escapedLocals.contains(x));
  }

  @Test
  public void testFunctionWithArguments() {
    String jsCode = "function foo(x, y) { }";
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertTrue(liveVariables.get(x.index));
    assertTrue(liveVariables.get(y.index));
    assertTrue(escapedLocals.contains(x));
    assertTrue(escapedLocals.contains(y));
  }
}