package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestHelper;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class FunctionRewriterTest {
  private NodeFactory nodeFactory;
  private ParserTestHelper parserHelper;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    parserHelper = new ParserTestHelper(nodeFactory);
  }

  @Test
  public void testEmptyFunctionReduction() {
    String testCode = "function Test() {}";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    Node traversedNode = NodeTraversal.depthFirst(nodeFactory, root);
    Node emptyFunctionNode = traversedNode.getFirstChild().getNext();
    Node expectedHelperNode = NodeTraversal.depthFirst(nodeFactory, root)
        .getChildAt(1);

    assertThat(emptyFunctionNode.getType()).isEqualTo(Token.CALL);
    assertThat(emptyFunctionNode.getString()).isEqualTo("emptyFn()");
    assertThat(expectedHelperNode.getType()).isEqualTo(Token.FUNCTION_DECL);
    assertThat(expectedHelperNode.getFirstChild().getLastChild().getString())
        .isEqualTo("return function() {}");
  }

  @Test
  public void testIdentityFunctionReduction() {
    String testCode = "function Test(x) { return x }";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    Node traversedNode = NodeTraversal.depthFirst(nodeFactory, root);
    Node identityFunctionNode = traversedNode.getFirstChild().getNext();
    Node expectedHelperNode = NodeTraversal.depthFirst(nodeFactory, root)
        .getChildAt(2);

    assertThat(identityFunctionNode.getType()).isEqualTo(Token.CALL);
    assertThat(identityFunctionNode.getString()).isEqualTo("identityFn_value");
    assertThat(expectedHelperNode.getType()).isEqualTo(Token.FUNCTION_DECL);
    assertThat(expectedHelperNode.getFirstChild().getLastChild().getString())
        .isEqualTo("return identityFn_value");
  }

  @Test
  public void testSetterFunctionReduction() {
    String testCode = "function Test() {" +
        "  this.x = function(value) { this.x_ = value }" +
        "}";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    Node traversedNode = NodeTraversal.depthFirst(nodeFactory, root);
    Node setterFunctionNode = traversedNode.getFirstChild().getNext();
    Node expectedHelperNode = NodeTraversal.depthFirst(nodeFactory, root)
        .getChildAt(3);

    assertThat(setterFunctionNode.getType()).isEqualTo(Token.CALL);
    assertThat(setterFunctionNode.getString()).isEqualTo("setter(\"x_\")");
    assertThat(expectedHelperNode.getType()).isEqualTo(Token.FUNCTION_DECL);
    assertThat(expectedHelperNode.getFirstChild().getLastChild().getString())
        .isEqualTo("this[" + "'x_'" + "] = JSCompiler_set(\"x_\", JSCompiler_setter_value)");
  }

  @Test(expected = IllegalStateException.class)
  public void testGetFunctionWithTwoArguments() {
    String testCode = "function Test(x, y) { return this.x }";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testSetFunctionWithTwoArguments() {
    String testCode = "function Test(x, y) { this.x = y }";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testNonFunctionNode() {
    String testCode = "function Test() { 1 }";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
  }

  @Test
  public void testReducerDoesNotSaveSpaceWhenNoReduction() {
    String testCode = "function Test() { return 1 + 2 }";
    Node root = parserHelper.parse(testCode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    Node traversedNode = NodeTraversal.depthFirst(nodeFactory, root);
    Node originalFunctionNode = traversedNode.getFirstChild().getNext();

    Node newFunctionNode = NodeTraversal.depthFirst(nodeFactory, root)
        .getChildAt(1);

    int originalFunctionCost = InlineCostEstimator.getCost(originalFunctionNode);
    int newFunctionCost = InlineCostEstimator.getCost(newFunctionNode);

    assertThat(originalFunctionCost).isGreaterThan(newFunctionCost);
    assertThat(originalFunctionCost - newFunctionCost).isAtMost(16);
  }
}