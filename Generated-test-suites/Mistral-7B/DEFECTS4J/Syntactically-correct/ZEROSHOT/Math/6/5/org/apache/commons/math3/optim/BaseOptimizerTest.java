import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest<PAIR> {

 private BaseOptimizer<PAIR> baseOptimizer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 baseOptimizer = (BaseOptimizer<PAIR>) mock(BaseOptimizer.class);
 }

 @Test
 public void testGetMaxEvaluations() {
 int maxEvaluations = 10;
 when(baseOptimizer.getMaxEvaluations()).thenReturn(maxEvaluations);
 assertEquals(maxEvaluations, baseOptimizer.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 int evaluations = 5;
 when(baseOptimizer.getEvaluations()).thenReturn(evaluations);
 assertEquals(evaluations, baseOptimizer.getEvaluations());
 }

 @Test
 public void testGetConvergenceChecker() {
 ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
 when(baseOptimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
 assertEquals(convergenceChecker, baseOptimizer.getConvergenceChecker());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedMethods() {
 try {
 baseOptimizer.optimize(new MaxEval(1, null));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 fail("optimize method should throw UnsupportedOperationException");
 }
}