import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyCode() {
    Node root = Node.NULL();
    int actualCost = getCost(root);
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfSingleStatement() {
    Node root = Node.newIntLiteral(42);
    int actualCost = getCost(root);
    int expectedCost = 1; // for the int literal
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfMultipleStatements() {
    Node root = Node.newBlock(
        Node.newExpressionStatement(Node.newIntLiteral(42)),
        Node.newExpressionStatement(Node.newIntLiteral(24))
    );
    int actualCost = getCost(root);
    int expectedCost = 3; // for the int literals and the block
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    Node root = Node.newExpressionStatement(
        Node.newBinaryOp(Node.OP_MUL, Node.newBinaryOp(Node.OP_ADD, Node.newIntLiteral(42), Node.newIntLiteral(5)), Node.newIntLiteral(3))
    );
    int actualCost = getCost(root);
    int expectedCost = 13; // for the int literals, operators, and the block
    assertEquals(expectedCost, actualCost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    int costThreshhold = -1;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshhold);
    estimator.add(Node.NULL());
  }

  @Test
  public void testGetCostWithCostThreshold() {
    int costThreshhold = 5;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshhold);
    estimator.add(Node.NULL());
    int actualCost = estimator.getCost();
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);

    estimator.add(Node.newIntLiteral(42));
    estimator.add(Node.newIntLiteral(24));
    estimator.add(Node.newBlock(
        Node.newExpressionStatement(Node.newIntLiteral(42)),
        Node.newExpressionStatement(Node.newIntLiteral(24))
    ));
    estimator.add(Node.newBinaryOp(Node.OP_ADD, Node.newIntLiteral(42), Node.newIntLiteral(24)));
    estimator.add(Node.newBinaryOp(Node.OP_MUL, Node.newIntLiteral(42), Node.newIntLiteral(24)));

    int finalCost = estimator.getCost();
    int expectedFinalCost = 25; // for the int literals, operators, and the block
    assertEquals(expectedFinalCost, finalCost);
  }
}