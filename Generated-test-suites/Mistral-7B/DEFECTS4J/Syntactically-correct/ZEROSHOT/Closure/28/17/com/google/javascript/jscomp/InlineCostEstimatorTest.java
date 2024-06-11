import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;

import org.junit.Test;

/**
 * Test class for InlineCostEstimator.
 */
public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.objectLiteral();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.objectLiteral();
    int costThreshhold = 10;
    int cost = getCost(root, costThreshhold);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithComplexNode() {
    Node root = Node.objectLiteral()
        .setProperty("a", Node.numberLiteral(1))
        .setProperty("b", Node.objectLiteral()
            .setProperty("c", Node.numberLiteral(2))
            .setProperty("d", Node.arrayLiteral(Node.numberLiteral(3), Node.numberLiteral(4)))
        );
    int costThreshhold = 10;
    int cost = getCost(root, costThreshhold);
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 12; // 4 identifiers for each node
    assertEquals(expectedCost, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    Node root = Node.objectLiteral();
    int costThreshhold = -1;
    getCost(root, costThreshhold);
  }

  @Test
  public void testCompiledSizeEstimator() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier1");
    estimator.addIdentifier("identifier2");
    estimator.addIdentifier("identifier3");
    int cost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 3;
    assertEquals(expectedCost, cost);
  }
}