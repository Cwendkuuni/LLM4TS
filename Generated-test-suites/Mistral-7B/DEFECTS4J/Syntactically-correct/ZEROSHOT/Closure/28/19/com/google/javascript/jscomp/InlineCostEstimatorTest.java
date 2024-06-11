import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledSizeEstimator;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    InlineCostEstimator estimator = new InlineCostEstimator();

    CompiledSizeEstimator compiledSizeEstimator = mock(CompiledSizeEstimator.class);
    when(compiledSizeEstimator.getCost()).thenReturn(10);
    when(estimator.new CompiledSizeEstimator(Integer.MAX_VALUE)).thenReturn(compiledSizeEstimator);

    when(root.getClass()).thenReturn(Node.class);

    assertEquals(10, estimator.getCost(root));
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = mock(Node.class);
    InlineCostEstimator estimator = new InlineCostEstimator();

    CompiledSizeEstimator compiledSizeEstimator = mock(CompiledSizeEstimator.class);
    when(compiledSizeEstimator.getCost()).thenReturn(15);
    when(estimator.new CompiledSizeEstimator(5)).thenReturn(compiledSizeEstimator);

    when(root.getClass()).thenReturn(Node.class);

    assertEquals(5, estimator.getCost(root, 5));
  }
}