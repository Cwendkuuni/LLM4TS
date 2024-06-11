import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostRoot() {
    Node rootMock = mock(Node.class);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(rootMock);
    assertEquals(-1, cost); // getCost with no cost threshold should return -1

    CompiledSizeEstimator estimatorWithThreshold = new CompiledSizeEstimator(1);
    when(rootMock.getClass()).thenReturn(Node.class); // mock the class of the root node
    when(rootMock.getFirstChild()).thenReturn(null); // mock an empty node
    cost = estimatorWithThreshold.getCost(rootMock);
    assertEquals(0, cost); // an empty node should have 0 cost
  }

  @Test
  public void testGetCostNodeWithChildren() {
    Node rootMock = mock(Node.class);
    Node childMock = mock(Node.class);

    InlineCostEstimator estimator = new InlineCostEstimator();
    when(rootMock.getClass()).thenReturn(Node.class);
    when(rootMock.getFirstChild()).thenReturn(childMock);

    CompiledSizeEstimator estimatorWithThreshold = new CompiledSizeEstimator(1);
    estimatorWithThreshold.add(rootMock);

    when(childMock.getClass()).thenReturn(Node.class);
    when(childMock.getFirstChild()).thenReturn(null);
    estimatorWithThreshold.add(childMock);

    int cost = estimatorWithThreshold.getCost();
    assertEquals(2, cost); // one node for the root and one node for the child
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(1);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(3, cost); // one node for the identifier and the length of the identifier (3 characters)
  }
}