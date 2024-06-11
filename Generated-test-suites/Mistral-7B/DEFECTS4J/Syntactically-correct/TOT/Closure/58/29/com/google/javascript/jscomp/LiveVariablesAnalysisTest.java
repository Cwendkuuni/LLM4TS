import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.isArgumentsName;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

public class LiveVariablesAnalysisTest {

  @Test
  public void testSimpleLocalVariableLiveness() {
    String jsCode = "function foo(x, y) {"
        + "var a = x + y;"
        + "return a;}"
        + "function bar() {"
        + "var z = 5;"
        + "return z;}"
        + "function main() {"
        + "var result = foo(2, 3);"
        + "var x = 1;"
        + "var y = 4;"
        + "console.log(result);"
        + "console.log(x);"
        + "console.log(y);"
        + "var z = bar();"
        + "console.log(z);"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of(scope.getVar("x"), scope.getVar("y"));
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    Set<Var> localVariables = Arrays.asList(
        scope.getVar("a"),
        scope.getVar("z")).stream().collect(Collectors.toSet());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    for (Var localVar : localVariables) {
      assertTrue(resultLattice.isLive(localVar));
    }

    for (Var globalVar : scope.getRootScope().getAllVars()) {
      assertFalse(resultLattice.isLive(globalVar));
    }
  }

  @Test
  public void testLocalVariableKilledByCondition() {
    String jsCode = "function foo(x, y) {"
        + "if (x > 5) {"
        + "  var a = x + y;"
        + "} else {"
        + "  var b = y * 2;"
        + "}"
        + "return a;}"
        + "function bar() {"
        + "return 10;"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    Set<Var> localVariables = Arrays.asList(
        scope.getVar("a"),
        scope.getVar("b")).stream().collect(Collectors.toSet());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(resultLattice.isLive(scope.getVar("x")));
    assertTrue(resultLattice.isLive(scope.getVar("y")));

    for (Var localVar : localVariables) {
      assertFalse(resultLattice.isLive(localVar));
    }
  }

  @Test
  public void testGlobalVariableLiveness() {
    String jsCode = "function foo() {"
        + "var x = 5;"
        + "console.log(x);"
        + "console.log(globalVar);"
        + "}"
        + "var globalVar = 10;";

    Scope rootScope = new Scope(new Node(Token.SCRIPT, 0, 0), "root");
    rootScope.declareVar("globalVar", new Node(Token.VAR, 0, 0), 10);

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    scope.declareVar("foo", new Node(Token.FUNCTION, 0, 0), new Node(Token.BLOCK, 0, 0));
    scope.declareVar("x", new Node(Token.VAR, 0, 0), new Node(Token.NAME, 0, 0), 5);

    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, rootScope);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(resultLattice.isLive(scope.getVar("x")));
    assertTrue(resultLattice.isLive(rootScope.getVar("globalVar")));
  }

  @Test
  public void testEscapedLocalVariable() {
    String jsCode = "function foo(x) {"
        + "function bar() {"
        + "return x;"
        + "}"
        + "return bar();}"
        + "function main() {"
        + "var result = foo(10);"
        + "console.log(result);"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of(scope.getVar("x"));
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(resultLattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testArgumentsName() {
    String jsCode = "function foo(x) {"
        + "return x * 2;"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(isArgumentsName(NodeUtil.getChildByName(cfg.getRootNode(), "foo", "arguments")));
    assertFalse(resultLattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testWhileLoop() {
    String jsCode = "function foo() {"
        + "var x = 0;"
        + "while (x < 5) {"
        + "  x++;"
        + "}"
        + "console.log(x);"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(resultLattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testForInLoop() {
    String jsCode = "function foo() {"
        + "for (var x in [1, 2, 3]) {"
        + "  console.log(x);"
        + "}"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(resultLattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testReturnStatement() {
    String jsCode = "function foo() {"
        + "return 5;"
        + "}";

    Scope scope = new Scope(new Node(Token.SCRIPT, 0, 0), "main");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(jsCode, new Scope(null, null));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = analysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.computeLattice(entryLattice);

    assertTrue(resultLattice.isLive(scope.getVar("x"))); // x is not defined, so it's considered alive by default
  }
}