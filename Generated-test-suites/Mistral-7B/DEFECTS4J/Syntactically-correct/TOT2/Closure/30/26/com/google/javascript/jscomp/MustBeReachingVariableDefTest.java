import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestingUtils.compile;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testBasicFunction() {
    String source =
        "function f() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a + b;" +
        "}";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useA = Node.newIntLiteral(1);
    Node useB = Node.newIntLiteral(3); // This is an edge case

    Node defA = analysis.getDef("a", useA);
    Node defB = analysis.getDef("b", useB);

    assertNotNull(defA);
    assertNotNull(defB);
    assertEquals(analysis.jsScope.getRootNode(), defA);
    assertEquals(analysis.jsScope.getRootNode(), defB);
  }

  @Test
  public void testConditionalBranch() {
    String source =
        "function f() {" +
        "  var a = 1;" +
        "  if (Math.random() < 0.5) {" +
        "    var b = 2;" +
        "  } else {" +
        "    var b = 3;" +
        "  }" +
        "  return a + b;" +
        "}";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useA = Node.newIntLiteral(1);
    Node useB = Node.newIntLiteral(4); // This is an edge case

    Node defA = analysis.getDef("a", useA);
    Node defB = analysis.getDef("b", useB);

    assertNotNull(defA);
    assertNotNull(defB);

    if (defB == null) {
      fail("b should have a definition.");
    }

    GraphNode<Node, Branch> branchNode = analysis.getCfg()
        .getBranchNode(analysis.getCfg().getBranch(NodeUtil.getConditionExpression(
            analysis.getCfg().getNode(useB))));

    FlowState<MustBeReachingVariableDef.MustDef> state = branchNode.getAnnotation();
    Definition defBValue = state.getIn().reachingDef.get("b");

    assertEquals(useB, defBValue.node);
  }

  @Test
  public void testFunctionCall() {
    String source =
        "function f() {" +
        "  function g() {" +
        "    var a = 1;" +
        "  }" +
        "  return g();" +
        "}" +
        "function g() {" +
        "  var b = 2;" +
        "}" +
        "f();";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useA = Node.newName("a");
    Node useB = Node.newName("b");

    Node defA = analysis.getDef("a", useA);
    Node defB = analysis.getDef("b", useB);

    assertNotNull(defA);
    assertNull(defB);

    // The function call g() does not define b. b is defined in the inner
    // function g().
  }

  @Test
  public void testVariableEscapedFromFunction() {
    String source =
        "function f() {" +
        "  var a = 1;" +
        "  function g() {" +
        "    var a = 2;" +
        "  }" +
        "  g();" +
        "  return a;" +
        "}"
        + "f();";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useA = Node.newName("a");

    Node defA = analysis.getDef("a", useA);

    assertNull(defA);
  }

  @Test
  public void testVariableEscapedFromGlobalScope() {
    String source =
        "var a = 1;" +
        "function f() {" +
        "  var a = 2;" +
        "}";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useA = Node.newName("a");

    Node defA = analysis.getDef("a", useA);

    assertNull(defA);
  }

  @Test
  public void testVariableDependsOnOuterScope() {
    String source =
        "function f() {" +
        "  var a = 1;" +
        "  function g() {" +
        "    var b = a;" +
        "  }" +
        "  g();" +
        "  return a;" +
        "}" +
        "f();";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useA = Node.newName("a");

    assertTrue(analysis.dependsOnOuterScopeVars("a", useA));
  }

  @Test(expected = AssertionError.class)
  public void testVariableUndefined() {
    String source = "function f() {} var x = undefined;";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        ControlFlowGraph.create(compile(source), scope, compiler), scope, compiler);

    Node useX = Node.newName("x");

    analysis.getDef("x", useX);
  }
}