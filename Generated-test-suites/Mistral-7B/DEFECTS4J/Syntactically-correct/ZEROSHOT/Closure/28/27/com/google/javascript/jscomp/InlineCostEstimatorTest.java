import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_NullNode_ReturnsZero() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    assertEquals(0, estimator.getCost(null));
  }

  @Test
  public void testGetCost_EmptyNode_ReturnsZero() {
    Node root = Node.EMPTY_NODE;
    InlineCostEstimator estimator = new InlineCostEstimator();
    assertEquals(0, estimator.getCost(root));
  }

  @Test
  public void testGetCost_SimpleNode_ReturnsCorrectCost() {
    Node root = Node.newIdent("var x");
    InlineCostEstimator estimator = new InlineCostEstimator();
    CodeConsumer mockCodeConsumer = mock(CodeConsumer.class);
    when(mockCodeConsumer.continueProcessing()).thenReturn(true);

    InlineCostEstimator.CompiledSizeEstimator compiledSizeEstimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.setCodeConsumer(compiledSizeEstimator);

    estimator.add(root);

    assertEquals(2, estimator.getCost(root));
  }

  @Test
  public void testGetCost_ComplexNode_ReturnsCorrectCost() {
    Node root = Node.newCall(Node.newIdent("Math"), "pow", Node.newDouble(2), Node.newDouble(3));
    InlineCostEstimator estimator = new InlineCostEstimator();
    CodeConsumer mockCodeConsumer = mock(CodeConsumer.class);
    when(mockCodeConsumer.continueProcessing()).thenReturn(true);

    InlineCostEstimator.CompiledSizeEstimator compiledSizeEstimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.setCodeConsumer(compiledSizeEstimator);

    estimator.add(root);

    assertEquals(13, estimator.getCost(root));
  }

  @Test
  public void testGetCostWithThreshold_NodeCostExceedsThreshold_ProcessingInterrupted() {
    Node root = Node.newCall(Node.newIdent("Math"), "pow", Node.newDouble(2), Node.newDouble(3));
    InlineCostEstimator estimator = new InlineCostEstimator();
    CodeConsumer mockCodeConsumer = mock(CodeConsumer.class);
    when(mockCodeConsumer.continueProcessing()).thenReturn(true);

    InlineCostEstimator.CompiledSizeEstimator compiledSizeEstimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(root);
    estimator.setCodeConsumer(compiledSizeEstimator);

    estimator.add(root);

    assertEquals(13, estimator.getCost(root));
    assertEquals(false, estimator.getCodeConsumer().continueProcessing());
  }
}