import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private ObjectiveFunction objectiveFunction;
 private BrentOptimizer optimizer;

 @Before
 public void setUp() {
 objectiveFunction = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.PI, 2);
 }
 };
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-8);
 }

 @Test
 public void testOptimize() {
 // Test minimization
 PointValuePair result = optimizer.optimize(objectiveFunction, 1.0, 2.0);
 assertEquals(Math.PI * Math.sqrt(6), result.getPoint(), 1.0e-6);
 assertEquals(-3.141592653589793, result.getValue(), 1.0e-8);

 // Test positive absolute threshold
 optimizer = new BrentOptimizer(1.0e-6, 1.0);
 result = optimizer.optimize(objectiveFunction, 1.0, 2.0);
 assertEquals(Math.PI * Math.sqrt(6), result.getPoint(), 1.0e-6);
 assertNotNull(result.getValue());

 // Test negative absolute threshold
 optimizer = new BrentOptimizer(1.0e-6, -1.0);
 try {
 optimizer.optimize(objectiveFunction, 1.0, 2.0);
 fail("Expected NumberIsStrictlyPositiveException");
 } catch (NumberIsStrictlyPositiveException e) {
 // Expected
 }

 // Test relative tolerance smaller than minimum
 optimizer = new BrentOptimizer(1.0e-10, 1.0e-8);
 try {
 optimizer.optimize(objectiveFunction, 1.0, 2.0);
 fail("Expected NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }

 // Test null convergence checker
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, null);
 result = optimizer.optimize(objectiveFunction, 1.0, 2.0);
 assertEquals(Math.PI * Math.sqrt(6), result.getPoint(), 1.0e-6);
 assertNotNull(result.getValue());

 // Test custom initial guess
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-8);
 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialPoint() {
 return 3.0;
 }
 };
 result = optimizer.optimize(objectiveFunction, initialGuess);
 assertEquals(Math.PI * Math.sqrt(6), result.getPoint(), 1.0e-6);
 assertNotNull(result.getValue());
 }
}