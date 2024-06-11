import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Tests for InlineCostEstimator class.
 */
public class InlineCostEstimatorTest {

  /**
   * Test method for getCost(Node root) using a simple Node as input.
   */
  @Test
  public void testGetCostSimpleNode() {
    Node root = Node.newBoolean(true);
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  /**
   * Test method for getCost(Node root, int costThreshhold) using a simple Node as input.
   */
  @Test
  public void testGetCostSimpleNodeWithThreshold() {
    Node root = Node.newBoolean(true);
    int cost = getCost(root, 10);
    assertEquals(0, cost);
  }

  /**
   * Test method for getCost(Node root) using a complex Node as input.
   */
  @Test
  public void testGetCostComplexNode() {
    Node root = Node.newObjectLiteral();
    root.put("a", Node.newNumber(1));
    root.put("b", Node.newBoolean(true));
    root.put("c", Node.newArray(Node.newNumber(2), Node.newNumber(3)));

    int cost = getCost(root);
    // Estimated identifier cost: 6 * 2 + 6 = 14
    // Whitespaces and other characters cost: around 20
    // So the total cost should be close to 30, but the exact value depends on the internal implementation
    assertTrue(cost > 20);
  }

  /**
   * Test method for getCost(Node root, int costThreshhold) using a complex Node as input.
   */
  @Test
  public void testGetCostComplexNodeWithThreshold() {
    Node root = Node.newObjectLiteral();
    root.put("a", Node.newNumber(1));
    root.put("b", Node.newBoolean(true));
    root.put("c", Node.newArray(Node.newNumber(2), Node.newNumber(3)));

    int cost = getCost(root, 30);
    // All characters should be added to the cost
    int expectedCost = root.getNumChildren() * ESTIMATED_IDENTIFIER_COST + root.getString().length();
    assertEquals(expectedCost, cost);
  }
}