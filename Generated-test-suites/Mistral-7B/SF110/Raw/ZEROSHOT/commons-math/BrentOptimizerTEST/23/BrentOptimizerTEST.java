import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.initializer.ValueDomain;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueDomain;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class BrentOptimizerTest {

 private UnivariateFunction function;
 private BrentOptimizer optimizer;

 @Test
 public void testBrentOptimizerWithNullFunction() {
 assertThrows(IllegalArgumentException.class, () -> {
 optimizer = new BrentOptimizer(0.01, 1.0, null);
 });
 }

 @Test
 public void testBrentOptimizerWithZeroAbsoluteThreshold() {
 assertThrows(NotStrictlyPositiveException.class, () -> {
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 });
 }

 @Test
 public void testBrentOptimizerWithNegativeRelativeThreshold() {
 assertThrows(NumberIsTooSmallException.class, () -> {
 optimizer = new BrentOptimizer(-0.01, 0.0, null);
 });
 }

 @Test
 public void testBrentMinimization() {
 function = new LinearObjectiveFunction(1.0, -2.0);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 ValueDomain domain = new ValueDomain(0.0, 10.0);
 optimizer.optimize(function, domain);
 double expectedResult = 1.0;
 PointValuePair result = optimizer.getSolution();
 assertEquals("Unexpected result in minimization", expectedResult, result.getPoint(), 0.0001);
 assertEquals("Unexpected objective value in minimization", -2.0, result.getValue(), 0.0001);
 }

 @Test
 public void testBrentMinimizationWithConstraint() {
 function = new LinearObjectiveFunction(1.0, -2.0);
 LinearConstraint constraint = new LinearConstraint(2.0, 5.0, 1.0, 1.0);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 ValueDomain domain = new ValueDomain(0.0, 10.0);
 optimizer.addConstraint(constraint);
 optimizer.optimize(function, domain);
 double expectedResult = 2.0;
 PointValuePair result = optimizer.getSolution();
 assertEquals("Unexpected result in minimization", expectedResult, result.getPoint(), 0.0001);
 assertEquals("Unexpected objective value in minimization", -2.0, result.getValue(), 0.0001);
 }

 @Test
 public void testBrentMinimizationWithDifferentInitialValues() {
 function = new LinearObjectiveFunction(1.0, -2.0);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 ValueDomain domain = new ValueDomain(0.0, 10.0);
 optimizer.optimize(function, domain);
 double expectedResult1 = 1.0;
 PointValuePair result1 = optimizer.getSolution();
 assertEquals("Unexpected result in minimization", expectedResult1, result1.getPoint(), 0.0001);
 assertEquals("Unexpected objective value in minimization", -2.0, result1.getValue(), 0.0001);
 optimizer.setInitialPoint(5.0);
 optimizer.optimize(function, domain);
 double expectedResult2 = 2.0;
 PointValuePair result2 = optimizer.getSolution();
 assertEquals("Unexpected result in minimization", expectedResult2, result2.getPoint(), 0.0001);
 assertEquals("Unexpected objective value in minimization", -2.0, result2.getValue(), 0.0001);
 }

 @Test
 public void testBrentMinimizationWithDifferentTolerances() {
 function = new LinearObjectiveFunction(1.0, -2.0);
 optimizer = new BrentOptimizer(0.001, 0.0, null);
 ValueDomain domain = new ValueDomain(0.0, 10.0);
 optimizer.optimize(function, domain);
 double expectedResult1 = 1.0;
 PointValuePair result1 = optimizer.getSolution();
 assertEquals("Unexpected result in minimization", expectedResult1, result1.getPoint(), 0.00001);
 assertEquals("Unexpected objective value in minimization", -2.0, result1.getValue(), 0.00001);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 optimizer.optimize(function, domain);
 double expectedResult2 = 1.0;
 PointValuePair result2 = optimizer.getSolution();
 assertEquals("Unexpected result in minimization", expectedResult2, result2.getPoint(), 0.001);
 assertEquals("Unexpected objective value in minimization", -2.0, result2.getValue(), 0.001);
 }

 @Test
 public void testBrentMaximization() {
 function = new LinearObjectiveFunction(1.0, 2.0);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 ValueDomain domain = new ValueDomain(0.0, 10.0);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 optimizer.optimize(function, domain);
 double expectedResult = 1.0;
 PointValuePair result = optimizer.getSolution();
 assertEquals("Unexpected result in maximization", expectedResult, result.getPoint(), 0.0001);
 assertEquals("Unexpected objective value in maximization", 2.0, result.getValue(), 0.0001);
 }

 @Test
 public void testBrentMaximizationWithNegativeInitialValue() {
 function = new LinearObjectiveFunction(1.0, 2.0);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 ValueDomain domain = new ValueDomain(-10.0, 10.0);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 optimizer.optimize(function, domain);
 double expectedResult = 1.0;
 PointValuePair result = optimizer.getSolution();
 assertEquals("Unexpected result in maximization", expectedResult, result.getPoint(), 0.0001);
 assertEquals("Unexpected objective value in maximization", 2.0, result.getValue(), 0.0001);
 }

 @Test
 public void testBrentMaximizationWithDifferentTolerances() {
 function = new LinearObjectiveFunction(1.0, 2.0);
 optimizer = new BrentOptimizer(0.001, 0.0, null);
 ValueDomain domain = new ValueDomain(-10.0, 10.0);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 optimizer.optimize(function, domain);
 double expectedResult1 = 1.0;
 PointValuePair result1 = optimizer.getSolution();
 assertEquals("Unexpected result in maximization", expectedResult1, result1.getPoint(), 0.00001);
 assertEquals("Unexpected objective value in maximization", 2.0, result1.getValue(), 0.00001);
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 optimizer.optimize(function, domain);
 double expectedResult2 = 1.0;
 PointValuePair result2 = optimizer.getSolution();
 assertEquals("Unexpected result in maximization", expectedResult2, result2.getPoint(), 0.001);
 assertEquals("Unexpected objective value in maximization", 2.0, result2.getValue(), 0.001);
 }

}