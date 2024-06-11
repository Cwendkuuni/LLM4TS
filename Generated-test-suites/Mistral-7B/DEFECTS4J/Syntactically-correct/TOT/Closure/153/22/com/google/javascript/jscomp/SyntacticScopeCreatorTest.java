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
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private ScopeCreator sc;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    sc = new SyntacticScopeCreator(compiler);
    rootNode = createTestNode("function foo() { var x = 1; var x = 2; }");
  }

  @Test
  public void testCreateScopeForFunction() {
    Scope parent = new Scope(null, null);
    Scope scope = sc.createScope(rootNode, parent);

    assertNotNull(scope);
    assertEquals(parent, scope.getParent());
  }

  @Test
  public void testCreateGlobalScope() {
    Scope parent = null;
    Scope scope = sc.createScope(createTestNode("var x = 1"), parent);

    assertNotNull(scope);
    assertNull(scope.getParent());
  }

  @Test
  public void testSingleVariableDeclaration() {
    Node parentNode = createTestNode("var x = 1;");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
  }

  @Test
  public void testMultipleVariableDeclarations() {
    Node parentNode = createTestNode("var x = 1, y = 2;");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
    assertNotNull(y);
    assertEquals("y", y.getName());
    assertEquals(parentNode, y.getParentNode());
  }

  @Test
  public void testFunctionVariableDeclaration() {
    Node parentNode = createTestNode("function foo() { var x = 1; }");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
  }

  @Test
  public void testFunctionParameterDeclaration() {
    Node parentNode = createTestNode("function foo(x) {}");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar(0);
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
  }

  @Test
  public void testVariableShadowing() {
    Node parentNode = createTestNode("function foo() { var x = 1; var x = 2; }");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x1 = scope.getVar("x");
    assertNotNull(x1);
    assertEquals("x", x1.getName());
    assertEquals(parentNode, x1.getParentNode());

    Var x2 = scope.getVar("x");
    assertNotNull(x2);
    assertEquals("x", x2.getName());
    assertEquals(parentNode, x2.getParentNode());
    assertEquals(x1, x2);
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    Node parentNode = createTestNode("function foo() { var x = 1; var x = 2; }");
    Scope parent = new Scope(parentNode, null);
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope scope = sc.createScope(parentNode, parent);

    assertNull(sc.scope);
  }

  @Test
  public void testGlobalVariableDeclaration() {
    Node parentNode = createTestNode("var x = 1;");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
  }

  @Test
  public void testGlobalVariableDeclarationWithJSDoc() {
    Node parentNode = createTestNode("/** @type {number} */ var x = 1;");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
    assertEquals(JSType.NUMBER, x.getType());
  }

  @Test
  public void testGlobalVariableDeclarationWithJSDocAndDuplicate() {
    Node parentNode = createTestNode("/** @type {number} */ var x = 1; /** @type {boolean} */ var x = true;");
    Scope parent = new Scope(parentNode, null);
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope scope = sc.createScope(parentNode, parent);

    assertNull(sc.scope);
  }

  @Test
  public void testVariableDeclarationWithParentVariable() {
    Node parentNode = createTestNode("function foo() { var x = 1; var y = x; }");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(parentNode, x.getParentNode());
    assertNotNull(y);
    assertEquals("y", y.getName());
    assertEquals(parentNode, y.getParentNode());
    assertEquals(x, y.getVariable());
  }

  @Test
  public void testVariableDeclarationWithParentFunction() {
    Node parentNode = createTestNode("function foo() { var x = 1; } function bar() { var x = 2; }");
    Scope parent = new Scope(parentNode, null);
    Scope scope = sc.createScope(parentNode, parent);

    Var x1 = scope.getVar("x");
    Var x2 = scope.getVar("x");
    assertNotNull(x1);
    assertEquals("x", x1.getName());
    assertEquals(parentNode, x1.getParentNode());
    assertNotNull(x2);
    assertEquals("x", x2.getName());
    assertEquals(parentNode, x2.getParentNode());
    assertEquals(x1, x2);
  }

  @Test
  public void testVariableDeclarationWithParentFunctionAndDuplicate() {
    Node parentNode = createTestNode("function foo() { var x = 1; } function bar() { var x = 1; }");
    Scope parent = new Scope(parentNode, null);
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope scope = sc.createScope(parentNode, parent);

    assertNull(sc.scope);
  }

  private Node createTestNode(String code) {
    return NodeFactory.createFromString(code, null, null, null);
  }

  private static class DuplicateHandler implements SyntacticScopeCreator.RedeclarationHandler {

    @Override
    public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
      throw new RuntimeException("Duplicate variable declaration: " + name);
    }
  }
}