package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Test class for InlineCostEstimator.
 */
public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    Node root = Node.newIntLiteral(1);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);
    assertEquals(1, cost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    String jsCode = "function foo(x) {" +
        "return x * 2 + 1;" +
        "}";
    Node root = Node.parse(jsCode);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);
    // The actual cost may vary, but it should be higher than simple expressions.
    assertTrue(cost > 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostOfNullNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.getCost(null);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    String jsCode = "function foo(x) {" +
        "for (var i = 0; i < 100000; i++) {" +
        "  x += i;" +
        "}" +
        "return x;" +
        "}";
    Node root = Node.parse(jsCode);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, 100);
    // The actual cost may vary, but it should be less than 100.
    assertTrue(cost < 100);
  }

  @Test
  public void testGetCostWithLargeCostThreshold() {
    String jsCode = "function foo(x) {" +
        "for (var i = 0; i < 1000000; i++) {" +
        "  x += i;" +
        "}" +
        "return x;" +
        "}";
    Node root = Node.parse(jsCode);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, Integer.MAX_VALUE);
    // The actual cost may vary, but it should be larger than what we test here.
    assertTrue(cost > 1000000);
  }
}