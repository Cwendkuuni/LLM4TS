import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BrentOptimizerTest {

 private UnivariateFunction testFunction;
 private double minValue;
 private double minPosition;

 @Before
 public void setUp() {
 testFunction = new MyTestFunction();
 }

 @Test
 public void testBrentOptimizerMinimize() throws OptimizeException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -1.0, 1.0);
 minValue = result.getValue();
 minPosition = result.getPoint();
 assertTrue(result.isSolution());
 assertEquals(minValue, 0.5, 1e-5);
 assertEquals(minPosition, 0.5, 1e-5);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerMinRelativeTooSmall() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-5, null);
 try {
 optimizer.optimize(testFunction, -1.0, 1.0);
 fail("NumberIsTooSmallException expected");
 } catch (NumberIsTooSmallException ex) {
 // pass
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerAbsThresholdNegative() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, -1.0, null);
 try {
 optimizer.optimize(testFunction, -1.0, 1.0);
 fail("NotStrictlyPositiveException expected");
 } catch (NotStrictlyPositiveException ex) {
 // pass
 }
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() throws OptimizeException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new MyConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -1.0, 1.0);
 minValue = result.getValue();
 minPosition = result.getPoint();
 assertTrue(result.isSolution());
 assertEquals(minValue, 0.5, 1e-5);
 assertEquals(minPosition, 0.5, 1e-5);
 }

 @Test
 public void testBrentOptimizerIterations() throws OptimizeException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -1.0, 1.0);
 minValue = result.getValue();
 minPosition = result.getPoint();
 int iterations = ((BrentOptimizer) optimizer).getIterations();
 assertTrue(iterations >= 1);
 }

 @After
 public void tearDown() {
 // clear static variables
 BrentOptimizer.GOLDEN_SECTION = 0;
 BrentOptimizer.MIN_RELATIVE_TOLERANCE = 0;
 }

 private static class MyTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - Math.pow(x, 2) + 2 * x - 2;
 }
 }

 private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean test(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return (previous != null && current.getValue() - previous.getValue() < 1e-6);
 }
 }
}