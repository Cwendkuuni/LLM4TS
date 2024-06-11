import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE =
      "function foo() {"
          + "var x = 1;"
          + "var y = 2;"
          + "if (true) {"
          + "  var z = 3;"
          + "}"
          + "return x + y; // returning x + y should reach the definition of x"
          + "}";

  private Scope jsScope;
  private MustBeReachingVariableDef mustBeReachingVariableDef;
  private Node rootNode;

  @Before
  public void setUp() throws Exception {
    jsScope = new Scope();
    jsScope.setParent(null);
    for (String identifier : new String[]{"x", "y", "z"}) {
      jsScope.declareVar(identifier, Token.VAR_DECL, null);
    }

    rootNode = NodeUtil.parseJsonToAbstractSyntaxTree(TEST_CODE);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, null);
  }

  @Test
  public void testGetDef_LocalVariable_ReturnsDefinitionNode() {
    Node useNode = rootNode.getChildAtIndex(2);
    Node defNode = mustBeReachingVariableDef.getDef("x", useNode);
    assertSame(rootNode.getChildAtIndex(0), defNode);
  }

  @Test
  public void testGetDef_NonExistingVariable_ReturnsNull() {
    Node useNode = rootNode.getChildAtIndex(3);
    assertNull(mustBeReachingVariableDef.getDef("z", useNode));
  }

  @Test
  public void testGetDef_VariableInAnotherFunction_ReturnsNull() {
    String anotherFunctionCode = "function bar() {}";
    Scope anotherJsScope = new Scope();
    anotherJsScope.setParent(null);
    for (String identifier : new String[]{"w"}) {
      anotherJsScope.declareVar(identifier, Token.VAR_DECL, null);
    }
    Node anotherRootNode = NodeUtil.parseJsonToAbstractSyntaxTree(anotherFunctionCode);

    ControlFlowGraph<Node> anotherCfg = new ControlFlowGraph<>(anotherRootNode, anotherJsScope);
    MustBeReachingVariableDef anotherMustBeReachingVariableDef =
        new MustBeReachingVariableDef(anotherCfg, anotherJsScope, null);

    Node useNode = rootNode.getChildAtIndex(3);
    assertNull(mustBeReachingVariableDef.getDef("w", useNode));
  }

  @Test
  public void testGetDef_VariableInExtern_ReturnsNull() {
    String externCode = "var x = 1;";

    Scope externJsScope = new Scope();
    externJsScope.setParent(null);
    for (String identifier : new String[]{"x"}) {
      externJsScope.declareVar(identifier, Token.VAR_DECL, null);
    }

    Node externRootNode = NodeUtil.parseJsonToAbstractSyntaxTree(externCode);
    ControlFlowGraph<Node> externCfg = new ControlFlowGraph<>(externRootNode, externJsScope);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(3);
    assertNull(mustBeReachingVariableDef.getDef("x", useNode));
  }

  @Test
  public void testGetDef_VariableDefinedInCatchBlock_ReturnsNull() {
    String testCodeWithCatch =
        "function foo() {"
            + "try {"
            + "  var x = 1;"
            + "  throw new Error();"
            + "}"
            + "catch (e) {"
            + "  var y = 2;"
            + "  return x + y;"
            + "}"
            + "}";

    Scope jsScope = new Scope();
    jsScope.setParent(null);
    for (String identifier : new String[]{"x", "y"}) {
      jsScope.declareVar(identifier, Token.VAR_DECL, null);
    }

    rootNode = NodeUtil.parseJsonToAbstractSyntaxTree(testCodeWithCatch);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(8);
    assertNull(mustBeReachingVariableDef.getDef("x", useNode));
  }

  @Test
  public void testGetDef_VariableEscaped_ReturnsNull() {
    String testCodeWithEscapedVariable =
        "function foo(x) {"
            + "var y = 2;"
            + "return x + y;"
            + "}";

    Scope jsScope = new Scope();
    jsScope.setParent(null);
    for (String identifier : new String[]{"x"}) {
      jsScope.declareVar(identifier, Token.VAR_DECL, null);
    }

    rootNode = NodeUtil.parseJsonToAbstractSyntaxTree(testCodeWithEscapedVariable);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(1);
    assertNull(mustBeReachingVariableDef.getDef("x", useNode));
  }

  @Test
  public void testDependOnOuterScopeVars_LocalVariable_ReturnsFalse() {
    Node useNode = rootNode.getChildAtIndex(2);
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode));
  }

  @Test
  public void testDependOnOuterScopeVars_VariableInAnotherFunction_ReturnsTrue() {
    String anotherFunctionCode = "function bar() {}";

    Scope anotherJsScope = new Scope();
    anotherJsScope.setParent(null);
    for (String identifier : new String[]{"w"}) {
      anotherJsScope.declareVar(identifier, Token.VAR_DECL, null);
    }

    rootNode.setFirstChild(Node.newBlock(ImmutableSet.of(
        Node.newText(Node.newName("require"), null),
        Node.newCall(Node.newName("require"), ImmutableSet.of(
            Node.newString("./another")), null),
        Node.newBlock(ImmutableSet.of(anotherRootNode), null))));

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(3);
    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("w", useNode));
  }

  @Test
  public void testDependOnOuterScopeVars_VariableInExtern_ReturnsFalse() {
    String externCode = "var x = 1;";

    rootNode.setFirstChild(Node.newBlock(ImmutableSet.of(
        Node.newText(Node.newName("require"), null),
        Node.newCall(Node.newName("require"), ImmutableSet.of(
            Node.newString("./extern")), null),
        Node.newBlock(ImmutableSet.of(externRootNode), null))));

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(3);
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode));
  }

  @Test
  public void testDependOnOuterScopeVars_VariableInCatchBlock_ReturnsTrue() {
    String testCodeWithCatch =
        "function foo() {"
            + "try {"
            + "  var x = 1;"
            + "  throw new Error();"
            + "}"
            + "catch (e) {"
            + "  var y = 2;"
            + "  return x + y;"
            + "}"
            + "}";

    rootNode = NodeUtil.parseJsonToAbstractSyntaxTree(testCodeWithCatch);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(8);
    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode));
  }

  @Test
  public void testDependOnOuterScopeVars_EscapedVariable_ReturnsFalse() {
    String testCodeWithEscapedVariable =
        "function foo(x) {"
            + "var y = 2;"
            + "return x + y;"
            + "}";

    rootNode = NodeUtil.parseJsonToAbstractSyntaxTree(testCodeWithEscapedVariable);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, null);

    Node useNode = rootNode.getChildAtIndex(1);
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode));
  }
}