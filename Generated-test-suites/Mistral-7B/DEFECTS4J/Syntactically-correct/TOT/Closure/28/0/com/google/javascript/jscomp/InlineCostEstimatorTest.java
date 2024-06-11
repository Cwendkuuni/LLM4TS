import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.InlineCostEstimator.ESTIMATED_IDENTIFIER;
import static com.google.javascript.jscomp.InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeOperations;

/**
 * Test class for InlineCostEstimator class.
 */
public class InlineCostEstimatorTest {

  /**
   * Test getCost method for empty Node.
   */
  @Test
  public void testGetCostEmptyNode() {
    Node root = NodeOperations.newNull();
    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  /**
   * Test getCost method for simple Node.
   */
  @Test
  public void testGetCostSimpleNode() {
    Node root = NodeOperations.newLiteral(1);
    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = ESTIMATED_IDENTIFIER_COST;
    assertEquals(expectedCost, actualCost);
  }

  /**
   * Test getCost method for complex Node.
   */
  @Test
  public void testGetCostComplexNode() {
    Node root = NodeOperations.newCall("Math", "pow", NodeOperations.newNumber(2), NodeOperations.newNumber(3));
    int actualCost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 9; // 3 identifiers for 'Math', 'pow' and anonymous function
    assertEquals(expectedCost, actualCost);
  }

  /**
   * Test getCost method for large Node.
   */
  @Test
  public void testGetCostLargeNode() {
    Node root = NodeOperations.newCall("Math", "pow", NodeOperations.newNumber(1000), NodeOperations.newNumber(1000));
    int actualCost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 2001; // 1001 identifiers for 'Math', 'pow' and anonymous function for each level of recursion
    assertEquals(expectedCost, actualCost);
  }

  /**
   * Test addIdentifier method.
   */
  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.addIdentifier("testIdentifier1");
    int actualCost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 2; // 1 for 'testIdentifier1' and 1 for '\0'
    assertEquals(expectedCost, actualCost);
  }

  /**
   * Test continueProcessing method.
   */
  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(3 * ESTIMATED_IDENTIFIER_COST);
    estimator.addIdentifier("testIdentifier1");
    estimator.addIdentifier("testIdentifier2");
    estimator.addIdentifier("testIdentifier3");
    assertFalse(estimator.continueProcessing());
  }

  /**
   * Test getLastChar method.
   */
  @Test
  public void testGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.addIdentifier("testIdentifier1");
    char actualLastChar = estimator.getLastChar();
    assertEquals('1', actualLastChar);
  }
}