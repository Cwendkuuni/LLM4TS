import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

/**
 * Test class for LiveVariablesAnalysis.
 */
public class LiveVariablesAnalysisTest {

  @SuppressWarnings("unchecked")
  private static ControlFlowGraph<Node> createCFG(String jsCode) {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler() {};
    return new ControlFlowGraph<Node>(scope.getRootNode(), new CompilerPasses(),
        new TypeCheckerPass(), compiler);
  }

  /**
   * Test method for {@link LiveVariablesAnalysis#LiveVariablesAnalysis(ControlFlowGraph, Scope, AbstractCompiler)}.
   */
  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function f(x, y) {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  var c = x + y;"
        + "  if (true) {"
        + "    var d = 3;"
        + "    a = d;"
        + "  } else {"
        + "    a = 4;"
        + "  }"
        + "  return a + c;"
        + "}";

    ControlFlowGraph<Node> cfg = createCFG(jsCode);
    Scope jsScope = cfg.getScope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Set<Var> escaped = analysis.getEscapedLocals();
    assertEquals(0, escaped.size());

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals(entryLattice.toString(), initialEstimateLattice.toString());

    LiveVariableLattice result = analysis.compute(cfg.getStartNode(), entryLattice);

    int aIndex = getVarIndex("a");
    int bIndex = getVarIndex("b");
    int cIndex = getVarIndex("c");
    int xIndex = getVarIndex("x");
    int yIndex = getVarIndex("y");

    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(bIndex));
    assertTrue(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));
    assertTrue(result.isLive(yIndex));

    // Test the control flow where 'if (true)' statement is executed.
    result = analysis.compute(cfg.getBranchNode(Branch.TRUE).getTarget(), result);
    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(dIndex));
    assertFalse(result.isLive(bIndex));
    assertTrue(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));
    assertTrue(result.isLive(yIndex));

    // Test the control flow where 'if (false)' statement is executed.
    result = analysis.compute(cfg.getBranchNode(Branch.FALSE).getTarget(), result);
    assertTrue(result.isLive(aIndex));
    assertFalse(result.isLive(dIndex));
    assertTrue(result.isLive(bIndex));
    assertFalse(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));
    assertTrue(result.isLive(yIndex));

    // Test the return statement.
    result = analysis.compute(cfg.getReturnNode(), result);
    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(cIndex));
  }

  /**
   * Test method for {@link LiveVariablesAnalysis#isLive(Var, LiveVariableLattice)}.
   */
  @Test
  public void testIsLiveVar() {
    String jsCode = "function f(x) {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  var c = x + 1;"
        + "  if (x > 0) {"
        + "    var d = 3;"
        + "    a = d;"
        + "  } else {"
        + "    a = 4;"
        + "  }"
        + "  return a + c;"
        + "}";

    ControlFlowGraph<Node> cfg = createCFG(jsCode);
    Scope jsScope = cfg.getScope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Set<Var> escaped = analysis.getEscapedLocals();
    assertEquals(0, escaped.size());

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals(entryLattice.toString(), initialEstimateLattice.toString());

    int aIndex = getVarIndex("a");
    int bIndex = getVarIndex("b");
    int cIndex = getVarIndex("c");
    int dIndex = getVarIndex("d");
    int xIndex = getVarIndex("x");

    LiveVariableLattice result = analysis.compute(cfg.getStartNode(), entryLattice);

    // Test the case when 'x > 0' condition is true.
    result = analysis.compute(cfg.getBranchNode(Branch.TRUE).getTarget(), result);
    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(dIndex));
    assertFalse(result.isLive(bIndex));
    assertTrue(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));

    // Test the case when 'x > 0' condition is false.
    result = analysis.compute(cfg.getBranchNode(Branch.FALSE).getTarget(), result);
    assertTrue(result.isLive(aIndex));
    assertFalse(result.isLive(dIndex));
    assertTrue(result.isLive(bIndex));
    assertFalse(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));

    // Test the return statement.
    result = analysis.compute(cfg.getReturnNode(), result);
    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(cIndex));

    // Test the case when 'x' is a dead variable.
    result = analysis.compute(cfg.getVariableNode(xIndex), result);
    assertFalse(result.isLive(aIndex));
    assertFalse(result.isLive(bIndex));
    assertFalse(result.isLive(cIndex));
    assertFalse(result.isLive(dIndex));
    assertFalse(result.isLive(xIndex));
  }

  /**
   * Test method for {@link LiveVariablesAnalysis#isLive(int, LiveVariableLattice)}.
   */
  @Test
  public void testIsLiveIndex() {
    String jsCode = "function f(x, y) {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  var c = x + y;"
        + "  if (x > 0) {"
        + "    var d = 3;"
        + "    a = d;"
        + "  } else {"
        + "    a = 4;"
        + "  }"
        + "  return a + c;"
        + "}";

    ControlFlowGraph<Node> cfg = createCFG(jsCode);
    Scope jsScope = cfg.getScope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Set<Var> escaped = analysis.getEscapedLocals();
    assertEquals(0, escaped.size());

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals(entryLattice.toString(), initialEstimateLattice.toString());

    int aIndex = getVarIndex("a");
    int bIndex = getVarIndex("b");
    int cIndex = getVarIndex("c");
    int dIndex = getVarIndex("d");
    int xIndex = getVarIndex("x");
    int yIndex = getVarIndex("y");

    LiveVariableLattice result = analysis.compute(cfg.getStartNode(), entryLattice);

    // Test the case when 'x > 0' condition is true.
    result = analysis.compute(cfg.getBranchNode(Branch.TRUE).getTarget(), result);
    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(dIndex));
    assertFalse(result.isLive(bIndex));
    assertTrue(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));

    // Test the case when 'x > 0' condition is false.
    result = analysis.compute(cfg.getBranchNode(Branch.FALSE).getTarget(), result);
    assertTrue(result.isLive(aIndex));
    assertFalse(result.isLive(dIndex));
    assertTrue(result.isLive(bIndex));
    assertFalse(result.isLive(cIndex));
    assertTrue(result.isLive(xIndex));

    // Test the return statement.
    result = analysis.compute(cfg.getReturnNode(), result);
    assertTrue(result.isLive(aIndex));
    assertTrue(result.isLive(cIndex));

    // Test the case when 'x' is a dead variable.
    result = analysis.compute(cfg.getVariableNode(xIndex), result);
    assertFalse(result.isLive(aIndex));
    assertFalse(result.isLive(bIndex));
    assertFalse(result.isLive(cIndex));
    assertFalse(result.isLive(dIndex));
    assertFalse(result.isLive(xIndex));

    // Test the case when 'a' is a dead variable.
    result = analysis.compute(cfg.getVariableNode(aIndex), result);
    assertFalse(result.isLive(aIndex));
    assertFalse(result.isLive(bIndex));
    assertFalse(result.isLive(cIndex));
    assertFalse(result.isLive(dIndex));
    assertFalse(result.isLive(xIndex));
    assertFalse(result.isLive(yIndex));
  }

  /**
   * Test method for {@link LiveVariablesAnalysis#getEscapedLocals()}.
   */
  @Test
  public void testGetEscapedLocals() {
    String jsCode = "function f(x) {"
        + "  function g(y) {"
        + "    var a = x;"
        + "    return a + y;"
        + "  }"
        + "  return g(1);"
        + "}";

    ControlFlowGraph<Node> cfg = createCFG(jsCode);
    Scope jsScope = cfg.getScope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Set<Var> escaped = analysis.getEscapedLocals();
    Set<Var> expected = Sets.newHashSet(jsScope.getVar("x"));

    assertEquals(expected, escaped);
  }
}