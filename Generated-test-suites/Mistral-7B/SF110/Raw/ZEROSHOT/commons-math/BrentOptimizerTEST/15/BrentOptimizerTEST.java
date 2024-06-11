import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

 private static final double EPSILON = 1e-8;

 @Test
 public void testBrentOptimizerWithMinimizationGoal() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 2.0;
 }
 }, GoalType.MINIMIZE);
 assertEquals(-2.0, result.getPoint(), EPSILON);
 assertEquals(-4.0, result.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithMaximizationGoal() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 1.0;
 }
 }, GoalType.MAXIMIZE);
 assertEquals(1.0, result.getPoint(), EPSILON);
 assertEquals(1.0, result.getValue(), EPSILON);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithInvalidRelativeTolerance() {
 BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-6, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithInvalidAbsoluteTolerance() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6, null);
 }

 @Test
 public void testBrentOptimizerWithCustomConvergenceChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, new ConvergenceChecker() {
 @Override
 public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
 return current.getValue() - previous.getValue() < 0.001;
 }
 });
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 0.0;
 }
 }, GoalType.MINIMIZE);
 assertEquals(Math.PI, result.getPoint(), EPSILON);
 assertEquals(0.0, result.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithInvalidGoalType() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 try {
 optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 2.0;
 }
 }, GoalType.NEUTRAL);
 fail("Brent Optimizer should not support neutral goal type.");
 } catch (IllegalArgumentException ex) {
 // Expected
 }
 }

 @Test
 public void testBrentOptimizerWithDifferentMinAndMax() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return -x * x;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 2.0;
 }
 }, GoalType.MINIMIZE);
 assertEquals(-2.0, result.getPoint(), EPSILON);
 assertEquals(4.0, result.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithInitialValuesCloseToEachOther() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 1.5;
 }
 }, GoalType.MINIMIZE);
 assertEquals(-2.0, result.getPoint(), EPSILON);
 assertEquals(-4.0, result.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithInitialValuesFarApart() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 1.0;
 }
 }, GoalType.MINIMIZE);
 assertEquals(-2.0, result.getPoint(), EPSILON);
 assertEquals(-4.0, result.getValue(), EPSILON);
 PointValuePair result2 = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 3.0;
 }
 }, GoalType.MINIMIZE);
 assertEquals(-2.0, result2.getPoint(), EPSILON);
 assertEquals(-4.0, result2.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithConvergedIterations() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 2.0;
 }
 }, GoalType.MINIMIZE);
 assertTrue(optimizer.getIterations() > 0);
 assertEquals(-2.0, result.getPoint(), EPSILON);
 assertEquals(-4.0, result.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithNoImprovement() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return x * Math.sin(Math.PI / 2.0) + 1.0;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 PointValuePair result = optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 1.0;
 }
 }, GoalType.MINIMIZE);
 assertEquals(1.0, result.getPoint(), EPSILON);
 assertEquals(1.0, result.getValue(), EPSILON);
 }

 @Test
 public void testBrentOptimizerWithDivergingFunction() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.exp(1.0 / (x + 0.001));
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 try {
 optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return -1.0;
 }
 }, GoalType.MINIMIZE);
 fail("Brent Optimizer should not find a minimum for a diverging function.");
 } catch (DivergingIterationsException ex) {
 // Expected
 }
 }

 @Test
 public void testBrentOptimizerWithNoSolution() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI) * Math.cos(Math.PI);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 try {
 optimizer.optimize(function, new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 0.0;
 }
 }, GoalType.MINIMIZE);
 fail("Brent Optimizer should not find a solution for a no-solution function.");
 } catch (NoSolutionException ex) {
 // Expected
 }
 }
}