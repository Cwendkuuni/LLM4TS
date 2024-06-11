import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledCode;
import com.google.javascript.rhino.Nodeops;
import com.google.javascript.rhino.Runtime;
import com.google.javascript.rhino.SourceFile;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    Node root = new Nodeops().parse(sourceFile, 0, null);

    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);

    assertEquals("Cost should be greater than zero", cost, estimator.ESTIMATED_IDENTIFIER_COST);
  }

  @Test
  public void testGetCostWithThreshold() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    Node root = new Nodeops().parse(sourceFile, 0, null);

    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, 10);

    assertEquals("Cost should be less than or equal to threshold", cost, estimator.ESTIMATED_IDENTIFIER_COST);
  }

  @Test
  public void testCompiledSizeEstimator() {
    Runtime runtime = mock(Runtime.class);
    CompiledCode compiledCode = mock(CompiledCode.class);
    Nodeops nodeops = mock(Nodeops.class);

    when(runtime.compileToCode(anyString(), anyInt(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(compiledCode);
    when(compiledCode.toString()).thenReturn("compiled code");

    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = new Nodeops().parse("test", nodeops, runtime);

    estimator.new CompiledSizeEstimator(10).add(root);

    assertEquals("Cost should be greater than zero", estimator.new CompiledSizeEstimator(10).getCost(), estimator.ESTIMATED_IDENTIFIER_COST);
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.new CompiledSizeEstimator(10).addIdentifier("testIdentifier");

    assertEquals("Cost should be greater than zero", estimator.new CompiledSizeEstimator(10).getCost(), estimator.ESTIMATED_IDENTIFIER_COST * 2);
  }
}