import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeFactory.*;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.rhino.Node;
import com.google.common.collect.ImmutableSet;

public class LiveVariablesAnalysisTest {
  // Test data
  private static final Scope jsScope = Scope.makeRootScope();
  private static final ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null, null);

  @org.junit.Test
  public void testSimpleLiveVariableAnalysis() {
    // Given
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    Node rootNode = newBlock("{var x = 1; var y = x; x = 2;}");

    // Then
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.compute(rootNode);

    // Check the result
    assertTrue(result.isLive(analysis.getVarIndex("x")));
    assertTrue(result.isLive(analysis.getVarIndex("y")));
  }

  @org.junit.Test
  public void testDeadVariableAnalysis() {
    // Given
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    Node rootNode = newBlock("{var x = 1; x = 2; var y = x; x = 3;}");

    // Then
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.compute(rootNode);

    // Check the result
    assertFalse(result.isLive(analysis.getVarIndex("x")));
    assertTrue(result.isLive(analysis.getVarIndex("y")));
  }

  @org.junit.Test
  public void testConditionalVariableAnalysis() {
    // Given
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    Node rootNode = newBlock("{var x = 1; if (true) { var y = x; } x = 2;}");

    // Then
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.compute(rootNode);

    // Check the result
    assertTrue(result.isLive(analysis.getVarIndex("x")));
    assertTrue(result.isLive(analysis.getVarIndex("y")));
  }

  @org.junit.Test
  public void testExceptionVariableAnalysis() {
    // Given
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    Node rootNode = newBlock("try { var x = 1; } catch (Exception e) { var y = x; }");

    // Then
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.compute(rootNode);

    // Check the result
    assertTrue(result.isLive(analysis.getVarIndex("x")));
    assertTrue(result.isLive(analysis.getVarIndex("y")));
  }

  @org.junit.Test
  public void testEscapedVariablesAnalysis() {
    // Given
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    Node rootNode = newBlock("function f() { for (var x in [1, 2]) { } } f();");

    // Then
    Set<Var> escaped = analysis.getEscapedLocals();

    // Check the result
    assertEquals(1, escaped.size());
    assertTrue(escaped.contains(jsScope.getVar("x")));
  }
}