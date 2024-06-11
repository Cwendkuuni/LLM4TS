import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function f(x, y) {" +
        "var a = 1; " +
        "if (x > 10) {" +
        "  var b = 2;" +
        "  return b;" +
        "} else {" +
        "  var c = 3;" +
        "  a = c;" +
        "} " +
        "return a; " +
        "}";

    Scope jsScope = new Scope();
    TestingCompiler compiler = new TestingCompiler();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        compiler.process(jsCode, jsScope), jsScope, compiler);

    // Test live variables within a function
    Node functionNode = jsScope.getRootNode().getLastChild();
    LiveVariablesAnalysis.LiveVariableLattice liveVariables = analysis.analyze(functionNode);

    assertTrue(liveVariables.isLive(analysis.getVarIndex("a")));
    assertTrue(liveVariables.isLive(analysis.getVarIndex("x")));
    assertTrue(liveVariables.isLive(analysis.getVarIndex("y")));
    assertFalse(liveVariables.isLive(analysis.getVarIndex("b")));

    // Test live variables within a try block
    String jsCodeTry = "function f() {" +
        "try {" +
        "  var a = 1;" +
        "  if (Math.random() < 0.5) {" +
        "    throw new Error(\"Exception occured\");" +
        "  } else {" +
        "    var b = 2;" +
        "  }" +
        "} catch (e) {" +
        "  // do something" +
        "}" +
        "return a; " +
        "}";

    jsScope = new Scope();
    compiler = new TestingCompiler();
    analysis = new LiveVariablesAnalysis(
        compiler.process(jsCodeTry, jsScope), jsScope, compiler);

    Node functionNodeTry = jsScope.getRootNode().getLastChild();
    liveVariables = analysis.analyze(functionNodeTry);

    assertTrue(liveVariables.isLive(analysis.getVarIndex("a")));
    assertFalse(liveVariables.isLive(analysis.getVarIndex("b")));

    // Test live variables with escaped locals
    String jsCodeEscaped = "function f(x, y) {" +
        "function g() {" +
        "  var a = 1;" +
        "  return a;" +
        "}" +
        "return g();" +
        "}";

    jsScope = new Scope();
    compiler = new TestingCompiler();
    analysis = new LiveVariablesAnalysis(
        compiler.process(jsCodeEscaped, jsScope), jsScope, compiler);

    functionNode = jsScope.getRootNode().getLastChild();
    liveVariables = analysis.analyze(functionNode);

    assertTrue(liveVariables.isLive(analysis.getVarIndex("a")));
    assertTrue(liveVariables.getEscapedLocals().contains(jsScope.getVar("g")));
  }
}