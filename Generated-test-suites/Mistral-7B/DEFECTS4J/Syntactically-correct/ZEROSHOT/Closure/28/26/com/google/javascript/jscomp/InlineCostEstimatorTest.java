import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompiledSizeEstimator;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);

    CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    when(root.getCodeConsumers()).thenReturn(compiledSizeEstimator);

    int cost = estimator.getCost(root);
    assertEquals("Cost should be calculated correctly.", cost, compiledSizeEstimator.getCost());
  }

  @Test
  public void testGetCostWithThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);

    CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(10);
    when(root.getCodeConsumers()).thenReturn(compiledSizeEstimator);

    int cost = estimator.getCost(root);
    assertEquals("Cost should be calculated correctly with threshold.", cost, compiledSizeEstimator.getCost());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCostWhenContinueProcessingIsFalse() {
    InlineCostEstimator estimator = new InlineCostEstimator();

    CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(5);
    compiledSizeEstimator.continueProcessing = false;

    Node root = mock(Node.class);
    when(root.getCodeConsumers()).thenReturn(compiledSizeEstimator);

    estimator.getCost(root);
  }
}