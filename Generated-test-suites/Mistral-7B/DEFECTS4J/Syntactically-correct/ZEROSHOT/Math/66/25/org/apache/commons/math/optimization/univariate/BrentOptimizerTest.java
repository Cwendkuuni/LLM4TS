import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double startValue = 1.8;
 PointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

 assertEquals("Incorrect minimum", 2.0, solution.getPoint(), 1E-8);
 assertEquals("Incorrect minimum value", 0.0, solution.getValue(), 1E-8);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerNegativeRelativeAccuracy() {
 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(-1E-12);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerZeroAbsoluteAccuracy() {
 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(0.0);
 optimizer.setRelativeAccuracy(1E-12);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testBrentOptimizerMaxIterationsExceeded() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(2); // Too few evaluations
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double startValue = 1.8;
 try {
 PointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // Expected exception
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBrentOptimizerFunctionEvaluationException() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) throws Exception {
 throw new Exception("Function evaluation exception");
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double startValue = 1.8;
 try {
 PointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
 fail("Expected FunctionEvaluationException");
 } catch (FunctionEvaluationException e) {
 // Expected exception
 }
 }

 @Test
 public void testBrentOptimizerGoalTypeConstraint() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double startValue = 1.8;
 GoalTypeConstraint goalType = new GoalTypeConstraint(GoalType.MINIMIZE, 1.0);
 PointValuePair solution = optimizer.optimize(function, goalType, min, max, startValue);

 assertEquals("Incorrect minimum", 2.0, solution.getPoint(), 1E-8);
 assertEquals("Incorrect minimum value", 0.0, solution.getValue(), 1E-8);
 }

 @Test
 public void testBrentOptimizerGoalTypeValue() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double startValue = 1.8;
 double expectedValue = 0.25;
 PointValuePair solution = optimizer.optimize(function, GoalTypeValue.ZERO, min, max, startValue);

 assertEquals("Incorrect minimum value", expectedValue, solution.getValue(), 1E-8);
 }

 @Test
 public void testBrentOptimizerAroundMinimumInitializer() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double initialPoint = 1.8;
 AroundMinimumInitializer initializer = new AroundMinimumInitializer(initialPoint, 1E-3);
 optimizer.setInitializer(initializer);
 PointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE, min, max, null);

 assertEquals("Incorrect minimum", 2.0, solution.getPoint(), 1E-8);
 assertEquals("Incorrect minimum value", 0.0, solution.getValue(), 1E-8);
 }

 @Test
 public void testBrentOptimizerValueInitializer() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double initialValue = 2.5;
 ValueInitializer initializer = new ValueInitializer(initialValue);
 optimizer.setInitializer(initializer);
 PointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE, min, max, null);

 assertEquals("Incorrect minimum", 2.0, solution.getPoint(), 1E-8);
 assertEquals("Incorrect minimum value", 0.0, solution.getValue(), 1E-8);
 }

 @Test
 public void testBrentOptimizerMultipleCalls() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxEvaluations(1000);
 optimizer.setMaximalIterationCount(100);
 optimizer.setAbsoluteAccuracy(1E-8);
 optimizer.setRelativeAccuracy(1E-12);

 double min = 1.5;
 double max = 2.5;
 double startValue = 1.8;

 PointValuePair solution1 = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
 PointValuePair solution2 = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
 PointValuePairList solutions = new PointValuePairList();
 solutions.add(solution1);
 solutions.add(solution2);

 assertEquals("Incorrect number of solutions", 2, solutions.size());
 }
}