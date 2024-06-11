import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateFunction testFunction;
 private BrentOptimizer testOptimizer;

 @Before
 public void setUp() {
 testFunction = new MyTestFunction();
 testOptimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 }

 @Test
 public void testOptimize_Minimum() {
 UnivariatePointValuePair result = testOptimizer.optimize(testFunction, 1.0, 3.0);
 assertEquals(2.0, result.getPoint(), 1.0e-6);
 assertEquals(-3.0, result.getValue(), 1.0e-6);
 }

 @Test
 public void testOptimize_Maximum() {
 UnivariatePointValuePair result = testOptimizer.optimize(testFunction.negate(), 1.0, 3.0);
 assertEquals(3.0, result.getPoint(), 1.0e-6);
 assertEquals(-2.0, result.getValue(), 1.0e-6);
 }

 @Test
 public void testOptimize_OutOfRange() {
 try {
 testOptimizer.optimize(testFunction, 1.0, 1.0);
 fail("Expected NumberOutOfBoundsException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }

 try {
 testOptimizer.optimize(testFunction, 3.0, 1.0);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
}

 @Test
 public void testOptimize_NoRoot() {
 testFunction = new MyTestFunctionNoRoot();
 try {
 testOptimizer.optimize(testFunction, 1.0, 3.0);
 fail("Expected UnsatisfiedConstraintException");
 } catch (UnsatisfiedConstraintException e) {
 // Expected
 }
}

 @Test
 public void testOptimize_ConvergenceChecker() {
 ConvergenceChecker<UnivariatePointValuePair> checker = new MyTestConvergenceChecker();
 testOptimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
 UnivariatePointValuePair result = testOptimizer.optimize(testFunction, 1.0, 3.0);
 assertTrue(checker.isConverged());
}

 @After
 public void tearDown() {
 testFunction = null;
 testOptimizer = null;
 }

 static class MyTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 }

 static class MyTestFunctionNoRoot implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + 1.0;
 }
 }

 static class MyTestConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() == previous.getValue();
 }
}
}