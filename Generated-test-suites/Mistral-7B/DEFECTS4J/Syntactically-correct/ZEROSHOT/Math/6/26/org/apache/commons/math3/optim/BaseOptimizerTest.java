import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

 private BaseOptimizer<Object> optimizer;
 private ConvergenceChecker<Object> convergenceChecker;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 optimizer = new BaseOptimizer<Object>() {
 @Override
 public int getMaxEvaluations() {
 return MaxEval.UNLIMITED;
 }

 @Override
 public int getEvaluations() {
 return 0;
 }

 @Override
 public ConvergenceChecker<Object> getConvergenceChecker() {
 return convergenceChecker;
 }
 };
 convergenceChecker = new ConvergenceChecker<Object>() {
 @Override
 public boolean isConverged(BaseOptimizer<Object> optimizer, Object bestPoint, double bestValue) {
 return false;
 }
 };
 optimizer.setConvergenceChecker(convergenceChecker);
 }

 @Test
 public void testGetMaxEvaluations() {
 assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMaxEvaluationsUnsupported() {
 try {
 optimizer.setMaxEvaluations(10);
 } catch (UnsupportedOperationException e) {
 throw new AssertionError("Should throw UnsupportedOperationException", e);
 }
 }

 @Test
 public void testGetEvaluations() {
 assertEquals(0, optimizer.getEvaluations());
 }

 @Test
 public void testSetAndGetConvergenceChecker() {
 ConvergenceChecker<Object> referenceChecker = new ConvergenceChecker<Object>() {
 @Override
 public boolean isConverged(BaseOptimizer<Object> optimizer, Object bestPoint, double bestValue) {
 return true;
 }
 };
 optimizer.setConvergenceChecker(referenceChecker);
 assertEquals(referenceChecker, optimizer.getConvergenceChecker());
}

 @Test(expected = UnsupportedOperationException.class)
 public void testSetConvergenceCheckerUnsupported() {
 try {
 optimizer.setConvergenceChecker(null);
 } catch (UnsupportedOperationException e) {
 throw new AssertionError("Should throw UnsupportedOperationException", e);
 }
}
}