import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    int actualCost = estimator.getCost(root);
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfSimpleNode() {
    String testCode = "function testFunction() { return 42; }";
    Node root = ParserUtil.parseSimpleScript(testCode);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int actualCost = estimator.getCost(root);
    int expectedCost = testCode.length();
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfComplexNode() {
    String testCode = "function factorial(n) { if (n <= 1) { return 1; } else { return n * factorial(n - 1); } }";
    Node root = ParserUtil.parseSimpleScript(testCode);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int actualCost = estimator.getCost(root);
    int expectedCost = testCode.length(); // Assuming estimated identifier cost is included in the length of the test code.
    assertEquals(expectedCost, actualCost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostOfNullNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.getCost(root);
  }
}