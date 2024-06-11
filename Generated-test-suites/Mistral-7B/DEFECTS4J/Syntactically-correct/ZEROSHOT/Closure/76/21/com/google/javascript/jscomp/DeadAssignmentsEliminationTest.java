package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterCallback.ENTER;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterCallback.EXIT;
import static com.google.javascript.jscomp.NodeTraversals.assertTreeEquals;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.DataSource;
import com.google.javascript.jscomp.GraphTestUtil;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.test.TestSourceFile;

import java.io.IOException;
import java.util.Arrays;

public class DeadAssignmentsEliminationTest {

  private static DeadAssignmentsElimination createDeadAssignmentsElimination(
      AbstractCompiler compiler) {
    return new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentsElimination() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setCompilationLevel(CompilationLevel.ADVANCED);

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DataSource getSourceFile(String sourceFileName) {
        return new TestSourceFile(sourceFileName);
      }

      @Override
      public JSErrorReporter getErrorReporter() {
        return null;
      }

      @Override
      public void setGenerator(NodeGenerator generator) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void compile(Node externs, Node root) throws IOException {
        CompilerPass pass = createDeadAssignmentsElimination(this);
        pass.process(externs, root);
      }

      @Override
      public void reportCodeChange() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void reportError(int line, String message) {
        throw new UnsupportedOperationException();
      }
    };

    // Test case 1: Dead assignment elimination is not done for global variables.
    String input1 = "function foo() {}";
    String expected1 = "function foo() {}";
    test(input1, expected1);

    // Test case 2: Dead assignment elimination is not done for inner functions.
    String input2 = "function foo() {function bar() {} }";
    String expected2 = "function foo() {function bar() {} }";
    test(input2, expected2);

    // Test case 3: Dead assignment elimination is not done if there are no dead assignments.
    String input3 = "function foo() {var x = 1; }";
    String expected3 = "function foo() {var x = 1; }";
    test(input3, expected3);

    // Test case 4: Dead assignment elimination for simple expressions.
    String input4 = "function foo() {var x = 1; x = 2; }";
    String expected4 = "function foo() {var x; }";
    test(input4, expected4);

    // Test case 5: Dead assignment elimination for expressions with side-effects.
    String input5 = "function foo() {" +
                    "  var x = [1];" +
                    "  x[0] = 2;" +
                    "  x = [3];" +
                    "}";
    String expected5 = "function foo() {" +
                       "  var x = [1];" +
                       "  x[0] = 2;" +
                       "}";
    test(input5, expected5);

    // Test case 6: Dead assignment elimination for assignments
    // within conditionals.
    String input6 = "function foo() {" +
                    "  if (x = 1 && x = 2) {" +
                    "    // This block will not be reached." +
                    "  }" +
                    "}";
    String expected6 = "function foo() {" +
                       "  if (true && x = 2) {" +
                       "    // This block will not be reached." +
                       "  }" +
                       "}";
    test(input6, expected6);

    // Test case 7: Dead assignment elimination for assignments within loops.
    String input7 = "function foo() {" +
                    "  var i = 0;" +
                    "  for (; i < 10 && (i = i + 1) == 5;) {" +
                    "    // This block will not be reached." +
                    "  }" +
                    "}";
    String expected7 = "function foo() {" +
                       "  var i = 0;" +
                       "  for (; i < 10 && i != 5;) {" +
                       "  }" +
                       "}";
    test(input7, expected7);

    // Test case 8: Dead assignment elimination for assignments within complex expressions.
    String input8 = "function foo() {" +
                    "  var x = 0;" +
                    "  var y = 1;" +
                    "  var z = (x = 2, y = 3);" +
                    "}";
    String expected8 = "function foo() {" +
                       "  var x = 2;" +
                       "  var y = 3;" +
                       "}";
    test(input8, expected8);

    // Test case 9: Dead assignment elimination for assignments within expressions
    // with side-effects.
    String input9 = "function foo() {" +
                    "  var x = 0;" +
                    "  var y = [1];" +
                    "  y[0] = x = 2;" +
                    "}";
    String expected9 = "function foo() {" +
                       "  var x = 2;" +
                       "  var y = [1];" +
                       "  y[0] = 2;" +
                       "}";
    test(input9, expected9);

    // Test case 10: Dead assignment elimination for assignments within expressions
    // with side-effects inside conditionals.
    String input10 = "function foo() {" +
                     "  var x = 0;" +
                     "  var y = [1];" +
                     "  if (x = 1 && (x = 2, y[0] = 3)) {" +
                     "    // This block will not be reached." +
                     "  }" +
                     "}";
    String expected10 = "function foo() {" +
                        "  var x = 1;" +
                        "  var y = [1];" +
                        "  if (x && (y[0] = 3)) {" +
                        "    // This block will not be reached." +
                        "  }" +
                        "}";
    test(input10, expected10);

