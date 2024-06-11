package com.google.javascript.jscomp.test;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo() {}";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_emptyFn"), null));

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testIdentityFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo(x) { return x }";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildrenToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_identityFn"),
                new Node(Token.NAME, 1, 1, "x")),
        null);

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testReturnConstantReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo() { return 1 }";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_returnArg"),
                new Node(Token.INT_LITERAL, 1, 1, 1)),
        null);

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testGetterReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo() { return this.a }";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_get"),
                new Node(Token.NAME, 1, 1, "a")),
        null);

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testSetterReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo(x) { this.a = x }";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildrenToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_set"),
                new Node(Token.NAME, 1, 1, "a"),
                new Node(Token.INT_LITERAL, 1, 1, 1)),
        null);

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testComplexFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo(x) { return x * 2 }";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildrenToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_mult"),
                new Node(Token.INT_LITERAL, 1, 1, 2),
                new Node(Token.NAME, 1, 1, "x")),
        null);

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testComplexFunctionReductionWithArguments() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String inputCode = "function Foo(x, y) { return x * y }";
    Node rootNode = compiler.parse(inputCode, null, null);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, rootNode);

    Node expectedCode = new Node(Token.PROGRAM, 1, 1);
    expectedCode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    expectedCode.getLastChild().addChildrenToBack(
        new Node(Token.CALL, 1, 1,
                new Node(Token.NAME, 1, 1, "JSCompiler_mult"),
                new Node(Token.NAME, 1, 1, "x"),
                new Node(Token.NAME, 1, 1, "y")),
        null);

    Node actualCode = compiler.getProcessedSource(rootNode);
    NodeTraversal.traverse(compiler, actualCode, new NodeAssertion());

    assertThat(actualCode).isEqualTo(expectedCode);
  }

  private static class NodeAssertion implements Callback {
    @Override
    public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
      return true;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      assertThat(node.getType()).isEqualTo(parent.getType());
    }
  }
}