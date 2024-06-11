import org.junit.Test;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSingleIdentifierNode() {
    String identifier = "testIdentifier";
    Node root = Node.newIdent(identifier);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, Integer.MAX_VALUE);
    assertEquals(identifier.length(), cost);
  }

  @Test
  public void testGetCostOfBinaryExpressionNode() {
    Node left = Node.newInt(1);
    Node right = Node.newInt(2);
    Node root = Node.newBinaryOp("+", left, right);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, Integer.MAX_VALUE);
    // Assuming '+' operator has a cost of 1.
    assertEquals(3, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostOfNullNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.getCost(null, Integer.MAX_VALUE);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    String identifier1 = "identifier1";
    String identifier2 = "identifier2";
    Node left = Node.newIdent(identifier1);
    Node right1 = Node.newBinaryOp("*", Node.newInt(2), left);
    Node right2 = Node.newBinaryOp("+", Node.newInt(3), Node.newInt(4));
    Node root = Node.newBinaryOp("+", right1, right2);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, Integer.MAX_VALUE);
    // Assuming '*' operator has a cost of 1, '+' operator has a cost of 1,
    // and identifiers have a cost of 2 characters.
    assertEquals(11, cost);
  }
}