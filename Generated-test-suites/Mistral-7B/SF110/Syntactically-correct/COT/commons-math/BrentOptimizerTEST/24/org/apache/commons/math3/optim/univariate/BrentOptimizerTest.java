import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private ObjectiveFunction testFunction;
 private BrentOptimizer optimizer;

 @Before
 public void setup() {
 testFunction = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 }

 @Test
 public void testConstructorWithChecker() {
 optimizer = new BrentOptimizer(1.0, 1.0, null);
 assertNotNull(optimizer.getConvergenceChecker());
 }

 @Test
 public void testConstructorWithoutChecker() {
 optimizer = new BrentOptimizer(1.0, 1.0);
 assertNull(optimizer.getConvergenceChecker());
 }

 @Test
 public void testOptimizeMinimum() throws Exception {
 PointValuePair result = optimizer.optimize(testFunction, 0.0, 10.0);
 assertEquals(1.0, result.getPoint(), 1e-6);
 assertEquals(0.0, result.getValue(), 1e-6);
 }

 @Test
 public void testOptimizeMaximum() throws Exception {
 optimizer = new BrentOptimizer(BrentOptimizer.GoalType.MAXIMIZE);
 PointValuePair result = optimizer.optimize(testFunction, 0.0, 10.0);
 assertEquals(10.0, result.getPoint(), 1e-6);
 assertEquals(100.0, result.getValue(), 1e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidRelativeThreshold() {
 optimizer = new BrentOptimizer(-1.0, 1.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidAbsoluteThreshold() {
 optimizer = new BrentOptimizer(1.0, -1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testInvalidRelativeThresholdValue() {
 optimizer = new BrentOptimizer(0.0, 1.0);
 }

 @Test
 public void testOptimizeWithInvalidRange() throws Exception {
 optimizer = new BrentOptimizer(1.0, 1.0);
 try {
 optimizer.optimize(testFunction, 10.0, 10.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testEdgeCaseWithFunctionsCrossing() throws Exception {
 double a = 0.0;
 double b = 2.0;
 double c = 4.0;
 testFunction = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 if (x <= a) return x * x;
 else return x * x * 2;
 }
 };
 PointValuePair result = optimizer.optimize(testFunction, a, c);
 assertEquals(a + Math.sqrt(3.0) / 2.0, result.getPoint(), 1e-6);
 }

 @Test
 public void testEdgeCaseWithFunctionsEqual() throws Exception {
 testFunction = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - Math.pow(x - 1.0, 2);
 }
 };
 PointValuePair result = optimizer.optimize(testFunction, 1.0, 2.0);
 assertEquals(1.0, result.getPoint(), 1e-6);
 assertEquals(0.0, result.getValue(), 1e-6);
}

 @Test
 public void testEdgeCaseWithFunctionsDiverging() throws Exception {
 testFunction = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return 1.0 / (x + 1.0);
 }
 };
 PointValuePair result = optimizer.optimize(testFunction, 0.0, -10.0);
 assertEquals(Double.NEGATIVE_INFINITY, result.getPoint(), 0.0);
 assertEquals(0.0, result.getValue(), 1e-6);
 }
}