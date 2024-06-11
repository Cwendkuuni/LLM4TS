package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class FunctionRewriterTest {

  private static Node createFunctionExpressionNode(String code) {
    Node externs = NodeFactory.createScopeNode();
    Node root = NodeFactory.createProgram(externs, NodeFactory.createStringLiteral(code));
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    functionRewriter.process(externs, root);
    return root;
  }

  @Test
  public void testEmptyFunctionReduction() {
    Node input = createFunctionExpressionNode("function() {}");
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    Node output = functionRewriter.process(createScopeNode(), input).getFirstChild();
    Node expectedOutput = NodeUtil.createCallNode(
        "JSCompiler_emptyFn", null, Token.NO_SOURCE_INFO, Token.DUMMY_POSITION);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testIdentityFunctionReduction() {
    Node input = createFunctionExpressionNode("function(a) { return a }");
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    Node output = functionRewriter.process(createScopeNode(), input).getFirstChild();
    Node expectedOutput = NodeUtil.createCallNode(
        "JSCompiler_identityFn", NodeUtil.createStringLiteral("a"),
        Token.NO_SOURCE_INFO, Token.DUMMY_POSITION);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testReturnConstantReduction() {
    Node input = createFunctionExpressionNode("function() { return 10 }");
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    Node output = functionRewriter.process(createScopeNode(), input).getFirstChild();
    Node expectedOutput = NodeUtil.createCallNode(
        "JSCompiler_returnArg", NodeUtil.createIntegerLiteral(10),
        Token.NO_SOURCE_INFO, Token.DUMMY_POSITION);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testSetterReduction() {
    Node input = createFunctionExpressionNode("function(value) { this.a = value }");
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    Node output = functionRewriter.process(createScopeNode(), input).getFirstChild();
    Node expectedOutput = NodeUtil.createCallNode(
        "JSCompiler_set", NodeUtil.createStringLiteral("a"),
        NodeUtil.createFunction(
            NodeUtil.createStringLiteral("value"),
            null,
            Token.NO_SOURCE_INFO,
            Token.DUMMY_POSITION),
        Token.NO_SOURCE_INFO,
        Token.DUMMY_POSITION);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testGetterReduction() {
    Node input = createFunctionExpressionNode("function() { return this.a }");
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    Node output = functionRewriter.process(createScopeNode(), input).getFirstChild();
    Node expectedOutput = NodeUtil.createCallNode(
        "JSCompiler_get", NodeUtil.createStringLiteral("a"), null,
        Token.NO_SOURCE_INFO, Token.DUMMY_POSITION);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test(expected = IllegalStateException.class)
  public void testNonFunctionNode() {
    Node input = NodeFactory.createProgram(NodeFactory.createScopeNode(), null);
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    functionRewriter.process(input, input);
  }

  @Test(expected = IllegalStateException.class)
  public void testNonReducibleFunction() {
    Node input = createFunctionExpressionNode("function(a, b) { return a + b }");
    FunctionRewriter functionRewriter = new FunctionRewriter(new TestCompiler());
    Node output = functionRewriter.process(createScopeNode(), input).getFirstChild();
    assertThat(output).isEqualTo(input);
  }

  private static class TestCompiler extends AbstractCompiler {
    // Empty for test purposes.
  }
}