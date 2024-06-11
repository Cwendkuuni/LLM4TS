import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

 @Test
 public void testConstructors() {
 BisectionSolver solver = new BisectionSolver();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFunction() {
 BisectionSolver solver = new BisectionSolver(null);
 }

 @Test
 public void testSolveSimpleFunction() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 4.0;
 }
 };
 BisectionSolver solver = new BisectionSolver();
 double root = solver.solve(function, -2.0, 2.0);
 Assert.assertNotNull(root);
 Assert.assertEquals(0.0, root, 1.0e-6);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveFunctionWithNoRootInInterval() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 1.0;
 }
 };
 BisectionSolver solver = new BisectionSolver();
 solver.solve(function, -1.0, 1.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveFunctionWithMaxIterations() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 BisectionSolver solver = new BisectionSolver(10);
 solver.solve(function, 0.0, Math.PI);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSolveFunctionWithSmallAccuracy() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.exp(-x) - 1.0 / (1.0 + x);
 }
 };
 BisectionSolver solver = new BisectionSolver(100000, 1.0e-12);
 solver.solve(function, 1.0, 2.0);
 }
}