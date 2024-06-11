import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setup() {
    CodeBlock codeBlock = new CodeBlock(new Token(Token.START_CODE_BLOCK));
    jsScope = new Scope(codeBlock);
    jsScope.declareVariable("x", Token.VAR, new Token(Token.IDENT, "x"));
    jsScope.declareVariable("y", Token.VAR, new Token(Token.IDENT, "y"));
    jsScope.declareVariable("z", Token.VAR, new Token(Token.IDENT, "z"));

    Node rootNode = codeBlock.getFirstChild();
    cfg = new ControlFlowGraph<Node>(rootNode, jsScope);
    analyzer = new MustBeReachingVariableDef(cfg, jsScope, null);
  }

  @Test
  public void testGetDef_VariableIsDefinedInSameFunction() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "x"),
        new Node(Token.IDENT, "x"));
    Node definitionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.NUMBER, 42));

    cfg.addEdge(definitionNode, useNode);

    Node def = analyzer.getDef("x", useNode);
    assertSame(definitionNode, def);
  }

  @Test
  public void testGetDef_VariableIsDefinedInAnotherFunction() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "x"),
        new Node(Token.GET_PROPERTY, new Node(Token.IDENT, "y"),
            new Node(Token.STRING_LITERAL, "x")));
    Node definitionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.NUMBER, 42));

    cfg.addEdge(definitionNode, useNode);

    Node def = analyzer.getDef("x", useNode);
    assertSame(definitionNode, def);
  }

  @Test
  public void testGetDef_VariableIsNotDefined() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "z"),
        new Node(Token.IDENT, "x"));

    Node def = analyzer.getDef("z", useNode);
    assertNull(def);
  }

  @Test
  public void testGetDef_VariableIsEscaped() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "x"),
        new Node(Token.GET_PROPERTY, new Node(Token.IDENT, "arguments"),
            new Node(Token.IDENT, "x")));

    Var x = jsScope.getVar("x");
    analyzer.escaped.add(x);

    Node def = analyzer.getDef("x", useNode);
    assertNull(def);
  }

  @Test
  public void testDependOnOuterScopeVars_VariableIsDefinedInSameFunction() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "x"),
        new Node(Token.IDENT, "x"));

    Node definitionNode = new Node(
        Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 42));

    Var outerVariable = jsScope.getVariableInEnclosingScope("y");
    if (outerVariable != null) {
      definitionNode.addAttribute(new Node(Token.ATTRIBUTE, "escaped",
          new Node(Token.BOOLEAN, true)));
      definitionNode.addAttribute(new Node(Token.ATTRIBUTE, "outerDef",
          new Node(Token.IDENT, outerVariable.getName())));
    }

    cfg.addEdge(definitionNode, useNode);

    boolean dependsOnOuterScopeVars = analyzer.dependsOnOuterScopeVars("x",
        useNode);
    assertFalse(dependsOnOuterScopeVars);
  }

  @Test
  public void testDependOnOuterScopeVars_VariableIsDefinedInAnotherFunction() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "x"),
        new Node(Token.GET_PROPERTY, new Node(Token.IDENT, "y"),
            new Node(Token.STRING_LITERAL, "x")));

    Var outerVariable = jsScope.getVariableInEnclosingScope("y");
    if (outerVariable != null) {
      outerVariable.setScope(new Scope(new CodeBlock(new Token(Token.START_CODE_BLOCK))))
          .declareVariable("x", Token.VAR, new Token(Token.IDENT, "x"));
    }

    Node definitionNode = new Node(Token.ASSIGN, jsScope.getVar("x").getNode(),
        new Node(Token.NUMBER, 42));

    cfg.addEdge(definitionNode, useNode);

    boolean dependsOnOuterScopeVars = analyzer.dependsOnOuterScopeVars("x",
        useNode);
    assertTrue(dependsOnOuterScopeVars);
  }

  @Test
  public void testDependOnOuterScopeVars_VariableIsNotDefined() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "z"),
        new Node(Token.IDENT, "x"));

    boolean dependsOnOuterScopeVars = analyzer.dependsOnOuterScopeVars("z",
        useNode);
    assertFalse(dependsOnOuterScopeVars);
  }

  @Test
  public void testDependOnOuterScopeVars_VariableIsEscaped() {
    Node useNode = new Node(Token.ASSIGN, new Token(Token.IDENT, "x"),
        new Node(Token.GET_PROPERTY, new Node(Token.IDENT, "arguments"),
            new Node(Token.IDENT, "x")));

    Var x = jsScope.getVar("x");
    analyzer.escaped.add(x);

    boolean dependsOnOuterScopeVars = analyzer.dependsOnOuterScopeVars("x",
        useNode);
    assertFalse(dependsOnOuterScopeVars);
  }
}