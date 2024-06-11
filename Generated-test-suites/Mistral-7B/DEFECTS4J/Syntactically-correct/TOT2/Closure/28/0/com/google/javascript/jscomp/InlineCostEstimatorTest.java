package com.google.javascript.jscomp;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import static org.junit.Assert.*;;

@RunWith(Enclosed.class)
public class InlineCostEstimatorTest {
  static class TestInlineCostEstimator {
    @Test
    public void testGetCost() {
      Node root = new Node("{}");
      int cost = InlineCostEstimator.getCost(root);
      assertEquals(0, cost);

      root = new Node("{a: 1, b: 2, c: 3}");
      cost = InlineCostEstimator.getCost(root);
      assertEquals(9, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
      Node root = new Node("{a: 1, b: 2, c: 3, d: 4, e: 5, f: 6, g: 7, h: 8, i: 9, j: 10}");
      int cost = InlineCostEstimator.getCost(root, 20);
      assertEquals(29, cost);
    }

    @Test
    public void testGetCostWithLargeJSONObject() {
      String json = "{\\"; // Create a large JSON object with 1000 properties
      for (int i = 0; i < 1000; i++) {
        json += "p" + i + ": " + i + ",";
      }
      json += "}"; // Close the JSON object

      Node root = new Node(json);
      int cost = InlineCostEstimator.getCost(root, 5000);
      assertEquals(5035, cost);
    }
  }

  static class TestExceptionHandling {
    @Test
    public void testGetCostWithNullInput() {
      Node root = null;
      InlineCostEstimator.getCost(root);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCostWithNullInputError() {
      Node root = null;
      InlineCostEstimator.getCost(root);
    }

    @Test
    public void testGetCostWithNegativeCostThreshold() {
      Node root = new Node("{}");
      InlineCostEstimator.getCost(root, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCostWithNegativeCostThresholdError() {
      Node root = new Node("{}");
      InlineCostEstimator.getCost(root, -1);
    }
  }
}