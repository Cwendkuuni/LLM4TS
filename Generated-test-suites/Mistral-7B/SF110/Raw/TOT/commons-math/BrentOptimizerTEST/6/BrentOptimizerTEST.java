package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any static variables or initialize any static objects here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Clean up any static resources here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or initialize any objects here
 }

 @After
 public void tearDown() {
 // Clean up any instance resources here
 }

 @Test
 public void testBrentOptimizerWithMinimizationGoal() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionType getFunctionType() {
 return FunctionType.UNIVARIATE;
 }

 @Override
 public FunctionFunction getFunction() {
 return f;
 }

 @Override
 public double getLowerBound() {
 return -10.0;
 }

 @Override
 public double getUpperBound() {
 return 10.0;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(2.0, result.getX(), 1e-6);
 }

 @Test
 public void testBrentOptimizerWithMaximizationGoal() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionType getFunctionType() {
 return FunctionType.UNIVARIATE;
 }

 @Override
 public FunctionFunction getFunction() {
 return new FunctionFunction() {
 @Override
 public double value(double x) {
 return -f.value(x);
 }
 };
 }

 @Override
 public double getLowerBound() {
 return -10.0;
 }

 @Override
 public double getUpperBound() {
 return 10.0;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(-2.0, result.getX(), 1e-6);
 }

 @Test(expected = GoalTypeNotFoundException.class)
 public void testBrentOptimizerWithInvalidGoalType() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionType getFunctionType() {
 return FunctionType.MULTIVARIATE;
 }

 @Override
 public FunctionFunction getFunction() {
 return null;
 }

 @Override
 public double getLowerBound() {
 return Double.NaN;
 }

 @Override
 public double getUpperBound() {
 return Double.NaN;
 }

 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }
 };
 optimizer.optimize(problem);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerWithNegativeRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerWithZeroOrNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 0.0, null);
 BrentOptimizer optimizer2 = new BrentOptimizer(1.0e-6, -1.0e-6, null);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithTooSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-6, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
 }

 @Test(expected = ConvergenceException.class)
 public void testBrentOptimizerWithNonConvergentFunction() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI * x) / x;
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionType getFunctionType() {
 return FunctionType.UNIVARIATE;
 }

 @Override
 public FunctionFunction getFunction() {
 return f;
 }

 @Override
 public double getLowerBound() {
 return -10.0;
 }

 @Override
 public double getUpperBound() {
 return 10.0;
 }
 };
 optimizer.optimize(problem);
 }

 @Test
 public void testBrentOptimizerWithConvergentFunction() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionType getFunctionType() {
 return FunctionType.UNIVARIATE;
 }

 @Override
 public FunctionFunction getFunction() {
 return f;
 }

 @Override
 public double getLowerBound() {
 return -10.0;
 }

 @Override
 public double getUpperBound() {
 return 10.0;
 }
 };
 ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iterations, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 return Math.abs(previous.getValue() - current.getValue()) < 1e-12;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(problem, checker);
 assertEquals(2.0, result.getX(), 1e-6);
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Clean up any static resources here
 }
}