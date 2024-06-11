package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Set;

/**
 * Test cases for VarCheck class.
 */
public class VarCheckTest {

  private static void testVarCheck(boolean strictExternCheck) {
    CompilerOptions options = new CompilerOptions(null);
    TestCompiler compiler = new TestCompiler();

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    VarCheck varCheck = new VarCheck(passManager.getAbstractCompiler(), strictExternCheck);
    passManager.addPass(varCheck);

    String input = "function foo() { bar(); }";
    CompilerInput inputCompiled = compiler.compile(input, null, null);
    Node root = inputCompiled.getAstRoot(compiler);

    // Check that foo is declared.
    Scope scope = NodeTraversal.scopeAt(compiler, root, 0);
    assertEquals(scope.getVars().size(), 1);
    Scope.Var fooVar = scope.getVars().get(0);
    assertEquals(fooVar.getName(), "foo");
    assertNull(fooVar.getInput());

    // Check that bar is not declared.
    Node barNode = NodeUtil.findFirstChildOfType(root, Token.NAME, "bar");
    assertNotNull(barNode);
    assertNull(NodeUtil.findVar(scope, "bar"));

    passManager.runPasses(inputCompiled, ImmutableList.of(root));

    if (strictExternCheck) {
      // Check that an error was reported for bar reference.
      assertTrue(compiler.getDiagnostics().hasError());

      // Check that error message is correct.
      Diagnostic error = compiler.getDiagnostics().getFirstError();
      assertEquals("JSC_UNDEFINED_VARIABLE", error.getType().getName());
      assertEquals("bar", error.getMessage(0));
    } else {
      // Check that no error was reported.
      assertFalse(compiler.getDiagnostics().hasError());

      // Check that synthesized externs input was created.
      CompilerInput synthesizedExterns = varCheck.getSynthesizedExternsInput();
      assertNotNull(synthesizedExterns);
      assertNotNull(synthesizedExterns.getAstRoot(compiler));

      // Check that a new variable for bar was created in synthesized externs.
      Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
      Node synthesizedExternsBarNode = NodeUtil.findFirstChildOfType(
          synthesizedExternsRoot, Token.VAR, "bar");
      assertNotNull(synthesizedExternsBarNode);
    }
  }

  @org.junit.Test
  public void testVarCheckNoErrors() {
    testVarCheck(false);
  }

  @org.junit.Test
  public void testVarCheckStrictExternCheckErrors() {
    testVarCheck(true);
  }

  private static void testVarCheckWithModuleDeps(boolean strictExternCheck) {
    CompilerOptions options = new CompilerOptions(null);
    TestCompiler compiler = new TestCompiler();

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    VarCheck varCheck = new VarCheck(passManager.getAbstractCompiler(), strictExernCheck);
    passManager.addPass(varCheck);

    String input = "module A {\n" +
        "  function foo() {\n" +
        "    bar();\n" +
        "  }\n" +
        "}\n" +

        "module B {\n" +
        "  function bar() {}\n" +
        "}" +
        "module C {\n" +
        "  import A;\n" +
        "  import B;\n" +
        "  function qux() {\n" +
        "    A.foo();\n" +
        "    B.bar();\n" +
        "  }\n" +
        "}";
    CompilerInput inputCompiled = compiler.compile(input, null, null);
    Node root = inputCompiled.getAstRoot(compiler);

    // Check that foo is declared in A and bar is declared in B.
    Scope aScope = NodeTraversal.scopeAt(compiler, root, 0);
    assertEquals(aScope.getVars().size(), 1);
    Scope.Var fooVar = aScope.getVars().get(0);
    assertEquals(fooVar.getName(), "foo");
    assertEquals(aScope, fooVar.scope);
    assertNull(fooVar.getInput());

    Scope bScope = NodeTraversal.scopeAt(compiler, root, 1);
    assertEquals(bScope.getVars().size(), 1);
    Scope.Var barVar = bScope.getVars().get(0);
    assertEquals(barVar.getName(), "bar");
    assertEquals(bScope, barVar.scope);
    assertNull(barVar.getInput());

    passManager.runPasses(inputCompiled, ImmutableList.of(root));

    if (strictExternCheck) {
      // Check that no errors were reported.
      assertFalse(compiler.getDiagnostics().hasError());
    } else {
      // Check that errors were reported for A.foo() and B.bar() references
      // in C.qux().
      int numErrors = compiler.getDiagnostics().size();
      assertEquals(2, numErrors);

      // Check that error messages are correct.
      Diagnostic error1 = compiler.getDiagnostics().get(0);
      assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", error1.getType().getName());
      assertEquals("module A", error1.getMessage(0));
      assertEquals("module C", error1.getMessage(1));
      assertEquals("foo", error1.getMessage(2));

      Diagnostic error2 = compiler.getDiagnostics().get(1);
      assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", error2.getType().getName());
      assertEquals("module B", error2.getMessage(0));
      assertEquals("module C", error2.getMessage(1));
      assertEquals("bar", error2.getMessage(2));
    }
  }

