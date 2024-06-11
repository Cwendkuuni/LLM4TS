import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

public class BrentOptimizerTest {

 @Test
 public void testBasicFunctionality() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 UnivariateOptimizer result = optimizer.optimize(x -> Math.pow(x, 2), 0, 1);
 PointValuePair expected = new PointValuePair(0.25, 0.0625);
 assertEquals(expected, result);
 }

 @Test
 public void testMinimization() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 UnivariateOptimizer result = optimizer.optimize(x -> Math.pow(x, 2), 2, 4);
 PointValuePair expected = new PointValuePair(2, 4);
 assertEquals(expected, result);
 }

 @Test
 public void testMaximization() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 UnivariateOptimizer result = optimizer.optimize(x -> -Math.pow(x, 2), 2, 4);
 PointValuePair expected = new PointValuePair(2, 16);
 assertEquals(expected, result);
 }

 @Test
 public void testEdgeCases() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

 // Function with roots
 UnivariateOptimizer result1 = optimizer.optimize(x -> Math.sin(x) - Math.sin(2 * x), 0, 2 * Math.PI);
 PointValuePair expected1 = new PointValuePair(Math.PI, 0);
 assertEquals(expected1, result1);

 // Function with multiple roots
 UnivariateOptimizer result2 = optimizer.optimizer(x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + 3 * x - 2, 1, 3);
 PointValuePair expected2 = new PointValuePair(1.5, 0.75);
 assertEquals(expected2, result2);

 // Function with inflection points
 UnivariateOptimizer result3 = optimizer.optimizer(x -> Math.exp(-x) * Math.sin(x), 0, 2 * Math.PI);
 PointValuePair expected3 = new PointValuePair(1.376293935, 0.1381136246);
 assertEquals(expected3, result3, 0.001);

 // Function with local minima/maxima
 UnivariateOptimizer result4 = optimizer.optimizer(x -> Math.pow(x, 2) * Math.sin(Math.PI / x), 0, 2);
 PointValuePair expected4 = new PointValuePair(1.265411766, 1.539213135);
 assertEquals(expected4, result4, 0.001);
 }

 @Test(expected = MaxEvalException.class)
 public void testFunctionWithNoRoots() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 UnivariateOptimizer result = optimizer.optimize(x -> Math.cos(x), 0, 2 * Math.PI);
 }

 @Test(expected = ConvergenceException.class)
 public void testFunctionWithNoConvergence() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 UnivariateOptimizer result = optimizer.optimize(x -> Math.sin(x) / x, 0, 1);
 }

 @Test
 public void testInitialGuess() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 UnivariateOptimizer result = optimizer.optimize(x -> Math.pow(x, 2), 2, 4, new InitialGuess(3));
 PointValuePair expected = new PointValuePair(3, 9);
 assertEquals(expected, result);
 }

 @Test
 public void testGoalTypeConstraint() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
 GoalTypeConstraint constraint = new GoalTypeConstraint(GoalType.MAXIMIZE);
 UnivariateOptimizer result = optimizer.optimize(x -> x * x, 2, 4, constraint);
 PointValuePair expected = new PointValuePair(4, 16);
 assertEquals(expected, result);
 }
}