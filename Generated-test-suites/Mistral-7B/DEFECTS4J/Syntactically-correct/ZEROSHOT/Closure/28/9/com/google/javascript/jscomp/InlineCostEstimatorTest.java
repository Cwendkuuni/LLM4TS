import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostRoot() {
    Node root = Node.objectLiteral();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostSimpleNode() {
    Node node = Node.identifier("identifier");
    int cost = getCost(node);
    assertEquals(2, cost);
  }

  @Test
  public void testGetCostComplexNode() {
    Node node = Node.newExpression("(a + b) * (c + d)");
    int cost = getCost(node);
    assertEquals(12, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node node = Node.newExpression("(a + b) * (c + d)");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    estimator.add(node);
    int cost = estimator.getCost();
    assertEquals(12, cost);
    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(3, cost);
    assertFalse(estimator.continueProcessing());
  }
}