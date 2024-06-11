import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BisectionSolverTest {

 @Test
 public void testBisectionSolverWithFunction() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };

 try {
 double root = new BisectionSolver().solve(function, -2.0, 2.0);
 assertEquals(0.0, root, 1e-6);
 } catch (MaxIterationsExceededException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBisectionSolverWithNullFunction() {
 UnivariateRealFunction function = null;
 assertThrows(IllegalArgumentException.class, () -> new BisectionSolver(function));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBisectionSolverWithInvalidMin() {
 UnivariateRealFunction function = x -> Math.pow(x, 2) - 1;
 assertThrows(IllegalArgumentException.class, () -> new BisectionSolver(function, Double.NEGATIVE_INFINITY, 0.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBisectionSolverWithInvalidMax() {
 UnivariateRealFunction function = x -> Math.pow(x, 2) - 1;
 assertThrows(IllegalArgumentException.class, () -> new BisectionSolver(function, 0.0, Double.POSITIVE_INFINITY));
 }

 @Test
 public void testBisectionSolverWithFunctionAndInitialGuess() {
 UnivariateRealFunction function = x -> Math.pow(x, 2) - 1;

 try {
 double root = new BisectionSolver().solve(function, -1.5, 0.5, -0.5);
 assertEquals(0.0, root, 1e-6);
 } catch (MaxIterationsExceededException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBisectionSolverWithFunctionThrowsException() {
 UnivariateRealFunction function = x -> {
 throw new NumberIsTooSmallException();
 };

 assertThrows(FunctionEvaluationException.class, () -> new BisectionSolver().solve(function, 0.0, 1.0));
 }
}