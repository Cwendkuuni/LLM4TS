import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolve() {
 UnivariateRealFunction function = new SimpleFunction();
 double x0 = 0.0;
 double x1 = 1.0;
 double expectedResult = 0.5;

 try {
 double result = UnivariateRealSolverUtils.solve(function, x0, x1);
 assertEquals("Solve function test with default solver and valid input", expectedResult, result, 0.001);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 fail("Solve function test with default solver and valid input failed: " + e.getMessage());
 }
 }

 @Test
 public void testSolveWithAbsoluteAccuracy() {
 UnivariateRealFunction function = new SimpleFunction();
 double x0 = 0.0;
 double x1 = 1.0;
 double absoluteAccuracy = 0.001;
 double expectedResult = 0.5;

 try {
 double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
 assertEquals("Solve function test with default solver, absolute accuracy and valid input", expectedResult, result, 0.0001);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 fail("Solve function test with default solver, absolute accuracy and valid input failed: " + e.getMessage());
 }
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveWithInvalidInterval() {
 UnivariateRealFunction function = new SimpleFunction();
 double x0 = 1.0;
 double x1 = 0.0;

 try {
 UnivariateRealSolverUtils.solve(function, x0, x1);
 fail("Solve function test with invalid interval should have thrown ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected exception
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithNullFunction() {
 UnivariateRealFunction function = null;
 double x0 = 0.0;
 double x1 = 1.0;

 try {
 UnivariateRealSolverUtils.solve(function, x0, x1);
 fail("Solve function test with null function should have thrown FunctionEvaluationException");
 } catch (FunctionEvaluationException e) {
 // Expected exception
 }
 }

 @Test
 public void testBracket() {
 UnivariateRealFunction function = new SimpleFunction();
 double initial = 1.0;
 double lowerBound = 0.0;
 double upperBound = 2.0;
 double[] expectedResult = new double[]{1.0, 2.0};

 try {
 double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 assertArrayEquals("Bracket function test with valid input", expectedResult, result, 0.001);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 fail("Bracket function test with valid input failed: " + e.getMessage());
 }
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketWithInvalidInterval() {
 UnivariateRealFunction function = new SimpleFunction();
 double initial = 1.0;
 double lowerBound = 2.0;
 double upperBound = 0.0;

 try {
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 fail("Bracket function test with invalid interval should have thrown ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected exception
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBracketWithNullFunction() {
 UnivariateRealFunction function = null;
 double initial = 1.0;
 double lowerBound = 0.0;
 double upperBound = 2.0;

 try {
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 fail("Bracket function test with null function should have thrown FunctionEvaluationException");
 } catch (FunctionEvaluationException e) {
 // Expected exception
 }
 }
}

class SimpleFunction implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return Math.sin(x) - x;
 }
}