  @org.junit.Test
  public void testVarCheckNoModuleDepsErrors() {
    testVarCheckWithModuleDeps(false);
  }

  @org.junit.Test
  public void testVarCheckStrictModuleDepsErrors() {
    testVarCheckWithModuleDeps(true);
  }

  @org.junit.Test
  public void testVarCheckMissingModuleDepsWarning() {
    CompilerOptions options = new CompilerOptions(null);
    TestCompiler compiler = new TestCompiler();

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    VarCheck varCheck = new VarCheck(passManager.getAbstractCompiler(), false);
    passManager.addPass(varCheck);

    String input = "module A {\n" +
        "  function foo() {\n" +
        "    bar();\n" +
        "  }\n" +
        "}\n" +

        "module B {\n" +
        "  function bar() {}\n" +
        "}" +
        "module C {\n" +
        "  import A;\n" +
        "  import notExists;\n" + // missing import statement
        "  function qux() {\n" +
        "    A.foo();\n" +
        "  }\n" +
        "}";
    CompilerInput inputCompiled = compiler.compile(input, null, null);
    Node root = inputCompiled.getAstRoot(compiler);

    passManager.runPasses(inputCompiled, ImmutableList.of(root));

    // Check that a warning was reported for missing module dependency.
    int numWarnings = compiler.getDiagnostics().size();
    assertEquals(1, numWarnings);

    Diagnostic warning = compiler.getDiagnostics().get(0);
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", warning.getType().getName());
    assertEquals("module C", warning.getMessage(0));
    assertEquals("module notExists", warning.getMessage(1));
    assertEquals("A.foo", warning.getMessage(2));
  }

  @org.junit.Test
  public void testVarCheckMissingModuleDepsWarningWithStrictExternCheck() {
    CompilerOptions options = new CompilerOptions(null);
    TestCompiler compiler = new TestCompiler();

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    VarCheck varCheck = new VarCheck(passManager.getAbstractCompiler(), true);
    passManager.addPass(varCheck);

    String input = "module A {\n" +
        "  function foo() {\n" +
        "    bar();\n" +
        "  }\n" +
        "}\n" +

        "module B {\n" +
        "  function bar() {}\n" +
        "}" +
        "module C {\n" +
        "  import A;\n" +
        "  import notExists;\n" + // missing import statement
        "  function qux() {\n" +
        "    A.foo();\n" +
        "  }\n" +
        "}";
    CompilerInput inputCompiled = compiler.compile(input, null, null);
    Node root = inputCompiled.getAstRoot(compiler);

    passManager.runPasses(inputCompiled, ImmutableList.of(root));

    // Check that an error was reported for missing module dependency.
    int numErrors = compiler.getDiagnostics().size();
    assertEquals(1, numErrors);

    Diagnostic error = compiler.getDiagnostics().get(0);
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", error.getType().getName());
    assertEquals("module C", error.getMessage(0));
    assertEquals("module notExists", error.getMessage(1));
    assertEquals("A.foo", error.getMessage(2));
  }
}