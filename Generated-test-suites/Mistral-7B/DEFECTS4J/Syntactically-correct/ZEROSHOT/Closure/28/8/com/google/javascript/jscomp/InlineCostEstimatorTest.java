import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompiledSizeEstimator;
import com.google.javascript.rhino.CodeGenerator;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class InlineCostEstimatorTest {

 private InlineCostEstimator target = new InlineCostEstimator();
 private CompiledSizeEstimator compiledSizeEstimator;
 private CodeGenerator codeGenerator;

 @Before
 public void setup() {
 compiledSizeEstimator = mock(CompiledSizeEstimator.class);
 codeGenerator = mock(CodeGenerator.class);
 }

 @Test
 public void testGetCost() {
 Node root = mock(Node.class);
 when(root.getClass()).thenReturn(Node.class);

 InlineCostEstimator.getCost(root);
 ArgumentCaptor<CompiledSizeEstimator> estimatorArgCaptor = ArgumentCaptor.forClass(CompiledSizeEstimator.class);
 verify(compiledSizeEstimator).add(root);
 }

 @Test
 public void testGetCostWithThreshold() {
 Node root = mock(Node.class);
 when(root.getClass()).thenReturn(Node.class);

 InlineCostEstimator.getCost(root, 10);
 ArgumentCaptor<CompiledSizeEstimator> estimatorArgCaptor = ArgumentCaptor.forClass(CompiledSizeEstimator.class);
 verify(compiledSizeEstimator).add(root);
 }

 @Test
 public void testAddIdentifier() {
 String identifier = "testIdentifier";
 InlineCostEstimator estimator = new InlineCostEstimator();
 estimator.new CompiledSizeEstimator(Integer.MAX_VALUE);
 estimator.getCostEstimator().addIdentifier(identifier);

 assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * identifier.length(), estimator.getCostEstimator().getCost());
 }
}