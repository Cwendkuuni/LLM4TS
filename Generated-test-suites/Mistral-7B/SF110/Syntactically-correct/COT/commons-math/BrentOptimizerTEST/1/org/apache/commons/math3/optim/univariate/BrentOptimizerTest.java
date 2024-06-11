package org.apache.commons.math3.optim.univariate.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 }

 @Test
 public void testConstructorWithRelAndAbs() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithSmallRel() {
 optimizer = new BrentOptimizer(1.0e-10, 1.0e-6);
 fail("NumberIsTooSmallException should be thrown");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeAbs() {
 optimizer = new BrentOptimizer(1.0e-6, -1.0e-6);
 fail("NotStrictlyPositiveException should be thrown");
 }

 @Test
 public void testDoOptimize_withMinimizationGoal() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.PI, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getFunction() {
 return function;
 }
 @Override
 public double getMinimum() {
 return -2.12162449;
 }
 @Override
 public double getMaximum() {
 return 2.0;
 }
 };
 optimizer.setObjectiveFunction(problem.getFunction());
 optimizer.setInitialGuess(0.5);
 optimizer.setBounds(new GoalType[] { GoalType.MINIMIZE }, new double[] { -3.0, 3.0 });
 UnivariatePointValuePair result = optimizer.optimize();
 assertTrue(result.getValue() < 1e-6);
 assertEquals(-2.12162449, result.getPoint(), 1e-6);
 }

 @Test
 public void testDoOptimize_withMaximizationGoal() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.PI, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getFunction() {
 return function;
 }
 @Override
 public double getMinimum() {
 return -2.12162449;
 }
 @Override
 public double getMaximum() {
 return 2.0;
 }
 };
 optimizer.setObjectiveFunction(problem.getFunction());
 optimizer.setInitialGuess(0.5);
 optimizer.setBounds(new GoalType[] { GoalType.MAXIMIZE }, new double[] { -3.0, 3.0 });
 UnivariatePointValuePair result = optimizer.optimize();
 assertTrue(result.getValue() > 1e-6);
 assertEquals(-2.12162449, result.getPoint(), 1e-6); // The result should be negative because we're maximizing
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_withNullObjectiveFunction() {
 optimizer.setObjectiveFunction(null);
 optimizer.optimize();
 fail("IllegalArgumentException should be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_withInvalidGoalType() {
 optimizer.setObjectiveFunction(function);
 optimizer.setInitialGuess(0.5);
 optimizer.setBounds(new GoalType[] { GoalType.VALUE_TYPE, GoalType.MINIMIZE }, new double[] { -3.0, 3.0 });
 optimizer.optimize();
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testDoOptimize_withBoundsSetToTheSamePoint() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getFunction() {
 return function;
 }
 @Override
 public double getMinimum() {
 return Double.NEGATIVE_INFINITY;
 }
 @Override
 public double getMaximum() {
 return Double.POSITIVE_INFINITY;
 }
 };
 optimizer.setObjectiveFunction(problem.getFunction());
 optimizer.setInitialGuess(1.0);
 optimizer.setBounds(new GoalType[] { GoalType.MINIMIZE }, new double[] { 1.0, 1.0 });
 UnivariatePointValuePair result = optimizer.optimize();
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testDoOptimize_withInvalidBounds() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getFunction() {
 return function;
 }
 @Override
 public double getMinimum() {
 return Double.NEGATIVE_INFINITY;
 }
 @Override
 public double getMaximum() {
 return Double.POSITIVE_INFINITY;
 }
 };
 optimizer.setObjectiveFunction(problem.getFunction());
 optimizer.setInitialGuess(1.0);
 optimizer.setBounds(new GoalType[] { GoalType.MINIMIZE }, new double[] { Double.NaN, 1.0 });
 UnivariatePointValuePair result = optimizer.optimize();
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testDoOptimize_withConvergenceChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getFunction() {
 return function;
 }
 @Override
 public double getMinimum() {
 return Double.NEGATIVE_INFINITY;
 }
 @Override
 public double getMaximum() {
 return Double.POSITIVE_INFINITY;
 }
 };
 optimizer.setObjectiveFunction(problem.getFunction());
 optimizer.setInitialGuess(1.0);
 optimizer.setBounds(new GoalType[] { GoalType.MINIMIZE }, new double[] { 0.0, 1.0 });
 ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() == previous.getValue();
 }
 };
 optimizer.setConvergenceChecker(checker);
 UnivariatePointValuePair result = optimizer.optimize();
 assertEquals(1, optimizer.getIterations());
 assertEquals(1.0, result.getPoint(), 1e-6);
 }

 @Test
 public void testDoOptimize_withInvalidConvergenceChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getFunction() {
 return function;
 }
 @Override
 public double getMinimum() {
 return Double.NEGATIVE_INFINITY;
 }
 @Override
 public double getMaximum() {
 return Double.POSITIVE_INFINITY;
 }
 };
 optimizer.setObjectiveFunction(problem.getFunction());
 optimizer.setInitialGuess(1.0);
 optimizer.setBounds(new GoalType[] { GoalType.MINIMIZE }, new double[] { 0.0, 1.0 });
 ConvergenceChecker<String> checker = new ConvergenceChecker<String>() {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.toString().equals(previous.toString());
 }
 };
 optimizer.setConvergenceChecker(checker);
 optimizer.optimize();
 fail("IllegalArgumentException should be thrown");
 }
}