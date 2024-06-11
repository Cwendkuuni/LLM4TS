package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(); // create an empty Node for testing
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root, 10);
    assertTrue(cost <= 10);
  }

  @Test
  public void testGetCostWithLargeThreshold() {
    Node root = new Node(); // create a complex Node for testing
    try {
      InlineCostEstimator.getCost(root, Integer.MIN_VALUE);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testCompiledSizeEstimator() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    String str = "abcdabcdabcdabcd";
    estimator.add(new Node.Identifier("identifier"));
    estimator.add(new Node.Text(str));
    int cost = estimator.getCost();
    assertEquals(12, cost); // 10 for 'identifier' + 2 for each 'd'
  }
}