import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {
  @Test
  public void testLiveVariablesAnalysis() {
    // Given a simple JavaScript function with no local variables
    Node rootNode = Node.newString("function foo() {}");
    Scope jsScope = new Scope(rootNode);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult();

    // Then the result should indicate that no local variables are live
    assertTrue(result.liveSet.isEmpty());

    // Given a JavaScript function with a local variable that is live
    rootNode = Node.newString("function foo(x) { return x; }");
    jsScope = new Scope(rootNode);

    analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, null);

    result = analysis.computeResult();

    // Then the result should indicate that the local variable 'x' is live
    Var x = jsScope.getVar("x");
    assertTrue(result.isLive(x));

    // Given a JavaScript function with a local variable that is dead
    rootNode = Node.newString("function foo() { var x; }");
    jsScope = new Scope(rootNode);

    analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, null);

    result = analysis.computeResult();

    // Then the result should indicate that the local variable 'x' is dead
    Var xVar = jsScope.getVar("x");
    assertFalse(result.isLive(xVar));

    // Given a JavaScript function with a local variable that is escaped
    rootNode = Node.newString("function foo(x) { return x; }");
    jsScope = new Scope(rootNode);
    jsScope.declare("x", new Scope.Var(0));
    analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();

    // Then the result should indicate that the local variable 'x' is escaped
    assertTrue(escapedLocals.contains(jsScope.getVar("x")));
  }
}