import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CompiledSizeEstimator;
import com.google.javascript.rhino.CompiledCode;
import com.google.javascript.rhino.CodeGenerator;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    CodeGenerator codeGenerator = mock(CodeGenerator.class);

    when(root.getFirstChild()).thenReturn(null);
    when(codeGenerator.toString(root)).thenReturn("");

    estimator.add(root);

    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostWithComplexNode() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    CodeGenerator codeGenerator = mock(CodeGenerator.class);

    when(root.getFirstChild()).thenReturn(mock(Node.class));
    when(root.getLastChild()).thenReturn(mock(Node.class));
    when(root.getClass()).thenReturn(CompiledCode.class);

    when(codeGenerator.toString(root)).thenReturn("complexCode");

    estimator.add(root);

    assertEquals(7, estimator.getCost());
  }

  @Test
  public void testGetCostWithCostThreshold() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    CodeGenerator codeGenerator = mock(CodeGenerator.class);

    when(root.getFirstChild()).thenReturn(null);
    when(codeGenerator.toString(root)).thenReturn("complexCode");

    estimator.add(root);

    assertEquals(5, estimator.getCost());
  }

  @Test(expected = IllegalStateException.class)
  public void testContinueProcessingFalse() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    CodeGenerator codeGenerator = mock(CodeGenerator.class);

    when(root.getFirstChild()).thenReturn(null);
    when(codeGenerator.toString(root)).thenReturn("complexCode");

    estimator.add(root);
    estimator.add(root);

    estimator.getCost();
  }
}