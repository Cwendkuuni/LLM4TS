import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    Node root = Node.number(-1);
    int cost = getCost(root, 10);
    assertEquals(1, cost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    String complexExpression = "2 + 3 * (4 - 5) / 6 + 7";
    Node root = Node.parse(complexExpression);
    int cost = getCost(root, 100);
    assertEquals(10, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    int costThreshhold = -1;
    Node root = Node.number(1);
    getCost(root, costThreshhold);
  }

  @Test
  public void testGetCostWithLargeCostThreshold() {
    int costThreshhold = Integer.MAX_VALUE;
    Node root = Node.parse("for (int i = 0; i < 100000; i++) { i++; }");
    int cost = getCost(root, costThreshhold);
    // The actual cost might vary, but it should be much larger than the threshold
    assertTrue(cost > costThreshhold);
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("variableName");
    int cost = estimator.getCost();
    assertEquals(12, cost); // 10 for "variableName" and 2 for estimation error
  }
}