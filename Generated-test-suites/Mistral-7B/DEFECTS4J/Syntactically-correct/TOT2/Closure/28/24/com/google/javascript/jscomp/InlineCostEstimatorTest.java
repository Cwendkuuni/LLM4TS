package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @org.junit.Test
  public void testGetCost() {
    Node root = new Node("someNode");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(1, cost);
  }

  @org.junit.Test
  public void testGetCostWithThreshold() {
    Node root = new Node("someNode");
    int cost = InlineCostEstimator.getCost(root, 1);
    assertEquals(1, cost);
  }

  @org.junit.Test
  public void testGetCostWithLargeInput() {
    Node largeInput = new Node("largeInputNode"); // create a large Node tree
    int cost = InlineCostEstimator.getCost(largeInput);
    // add assertions based on expected behaviour
    assertTrue(cost > 1);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNullInput() {
    Node nullInput = null;
    int cost = InlineCostEstimator.getCost(nullInput);
  }

  @org.junit.Test
  public void testGetCostWithConstants() {
    Node constantsInput = new Node("true");;
    int cost = InlineCostEstimator.getCost(constantsInput);
    // add assertions based on expected behaviour
    assertEquals(0, cost);
  }
}