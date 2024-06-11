package com.google.javascript.jscomp;

import org.junit.Test;
import static com.google.common.truth.Truth.*;
import static com.google.javascript.rhino.NodeUtil.*;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "x = 2;" + // Dead assignment
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 1;" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditionalBranch() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "if (Math.random() > 0.5) {" +
        "x = 2;" + // Dead assignment
        "}" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 1;" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithLoop() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "for (var i = 0; i < 10; i++) {" +
        "x = 2;" + // Dead assignment
        "}" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 1;" +
        "for (var i = 0; i < 10; i++) {}" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    String jsCode = "function test() {" +
        "function innerFunction() {" +
        "var x = 1;" +
        "x = 2;" + // Dead assignment
        "}" +
        "return innerFunction;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "function innerFunction() {}" +
        "return innerFunction;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableReuse() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "x = 2;" + // Dead assignment
        "x = 3;" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 3;" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunctionCall() {
    String jsCode = "function test() {" +
        "function foo() {" +
        "var x = 1;" +
        "x = 2;" + // Dead assignment
        "return x;" +
        "}" +
        "return foo();" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "function foo() {" +
        "return 1;" +
        "}" +
        "return foo();" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableDeclaration() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "var x = 2;" + // Dead assignment
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 1;" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithGlobalVariable() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "window.x = 2;" + // Dead assignment
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 1;" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithGlobalVariableAndFunctionCall() {
    String jsCode = "function test() {" +
        "var x = 1;" +
        "window.foo = function() {" +
        "x = 2;" + // Dead assignment
        "}" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    CompilerOptions options = new CompilerOptions(null);
    RewrittenSourceFile out = compiler.compile(new SourceFile("test.js", jsCode),
        options, null);

    String expectedCode = "function test() {" +
        "var x = 1;" +
        "return x;" +
        "}";

    assertThat(out.getCode()).isEqualTo(expectedCode);
  }
}