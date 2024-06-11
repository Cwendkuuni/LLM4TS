import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSTypeFactory;

import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node root = Node.createTree("var x = 1;");
    Scope globalScope = creator.createScope(root, null);

    Var xVar = globalScope.getVar("x");
    assertEquals(JSTypeFactory.forSimpleType(JSType.NUMBER), xVar.type);
    assertEquals(1, globalScope.getSize());
  }

  @Test
  public void testCreateLocalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "function test() {"
        + "  var x = 1;"
        + "  var y = 2;"
        + "  function foo() {}"
        + "}"
    );

    Scope parentScope = Node.createTree("function test() {}");
    Scope localScope = creator.createScope(root, parentScope);

    Var xVar = localScope.getVar("x");
    assertEquals(JSTypeFactory.forSimpleType(JSType.NUMBER), xVar.type);

    Var yVar = localScope.getVar("y");
    assertEquals(JSTypeFactory.forSimpleType(JSType.NUMBER), yVar.type);

    assertEquals(2, localScope.getSize());

    Node foo = localScope.getChild(2);
    Var fooVar = localScope.getVar("foo");

    assertEquals(JSTypeFactory.forFunctionType(), fooVar.type);
    assertEquals(3, localScope.getSize());
  }

  @Test
  public void testCreateScopeWithFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "function test() {"
        + "  function foo() {}"
        + "}"
    );

    Scope parentScope = Node.createTree("function test() {}");
    Scope localScope = creator.createScope(root, parentScope);

    Var fooVar = localScope.getVar("foo");

    assertEquals(JSTypeFactory.forFunctionType(), fooVar.type);
    assertEquals(1, localScope.getSize());
  }

  @Test
  public void testCreateScopeWithCatchClause() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "try {"
        + "  throw new Error('Test');"
        + "}"
        + " catch (x) {"
        + "  var x = 1;"
        + "}"
    );

    Scope parentScope = Node.createTree("try {}");
    Scope localScope = creator.createScope(root, parentScope);

    Var xVar = localScope.getVar("x");
    assertEquals(JSTypeFactory.forSimpleType(JSType.NUMBER), xVar.type);
    assertEquals(1, localScope.getSize());
  }

  @Test
  public void testCreateScopeWithDuplicateDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "function test() {"
        + "  var x = 1;"
        + "  var x = 2;"
        + "}"
    );

    DiagnosticGroup diagnostics = new DiagnosticGroup();
    Scope parentScope = Node.createTree("function test() {}");
    Scope localScope = creator.createScope(root, parentScope, diagnostics);

    assertEquals(1, localScope.getSize());
    assertEquals(1, diagnostics.getDiagnostics().size());

    JSError error = diagnostics.getDiagnostics().get(0);
    assertEquals("Variable x first declared in test", error.getMessage());
  }

  @Test
  public void testCreateScopeWithDuplicateDeclarationInGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "var x = 1;"
        + "var x = 2;"
    );

    DiagnosticGroup diagnostics = new DiagnosticGroup();
    Scope globalScope = creator.createScope(root, null, diagnostics);

    assertEquals(0, globalScope.getSize());
    assertEquals(1, diagnostics.getDiagnostics().size());

    JSError error = diagnostics.getDiagnostics().get(0);
    assertEquals("Variable x first declared in ?", error.getMessage());
  }

  @Test
  public void testCreateScopeWithDuplicateDeclarationInLocalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "function test() {"
        + "  var x = 1;"
        + "  function test() {"
        + "  var x = 2;"
        + "  }"
        + "}"
    );

    DiagnosticGroup diagnostics = new DiagnosticGroup();
    Scope parentScope = Node.createTree("function test() {}");
    Scope localScope = creator.createScope(root, parentScope, diagnostics);

    assertEquals(0, localScope.getSize());
    assertEquals(1, diagnostics.getDiagnostics().size());

    JSError error = diagnostics.getDiagnostics().get(0);
    assertEquals("Function test shadowed \"arguments\"", error.getMessage());
  }

  @Test
  public void testCreateScopeWithArgumentsDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "function test(x) {"
        + "  var x = 1;"
        + "}"
    );

    DiagnosticGroup diagnostics = new DiagnosticGroup();
    Scope parentScope = Node.createTree("function test(x) {}");
    Scope localScope = creator.createScope(root, parentScope, diagnostics);

    Var xVar = localScope.getVar("x");
    assertEquals(JSTypeFactory.forSimpleType(JSType.NUMBER), xVar.type);
    assertEquals(1, localScope.getSize());
  }

  @Test
  public void testCreateScopeWithArgumentsDeclarationInGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node root = Node.createTree(
        "function test(x) {"
        + "}"
    );

    DiagnosticGroup diagnostics = new DiagnosticGroup();
    Scope globalScope = creator.createScope(root, null, diagnostics);

    assertEquals(0, globalScope.getSize());
    assertEquals(1, diagnostics.getDiagnostics().size());

    JSError error = diagnostics.getDiagnostics().get(0);
    assertEquals("Shadowing \"arguments\" is not allowed", error.getMessage());
  }
}