import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckSideEffects;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(
        "test",
        "function foo() { return 42; }\n" +
        "function bar() {\n" +
        "  var x = 1;\n" +
        "  foo();\n" +
        "}\n" +
        "bar();");

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getExterns(), input.getAstRoot());

    List<Node> problemNodes = checker.getProblemNodes();
    assertTrue(problemNodes.isEmpty());

    input.setSourceFileName("test2.js");
    input.setSourceCode(
        "function foo() {\n" +
        "  var x = 1;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var x = 1;\n" +
        "  x = foo();\n" +
        "}\n" +
        "bar();");

    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getExterns(), input.getAstRoot());

    problemNodes = checker.getProblemNodes();
    assertTrue(problemNodes.size() == 1);
    Node problemNode = problemNodes.get(0);
    assertTrue(problemNode.getParent() instanceof Node.Block);
    assertTrue(problemNode.getParent().getParent() instanceof Node.ExpressionStatement);
    assertTrue(problemNode.isAssignment());
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(
        "test",
        "function foo() { return 42; }\n" +
        "function bar() {\n" +
        "  var x = JSCOMPILER_PRESERVE(foo());\n" +
        "}\n" +
        "bar();");

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
    checker.process(input.getExterns(), input.getAstRoot());

    List<Node> problemNodes = checker.getProblemNodes();
    assertTrue(problemNodes.isEmpty());

    input.setSourceFileName("test2.js");
    input.setSourceCode(
        "function foo() {\n" +
        "  var x = 1;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var x = JSCOMPILER_PRESERVE(foo());\n" +
        "  x = 2;\n" +
        "}\n" +
        "bar();");

    checker = new CheckSideEffects(compiler, WARNING, true);
    checker.process(input.getExterns(), input.getAstRoot());

    problemNodes = checker.getProblemNodes();
    assertTrue(problemNodes.size() == 1);
    Node problemNode = problemNodes.get(0);
    assertTrue(problemNode.isAssignment());
  }

  @Test
  public void testRemoveProtection() {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(
        "test",
        "function JSCOMPILER_PRESERVE(x) { return x; }\n" +
        "function foo() {\n" +
        "  var x = JSCOMPILER_PRESERVE(1);\n" +
        "}\n" +
        "foo();");

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getExterns(), input.getAstRoot());

    List<Node> problemNodes = checker.getProblemNodes();
    assertTrue(problemNodes.isEmpty());

    StripProtection stripper = new StripProtection(compiler);
    stripper.process(input.getExterns(), input.getAstRoot());

    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getExterns(), input.getAstRoot());

    problemNodes = checker.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }
}