package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Set;

/**
 * Tests for {@link VarCheck}.
 */
public class VarCheckTest {

  private static final DiagnosticType UNDEFINED_VAR_ERROR =
      DiagnosticType.error("JSC_UNDEFINED_VARIABLE",
          "variable {0} is undefined");

  private static final DiagnosticType MISSING_MODULE_DEP_ERROR =
      DiagnosticType.warning("JSC_MISSING_MODULE_DEPENDENCY",
          "missing module dependency; module {0} should depend " +
          "on module {1} because it references {2}");

  private static final DiagnosticType VIOLATED_MODULE_DEP_ERROR =
      DiagnosticType.error("JSC_VIOLATED_MODULE_DEPENDENCY",
          "module {0} cannot reference {2}, defined in " +
          "module {1}, since {1} loads after {0}");

  private static final DiagnosticType NAME_REFERENCE_IN_EXTERNS_ERROR =
      DiagnosticType.warning("JSC_NAME_REFERENCE_IN_EXTERNS",
          "accessing name {0} in externs has no effect");

  private static final DiagnosticType UNDEFINED_EXTERN_VAR_ERROR =
      DiagnosticType.warning("JSC_UNDEFINED_EXTERN_VAR_ERROR",
          "name {0} is not undefined in the externs.");

  private static final DiagnosticType INVALID_FUNCTION_DECL =
      DiagnosticType.error("JSC_INVALID_FUNCTION_DECL",
          "Syntax error: function declaration must have a name");

  private static final DiagnosticType STRICT_MODULE_DEP_ERROR =
      DiagnosticType.disabled("JSC_STRICT_MODULE_DEPENDENCY",
          "module {0} cannot reference {2}, defined in " +
          "module {1}");

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = new TestCompiler();
    nodeFactory = compiler.getFactory();
  }

  public void testUndefinedVariable() {
    String code = "function foo() { x = 42; }";
    CompilerPass pass = new VarCheck(compiler);
    Node input = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input), pass);
    Scope scope = pass.getScope();
    assertNull(scope.getVar("x"));
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testUndefinedVariableInGlobalScope() {
    String code = "x = 42;";
    CompilerPass pass = new VarCheck(compiler, true);
    Node input = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input), pass);
    Scope scope = pass.getScope();
    assertNull(scope.getVar("x"));
  }

  public void testUndefinedVariableWithStrictExternCheck() {
    String code = "module A {\n" +
        "  export function foo() { x = 42; }\n" +
        "}";
    CompilerPass pass = new VarCheck(compiler, false, true);
    CompilerInput input = compiler.newInput("a.js", code, null);
    Node root = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getAstRoot(compiler), root), pass);
    Scope scope = pass.getScope();
    assertNull(scope.getVar("x"));
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(STRICT_MODULE_DEP_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testUndefinedVariableWithMissingModuleDependency() {
    String codeA = "module A {\n" +
        "  export function foo() { import B; x = 42; }\n" +
        "}";
    String codeB = "module B {}";
    CompilerPass pass = new VarCheck(compiler, false);
    CompilerInput aInput = compiler.newInput("a.js", codeA, null);
    CompilerInput bInput = compiler.newInput("b.js", codeB, null);
    Node aRoot = NodePrinter.parseJs(codeA, null);
    Node bRoot = NodePrinter.parseJs(codeB, null);
    NodeTraversal.traverseRoots(compiler,
        Lists.newArrayList(aInput.getAstRoot(compiler), bRoot), pass);
    Scope scopeA = pass.getScope();
    assertNull(scopeA.getVar("x"));
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(MISSING_MODULE_DEP_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testVariableWithModuleDependency() {
    String codeA = "module A {\n" +
        "  export function foo() { import B; x = B.y; }\n" +
        "}";
    String codeB = "module B {}";
    CompilerPass pass = new VarCheck(compiler, false);
    CompilerInput aInput = compiler.newInput("a.js", codeA, null);
    CompilerInput bInput = compiler.newInput("b.js", codeB, null);
    Node aRoot = NodePrinter.parseJs(codeA, null);
    Node bRoot = NodePrinter.parseJs(codeB, null);
    NodeTraversal.traverseRoots(compiler,
        Lists.newArrayList(aInput.getAstRoot(compiler), bRoot), pass);
    Scope scopeA = pass.getScope();
    Scope.Var var = scopeA.getVar("x");
    assertEquals("B", var.input.getModule().getName());
  }

  public void testVariableWithNameReferenceInExterns() {
    String code = "var x = y;";
    CompilerPass pass = new VarCheck(compiler, false);
    Node input = NodePrinter.parseJs("(function() {\n" + code + "\n})()", null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input), pass);
    Scope scope = pass.getScope();
    Scope.Var var = scope.getVar("x");
    assertEquals("x", var.name);
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(NAME_REFERENCE_IN_EXTERNS_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testDeclaredVariableInExterns() {
    String code = "var x = 42;";
    CompilerPass pass = new VarCheck(compiler, false);
    CompilerInput input = compiler.newExternInput("{x: number}");
    Node root = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getAstRoot(compiler), root), pass);
    Scope scope = pass.getScope();
    Scope.Var var = scope.getVar("x");
    assertEquals("x", var.name);
    assertEquals(0, pass.getDiagnostics().size());
  }

  public void testUndefinedVariableInExterns() {
    String code = "var x = 42;";
    CompilerPass pass = new VarCheck(compiler, false);
    CompilerInput input = compiler.newExternInput("{y: number}");
    Node root = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getAstRoot(compiler), root), pass);
    Scope scope = pass.getScope();
    Scope.Var var = scope.getVar("x");
    assertNull(var);
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testUndefinedVariableInFunctionDeclaration() {
    String code = "function foo() { var x = 42; }";
    CompilerPass pass = new VarCheck(compiler, false);
    Node input = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input), pass);
    Scope scope = pass.getScope();
    assertNull(scope.getVar("x"));
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testUndefinedVariableInFunctionExpression() {
    String code = "var foo = function() { var x = 42; }";
    CompilerPass pass = new VarCheck(compiler, false);
    Node input = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input), pass);
    Scope scope = pass.getScope();
    assertNull(scope.getVar("x"));
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, pass.getDiagnostics().get(0).getType());
  }

  public void testInvalidFunctionDeclaration() {
    String code = "function() {}";
    CompilerPass pass = new VarCheck(compiler, false);
    Node input = NodePrinter.parseJs(code, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input), pass);
    assertEquals(1, pass.getDiagnostics().size());
    assertEquals(INVALID_FUNCTION_DECL, pass.getDiagnostics().get(0).getType());
  }
}