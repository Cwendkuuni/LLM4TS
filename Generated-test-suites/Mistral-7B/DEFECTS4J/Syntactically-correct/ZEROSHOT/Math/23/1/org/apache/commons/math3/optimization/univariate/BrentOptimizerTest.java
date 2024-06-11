import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6); //Relative and Absolute threshold

 // Test with a simple function
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2 * x + 1;
 }

 @Override
 public double derivative(double x) {
 return 2 * x - 2;
 }
 };

 SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
 InitialGuess initialGuess = new InitialGuess(0.0);
 PointValuePair optimalSolution = optimizer.optimize(function, bounds, initialGuess);
 double expectedSolution = -1.0;
 double tolerance = 1.0e-6;
 assertTrue("The optimal solution is " + optimalSolution + " while the expected solution is " + expectedSolution,
 Precision.isClose(optimalSolution.getPoint(), expectedSolution, tolerance));
 }

 @Test
 public void testBrentOptimizerWithUserDefinedChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-4, 1.0e-4); //Relative and Absolute threshold

 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x + 1;
 }

 @Override
 public double derivative(double x) {
 return 3 * Math.pow(x, 2) - 2;
 }
 };

 SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
 InitialGuess initialGuess = new InitialGuess(0.0);
 BrentOptimizer.ConvergenceChecker<PointValuePair> customChecker = new BrentOptimizer.ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
 return current.getValue() <= previous.getValue() && iteration > 10;
 }
 };

 optimizer.setConvergenceChecker(customChecker);
 PointValuePair optimalSolution = optimizer.optimize(function, bounds, initialGuess);
 double expectedSolution = -1.25;
 double tolerance = 1.0e-4;
 assertTrue("The optimal solution is " + optimalSolution + " while the expected solution is " + expectedSolution,
 Precision.isClose(optimalSolution.getPoint(), expectedSolution, tolerance));
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithInvalidAbsThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6); //Invalid Absolute threshold
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithInvalidRelThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6); //Invalid Relative threshold
 }
}