import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis_SimpleFunction() {
    // Given
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResults();

    // Then
    assertEquals("{liveset=[0]}", result.toString());
  }

  @Test
  public void testLiveVariablesAnalysis_FunctionWithLocalVariable() {
    // Given
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When
    analysis.computeResults();

    // Then
    assertEquals("{liveset=[0, 1]}", analysis.getEscapedLocals().toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testLiveVariablesAnalysis_FunctionWithException() {
    // Given
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // When and Then
    analysis.computeResults(); // No exception thrown

    // Given - Exception in the function
    analysis.cfg.getEdges().get(0).setValue(Branch.THROW);

    // When and Then
    analysis.computeResults(); // IllegalStateException is thrown
  }
}