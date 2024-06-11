import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.function.GoalTypeAwareScalarFunction;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);

 // Test with a simple function
 GoalTypeAwareScalarFunction func = new GoalTypeAwareScalarFunction() {
 @Override
 public double value(double x) {
 return Math.exp(x) - E.E;
 }

 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 };

 PointValuePair solution = optimizer.optimize(func, new SimpleBounds(0.0, 1.0), new MaxEval(100));
 assertEquals(0.0, solution.getPoint(), 0.0001);
 assertEquals(-E.E, solution.getValue(), 1e-14);

 // Test with a function with multiple roots
 GoalTypeAwareScalarFunction funcMultipleRoots = new GoalTypeAwareScalarFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 3 * Math.pow(x, 2) + 2 * x - 2;
 }

 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 };

 PointValuePair multipleRootsSolution = optimizer.optimize(funcMultipleRoots, new SimpleBounds(-2.0, 2.0), new MaxEval(100));
 assertEquals(-1.0, multipleRootsSolution.getPoint(), 0.01);
 assertEquals(2.0, multipleRootsSolution.getValue(), 1e-14);

 // Test with non-strictly positive initial threshold
 try {
 new BrentOptimizer(-0.1, 0.01);
 fail("Expecting a NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 // Test with a too small relative threshold
 try {
 new BrentOptimizer(0.0, 0.0);
 fail("Expecting a NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
 }
}