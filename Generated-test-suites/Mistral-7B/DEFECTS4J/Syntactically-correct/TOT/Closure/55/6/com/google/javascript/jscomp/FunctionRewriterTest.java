package com.google.javascript.jscomp.FunctionRewriterTest;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for FunctionRewriter class.
 */
public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node root = NodeOperations.parseString("function a() {}");
    rewriter.process(null, root);

    Node expectedHelperCode = NodeOperations.parseString(
        "function JSCompiler_emptyFn() {" +
        "  return function() {}" +
        "}");

    String helperCodeName = "JSCompiler_emptyFn";
    int helperCodeCost = InlineCostEstimator.getCost(expectedHelperCode);

    int savings = 0;
    for (Node child : root.getChildren()) {
      if (child.getType() == Token.FUNCTION) {
        Node functionNode = child;
        if (NodeUtil.isFunctionExpression(functionNode) &&
            NodeUtil.isEmptyFunctionExpression(functionNode)) {
          Node oldChild = functionNode.getFirstChild();
          Node newChild = rewriter.parseHelperCode(EmptyFunctionReducer.class).getFirstChild();
          Reduction reduction = new Reduction(functionNode, oldChild, newChild);
          savings += reduction.estimateSavings();
          reduction.apply();
          Node addedHelperCode = compiler.getNodeForCodeInsertion(null);
          addedHelperCode.addChildrenToFront(
              rewriter.parseHelperCode(EmptyFunctionReducer.class));
          rewriter.compiler.reportCodeChange();
        }
      }
    }

    int actualSavings = InlineCostEstimator.getCost(root) - helperCodeCost;
    assertEquals("Unexpected savings", savings, actualSavings);
  }

  @Test
  public void testIdentityFunctionReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node root = NodeOperations.parseString("function a(x) {return x;}");
    rewriter.process(null, root);

    Node expectedHelperCode = NodeOperations.parseString(
        "function JSCompiler_identityFn() {" +
        "  return function(JSCompiler_identityFn_value) {" +
        "    return JSCompiler_identityFn_value" +
        "  }" +
        "}");

    String helperCodeName = "JSCompiler_identityFn";
    int helperCodeCost = InlineCostEstimator.getCost(expectedHelperCode);

    int savings = 0;
    for (Node child : root.getChildren()) {
      if (child.getType() == Token.FUNCTION) {
        Node functionNode = child;
        if (NodeUtil.isFunctionExpression(functionNode) &&
            isIdentityFunction(functionNode)) {
          Node oldChild = functionNode.getFirstChild();
          Node newChild = rewriter.parseHelperCode(IdentityReducer.class).getFirstChild();
          Reduction reduction = new Reduction(functionNode, oldChild, newChild);
          savings += reduction.estimateSavings();
          reduction.apply();
          Node addedHelperCode = compiler.getNodeForCodeInsertion(null);
          addedHelperCode.addChildrenToFront(
              rewriter.parseHelperCode(IdentityReducer.class));
          rewriter.compiler.reportCodeChange();
        }
      }
    }

    int actualSavings = InlineCostEstimator.getCost(root) - helperCodeCost;
    assertEquals("Unexpected savings", savings, actualSavings);
  }

  @Test
  public void testReturnConstantReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node root = NodeOperations.parseString("function a() {return 10;}");
    rewriter.process(null, root);

    Node expectedHelperCode = NodeOperations.parseString(
        "function JSCompiler_returnArg(arg) {" +
        "  return function() {return arg}" +
        "}");

    String helperCodeName = "JSCompiler_returnArg";
    int helperCodeCost = InlineCostEstimator.getCost(expectedHelperCode);

    int savings = 0;
    for (Node child : root.getChildren()) {
      if (child.getType() == Token.FUNCTION) {
        Node functionNode = child;
        if (NodeUtil.isFunctionExpression(functionNode)) {
          Node oldReturnNode = getValueNode(functionNode);
          if (oldReturnNode != null) {
            Node newChild = rewriter.parseHelperCode(ReturnConstantReducer.class).getFirstChild();
            Reduction reduction = new Reduction(functionNode, oldReturnNode, newChild);
            savings += reduction.estimateSavings();
            reduction.apply();
            Node addedHelperCode = compiler.getNodeForCodeInsertion(null);
            addedHelperCode.addChildrenToFront(
                rewriter.parseHelperCode(ReturnConstantReducer.class));
            rewriter.compiler.reportCodeChange();
          }
        }
      }
    }

    int actualSavings = InlineCostEstimator.getCost(root) - helperCodeCost;
    assertEquals("Unexpected savings", savings, actualSavings);
  }

  @Test
  public void testGetterReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node root = NodeOperations.parseString(
        "function C() {" +
        "  this.a_ = 0;" +
        "  this.getA = function() {return this.a_};" +
        "}");

    rewriter.process(null, root);

    Node expectedHelperCode = NodeOperations.parseString(
        "function JSCompiler_get(name) {" +
        "  return function() {return this[" + name + "]}" +
        "}");

    String helperCodeName = "JSCompiler_get";
    int helperCodeCost = InlineCostEstimator.getCost(expectedHelperCode);

    int savings = 0;
    for (Node child : root.getChildren()) {
      if (child.getType() == Token.FUNCTION) {
        Node functionNode = child;
        if (NodeUtil.isFunctionExpression(functionNode) &&
            NodeUtil.isGetter(functionNode)) {
          Node oldChild = functionNode.getFirstChild();
          Node newChild = rewriter.parseHelperCode(GetterReducer.class).getFirstChild();
          Reduction reduction = new Reduction(functionNode, oldChild, newChild);
          savings += reduction.estimateSavings();
          reduction.apply();
          Node addedHelperCode = compiler.getNodeForCodeInsertion(null);
          addedHelperCode.addChildrenToFront(
              rewriter.parseHelperCode(GetterReducer.class));
          rewriter.compiler.reportCodeChange();
        }
      }
    }

    int actualSavings = InlineCostEstimator.getCost(root) - helperCodeCost;
    assertEquals("Unexpected savings", savings, actualSavings);
  }

  @Test
  public void testSetterReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node root = NodeOperations.parseString(
        "function C() {" +
        "  this.a_ = 0;" +
        "  this.setA = function(newValue) {this.a_ = newValue};" +
        "}");

    rewriter.process(null, root);

    Node expectedHelperCode = NodeOperations.parseString(
        "function JSCompiler_set(name) {" +
        "  return function(value) {" +
        "this[" + name + "] = value" +
        "  }" +
        "}");

    String helperCodeName = "JSCompiler_set";
    int helperCodeCost = InlineCostEstimator.getCost(expectedHelperCode);

    int savings = 0;
    for (Node child : root.getChildren()) {
      if (child.getType() == Token.FUNCTION) {
        Node functionNode = child;
        if (NodeUtil.isFunctionExpression(functionNode) &&
            NodeUtil.isSetter(functionNode)) {
          Node oldChild = functionNode.getFirstChild();
          Node newChild = rewriter.parseHelperCode(SetterReducer.class).getFirstChild();
          Reduction reduction = new Reduction(functionNode, oldChild, newChild);
          savings += reduction.estimateSavings();
          reduction.apply();
          Node addedHelperCode = compiler.getNodeForCodeInsertion(null);
          addedHelperCode.addChildrenToFront(
              rewriter.parseHelperCode(SetterReducer.class));
          rewriter.compiler.reportCodeChange();
        }
      }
    }

    int actualSavings = InlineCostEstimator.getCost(root) - helperCodeCost;
    assertEquals("Unexpected savings", savings, actualSavings);
  }

  @Test
  public void testUnreducedFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node root = NodeOperations.parseString("function a() {return a() + 1;}");
    rewriter.process(null, root);

    int actualSavings = InlineCostEstimator.getCost(root);
    int expectedSavings = 0;
    assertEquals("Unexpected savings", expectedSavings, actualSavings);
  }

  private static boolean isIdentityFunction(Node functionNode) {
    Node argList = functionNode.getFirstChild().getNext();
    Node paramNode = argList.getFirstChild();
    Node value = maybeGetSingleReturnRValue(functionNode);
    return value != null && value.getString().equals(paramNode.getString());
  }

  private static Node maybeGetSingleReturnRValue(Node functionNode) {
    Node body = functionNode.getLastChild();
    if (!body.hasOneChild()) {
      return null;
    }

    Node statement = body.getFirstChild();
    if (statement.getType() != Token.RETURN) {
      return null;
    }

    Node valueNode = statement.getFirstChild();
    if (valueNode == null) {
      return null;
    }

    return valueNode;
  }

  private static Node getValueNode(Node functionNode) {
    Node body = functionNode.getLastChild();
    if (!body.hasOneChild()) {
      return null;
    }

    Node statement = body.getFirstChild();
    if (statement.getType() != Token.RETURN) {
      return null;
    }

    Node valueNode = statement.getFirstChild();
    if (valueNode == null || !NodeUtil.isImmutableValue(valueNode)) {
      return null;
    }

    return valueNode;
  }
}