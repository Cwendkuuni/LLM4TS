import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;
 private UnivariateFunction function;

 @Before
 public void setUp() {
 function = new MyUnivariateFunction();
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5);
 }

 @Test
 public void testOptimization() {
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
 assertEquals(3.0, result.getPoint(), 1.0E-5);
 assertEquals(-2.3336779772205586, result.getValue(), 1.0E-5);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSmallRelativeTolerance() {
 optimizer = new BrentOptimizer(1.0E-9, 1.0E-5);
 optimizer.optimize(function, 0.0, 10.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1.0E-5, -1.0E-5);
 optimizer.optimize(function, 0.0, 10.0);
 }

 @Test
 public void testConvergenceChecker() {
 ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, checker);
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
 checker.assertConverged(0, null, result);
 }

 @Test
 public void testGoalType() {
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 UnivariatePointValuePair resultMin = optimizer.optimize(new MyUnivariateFunctionMin(), -10.0, 10.0);
 assertEquals(-3.0, resultMin.getPoint(), 1.0E-5);
 assertEquals(3.0, resultMin.getValue(), 1.0E-5);

 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 UnivariatePointValuePair resultMax = optimizer.optimize(new MyUnivariateFunctionMax(), -10.0, 10.0);
 assertEquals(3.0, resultMax.getPoint(), 1.0E-5);
 assertEquals(-3.0, resultMax.getValue(), 1.0E-5);
 }

 @Test
 public void testEdgeCases() {
 // Minimization: lower bound is equal to the minimum
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 UnivariatePointValuePair resultMin = optimizer.optimize(new MyUnivariateFunctionMin(), -1.0, -1.0);
 assertEquals(-1.0, resultMin.getPoint(), 1.0E-5);
 assertEquals(0.0, resultMin.getValue(), 1.0E-5);

 // Minimization: upper bound is equal to the minimum
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 UnivariatePointValuePair resultMin = optimizer.optimize(new MyUnivariateFunctionMin(), 1.0, 1.0);
 assertEquals(1.0, resultMin.getPoint(), 1.0E-5);
 assertEquals(0.0, resultMin.getValue(), 1.0E-5);

 // Maximization: lower bound is equal to the maximum
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 UnivariatePointValuePair resultMax = optimizer.optimize(new MyUnivariateFunctionMax(), -1.0, -1.0);
 assertEquals(-1.0, resultMax.getPoint(), 1.0E-5);
 assertEquals(1.0, resultMax.getValue(), 1.0E-5);

 // Maximization: upper bound is equal to the maximum
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 UnivariatePointValuePair resultMax = optimizer.optimize(new MyUnivariateFunctionMax(), 1.0, 1.0);
 assertEquals(1.0, resultMax.getPoint(), 1.0E-5);
 assertEquals(1.0, resultMax.getValue(), 1.0E-5);
 }

 @Test
 public void testErrorScenarios() {
 // Null function
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 try {
 optimizer.optimize(null, 0.0, 10.0);
 fail("Expected NumberIsNotDefinedException");
 } catch (NumberIsNotDefinedException e) {
 // Expected
 }

 // Null lower bound
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 try {
 optimizer.optimize(function, null, 10.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Null upper bound
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 try {
 optimizer.optimize(function, 0.0, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}

class MyUnivariateFunction implements UnivariateFunction {

 @Override
 public double value(double x) {
 return Math.pow(x, 3.0) - 2.0 * x + 3.0;
 }
}

class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

 @Override
 public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 return previous == null && current != null;
 }
}