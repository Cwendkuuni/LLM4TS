import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testBasicUsage() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  return y;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node defNode = analysis.getCFG().getNode(analysis.getEntryNode()).get();

    // Check that x is used only at the definition
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertEquals(defNode, uses.iterator().next());

    // Check that y is used at the definition and the return statement
    uses = analysis.getUses("y", defNode);
    assertEquals(2, uses.size());
    assertEquals(defNode, uses.iterator().next());
    assertEquals(analysis.getReturnNode(defNode).get(), uses.iterator().next());
  }

  @Test
  public void testNestedScopes() {
    String jsCode = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "    return x;" +
        "  }" +
        "  return bar();" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node defNode = analysis.getCFG().getNode(analysis.getEntryNode()).get();

    // Check that x is used only at the definition in the nested scope
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(analysis.getCFG().getNodes(
        analysis.getCFG().getNodesByKind(Token.FUNCTION, "bar"), 0).getLastChild())
        .get(), uses.iterator().next());

    // Check that the return value of bar is used at the return statement
    uses = analysis.getUses("x", analysis.getReturnNode(defNode));
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(analysis.getCFG().getNodes(
        analysis.getCFG().getNodesByKind(Token.FUNCTION, "bar"), 0).getLastChild())
        .get(), uses.iterator().next());
  }

  @Test
  public void testEscapedVariables() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  try {" +
        "    x = 2;" +
        "  } catch (e) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node defNode = analysis.getCFG().getNode(analysis.getEntryNode()).get();

    // Check that x is used only at the definition in the try block
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(analysis.getCFG().getNodes(
        analysis.getCFG().getNodesByKind(Token.TRY, null), 0).getLastChild())
        .get(), uses.iterator().next());

    // Check that the variable is not used in the catch block
    assertNull(analysis.getUses("x", analysis.getCFG().getNode(
        analysis.getCFG().getNodesByKind(Token.CATCH, null).get(0))));

    // Check that the return value is the one defined in the try block
    uses = analysis.getUses("x", analysis.getReturnNode(defNode));
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(analysis.getCFG().getNodes(
        analysis.getCFG().getNodesByKind(Token.TRY, null), 0).getLastChild())
        .get(), uses.iterator().next());
  }

  @Test
  public void testConditionalExecution() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node defNode = analysis.getCFG().getNode(analysis.getEntryNode()).get();

    // Check that x is used only at the definition
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertEquals(defNode, uses.iterator().next());

    // Check that x is also used at the return statement
    uses = analysis.getUses("x", analysis.getReturnNode(defNode));
    assertEquals(1, uses.size());
    assertEquals(analysis.getReturnNode(defNode).get(), uses.iterator().next());
  }

  @Test
  public void testFunctionParameters() {
    String jsCode = "function foo(x) {" +
        "  return x;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node defNode = analysis.getCFG().getNode(analysis.getEntryNode()).get();

    // Check that x is used only at the definition
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(analysis.getCFG().getNodes(
        analysis.getCFG().getNodesByKind(Token.FUNCTION, null), 0).getFirstChild())
        .get(), uses.iterator().next());

    // Check that the return value is the same as the parameter
    uses = analysis.getUses("x", analysis.getReturnNode(defNode));
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(analysis.getCFG().getNodes(
        analysis.getCFG().getNodesByKind(Token.FUNCTION, null), 0).getFirstChild())
        .get(), uses.iterator().next());
  }
}