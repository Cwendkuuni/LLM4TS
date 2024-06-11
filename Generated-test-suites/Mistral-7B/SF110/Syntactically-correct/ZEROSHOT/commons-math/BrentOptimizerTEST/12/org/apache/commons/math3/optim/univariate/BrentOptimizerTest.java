import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueHolder;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
 }
 };

 @Test
 public void testBrentOptimizer() {
 // Test with default constructor and default ConvergenceChecker
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 try {
 PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(Math.PI, result.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 // Test with custom ConvergenceChecker
 BrentOptimizer optimizer2 = new BrentOptimizer(0.001, 0.01, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() - previous.getValue() < 0.0001;
 }
 });
 try {
 PointValuePair result2 = optimizer2.optimize(function, -5.0, 5.0);
 assertEquals(Math.PI, result2.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 // Test with invalid input
 try {
 BrentOptimizer optimizer3 = new BrentOptimizer(-0.001, 0.01);
 fail("Should throw NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 BrentOptimizer optimizer4 = new BrentOptimizer(0.001, -0.01);
 fail("Should throw NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 BrentOptimizer optimizer5 = new BrentOptimizer(0.0, 0.01);
 fail("Should throw NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
}

 @Test
 public void testBrentOptimizerMinimize() {
 // Test minimizing a function
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 try {
 PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
 }
 }, -5.0, 5.0, GoalType.MINIMIZE);
 assertEquals(Math.PI, result.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 // Test minimizing a function with custom ConvergenceChecker
 BrentOptimizer optimizer2 = new BrentOptimizer(0.001, 0.01, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() - previous.getValue() < 0.0001;
 }
 });
 try {
 PointValuePair result2 = optimizer2.optimize(new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
 }
 }, -5.0, 5.0, GoalType.MINIMIZE);
 assertEquals(Math.PI, result2.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
}

 @Test
 public void testBrentOptimizerMaximize() {
 // Test maximizing a function
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 try {
 PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return -(Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2));
 }
 }, -5.0, 5.0, GoalType.MAXIMIZE);
 assertEquals(-Math.PI, result.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 // Test maximizing a function with custom ConvergenceChecker
 BrentOptimizer optimizer2 = new BrentOptimizer(0.001, 0.01, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() - previous.getValue() < 0.0001;
 }
 });
 try {
 PointValuePair result2 = optimizer2.optimize(new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return -(Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2));
 }
 }, -5.0, 5.0, GoalType.MAXIMIZE);
 assertEquals(-Math.PI, result2.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
}

 @Test
 public void testBrentOptimizerWithInitialPoint() {
 // Test minimizing a function with initial point
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 double initialPoint = 1.5;
 try {
 PointValuePair result = optimizer.optimize(function, initialPoint, 5.0, GoalType.MINIMIZE);
 assertEquals(Math.PI, result.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 // Test maximizing a function with initial point
 try {
 PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return -(Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2));
 }
 }, initialPoint, 5.0, GoalType.MAXIMIZE);
 assertEquals(-Math.PI, result.getPoint(), 0.001);
 } catch (NotStrictlyPositiveException | NumberIsTooSmallException | MaxCountExceededException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
}

 @Test
 public void testBrentOptimizerException() {
 // Test minimizing a function with invalid input
 try {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 PointValuePair result = optimizer.optimize(null, -5.0, 5.0, GoalType.MINIMIZE);
 fail("Exception should be thrown");
 } catch (NullPointerException e) {
 // Expected
 }

 // Test maximizing a function with invalid input
 try {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 PointValuePair result = optimizer.optimize(function, null, 5.0, GoalType.MAXIMIZE);
 fail("Exception should be thrown");
 } catch (NullPointerException e) {
 // Expected
 }

 // Test minimizing a function with invalid lower bound
 try {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 PointValuePair result = optimizer.optimize(function, -Double.MAX_VALUE, 5.0, GoalType.MINIMIZE);
 fail("Exception should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test maximizing a function with invalid upper bound
 try {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 PointValuePair result = optimizer.optimize(function, 5.0, Double.MAX_VALUE, GoalType.MAXIMIZE);
 fail("Exception should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 class ConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() - previous.getValue() < 0.0001;
 }
 }
}