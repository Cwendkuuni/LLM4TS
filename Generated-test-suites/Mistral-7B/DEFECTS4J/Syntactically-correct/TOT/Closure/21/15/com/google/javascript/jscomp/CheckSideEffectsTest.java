package com.google.javascript.jscomp.tests;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.CheckerFactory.checkerName;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOutput;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Unit tests for CheckSideEffects class.
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CompilerPass checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    CheckLevel level = WARNING;

    // Test empty program
    testProgram(compiler, options, checkSideEffects, level);

    // Test expression statement
    testExpressionStatement(compiler, options, checkSideEffects, level);

    // Test statement statement
    testStatementStatement(compiler, options, checkSideEffects, level);

    // Test statement with side effect
    testStatementWithSideEffect(compiler, options, checkSideEffects, level);

    // Test compound assignment
    testCompoundAssignment(compiler, options, checkSideEffects, level);

    // Test for loop
    testForLoop(compiler, options, checkSideEffects, level);

    // Test if statement
    testIfStatement(compiler, options, checkSideEffects, level);

    // Test switch statement
    testSwitchStatement(compiler, options, checkSideEffects, level);

    // Test try statement
    testTryStatement(compiler, options, checkSideEffects, level);

    // Test label statement
    testLabelStatement(compiler, options, checkSideEffects, level);

    // Test with statement
    testWithStatement(compiler, options, checkSideEffects, level);

    // Test expressions with side effects
    testExpressionsWithSideEffects(compiler, options, checkSideEffects, level);

    // Test statements with side effects
    testStatementsWithSideEffects(compiler, options, checkSideEffects, level);

    // Test statements with no side effects
    testStatementsWithNoSideEffects(compiler, options, checkSideEffects, level);

    // Test statements with side effects and uses
    testStatementsWithSideEffectsAndUses(compiler, options, checkSideEffects, level);

    // Test function calls with side effects
    testFunctionCallsWithSideEffects(compiler, options, checkSideEffects, level);

    // Test function calls with no side effects
    testFunctionCallsWithNoSideEffects(compiler, options, checkSideEffects, level);

    // Test object literals
    testObjectLiterals(compiler, options, checkSideEffects, level);

    // Test array literals
    testArrayLiterals(compiler, options, checkSideEffects, level);

    // Test statements inside blocks
    testStatementsInsideBlocks(compiler, options, checkSideEffects, level);
  }

  private void testProgram(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "// Test empty program.";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testExpressionStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = 1;";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testStatementStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = 1;\nvar y = 2;";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testStatementWithSideEffect(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = document.createElement('div');";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(1, results.getDiagnostics().size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. document.createElement('div')",
        results.getDiagnostics().get(0).getMessage());
  }

  private void testCompoundAssignment(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = 1;\nx += 1;";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testForLoop(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "for (var i = 0; i < 10; i++) {}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testIfStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "if (1 == 1) {}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testSwitchStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "switch (1) { case 1: break; }";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testTryStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "try {} catch (e) {}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testLabelStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "label: for (var i = 0; i < 10; i++) {}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testWithStatement(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "with (obj) {}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testExpressionsWithSideEffects(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = document.createElement('div');";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(1, results.getDiagnostics().size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. document.createElement('div')",
        results.getDiagnostics().get(0).getMessage());
  }

  private void testStatementsWithSideEffects(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = document.createElement('div');\nvar y = 1;";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(1, results.getDiagnostics().size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. document.createElement('div')",
        results.getDiagnostics().get(0).getMessage());
  }

  private void testStatementsWithNoSideEffects(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = 1;\nvar y = 2;";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testStatementsWithSideEffectsAndUses(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "var x = 1;\nvar y = document.createElement('div');";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testFunctionCallsWithSideEffects(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "document.createElement('div').appendChild(document.createElement('div'));";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(1, results.getDiagnostics().size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. document.createElement('div').appendChild(document.createElement('div'))",
        results.getDiagnostics().get(0).getMessage());
  }

  private void testFunctionCallsWithNoSideEffects(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "Math.pow(2, 3);";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testObjectLiterals(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "{a: 1, b: 2}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testArrayLiterals(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "[1, 2]";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(0, results.getDiagnostics().size());
  }

  private void testStatementsInsideBlocks(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level) {
    String jsCode = "if (1 == 1) {\n  var x = document.createElement('div');\n}";
    CompilerResults results = testAndCompile(compiler, options, checkSideEffects,
        level, jsCode);
    assertEquals(1, results.getDiagnostics().size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. document.createElement('div')",
        results.getDiagnostics().get(0).getMessage());
  }

  private CompilerResults testAndCompile(Compiler compiler, CompilerOptions options,
      CheckSideEffects checkSideEffects, CheckLevel level, String jsCode) {
    CompilerInput input = new CompilerInput(jsCode, "Test", null, null, null);
    CompilerOutput output = compiler.compile(input, options);
    CompilerResults results = new CompilerResults(output, input);
    checkSideEffects.process(input.getAstRoot(compiler), null);
    return results;
  }
}