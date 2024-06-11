import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.jstype.JSType;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;
  private Scope parentScope;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    parentScope = mock(Scope.class);
    scopeCreator = new SyntacticScopeCreator(compiler);
    rootNode = Node.parse(
        "function testFunction(a, b) { var x = 1; var y = 2; }");
  }

  @Test
  public void testCreateScope() {
    Scope returnedScope = scopeCreator.createScope(rootNode, parentScope);
    assertNotNull(returnedScope);
  }

  @Test
  public void testScanRootFunction() {
    scopeCreator.scanRoot(rootNode, parentScope);

    Var x = parentScope.getVar("x");
    Var y = parentScope.getVar("y");
    assertNotNull(x);
    assertNotNull(y);
  }

  @Test(expected = AssertionError.class)
  public void testScanRootFunctionDuplicateDeclaration() {
    String sourceName = "testSourceName";
    Node rootNodeDuplicate = Node.parse(
        "function testFunction(a, b) { var x = 1; var x = 2; }");

    scopeCreator.scanRoot(rootNodeDuplicate, parentScope);
  }

  @Test
  public void testScanFunctionArgs() {
    String sourceNameFunctionArgs = "testSourceNameFunctionArgs";
    Node functionArgsNode = Node.parse(
        "function testFunction(a, b)");

    scopeCreator.scanRoot(functionArgsNode, parentScope);

    Var a = parentScope.getVar("a");
    Var b = parentScope.getVar("b");
    assertNotNull(a);
    assertNotNull(b);
  }

  @Test(expected = AssertionError.class)
  public void testScanFunctionArgsDuplicateDeclaration() {
    String sourceNameFunctionArgsDuplicate = "testSourceNameFunctionArgsDuplicate";
    Node functionArgsDuplicateNode = Node.parse(
        "function testFunction(a, a)");

    scopeCreator.scanRoot(functionArgsDuplicateNode, parentScope);
  }

  @Test
  public void testScanFunctionBody() {
    String sourceNameFunctionBody = "testSourceNameFunctionBody";
    Node functionBodyNode = Node.parse(
        "function testFunction(a, b) { var x = 1; }");

    scopeCreator.scanRoot(functionBodyNode, parentScope);

    Var x = parentScope.getVar("x");
    assertNotNull(x);
  }

  @Test
  public void testScanGlobalBlock() {
    String sourceNameGlobalBlock = "testSourceNameGlobalBlock";
    Node globalBlockNode = Node.parse("var x = 1; var y = 2;");

    scopeCreator.scanRoot(globalBlockNode, null);

    Var x = ((Scope) parentScope).getGlobalScope().getVar("x");
    Var y = ((Scope) parentScope).getGlobalScope().getVar("y");
    assertNotNull(x);
    assertNotNull(y);
  }

  @Test
  public void testRedeclarationHandlerOnRedeclaration() {
    DiagnosticType varMultiplyDeclaredError = DiagnosticType.error(
        "JSC_VAR_MULTIPLY_DECLARED_ERROR",
        "Variable {0} first declared in {1}");
    DiagnosticType varArgumentsShadowedError = DiagnosticType.error(
        "JSC_VAR_ARGUMENTS_SHADOWED_ERROR",
        "Shadowing \"arguments\" is not allowed");

    Scope scope = new Scope(parentScope, null);
    SyntacticScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();

    scope.declare("x", Node.NEW_INT_LIT(1), JSType.NUMBER, null);
    scope.declare("x", Node.NEW_INT_LIT(2), JSType.NUMBER, null);

    redeclarationHandler.onRedeclaration(scope, "x", null, null, null, null);

    assert(compiler.getDiagnostics().contains(
        new SyntacticScopeCreator.JSError(
            varMultiplyDeclaredError, "x", "testSourceName", null, 1, 1)));

    scope.clear();
    scope.declare("arguments", null, null, null);
    scope.declare("arguments", Node.IDENT("arguments"), null, null);

    redeclarationHandler.onRedeclaration(scope, "arguments", null, null, null, null);

    assert(compiler.getDiagnostics().contains(
        new SyntacticScopeCreator.JSError(
            varArgumentsShadowedError, null, "testSourceName", null, 1, 1)));
  }
}