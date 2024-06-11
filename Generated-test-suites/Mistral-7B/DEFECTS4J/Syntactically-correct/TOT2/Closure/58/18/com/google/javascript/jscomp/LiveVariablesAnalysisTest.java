import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Nodeops.isAssignmentOp;
import static com.google.javascript.rhino.Nodeops.isName;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setup() {
    compiler = new Compiler();
    jsScope = compiler.getFactory().makeScope();
    jsScope.setParent(null);
  }

  @Test
  public void testEmptyFunction() {
    String jsCode = "function test() {}";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet();
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithLocalVariable() {
    String jsCode = "function test(x) { var y = 1; }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithEscapeVariable() {
    String jsCode = "function test(x) { var y = x; return x; }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithAssignments() {
    String jsCode = "function test(x) { var y = x; y += 1; }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithConditionals() {
    String jsCode = "function test(x) { if (x > 0) { var y = 1; } }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet();
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithLoops() {
    String jsCode = "function test(x) { for (var i = 0; i < 10; i++) { var y = i; } }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet("i");
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithNestedLoops() {
    String jsCode = "function test(x) { for (var i = 0; i < 10; i++) { for (var j = 0; j < 5; j++) { } } }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet("i");
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test
  public void testFunctionWithTryCatch() {
    String jsCode = "function test() { try { var x = 1; throw new Error(); } catch (e) { var y = e.message; } }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
    Set<Var> expected = Sets.newHashSet("x", "y");
    assertEquals(expected, analysis.getEscapedLocals());
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithInvalidSyntax() {
    String jsCode = "function test(x) { var y = 1;  x = 5; }";
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.process(jsCode);
  }
}