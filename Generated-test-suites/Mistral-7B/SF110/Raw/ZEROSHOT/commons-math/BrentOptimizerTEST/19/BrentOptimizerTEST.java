import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;
 private UnivariateFunction function;

 @Before
 public void setUp() {
 function = new PolynomialFunction(2, new double[]{-1.0, 1.0, 2.0});
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 }

 @Test
 public void testEmptyConstructor() {
 try {
 new BrentOptimizer(null, null);
 fail("Expected NumberIsTooSmallException or NotStrictlyPositiveException");
 } catch (NumberIsTooSmallException | NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testMinimize() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariatePointValuePair result = optimizer.optimize(function);
 assertEquals(0.0, result.getPoint(), 1.0e-6);
 assertEquals(-2.0, result.getValue(), 1.0e-6);
 }

 @Test
 public void testMaximize() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new MaxValueChecker(1.0e-6));
 UnivariatePointValuePair result = optimizer.optimize(function);
 assertEquals(2.0, result.getPoint(), 1.0e-6);
 assertEquals(2.0, result.getValue(), 1.0e-6);
 }

 @Test
 public void testConvergenceChecker() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new SimpleCheckpointChecker(10));
 List<UnivariatePointValuePair> checkpoints = new ArrayList<>();
 UnivariatePointValuePair result;
 int iteration = 0;
 do {
 result = optimizer.optimize(function);
 checkpoints.add(result);
 iteration++;
 } while (!optimizer.isOptimized());
 assertEquals(10, checkpoints.size());
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testMinRelativeToleranceIsTooSmall() {
 optimizer = new BrentOptimizer(0.5, 1.0e-6);
 try {
 optimizer.optimize(function);
 fail("Expected NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbsoluteThresholdIsZero() {
 optimizer = new BrentOptimizer(1.0e-6, 0.0);
 try {
 optimizer.optimize(function);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }
}