import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompiledSizeEstimator;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    when(root.getFirstChild()).thenReturn(null);

    InlineCostEstimator.getCost(root, estimator.getMaxCost());

    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostWithChild() {
    Node root = mock(Node.class);
    Node child = mock(Node.class);

    when(child.getTokenType()).thenReturn(Node.TOKEN_Identifier);
    when(child.getFirstChild()).thenReturn(null);
    when(root.getFirstChild()).thenReturn(child);

    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    InlineCostEstimator.getCost(root, estimator.getMaxCost());

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testGetCostWithMultipleChildren() {
    Node root = mock(Node.class);
    Node child1 = mock(Node.class);
    Node child2 = mock(Node.class);

    when(child1.getTokenType()).thenReturn(Node.TOKEN_Identifier);
    when(child1.getFirstChild()).thenReturn(null);
    when(child2.getTokenType()).thenReturn(Node.TOKEN_Identifier);
    when(child2.getFirstChild()).thenReturn(null);
    when(root.getFirstChild()).thenReturn(child1);
    when(child1.getNextSibling()).thenReturn(child2);

    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    InlineCostEstimator.getCost(root, estimator.getMaxCost());

    assertEquals(2 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testGetCostWithCostThresholdExceeded() {
    Node root = mock(Node.class);
    Node child = mock(Node.class);

    when(child.getTokenType()).thenReturn(Node.TOKEN_Literal);
    when(child.getFirstChild()).thenReturn(null);
    when(root.getFirstChild()).thenReturn(child);

    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);

    InlineCostEstimator.getCost(root, estimator.getMaxCost());

    assertEquals(5, estimator.getCost()); // cost threshold exceeded, but we still process the node
  }
}