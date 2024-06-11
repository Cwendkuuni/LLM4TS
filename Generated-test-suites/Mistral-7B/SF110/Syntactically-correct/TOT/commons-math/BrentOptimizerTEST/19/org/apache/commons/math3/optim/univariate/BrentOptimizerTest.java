package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerWithMinimum() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-3);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2.0 * Math.pow(x, 2) + x + 1.0;
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionFunction getObjectiveFunction() {
 return f;
 }
 @Override
 public double getLowerBound(int dimension) {
 return -10.0;
 }
 @Override
 public double getUpperBound(int dimension) {
 return 10.0;
 }
 };
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(Math.sqrt(2.0), solution.getPoint(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithMaximum() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-3);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2.0 * Math.pow(x, 2) + x - 1.0;
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionFunction getObjectiveFunction() {
 return f;
 }
 @Override
 public double getLowerBound(int dimension) {
 return -10.0;
 }
 @Override
 public double getUpperBound(int dimension) {
 return 10.0;
 }
 };
 optimizer.setGoalType(GoalType.MAXIMIZE);
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(-Math.sqrt(2.0), solution.getPoint(), 1.0e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithSmallRelativeTolerance() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-10, 1.0e-3);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionFunction getObjectiveFunction() {
 return f;
 }
 @Override
 public double getLowerBound(int dimension) {
 return 0.0;
 }
 @Override
 public double getUpperBound(int dimension) {
 return 10.0;
 }
 };
 optimizer.optimize(problem);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithNegativeAbsoluteTolerance() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-3);
 FunctionFunction f = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public FunctionFunction getObjectiveFunction() {
 return f;
 }
 @Override
 public double getLowerBound(int dimension) {
 return 0.0;
 }
 @Override
 public double getUpperBound(int dimension) {
 return 10.0;
 }
 };
 optimizer.optimize(problem);
 }
}