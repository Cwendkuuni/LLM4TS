import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompiledSizeEstimator;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeGenerator;
import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);

    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(root)).thenReturn(estimator);

    InlineCostEstimator.getCost(root);

    assertEquals(estimator.getCost(), InlineCostEstimator.getCost(root));
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(root)).thenReturn(estimator);

    InlineCostEstimator.getCost(root, 10);

    assertEquals(estimator.getCost(), InlineCostEstimator.getCost(root, 10));
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    InlineCostEstimator.InlineCostEstimator innerEstimator = new InlineCostEstimator.InlineCostEstimator();
    when(innerEstimator.getLastChar()).thenReturn('b');
    when(innerEstimator.continueProcessing()).thenReturn(true);

    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(mock(Node.class))).thenReturn(innerEstimator);

    InlineCostEstimator.addIdentifier(estimator, "identifier");

    assertEquals(estimator.getCost(), InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2);
  }

  @Test
  public void testAppend() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    when(estimator.getLastChar()).thenReturn('a');
    when(estimator.continueProcessing()).thenReturn(true);

    InlineCostEstimator.InlineCostEstimator innerEstimator = new InlineCostEstimator.InlineCostEstimator();
    when(innerEstimator.getLastChar()).thenReturn('b');
    when(innerEstimator.continueProcessing()).thenReturn(true);

    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(mock(Node.class))).thenReturn(innerEstimator);

    InlineCostEstimator.append(estimator, "test");

    assertEquals(estimator.getCost(), InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2 + 4);
  }

  @Test(expected = IllegalStateException.class)
  public void testExceedThreshold() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(1);

    when(estimator.getLastChar()).thenReturn('a');
    when(estimator.continueProcessing()).thenReturn(true);

    InlineCostEstimator.InlineCostEstimator innerEstimator = new InlineCostEstimator.InlineCostEstimator();
    when(innerEstimator.getLastChar()).thenReturn('b');
    when(innerEstimator.continueProcessing()).thenReturn(true);

    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(mock(Node.class))).thenReturn(innerEstimator);

    InlineCostEstimator.append(estimator, "test");

    InlineCostEstimator.append(estimator, "test2");
  }
}