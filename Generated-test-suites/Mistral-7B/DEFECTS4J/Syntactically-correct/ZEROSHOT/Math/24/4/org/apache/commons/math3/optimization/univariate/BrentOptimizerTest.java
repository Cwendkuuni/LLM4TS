import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 // Test with default constructor
 BrentOptimizer optimizer = new BrentOptimizer(null);
 UnivariateFunction function = (x) -> Math.pow(x, 2);
 OptimizationResult result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(3.0, Precision.round(result.getPoint().getValue(), 3), 0.001);

 // Test with constructor with relative and absolute tolerance
 BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.1, null);
 OptimizationResult result1 = optimizer1.optimize(function, -5.0, 5.0);
 assertEquals(2.449, Precision.round(result1.getPoint().getValue(), 4), 0.001);

 // Test with constructor with only relative tolerance
 BrentOptimizer optimizer2 = new BrentOptimizer(0.01, null);
 OptimizationResult result2 = optimizer2.optimize(function, -5.0, 5.0);
 assertEquals(2.44948974278317, Precision.round(result2.getPoint().getValue(), 7), 0.001);

 // Test with constructor with only absolute tolerance
 BrentOptimizer optimizer3 = new BrentOptimizer(0.0, 0.1);
 OptimizationResult result3 = optimizer3.optimize(function, -5.0, 5.0);
 assertEquals(2.962500000000001, Precision.round(result3.getPoint().getValue(), 6), 0.001);

 // Test with a custom ConvergenceChecker
 class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 return (current.getValue() < 0.001);
 }
 }
 BrentOptimizer optimizer4 = new BrentOptimizer(0.01, 0.1, new MyConvergenceChecker());
 OptimizationResult result4 = optimizer4.optimize(function, -5.0, 5.0);
 assertEquals(2.44948974278317, Precision.round(result4.getPoint().getValue(), 7), 0.001);
 }
}