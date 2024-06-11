package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RenameVarsTest {
  @Test
  public void testSimpleRename() {
    String code = "function foo(a, b) { return a + b; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, null, false, false, false, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    Node externs = result.getOutput().asSource();
    Node root = NodeUtil.parse(code);

    renamer.process(externs, root);

    String renamedCode = result.getOutput().getSourceCode();
    assertTrue("Code should have been changed.", renamedCode.length() < code.length());

    String expectedCode = "function fa(a, b) { return a + b; }";
    assertEquals("Code should be renamed to.\n" + expectedCode, renamedCode);
  }

  @Test
  public void testLocalRename() {
    String code = "function foo(a, b) { var c = a; return c + b; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, null, true, false, false, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    Node externs = result.getOutput().asSource();
    Node root = NodeUtil.parse(code);

    renamer.process(externs, root);

    String renamedCode = result.getOutput().getSourceCode();
    assertTrue("Code should have been changed.", renamedCode.length() < code.length());

    String expectedCode = "function foo(a, b) { var d = a; return d + b; }";
    assertEquals("Code should be renamed to.\n" + expectedCode, renamedCode);
  }

  @Test
  public void testExternRename() {
    String code = "var a = 1, b = 2; function foo(a, b) { return a + b; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, null, false, false, false, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    Node externs = result.getOutput().asSource();
    Node root = NodeUtil.parse(code);

    renamer.process(externs, root);

    String renamedCode = result.getOutput().getSourceCode();
    assertTrue("Code should have been changed.", renamedCode.length() < code.length());

    String expectedCode = "var a_1 = 1, b_1 = 2; function foo(a, b) { return a + b; }";
    assertEquals("Code should be renamed to.\n" + expectedCode, renamedCode);
  }

  @Test
  public void testGlobalRename() {
    String code = "var a = 1, b = 1; function foo() { var a = 2; return a + b; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, "g_", false, false, false, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    Node externs = result.getOutput().asSource();
    Node root = NodeUtil.parse(code);

    renamer.process(externs, root);

    String renamedCode = result.getOutput().getSourceCode();
    assertTrue("Code should have been changed.", renamedCode.length() < code.length());

    String expectedCode = "var g_a_1 = 1, b = 1; function foo() { var g_a_2 = 2; return g_a_2 + b; }";
    assertEquals("Code should be renamed to.\n" + expectedCode, renamedCode);
  }

  @Test
  public void testPreserveAnonymousFunctionNames() {
    String code = "function foo() { return (function(a) { return a; })(1); }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, null, false, true, false, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    Node externs = result.getOutput().asSource();
    Node root = NodeUtil.parse(code);

    renamer.process(externs, root);

    String renamedCode = result.getOutput().getSourceCode();
    assertTrue("Code should have been changed.", renamedCode.length() < code.length());

    String expectedCode = "function foo() { return (function(a) { return a; })(1); }";
    assertEquals("Code should not be renamed.\n" + expectedCode, renamedCode);
  }

  @Test
  public void testGeneratePseudoNames() {
    String code = "function foo() { var a = 1, b = 2; return a + b; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, null, false, false, true, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    Node externs = result.getOutput().asSource();
    Node root = NodeUtil.parse(code);

    renamer.process(externs, root);

    String renamedCode = result.getOutput().getSourceCode();
    assertTrue("Code should have been changed.", renamedCode.length() < code.length());

    String expectedCode = "function foo() { var $a_$1 = 1, $b_$2 = 2; return $a_$1 + $b_$2; }";
    assertEquals("Code should be renamed with pseudo names.\n" + expectedCode, renamedCode);
  }

  @Test
  public void testErrorHandling() {
    String code = "var a = 1, b = 2; function foo(a, b) { return a + b; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renamer = new RenameVars(compiler, null, false, false, false, null, null, null);

    CompilationResult result = compiler.compile(code, options, null, null);
    List<String> errors = result.getErrors();
    assertFalse("Should not have compilation errors.", errors.isEmpty());

    // Intentionally causing an error by providing a null set of reserved names.
    Set<String> reservedNames = null;
    renamer = new RenameVars(compiler, null, false, false, false, null, null, reservedNames);

    CompilationResult errorResult = compiler.compile(code, options, null, null);
    List<String> errorErrors = errorResult.getErrors();
    assertTrue("Should have compilation errors.", !errorErrors.isEmpty());
    String errorMessage = errorErrors.get(0);
    assertTrue("Error message should indicate an issue with reservedNames.",
        errorMessage.contains("Error: Set<String> cannot be null"));

    // Resetting the compiler and renamer to their original state.
    compiler = new AbstractCompiler();
    renamer = new RenameVars(compiler, null, false, false, false, null, null, null);
  }
}