import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

 @Test
 public void testConstructor() {
 BrentSolver solver = new BrentSolver();
 assertDoubleNaN(solver.getRoot());
 }

 @Test
 public void testFindRoot_simple() throws Exception {
 ComputableFunction func = FunctionFactory.createPolynomialFunction(x -> x * x - 2);
 BrentSolver solver = new BrentSolver();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 if (Math.abs(fx) < 1e-10) {
 return ConvergenceChecker.CONVERGED;
 }
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };
 double x0 = 1.0;
 double f0 = x0 * x0 - 2;
 double x1 = 2.0;
 double f1 = x1 * x1 - 2;
 boolean result = solver.findRoot(func, checker, 10, x0, f0, x1, f1);
 assertTrue(result);
 double expected = -1.0;
 double actual = solver.getRoot();
 assertEquals(expected, actual, 1e-8);
 }

 @Test(expected = FunctionException.class)
 public void testFindRoot_functionException() throws Exception {
 ComputableFunction func = () -> { throw new FunctionException("Test"); };
 BrentSolver solver = new BrentSolver();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };
 double x0 = 0.0;
 double f0 = Double.NaN;
 double x1 = 1.0;
 double f1 = 0.0;
 solver.findRoot(func, checker, 10, x0, f0, x1, f1);
 }

 @Test
 public void testFindRoot_diverge() throws Exception {
 ComputableFunction func = x -> Math.log(Math.abs(x - 1));
 BrentSolver solver = new BrentSolver();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 if (Math.abs(fx) < 1e-10) {
 return ConvergenceChecker.CONVERGED;
 }
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };
 double x0 = 0.5;
 double f0 = Math.log(Math.abs(x0 - 1));
 double x1 = 2.0;
 double f1 = Math.log(Math.abs(x1 - 1));
 boolean result = solver.findRoot(func, checker, 10, x0, f0, x1, f1);
 assertFalse(result);
 }

 @Test
 public void testFindRoot_intervalEmpty() throws Exception {
 ComputableFunction func = x -> x * x - 2;
 BrentSolver solver = new BrentSolver();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 if (Math.abs(fx) < 1e-10) {
 return ConvergenceChecker.CONVERGED;
 }
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };
 double x0 = 2.0;
 double f0 = x0 * x0 - 2;
 double x1 = x0;
 double f1 = f0;
 boolean result = solver.findRoot(func, checker, 10, x0, f0, x1, f1);
 assertFalse(result);
 }

}