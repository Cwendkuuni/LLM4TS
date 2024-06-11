import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

 @Test
 public void testBisectionSolverWithValidInput() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BisectionSolver solver = new BisectionSolver();
 double root = solver.solve(function, -2.0, 2.0);
 assertEquals(0.0, root, 1e-6);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testBisectionSolverWithMaxIterationsExceeded() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 BisectionSolver solver = new BisectionSolver();
 solver.setMaxIterations(1);
 try {
 solver.solve(function, -1.0, 1.0);
 } catch (FunctionEvaluationException e) {
 // The exception is expected if the function is not defined in the interval
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBisectionSolverWithFunctionEvaluationException() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new ArithmeticException("Test ArithmeticException");
 }
 };
 BisectionSolver solver = new BisectionSolver();
 try {
 solver.solve(function, -2.0, 2.0);
 } catch (MaxIterationsExceededException e) {
 // The exception is not expected
 }
 }

 @Test
 public void testBisectionSolverWithDifferentInitialValues() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
 };
 BisectionSolver solver = new BisectionSolver();
 double root1 = solver.solve(function, -2.0, 2.0);
 double root2 = solver.solve(function, -3.0, 2.0);
 assertEquals(0.0, root1, 1e-6);
 assertEquals(0.0, root2, 1e-6);
 }
}