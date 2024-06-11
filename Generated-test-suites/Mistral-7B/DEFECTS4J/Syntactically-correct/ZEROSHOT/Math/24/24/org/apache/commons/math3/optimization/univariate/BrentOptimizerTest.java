import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 // Test with default constructor
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);

 // Test with custom constructor
 BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001, null);

 // Test optimization of simple function
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return x * x;
 }
 };
 UnivariateOptimizer result = optimizer.optimize(function, -5.0, 5.0);
 double expected = 0.0;
 assertEquals(expected, result.getPoint(), 0.001);

 // Test optimization of function with goal type set to MAXIMIZE
 optimizer.setGoalType(UnivariateOptimizer.GOAL_MAXIMIZE);
 result = optimizer.optimize(function.negate(), -5.0, 5.0);
 expected = 25.0;
 assertEquals(expected, result.getPoint(), 0.001);

 // Test optimization of function with custom convergence checker
 ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
 return current.getValue() <= previous.getValue() - 0.0001;
 }
 };
 optimizer.setConvergenceChecker(customChecker);
 result = optimizer.optimize(function, -5.0, 5.0);
 assertNotNull(result);
 }
}