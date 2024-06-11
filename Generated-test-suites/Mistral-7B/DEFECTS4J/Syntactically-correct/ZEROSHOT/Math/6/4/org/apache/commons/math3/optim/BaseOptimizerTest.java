import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.optimize.AbstractDifferentiableOptimizer;
import org.apache.commons.math3.optim.nonlinear.optimize.DifferentialSearchOptimizer;
import org.apache.commons.math3.optim.precision.Precision;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

 private BaseOptimizer<PAIR> optimizer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 optimizer = new DifferentialSearchOptimizer<>(new LinearFunction(new double[][]{{0, 1}, {-1, -1}}, new double[]{-1, -1}), new AroundMinimumInitializer(0.1), new MaxEval(1000), GoalType.MINIMIZE, new SimpleBounds(0, 1, 0, 1));
 }

 @Test
 public void testGetMaxEvaluations() {
 int maxEvaluations = optimizer.getMaxEvaluations();
 assertEquals(1000, maxEvaluations);
 }

 @Test
 public void testGetEvaluations() {
 int evaluations = optimizer.getEvaluations();
 assertEquals(0, evaluations);
 }

 @Test
 public void testGetConvergenceChecker() {
 ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
 assertNotNull(convergenceChecker);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetConvergenceCheckerWithCustomImplementation() {
 optimizer = new BaseOptimizer<PAIR>() {
 @Override
 public ConvergenceChecker<PAIR> getConvergenceChecker() {
 throw new UnsupportedOperationException();
 }
 };
 optimizer.getConvergenceChecker();
 }

 @Test
 public void testSetAndGetConvergenceChecker() {
 ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
 @Override
 public boolean isConverged(PAIR currentValue, PAIR previousValue) {
 return Precision.abs(currentValue.getValue() - previousValue.getValue()) < 0.001;
 }
 };
 optimizer.setConvergenceChecker(convergenceChecker);
 ConvergenceChecker<PAIR> convergenceCheckerSet = optimizer.getConvergenceChecker();
 assertEquals(convergenceChecker, convergenceCheckerSet);
 }

 @Test
 public void testOptimize() {
 PointValuePair result = optimizer.optimize();
 assertNotNull(result);
 assertEquals(GoalType.MINIMIZE, result.getGoalType());
 }

 @Test(expected = UnconstrainedOptimizationException.class)
 public void testOptimizeWithConvergenceCheckerThrowsException() {
 ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
 @Override
 public boolean isConverged(PAIR currentValue, PAIR previousValue) {
 return false;
 }
 };
 optimizer.setConvergenceChecker(convergenceChecker);
 assertThrows(UnconstrainedOptimizationException.class, () -> optimizer.optimize());
 }

 @Test
 public void testOptimizeWithLinearConstraints() {
 LinearConstraint constraint = new LinearConstraint(new double[][]{new double[]{1, 1}}, 1);
 optimizer = new DifferentialSearchOptimizer<>(new LinearFunction(new double[][]{{0, 1}, {-1, -1}}, new double[]{-1, -1}), new AroundMinimumInitializer(0.1), new MaxEval(1000), GoalType.MINIMIZE, new SimpleBounds(0, 1, 0, 1), new LinearConstraint[]{constraint});
 PointValuePair result = optimizer.optimize();
 assertNotNull(result);
 assertFalse(constraint.isSatisfied(result.getPoint()));
 }

 @Test
 public void testGetEvaluationsAfterOptimize() {
 int evaluationsBeforeOptimize = optimizer.getEvaluations();
 optimizer.optimize();
 int evaluationsAfterOptimize = optimizer.getEvaluations();
 assertEquals(evaluationsBeforeOptimize + 1, evaluationsAfterOptimize);
 }
}