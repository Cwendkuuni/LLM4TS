package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.DiagnosticGroups.ACCESS_CONTROLS;
import static com.google.javascript.jscomp.DiagnosticGroups.INVALID_CASTS;
import static com.google.javascript.jscomp.DiagnosticGroups.CHECK_TYPES;
import static com.google.javascript.jscomp.DiagnosticGroups.CHECK_VARIABLES;
import static com.google.javascript.jscomp.DiagnosticGroups.MISSING_PROPERTIES;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.testing.Tests;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testAccessControl() throws Exception {
    String jsCode = "function foo() { class A { } class B extends A { } var a = new A(); var b = new B(); }";
    Compiler compiler = new Compiler();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(ACCESS_CONTROLS, CompilerOptions.WarningLevel.ERROR);

    DiagnosticType error = Tests.compile(compiler, jsCode, options).getDiagnostics().get(0);
    assertEquals("Private constructor 'B' called from 'foo'", error.getMessage());
  }

  @org.junit.Test
  public void testInvalidCast() throws Exception {
    String jsCode = "function foo() { var x = 1; var y = (String) x; }";
    Compiler compiler = new Compiler();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(INVALID_CASTS, CompilerOptions.WarningLevel.ERROR);

    DiagnosticType error = Tests.compile(compiler, jsCode, options).getDiagnostics().get(0);
    assertEquals("Type error: Cannot cast number to String", error.getMessage());
  }

  @org.junit.Test
  public void testTypeCheck() throws Exception {
    String jsCode = "function foo() { var x = 1; var y = x.toString(); }";
    Compiler compiler = new Compiler();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CHECK_TYPES, CompilerOptions.WarningLevel.ERROR);

    DiagnosticType error = Tests.compile(compiler, jsCode, options).getDiagnostics().get(0);
    assertEquals("Type error: Cannot call method 'toString' on number", error.getMessage());
  }

  @org.junit.Test
  public void testVariableCheck() throws Exception {
    String jsCode = "function foo() { var x; }";
    Compiler compiler = new Compiler();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CHECK_VARIABLES, CompilerOptions.WarningLevel.ERROR);

    DiagnosticType error = Tests.compile(compiler, jsCode, options).getDiagnostics().get(0);
    assertEquals("Variable 'x' is not defined", error.getMessage());
  }

  @org.junit.Test
  public void testMissingProperty() throws Exception {
    String jsCode = "function foo() { var obj = {}; obj.missingProperty = 1; }";
    Compiler compiler = new Compiler();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(MISSING_PROPERTIES, CompilerOptions.WarningLevel.ERROR);

    DiagnosticType error = Tests.compile(compiler, jsCode, options).getDiagnostics().get(0);
    assertEquals("Property 'missingProperty' does not exist on value of type Object", error.getMessage());
  }

  @org.junit.Test
  public void testRegisterDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("accessControls",
        DiagnosticGroups.CheckAccessControls.DEPRECATED,
        DiagnosticGroups.CheckAccessControls.DEPRECATED_PROP,
        DiagnosticGroups.CheckAccessControls.DEPRECATED_CLASS,
        DiagnosticGroups.CheckAccessControls.PRIVATE_OVERRIDE,
        DiagnosticGroups.CheckAccessControls.VISIBILITY_MISMATCH);

    assertEquals(6, diagnosticGroups.getRegisteredGroups().size());
    assertEquals("accessControls", diagnosticGroups.getRegisteredGroups().get("accessControls").getName());
  }
}