package com.google.javascript.jscomp.tests;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.testing.JslUnitTestBase;

import java.util.Arrays;
import java.util.List;

public class CheckSideEffectsTest extends JslUnitTestBase {

  public void testCheckSideEffects() {
    String source =
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x++;\n" +
        "}\n" +
        "function main() {\n" +
        "  var y = foo();\n" +
        "  y++;\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions(null);
    options.setCheckSideEffects(CheckLevel.WARNING);
    Compiler compiler = new TestCompiler(options, null);

    List<CompilerPass> passes = Arrays.asList(
        new CheckSideEffects(compiler, CheckLevel.WARNING, false)
    );

    Node ast = compileAndGetAst(compiler, source);
    NodeTraversal traverser = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(compiler, ast, new StripProtection(compiler));
    passes.forEach(p -> p.process(null, ast));

    assertNumberOfErrors(1);
    assertErrorContains("Suspicious code. This code lacks side-effects. Is there a bug?");

    source =
        "function foo() {\n" +
        "  return 1 + 1;\n" +
        "}\n" +
        "function main() {\n" +
        "  var x = foo();\n" +
        "}\n";

    options.setCheckSideEffects(CheckLevel.ERROR);
    compiler = new TestCompiler(options, null);
    passes = Arrays.asList(
        new CheckSideEffects(compiler, CheckLevel.ERROR, false)
    );

    ast = compileAndGetAst(compiler, source);
    traverser = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(compiler, ast, new StripProtection(compiler));
    passes.forEach(p -> p.process(null, ast));

    assertCompilesWithoutErrors(compiler, source);

    source =
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  x++;\n" +
        "  return x;\n" +
        "}\n" +
        "function main() {\n" +
        "  var y = foo();\n" +
        "  y++;\n" +
        "}\n";

    options.setCheckSideEffects(CheckLevel.WARNING);
    compiler = new TestCompiler(options, null);
    passes = Arrays.asList(
        new CheckSideEffects(compiler, CheckLevel.WARNING, false)
    );

    ast = compileAndGetAst(compiler, source);
    traverser = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(compiler, ast, new StripProtection(compiler));
    passes.forEach(p -> p.process(null, ast));

    assertCompilesWithoutErrors(compiler, source);
  }

  static class StripProtection extends AbstractPostOrderCallback implements CompilerPass {

    private final Compiler compiler;

    StripProtection(Compiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal.traverse(compiler, root, this);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall()) {
        Node target = n.getFirstChild();
        if (target.isName() && target.getString().equals("JSCOMPILER_PRESERVE")) {
          Node expr = n.getLastChild();
          n.detachChildren();
          parent.replaceChild(n, expr);
        }
      }
    }
  }

  static class TestCompiler extends Compiler {

    TestCompiler(CompilerOptions options, List<CompilerPass> passes) {
      super(options, passes);
    }

    TestCompiler(CompilerOptions options, List<CompilerPass> passes, Node root) {
      super(options, passes, root);
    }

    Node compileAndGetAst(CompilerOptions options, String source) {
      clearErrors();
      CompilerInput input = new CompilerInput();
      input.setSource(source);
      Node root = createTopLevelNode(input, null);
      setOptions(options);
      setInput(input);
      compile(root);
      return root;
    }

    void assertNumberOfErrors(int number) {
      assertNumErrors(number);
    }

    void assertErrorContains(String message) {
      assertErrorMessage(0, message);
    }

    void assertCompilesWithoutErrors(Compiler compiler, String source) {
      Node root = compileAndGetAst(compiler.getOptions(), source);
      assertErrorsEmpty();
    }
  }
}