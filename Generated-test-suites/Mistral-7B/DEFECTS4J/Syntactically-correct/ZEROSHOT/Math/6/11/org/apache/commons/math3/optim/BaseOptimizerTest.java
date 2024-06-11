import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseOptimizerTest {

 private BaseOptimizer<Object> optimizer;
 private ConvergenceChecker<Object> convergenceChecker;

 @Before
 public void setUp() {
 optimizer = new BaseOptimizer<Object>() {
 @Override
 public int getMaxEvaluations() {
 return 10;
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
 public boolean isConverged(Object point, Object objective, int iteration) {
 return false;
 }
 };
 optimizer.setConvergenceChecker(convergenceChecker);
 }

 @Test
 public void testGetMaxEvaluations() {
 assertEquals(10, optimizer.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 optimizer.optimize(null, null);
 assertEquals(1, optimizer.getEvaluations());
 }

 @Test
 public void testGetConvergenceChecker() {
 ConvergenceChecker<Object> checker = optimizer.getConvergenceChecker();
 assertNotNull(checker);
 }
}