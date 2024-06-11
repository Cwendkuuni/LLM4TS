package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.BaseCompilerTest;

import java.util.List;

/**
 * Tests for the RemoveConstantExpressions compiler pass.
 */
public class RemoveConstantExpressionsTest extends BaseCompilerTest {

  @Override
  protected CompilerPass[] getProcessorOrder() {
    return new CompilerPass[] {
        new RemoveConstantExpressions(compiler)
    };
  }

  @Override
  protected ParallelCompilerPass getParallelProcessor() {
    return new ParallelCompilerPass() {
      @Override
      public void process(Node externs, Node root, CompilerPassManager passes) {
        new RemoveConstantExpressions(compiler).process(externs, root);
      }
    };
  }

  /**
   * Test that RemoveConstantExpressions correctly simplifies expressions.
   */
  public void testRemoveConstantExpressions() {
    String jsCode = "function foo() { return 1; } function bar() { return 2; } "
        + "function test() { return 1 + foo() + bar(); }";

    TestCompiler compiler = new TestCompiler(false);
    compiler.addSourceFile("test.js", jsCode);
    List<Node> ast = compiler.getAST();

    Node testFunction = findNodeByUniqueName(ast, "test");
    Node testCall = testFunction.getFirstChild();
    Node testExpression = testCall.getFirstChild();

    // Original AST
    // test()
    //  |
    //  foo()
    //  |
    //  1
    // +
    //  bar()
    //  |
    //  2

    // Expected AST after RemoveConstantExpressions pass
    // test()
    //  |
    //  foo()
    //  |
    //  bar()

    assertTrue(NodeUtil.areNodesEqual(testExpression.getChildren(),
        Lists.newArrayList(testExpression.getChildren().get(0),
            testExpression.getChildren().get(1))));
  }

  /**
   * Test that RemoveConstantExpressions does not simplify expressions with side effects.
   */
  public void testRemoveConstantExpressionsWithSideEffects() {
    String jsCode = "function foo() { return 1; } function bar() { alert('Hello'); } "
        + "function test() { return foo() + bar(); }";

    TestCompiler compiler = new TestCompiler(false);
    compiler.addSourceFile("test.js", jsCode);
    List<Node> ast = compiler.getAST();

    Node testFunction = findNodeByUniqueName(ast, "test");
    Node testCall = testFunction.getFirstChild();
    Node testExpression = testCall.getFirstChild();

    // Original AST
    // test()
    //  |
    //  foo()
    //  |
    //  1
    // +
    //  bar()
    //  |
    //  alert('Hello')

    // Expected AST after RemoveConstantExpressions pass
    // test()
    //  |
    //  foo()
    //  |
    //  bar()

    assertFalse(NodeUtil.areNodesEqual(testExpression.getChildren(),
        Lists.newArrayList(testExpression.getChildren().get(0),
            testExpression.getChildren().get(1))));
  }
}