    // Test case 11: Dead assignment elimination for assignments within expressions
    // with side-effects inside loops.
    String input11 = "function foo() {" +
                     "  var i = 0;" +
                     "  while (i < 10 && (i = i + 1, x = 2)) {" +
                     "  }" +
                     "}";
    String expected11 = "function foo() {" +
                        "  var i = 0;" +
                        "  while (i < 10) {" +
                        "    i = i + 1;" +
                        "    x = 2;" +
                        "  }" +
                        "}";
    test(input11, expected11);

    // Test case 12: Dead assignment elimination for assignments within expressions
    // with side-effects inside complex expressions.
    String input12 = "function foo() {" +
                     "  var x = 0;" +
                     "  var y = [1];" +
                     "  var z = (x = 2, y[0] = 3, x = 4)" +
                     "}";
    String expected12 = "function foo() {" +
                        "  var x = 4;" +
                        "  var y = [1];" +
                        "  y[0] = 3;" +
                        "}";
    test(input12, expected12);

    // Test case 13: Dead assignment elimination for multiple assignments within an expression.
    String input13 = "function foo() {" +
                     "  var x = 0;" +
                     "  var y = 1;" +
                     "  var z = (x = 2, x = 3, y = 4)" +
                     "}";
    String expected13 = "function foo() {" +
                        "  var x = 3;" +
                        "  var y = 4;" +
                        "}";
    test(input13, expected13);

    // Test case 14: Dead assignment elimination for multiple assignments within an expression inside a conditional.
    String input14 = "function foo() {" +
                     "  if (x = 1 && (x = 2, y = 3)) {" +
                     "    // This block will not be reached." +
                     "  }" +
                     "}";
    String expected14 = "function foo() {" +
                        "  if (x = 1 && y = 3) {" +
                        "    // This block will not be reached." +
                        "  }" +
                        "}";
    test(input14, expected14);

    // Test case 15: Dead assignment elimination for multiple assignments within an expression inside a loop.
    String input15 = "function foo() {" +
                     "  var i = 0;" +
                     "  while (i < 10 && (x = 2, x = 3)) {" +
                     "  }" +
                     "}";
    String expected15 = "function foo() {" +
                        "  var i = 0;" +
                        "  while (i < 10 && x = 3) {" +
                        "  }" +
                        "}";
    test(input15, expected15);

    // Test case 16: Dead assignment elimination for multiple assignments within an expression inside a complex expression.
    String input16 = "function foo() {" +
                     "  var x = 0;" +
                     "  var y = 1;" +
                     "  var z = (x = 2, x = 3, y = 4, x = 5)" +
                     "}";
    String expected16 = "function foo() {" +
                        "  var x = 5;" +
                        "  var y = 4;" +
                        "}";
    test(input16, expected16);

    // Test case 17: Dead assignment elimination for recursive function calls with dead assignments.
    String input17 = "function foo() {" +
                     "  function bar() {" +
                     "    var x = 1;" +
                     "    x = 2;" +
                     "    return bar();" +
                     "  }" +
                     "  bar();" +
                     "}";
    String expected17 = "function foo() {" +
                        "  function bar() {" +
                        "    var x = 1;" +
                        "  }" +
                        "  bar();" +
                        "}";
    test(input17, expected17);
  }

  private void test(String input, String expectedOutput) throws IOException {
    String sourceName = "test.js";
    DataSource source = new TestSourceFile(sourceName);
    Parser parser = new Parser(new CompilerOptions());
    Node externs = parser.parse(source, null, null);
    Node root = parser.parse(source, externs, null);

    CompilerPass pass = createDeadAssignmentsElimination(new AbstractCompiler() {
      @Override
      public DataSource getSourceFile(String sourceFileName) {
        if (!sourceFileName.equals(sourceName)) {
          throw new RuntimeException("Unexpected source file name: " + sourceFileName);
        }
        return source;
      }

      @Override
      public JSErrorReporter getErrorReporter() {
        return new JSErrorReporter() {
          @Override
          public void reportError(int line, String message) {
            throw new RuntimeException("Compiler error at line " + line + ": " + message);
          }
        };
      }
    });

    pass.process(externs, root);

    Node actualOutput = pass.getCompiledCode();

    assertEquals(expectedOutput, GraphTestUtil.getSourceCode(actualOutput));

    // Verify that the tree structure is still correct.
    Node expected = Parser.parse(new TestSourceFile(sourceName), null, null);
    assertTreeEquals(expected, actualOutput);
  }
}