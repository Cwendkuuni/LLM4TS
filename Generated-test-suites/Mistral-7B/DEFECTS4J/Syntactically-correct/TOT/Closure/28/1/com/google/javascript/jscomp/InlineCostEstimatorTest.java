import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    getCost(root, estimator.getMaxCost());
    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostOfSimpleNode() {
    Node root = Node.IDENT("a");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    getCost(root, estimator.getMaxCost());
    assertEquals(ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testGetCostOfComplexNode() {
    Node root = Node.BINARY_EXPRESSION(
        Node.IDENT("a"),
        Node.OP_ADD(),
        Node.NUMBER(10)
    );
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    getCost(root, estimator.getMaxCost());
    assertEquals(ESTIMATED_IDENTIFIER_COST * 3, estimator.getCost());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(-1);
    getCost(Node.IDENT("a"), estimator.getMaxCost());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNullNode() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    getCost(Node.NULL(), estimator.getMaxCost());
  }
}