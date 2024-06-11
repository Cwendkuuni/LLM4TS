import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private ScopeCreator creator;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    creator = new SyntacticScopeCreator(compiler);
    rootNode = createNode("function bar() { var x = 1; var x = 2; }");
  }

  @Test
  public void testVarDeclaredTwiceInFunction() {
    Scope parent = new Scope(null);
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(2, scope.size());

    Var x1 = scope.getVar("x");
    assertEquals("x", x1.name);
    assertEquals(Node.FUNCTION, x1.node.getType());
    assertEquals(NodeUtil.getLineNumber(x1.node), 3);

    Var x2 = scope.getVar("x");
    assertEquals("x", x2.name);
    assertEquals(Node.FUNCTION, x2.node.getType());
    assertEquals(NodeUtil.getLineNumber(x2.node), 4);

    assertEquals(DiagnosticType.ERROR, x1.diagnosticType);
    assertEquals(DiagnosticType.ERROR, x2.diagnosticType);
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", x1.diagnosticType.name());
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", x2.diagnosticType.name());
  }

  @Test
  public void testVarDeclaredTwiceInGlobalScope() {
    Scope parent = null;
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var x = scope.getVar("x");
    assertEquals("x", x.name);
    assertNull(x.diagnosticType);
  }

  @Test
  public void testVarDeclaredTwiceAtSameLineInFunction() {
    rootNode = createNode("function bar() { var x = 1; var x = 1; }");
    Scope parent = new Scope(null);
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var x = scope.getVar("x");
    assertEquals("x", x.name);
    assertEquals(DiagnosticType.ERROR, x.diagnosticType);
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", x.diagnosticType.name());
  }

  @Test
  public void testVarDeclaredTwiceInCatchBlock() {
    rootNode = createNode("try { throw 1; } catch (x) { var x = 2; }");
    Scope parent = new Scope(null);
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var x = scope.getVar("x");
    assertEquals("x", x.name);
    assertEquals(DiagnosticType.ERROR, x.diagnosticType);
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", x.diagnosticType.name());
  }

  @Test
  public void testVarDeclaredTwiceInScriptBlock() {
    rootNode = createNode("var x = 1; var x = 2;");
    Scope parent = null;
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var x = scope.getVar("x");
    assertEquals("x", x.name);
    assertEquals(DiagnosticType.ERROR, x.diagnosticType);
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", x.diagnosticType.name());
  }

  @Test
  public void testVarDeclaredTwiceWithJSDoc() {
    rootNode = createNode("/** @suppress {duplicates} */ function bar() { /** @type {number} */ var x = 1; /** @type {number} */ var x = 2; }");
    Scope parent = new Scope(null);
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var x = scope.getVar("x");
    assertEquals("x", x.name);
    assertNull(x.diagnosticType);
  }

  @Test
  public void testArgumentsShadowed() {
    rootNode = createNode("function foo(x) { var arguments = x; }");
    Scope parent = new Scope(null);
    try {
      creator.createScope(rootNode, parent);
      fail("Argument 'x' should be shadowed by 'arguments'.");
    } catch (AssertionError e) {
      assertEquals("Shadowing \"arguments\" is not allowed", e.getMessage());
    }
  }

  @Test
  public void testFunctionDeclarationWithJSDocInFunction() {
    rootNode = createNode("function foo() { /** @param {number} x */ /** @this {Number} */ function bar(x) { } }");
    Scope parent = new Scope(null);
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var bar = scope.getVar("bar");
    assertEquals("bar", bar.name);
    assertNotNull(bar.declaredType);
    assertEquals("function", bar.declaredType.getClassName());
    assertEquals("number", bar.declaredType.getParameterTypes()[0].getName());
    assertEquals("Number", bar.declaredType.getThisType().getName());
    assertNull(bar.diagnosticType);
  }

  @Test
  public void testFunctionDeclarationWithJSDocInGlobalScope() {
    rootNode = createNode("/** @param {number} x */ /** @this {Number} */ function foo(x) { }");
    Scope parent = null;
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var foo = scope.getVar("foo");
    assertEquals("foo", foo.name);
    assertNotNull(foo.declaredType);
    assertEquals("function", foo.declaredType.getClassName());
    assertNull(foo.diagnosticType);
  }

  @Test
  public void testFunctionDeclarationWithoutJSDocInFunction() {
    rootNode = createNode("function foo() { function bar() { } }");
    Scope parent = new Scope(null);
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var bar = scope.getVar("bar");
    assertEquals("bar", bar.name);
    assertNull(bar.declaredType);
    assertNull(bar.diagnosticType);
  }

  @Test
  public void testFunctionDeclarationWithoutJSDocInGlobalScope() {
    rootNode = createNode("function foo() { function bar() { } }");
    Scope parent = null;
    Scope scope = creator.createScope(rootNode, parent);
    assertEquals(1, scope.size());

    Var foo = scope.getVar("foo");
    assertEquals("foo", foo.name);
    assertNotNull(foo.declaredType);
    assertEquals("function", foo.declaredType.getClassName());
    assertNull(foo.diagnosticType);
  }

  private Node createNode(String code) {
    return RhinoCompiler.compileStringToNode(code);
  }
}