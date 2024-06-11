import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testSimpleVariable() {
    String code = "function f() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  x = 2;" +
        "  return y; // should return 1, not 2" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    LiveVariableLattice result = analysis.computeResult();
    assertTrue(result.isLive(getVarIndex("x")));
    assertTrue(result.isLive(getVarIndex("y")));
  }

  @Test
  public void testDeadVariable() {
    String code = "function f() {" +
        "  var x = 1;" +
        "  if (false) {" +
        "    var y = x;" +
        "  }" +
        "  return x; // should be live, but x is dead in this case" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    LiveVariableLattice result = analysis.computeResult();
    assertFalse(result.isLive(getVarIndex("x")));
    assertTrue(result.isLive(getVarIndex("y")));
  }

  @Test
  public void testNestedScope() {
    String code = "function f() {" +
        "  function g() {" +
        "    var x = 1;" +
        "  }" +
        "  g();" +
        "  return x; // should be live" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    LiveVariableLattice result = analysis.computeResult();
    assertTrue(result.isLive(getVarIndex("x")));
  }

  @Test
  public void testFunctionCall() {
    String code = "function f() {" +
        "  function g() {" +
        "    var x = 1;" +
        "  }" +
        "  g();" +
        "  return x; // should be live" +
        "}" +
        "function h() {" +
        "  return f();" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    LiveVariableLattice result = analysis.computeResult();
    assertTrue(result.isLive(getVarIndex("x")));
  }

  @Test
  public void testTryCatchBlock() {
    String code = "function f() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error();" +
        "  } catch (e) {" +
        "    var y = x;" +
        "  }" +
        "  return y; // should be live" +
        "}" +
        "function h() {" +
        "  return f();" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    LiveVariableLattice result = analysis.computeResult();
    assertTrue(result.isLive(getVarIndex("x")));
    assertTrue(result.isLive(getVarIndex("y")));
  }

  @Test(expected = RuntimeException.class)
  public void testNullScope() {
    String code = "function f() {" +
        "  var x = 1;" +
        "}" +
        "var scope = null;" +
        "new LiveVariablesAnalysis(new ControlFlowGraph(code, scope), scope, null);";

    try {
      // The test should fail because scope is null
    } catch (Exception e) {
      throw new RuntimeException("Unexpected exception", e);
    }
  }
}