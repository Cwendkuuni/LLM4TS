import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private ScopeCreator scopeCreator;

  @Before
  public void setup() {
    options = new CompilerOptions(new TestSourceFile("test.js"));
    compiler = new Compiler(options);
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope() {
    Node n = Node.newNode(Token.FUNCTION, "testFunc", ImmutableList.of(), null, null);

    Scope scope = scopeCreator.createScope(n, null);
    assertEquals(scope.getName(), "testFunc");
    assertNull(scope.getParent());
  }

  @Test
  public void testCreateScopeWithParent() {
    Node parent = Node.newNode(Token.NAME, "parent");
    Node n = Node.newNode(Token.FUNCTION, "testFunc", ImmutableList.of(), null, null);

    Scope scope = scopeCreator.createScope(n, parent);
    assertEquals(scope.getName(), "testFunc");
    assertEquals(scope.getParent(), parent);
  }

  @Test
  public void testScanRootFunctionWithArguments() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(
        Token.FUNCTION,
        "testFunc",
        ImmutableList.of(
          Node.newNode(Token.LP),
          Node.newNode(Token.NAME, "a"),
          Node.newNode(Token.COMMA),
          Node.newNode(Token.NAME, "b"),
          Node.newNode(Token.RP),
          Node.newNode(Token.LP_BRACE),
          Node.newNode(Token.RP_BRACE)
        ),
        null,
        null
    );

    Scope scope = scopeCreator.createScope(rootNode, null);

    assertEquals(scope.size(), 3);
    assertTrue(scope.containsVariable("a"));
    assertTrue(scope.containsVariable("b"));
    assertFalse(scope.containsVariable("c"));
  }

  @Test
  public void testScanRootFunctionWithNoArguments() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(
        Token.FUNCTION,
        "testFunc",
        ImmutableList.of(),
        null,
        null
    );

    Scope scope = scopeCreator.createScope(rootNode, null);

    assertEquals(scope.size(), 0);
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeWithInvalidRootNode() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(Token.LP, null, null);
    scopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testScanVarsWithSimpleVarDeclaration() {
    String sourceName = "test.js";

    Node n = Node.newNode(
        Token.VAR,
        null,
        ImmutableList.of(
          Node.newNode(Token.NAME, "x")
        ),
        null,
        null,
        null
    );

    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(n, null, scope);

    assertEquals(scope.size(), 1);
    assertTrue(scope.containsVariable("x"));
  }

  @Test
  public void testScanVarsWithFunctionDeclaration() {
    String sourceName = "test.js";

    Node n = Node.newNode(
        Token.FUNCTION,
        "testFunc",
        ImmutableList.of(),
        null,
        null
    );
    n = n.setFirstChild(Node.newNode(Token.NAME, "testFunc"));

    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(n, null, scope);

    assertEquals(scope.size(), 1);
    assertTrue(scope.containsVariable("testFunc"));
  }

  @Test
  public void testScanVarsWithFunctionExpression() {
    String sourceName = "test.js";

    Node n = Node.newNode(Token.FUNCTION, null, null, null, null);
    n = n.setFirstChild(Node.newNode(Token.NAME, "testFunc"));

    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(n, null, scope);

    assertEquals(scope.size(), 1);
    assertTrue(scope.containsVariable("testFunc"));
  }

  @Test(expected = AssertionError.class)
  public void testScanVarsWithNonStatementNode() {
    String sourceName = "test.js";

    Node n = Node.newNode(Token.LP, null, null);
    scopeCreator.scanVars(n, null, null);
  }

  @Test
  public void testOnRedeclarationGlobalScope() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(
        Token.FUNCTION,
        "testFunc",
        ImmutableList.of(),
        null,
        null
    );

    Node varNode1 = Node.newNode(Token.VAR, null, null, null, null, null);
    varNode1 = varNode1.setFirstChild(Node.newNode(Token.NAME, "x"));

    Node varNode2 = Node.newNode(Token.VAR, null, null, null, null, null);
    varNode2 = varNode2.setFirstChild(Node.newNode(Token.NAME, "x"));

    rootNode = rootNode.setFirstChild(varNode1);
    rootNode = rootNode.setNext(varNode2);

    Scope scope = scopeCreator.createScope(rootNode, null);

    assertEquals(scope.size(), 1);
    assertTrue(scope.containsVariable("x"));

    Node reportNode = Node.newNode(Token.NAME, "x");
    compiler.report(JSError.make(sourceName, reportNode, VAR_MULTIPLY_DECLARED_ERROR));

    assertEquals(compiler.getDiagnostics().size(), 1);
    assertEquals(compiler.getDiagnostics().get(0).getDiagnosticType(), VAR_MULTIPLY_DECLARED_ERROR);
  }

  @Test
  public void testOnRedeclarationLocalScope() {
    String sourceName = "test.js";

    Node parentNode = Node.newNode(Token.NAME, "parent");

    Node rootNode = Node.newNode(
        Token.FUNCTION,
        "testFunc",
        ImmutableList.of(),
        null,
        null
    );

    Node varNode1 = Node.newNode(Token.VAR, null, null, null, null, null);
    varNode1 = varNode1.setFirstChild(Node.newNode(Token.NAME, "x"));

    Node varNode2 = Node.newNode(Token.VAR, null, null, null, null, null);
    varNode2 = varNode2.setFirstChild(Node.newNode(Token.NAME, "x"));

    rootNode = rootNode.setFirstChild(varNode1);
    rootNode = rootNode.setNext(parentNode);
    parentNode = parentNode.setNext(varNode2);

    Scope scope = scopeCreator.createScope(rootNode, null);

    assertEquals(scope.size(), 1);
    assertTrue(scope.containsVariable("x"));

    Node reportNode = Node.newNode(Token.NAME, "x");
    scopeCreator.scope.declare(
        "x", reportNode, null, null); // Declare a new variable "x" inside testFunc()

    assertEquals(scope.size(), 2);
    assertTrue(scope.containsVariable("x"));

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, "x", reportNode, parentNode, null, reportNode);

    assertEquals(scope.size(), 1);
    assertFalse(scope.containsVariable("x"));
  }

  @Test
  public void testOnRedeclarationShadowingArguments() {
    String sourceName = "test.js";

    Node rootNode = Node.newNode(
        Token.FUNCTION,
        "testFunc",
        ImmutableList.of(),
        null,
        null
    );

    Node argsNode = Node.newNode(Token.LP, null, null);
    Node argNode = Node.newNode(Token.NAME, "arguments");
    argsNode = argsNode.setNext(argNode);

    Node varNode = Node.newNode(Token.VAR, null, null, null, null, null);
    varNode = varNode.setFirstChild(Node.newNode(Token.NAME, "arguments"));

    rootNode = rootNode.setFirstChild(argsNode);
    rootNode = rootNode.setNext(varNode);

    Scope scope = scopeCreator.createScope(rootNode, null);

    assertTrue(scope.containsVariable("arguments"));

    Node reportNode = Node.newNode(Token.NAME, "arguments");
    compiler.report(JSError.make(sourceName, reportNode, VAR_ARGUMENTS_SHADOWED_ERROR));

    assertEquals(compiler.getDiagnostics().size(), 1);
    assertEquals(compiler.getDiagnostics().get(0).getDiagnosticType(), VAR_ARGUMENTS_SHADOWED_ERROR);
  }
}