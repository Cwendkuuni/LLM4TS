import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeRegistry;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.equals;
import static org.apache.commons.math3.util.Precision.abs;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;
 private ConvergenceChecker<PointValuePair> checker;

 @Before
 public void setup() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 checker = new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
 return iter > 1000;
 }
 };
 }

 @Test
 public void testBrentOptimizerConstructorWithRelAndAbs() {
 optimizer = new BrentOptimizer(1.0, 1.0, checker);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerConstructorWithRelTooSmall() {
 optimizer = new BrentOptimizer(0.5, 1.0, checker);
 fail("Should not create optimizer with relative tolerance smaller than minimum");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerConstructorWithAbsLessThanZero() {
 optimizer = new BrentOptimizer(1.0, -1.0, checker);
 fail("Should not create optimizer with absolute tolerance less than zero");
 }

 @Test
 public void testDoOptimize_minimize() {
 PointValuePair result = optimizer.optimize(new ScalarFunction("f", x -> Math.pow(x, 2) + Math.cos(x)), new InitialGuess("x0", 1.0), GoalTypeRegistry.MINIMIZE);
 PointValuePair expected = new PointValuePair(1.0, 0.0);
 assertEquals(expected, result);
 }

 @Test
 public void testDoOptimize_minimize_nonlinearConstraint() {
 PointValuePair result;
 ScalarFunction function = new ScalarFunction("f", x -> Math.pow(x, 2) + Math.cos(x) - 2.0);
 NonlinearConstraint constraint = new NonlinearConstraint("x > 0", x -> x > 0.0);
 InitialGuess initialGuess = new InitialGuess("x0", 1.0);
 result = optimizer.optimize(function, initialGuess, GoalTypeRegistry.MINIMIZE, constraint);
 PointValuePair expected = new PointValuePair(1.0, 1.0);
 assertEquals(expected, result);
 }

 @Test
 public void testDoOptimize_maximize() {
 PointValuePair result = optimizer.optimize(new ScalarFunction("f", x -> Math.pow(x, 2) - Math.cos(x)), new InitialGuess("x0", 1.0), GoalTypeRegistry.MAXIMIZE);
 PointValuePair expected = new PointValuePair(1.0, 0.0);
 assertEquals(expected, result);
 }

 @Test
 public void testDoOptimize_maximize_nonlinearConstraint() {
 PointValuePair result;
 ScalarFunction function = new ScalarFunction("f", x -> Math.pow(x, 2) - Math.cos(x) + 2.0);
 NonlinearConstraint constraint = new NonlinearConstraint("x < 0", x -> x < 0.0);
 InitialGuess initialGuess = new InitialGuess("x0", 1.0);
 result = optimizer.optimize(function, initialGuess, GoalTypeRegistry.MAXIMIZE, constraint);
 PointValuePair expected = new PointValuePair(1.0, 1.0);
 assertEquals(expected, result);
 }

 @Test
 public void testBest() {
 PointValuePair a = new PointValuePair(1.0, 1.0);
 PointValuePair b = new PointValuePair(2.0, 2.0);
 PointValuePair c = new PointValuePair(3.0, 3.0);
 assertTrue(optimizer.best(null, b, GoalTypeRegistry.MINIMIZE) == b);
 assertTrue(optimizer.best(a, null, GoalTypeRegistry.MINIMIZE) == a);
 assertTrue(optimizer.best(a, b, GoalTypeRegistry.MINIMIZE) == (a.getValue() <= b.getValue() ? a : b));
 assertTrue(optimizer.best(a, b, GoalTypeRegistry.MAXIMIZE) == (a.getValue() >= b.getValue() ? a : b));
 }
}