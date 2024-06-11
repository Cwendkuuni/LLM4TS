import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = Node.NULL();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleNode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = Node.IDENT("a");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostOfComplexNode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = Node.BINARY_OP(Node.IDENT("a"), Node.OP_PLUS, Node.IDENT("b"));
    int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    int expectedCost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3;
    assertEquals(expectedCost, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = Node.IDENT("a");
    int cost = InlineCostEstimator.getCost(root, -1);
  }
}

// Mockito is used here for testing, but you can also use JUnit's TestCase and create your mocks manually