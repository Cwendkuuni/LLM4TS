import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.nullLiteral();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSingleIdentifierNode() {
    String identifier = "testIdentifier";
    Node root = Node.newIdent(identifier);
    int cost = getCost(root);
    assertEquals(identifier.length(), cost);
  }

  @Test
  public void testGetCostOfSimpleExpressionNode() {
    String identifier1 = "identifier1";
    String identifier2 = "identifier2";
    Node root = Node.newBinOp("+", Node.newIdent(identifier1), Node.newIdent(identifier2));
    int cost = getCost(root, 100);
    assertEquals(identifier1.length() + identifier2.length() + 2, cost); // + sign and space
  }

  @Test
  public void testGetCostOfComplexExpressionNode() {
    String identifier1 = "identifier1";
    String identifier2 = "identifier2";
    Node root = Node.newBinOp("+", Node.newBinOp("*", Node.newIdent(identifier1), Node.newNumber(2)),
        Node.newBinOp("-", Node.newIdent(identifier2), Node.newNumber(3)));
    int cost = getCost(root, 150);
    assertEquals(identifier1.length() + 2 + identifier2.length() + 4, cost); // * / + - and spaces
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    int costThreshhold = -1;
    Node root = Node.newNumber(1);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(costThreshhold);
    estimator.add(root);
  }

  @Test
  public void testGetCostWithMaxCostReached() {
    String longIdentifier = "longLongLongIdentifier";
    Node root = Node.newIdent(longIdentifier);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(longIdentifier.length().intValue());
    estimator.add(root);
    assertTrue(estimator.continueProcessing());
    int cost = estimator.getCost();
    assertEquals(longIdentifier.length(), cost);
    assertFalse(estimator.continueProcessing());
  }
}