package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolve() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double expectedResult = 1.5;

 double result = UnivariateRealSolverUtils.solve(function, x0, x1);
 assertEquals("Solve: incorrect result", expectedResult, result, 0.001);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithInvalidFunction() throws Exception {
 UnivariateRealFunction function = null;
 double x0 = 1.0;
 double x1 = 2.0;
 UnivariateRealSolverUtils.solve(function, x0, x1);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveConvergenceException() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI / x);
 }
 };
 double x0 = 0.1;
 double x1 = 1.0;
 UnivariateRealSolverUtils.solve(function, x0, x1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithInvalidInterval() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 3.0;
 double x1 = 2.0;
 UnivariateRealSolverUtils.solve(function, x0, x1);
 }

 @Test
 public void testSolveWithAbsoluteAccuracy() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = 0.001;
 double expectedResult = 1.5;

 double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
 assertEquals("Solve: incorrect result", expectedResult, result, 0.001);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithInvalidFunctionForAbsoluteAccuracy() throws Exception {
 UnivariateRealFunction function = null;
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = 0.001;
 UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveWithConvergenceExceptionForAbsoluteAccuracy() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI / x);
 }
 };
 double x0 = 0.1;
 double x1 = 1.0;
 double absoluteAccuracy = 0.001;
 UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithInvalidIntervalForAbsoluteAccuracy() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 2.0;
 double x1 = 1.0;
 double absoluteAccuracy = 0.001;
 UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
 }

 @Test
 public void testBracket() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 0.0;
 double upperBound = 3.0;
 double[] expectedResult = {1.0, 2.0};
 double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

 assertEquals("Bracket: incorrect result", expectedResult[0], result[0], 0.001);
 assertEquals("Bracket: incorrect result", expectedResult[1], result[1], 0.001);
 }

 @Test
 public void testBracketWithMaximumIterations() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 0.0;
 double upperBound = 3.0;
 int maximumIterations = 5;
 double[] expectedResult = {1.0, 2.0};
 double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);

 assertEquals("Bracket: incorrect result", expectedResult[0], result[0], 0.001);
 assertEquals("Bracket: incorrect result", expectedResult[1], result[1], 0.001);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBracketWithInvalidFunction() throws Exception {
 UnivariateRealFunction function = null;
 double initial = 1.5;
 double lowerBound = 0.0;
 double upperBound = 3.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithInvalidInitial() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 3.0;
 double lowerBound = 0.0;
 double upperBound = 3.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithInvalidLowerBound() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 3.0;
 double upperBound = 3.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithInvalidUpperBound() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 0.0;
 double upperBound = 0.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketWithConvergenceException() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI / x);
 }
 };
 double initial = 0.1;
 double lowerBound = 0.0;
 double upperBound = 1.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }
}