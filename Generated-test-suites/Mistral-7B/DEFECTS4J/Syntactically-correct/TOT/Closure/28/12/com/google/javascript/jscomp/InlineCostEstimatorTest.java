import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyAST() {
    Node root = NodeFactory.createObject();
    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    Node root = NodeFactory.createIdentifier("a");
    int actualCost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    int expectedCost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    Node root = NodeFactory.createBinaryOp("+", NodeFactory.createNumber(1), NodeFactory.createNumber(2), null);
    int actualCost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    int expectedCost = 3; // Assuming '+' operator has a cost of 1
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    Node root = NodeFactory.createObject();
    Node child = NodeFactory.createBinaryOp("+", NodeFactory.createNumber(1), NodeFactory.createNumber(2), null);
    root.setFirstChild(child);

    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.getCost(root, 5); // Cost threshold of 5

    // Since the cost threshold was reached, the estimator should not process more nodes
    Node child2 = NodeFactory.createNumber(3);
    estimator.add(child2);

    // The estimator should not have added the node to the cost
    int actualCost = estimator.getCost();
    int expectedCost = 5;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testCompiledSizeEstimator() {
    String complexExpression = "function foo(a, b, c) {\n" +
        "  var d = a + b;\n" +
        "  var e = d * c;\n" +
        "  return e;\n" +
        "}";

    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = NodeFactory.createScript(complexExpression);
    estimator.add(root);

    int actualCost = estimator.getCost();
    int expectedCost = complexExpression.length() - (3 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST);
    assertEquals(expectedCost, actualCost);
  }

  @Test(expected = AssertionError.class)
  public void testNegativeCost() {
    Node root = NodeFactory.createNumber(-1);
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.add(root);
    estimator.getCost();
  }

  @Test
  public void testContinueProcessingFalse() {
    String complexExpression = "function foo(a, b, c) {\n" +
        "  var d = a + b;\n" +
        "  var e = d * c;\n" +
        "  return e;\n" +
        "}";

    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = NodeFactory.createScript(complexExpression);
    estimator.add(root);
    estimator.add(NodeFactory.createNumber(1));

    estimator.setContinueProcessing(false);

    int actualCost = estimator.getCost();
    int expectedCost = estimator.getCost(); // The cost should not have been updated
    assertEquals(expectedCost, actualCost);

    assertFalse(estimator.continueProcessing());
  }
}