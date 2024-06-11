import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

import java.util.Arrays;

public class BrentOptimizerTest {

 private UnivariateFunction testFunction;
 private BrentOptimizer brentOptimizer;

 @Before
 public void setup() {
 testFunction = new MyTestFunction();
 brentOptimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 }

 @Test
 public void testBrentOptimizerWithMinimizeGoal() {
 UnivariatePointValuePair result = brentOptimizer.optimize(testFunction, -5.0, 5.0);
 assertEquals(-1.0, result.getPoint(), 1e-5);
 assertEquals(-2.0, result.getValue(), 1e-5);
 }

 @Test
 public void testBrentOptimizerWithMaximizeGoal() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new MaximizationChecker(100));
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -5.0, 5.0);
 assertEquals(1.0, result.getPoint(), 1e-5);
 assertEquals(3.0, result.getValue(), 1e-5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testThrowsExceptionWhenAbsoluteThresholdIsZero() {
 new BrentOptimizer(1.0e-5, 0.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testThrowsExceptionWhenRelativeThresholdIsTooSmall() {
 new BrentOptimizer(1.0e-8, 1.0e-5);
 }

 @Test
 public void testConvergenceCheckerIsCalled() {
 ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
 brentOptimizer = new BrentOptimizer(1.0e-5, 1.0e-5, convergenceChecker);
 BrentOptimizer.UnivariatePointValuePair result = brentOptimizer.optimize(testFunction, -5.0, 5.0);
 verify(convergenceChecker).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
 }

 @Test
 public void testBrentOptimizerWithCustomStartValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 optimizer.setStartValue(2.0);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -5.0, 5.0);
 assertEquals(2.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerWithCustomMinValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 optimizer.setMin(-3.0);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -5.0, 5.0);
 assertEquals(-3.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerWithCustomMaxValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 optimizer.setMax(3.0);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -5.0, 5.0);
 assertEquals(3.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerWithCustomFunction() {
 UnivariateFunction customFunction = new MyCustomFunction();
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 UnivariatePointValuePair result = optimizer.optimize(customFunction, -5.0, 5.0);
 assertEquals(1.0, result.getPoint(), 1e-5);
 assertEquals(4.0, result.getValue(), 1e-5);
 }

 @After
 public void teardown() {
 testFunction = null;
 brentOptimizer = null;
 }

 private static class MyTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2.0) - 1.0;
 }
 }

 private static class MyCustomFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2.0) + 3.0;
 }
 }
}