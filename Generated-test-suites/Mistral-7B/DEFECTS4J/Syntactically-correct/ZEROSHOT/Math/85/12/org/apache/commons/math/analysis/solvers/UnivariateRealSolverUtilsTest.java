import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolve() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double expected = -1.0;
 double result = UnivariateRealSolverUtils.solve(f, x0, x1);
 assertEquals(expected, result, 0.001);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveFunctionEvaluationException() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("test");
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 UnivariateRealSolverUtils.solve(f, x0, x1);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveConvergenceException() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(1 / x);
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 UnivariateRealSolverUtils.solve(f, x0, x1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveIllegalArgumentException() throws Exception {
 UnivariateRealFunction f = null;
 double x0 = 1.0;
 double x1 = 2.0;
 try {
 UnivariateRealSolverUtils.solve(f, x0, x1);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("function is null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveIllegalArgumentExceptionEndpoints() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 2.0;
 double x1 = 1.0;
 try {
 UnivariateRealSolverUtils.solve(f, x0, x1);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("endpoints do not specify a valid interval", e.getMessage());
 }
 }

 @Test
 public void testSolveWithAbsoluteAccuracy() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = 0.001;
 double expected = -1.0;
 double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 assertEquals(expected, result, absoluteAccuracy);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithFunctionEvaluationException() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("test");
 }
 };
 UnivariateRealSolverUtils.solve(f, 1.0, 2.0, 0.001);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveWithConvergenceException() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(1 / x);
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = 0.001;
 try {
 UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 } catch (ConvergenceException e) {
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithIllegalArgumentException() throws Exception {
 UnivariateRealFunction f = null;
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = 0.001;
 try {
 UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("function is null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithIllegalArgumentExceptionEndpoints() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 2.0;
 double x1 = 1.0;
 double absoluteAccuracy = 0.001;
 try {
 UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("endpoints do not specify a valid interval", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithNegativeAbsoluteAccuracy() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = -0.001;
 try {
 UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("absoluteAccuracy is not valid for the default solver", e.getMessage());
 }
 }

 @Test
 public void testBracket() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 1.0;
 double upperBound = 2.0;
 double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 double[] expected = {1.0, 2.0};
 assertEquals(expected[0], result[0], 0.001);
 assertEquals(expected[1], result[1], 0.001);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketConvergenceException() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(1 / x);
 }
 };
 double initial = 1.0;
 double lowerBound = 0.0;
 double upperBound = 2.0;
 UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBracketFunctionEvaluationException() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("test");
 }
 };
 double initial = 1.0;
 double lowerBound = 1.0;
 double upperBound = 2.0;
 try {
 UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketIllegalArgumentException() throws Exception {
 UnivariateRealFunction f = null;
 double initial = 1.0;
 double lowerBound = 1.0;
 double upperBound = 2.0;
 try {
 UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("function is null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketIllegalArgumentExceptionEndpoints() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 2.0;
 double upperBound = 1.0;
 try {
 UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("endpoints do not specify a valid interval", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketIllegalArgumentExceptionMaxIterations() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = 1.5;
 double lowerBound = 1.0;
 double upperBound = 2.0;
 int maximumIterations = -1;
 try {
 UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("maximum iterations is not positive", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketIllegalArgumentExceptionInitial() throws Exception {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 double initial = Double.NaN;
 double lowerBound = 1.0;
 double upperBound = 2.0;
 try {
 UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 } catch (ConvergenceException e) {
 } catch (FunctionEvaluationException e) {
 } catch (IllegalArgumentException e) {
 assertEquals("initial is not between lowerBound and upperBound", e.getMessage());
 }
 }
}