import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.JSType;

import org.junit.Test;

/**
 * Test class for {@link SyntacticScopeCreator}.
 */
public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Scope parentScope = mock(Scope.class);
    Node rootNode = createNode("function test() {}", null);
    Scope createdScope = scopeCreator.createScope(rootNode, parentScope);

    assertNotNull(createdScope);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeNullParent() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope parentScope = null;
    Node rootNode = createNode("function test() {}", null);
    scopeCreator.createScope(rootNode, parentScope);
  }

  @Test
  public void testScanRootFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = createNode("function test(arg1, arg2) { var x = 1; }", null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);

    String expectedName = "test";
    String[] expectedVars = {"arg1", "arg2", "x"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanRootFunctionNoArgs() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = createNode("function test() {}", null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);

    String expectedName = "test";
    String[] expectedVars = {"arguments"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanRootFunctionWithArgumentsDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = createNode("function test(arg1, arg2) { var x = 1; }", null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);

    String expectedName = "test";
    String[] expectedVars = {"arg1", "arg2", "x"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanRootFunctionNoVars() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = createNode("function test() {}", null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);

    String expectedName = "test";
    String[] expectedVars = {};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanRootScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = createNode("var x = 1;", null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);

    String expectedName = "";
    String[] expectedVars = {"x"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScanRootInvalidNodeType() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = createNode("1 + 1", null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testScanVarsFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = createNode("function test(arg1, arg2) {}", null);
    Node childNode = createNode("var x = 1, y = 2;", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "test";
    String[] expectedVars = {"arg1", "arg2", "x", "y"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsFunctionNoVars() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = createNode("function test() {}", null);
    Node childNode = createNode("", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "test";
    String[] expectedVars = {};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsFunctionWithArgsOnly() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = createNode("function test(arg1, arg2)", null);
    Node childNode = createNode("", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "test";
    String[] expectedVars = {"arg1", "arg2"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsFunctionWithArgsAndVars() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = createNode("function test(arg1, arg2) {}", null);
    Node childNode = createNode("var x = 1, y = 2;", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "test";
    String[] expectedVars = {"arg1", "arg2", "x", "y"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = null;
    Node childNode = createNode("var x = 1, y = 2;", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "";
    String[] expectedVars = {"x", "y"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsScriptEmpty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = null;
    Node childNode = createNode("", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "";
    String[] expectedVars = {};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsScriptWithVarDeclarationOnly() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = null;
    Node childNode = createNode("var x = 1;", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "";
    String[] expectedVars = {"x"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test
  public void testScanVarsScriptWithVarDeclarationAndStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = null;
    Node childNode = createNode("var x = 1; x = 2;", parentNode);

    Scope createdScope = scopeCreator.createScope(childNode, parentNode);

    String expectedName = "";
    String[] expectedVars = {"x"};

    assertScopeContents(createdScope, expectedName, expectedVars);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScanVarsInvalidNodeType() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = null;
    Node childNode = createNode("1 + 1", null);
    Scope createdScope = scopeCreator.createScope(childNode, parentNode);
  }

  @Test
  public void testOnRedeclarationGlobal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();

    String name = "x";
    Node n = createNode("var x = 1;", null);
    Node parent = createNode("var x = 2;", null);
    Node gramps = null;
    Node nodeWithLineNumber = null;

    redeclarationHandler.onRedeclaration(
        scopeCreator.new Scope(null, null), name, n, parent, gramps, nodeWithLineNumber);

    String errorMessage = "Variable x first declared in ?";
    JSError expectedError = new JSError(
        DiagnosticType.error(
            "JSC_VAR_MULTIPLY_DECLARED_ERROR", errorMessage),
        null, null, errorMessage);

    compiler.expectError(expectedError);
  }

  @Test
  public void testOnRedeclarationLocal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();

    String name = "x";
    Node n = createNode("function test() { var x = 1; }", null);
    Node parent = createNode("function test() { var x = 2; }", null);
    Node gramps = null;
    Node nodeWithLineNumber = null;

    redeclarationHandler.onRedeclaration(
        scopeCreator.new Scope(null, null), name, n, parent, gramps, nodeWithLineNumber);

    String errorMessage = "Variable x first declared in test";
    JSError expectedError = new JSError(
        DiagnosticType.error(
            "JSC_VAR_MULTIPLY_DECLARED_ERROR", errorMessage),
        null, null, errorMessage);

    compiler.expectError(expectedError);
  }

  @Test
  public void testOnRedeclarationLocalArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();

    String name = "arguments";
    Node n = createNode("function test() { }", null);
    Node parent = createNode("function test() { var arguments = 1; }", null);
    Node gramps = null;
    Node nodeWithLineNumber = null;

    redeclarationHandler.onRedeclaration(
        scopeCreator.new Scope(null, null), name, n, parent, gramps, nodeWithLineNumber);

    String errorMessage = "Shadowing \"arguments\" is not allowed";
    JSError expectedError = new JSError(
        DiagnosticType.error(
            "JSC_VAR_ARGUMENTS_SHADOWED_ERROR", errorMessage),
        null, null, errorMessage);

    compiler.expectError(expectedError);
  }

  @Test
  public void testOnRedeclarationLocalWithJSDoc() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();

    String name = "x";
    Node n = createNode("/** @suppress{duplicate} */ function test() { var x = 1; }", null);
    Node parent = createNode("function test() { var x = 2; }", null);
    Node gramps = null;
    Node nodeWithLineNumber = null;

    redeclarationHandler.onRedeclaration(
        scopeCreator.new Scope(null, null), name, n, parent, gramps, nodeWithLineNumber);

    // No error expected, as 'duplicate' is suppressed
  }

  private void assertScopeContents(Scope scope, String expectedName, String[] expectedVars) {
    assertNotNull(scope);
    assertEquals(expectedName, scope.getName());

    String[] actualVars = scope.getAllVariableNames();

    assertArrayEquals(expectedVars, actualVars);
  }

  private Node createNode(String code, Node parent) {
    JSDocInfo jsdocInfo = null;
    if (code.startsWith("/**")) {
      String[] lines = code.split("\\r\\n|\\n");
      String jsdocCode = lines[0].substring(3);
      jsdocInfo = new JSDocInfo(jsdocCode);
      code = code.substring(jsdocCode.length());
    }

    Token[] tokens = NodeUtil.createTokens(code, null, null, null, null);

    int start = 0;
    int end = 0;
    for (int i = 0; i < tokens.length; i++) {
      Token token = tokens[i];
      if (token.getType() == Token.LBRACE) {
        start = i;
      }
      if (token.getType() == Token.RBRACE) {
        end = i;
        break;
      }
    }

    Preconditions.checkState(start >= 0 && end >= 0);
    Node node = new Node(Node.SCRIPT, start, end);
    node.setJSDocInfo(jsdocInfo);

    if (parent != null) {
      parent.addChild(node);
    }

    return node;
  }
}