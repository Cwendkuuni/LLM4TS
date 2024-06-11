import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  private static ControlFlowGraph<Node> createControlFlowGraph(String jsCode) {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, scope, compiler);
    return analysis.getControlFlowGraph();
  }

  @org.junit.Test
  public void testEmptyFunction() {
    String jsCode = "function foo() {}";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet();
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("foo")));
  }

  @org.junit.Test
  public void testAssignment() {
    String jsCode = "function foo() { var x = 1; x = 2; }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @org.junit.Test
  public void testLoop() {
    String jsCode = "function foo() { var x = 0; while (x < 10) { x++; } }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @org.junit.Test
  public void testIfStatement() {
    String jsCode = "function foo() { var x = 1; if (x > 0) { x = 2; } }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @org.junit.Test
  public void testFunctionCall() {
    String jsCode = "function foo() { var x = 1; bar(x); } function bar(y) { y++; }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @org.junit.Test
  public void testReturnStatement() {
    String jsCode = "function foo() { var x = 1; return x; }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @org.junit.Test
  public void testExceptionHandling() {
    String jsCode = "function foo() { try { var x = 1; x = 2; } catch (e) {} }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @org.junit.Test
  public void testForInStatement() {
    String jsCode = "function foo() { for (var x in [1, 2, 3]) { x++; } }";
    ControlFlowGraph<Node> cfg = createControlFlowGraph(jsCode);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(cfg.getEntryNode(), entry);
    Set<Var> expected = Sets.newHashSet(scope.getVar("x"));
    assertEquals(expected, analysis.getEscapedLocals());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }
}