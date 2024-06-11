import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.BentFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.ConvergenceChecker;

public class BrentSolverTest {

 private double epsilon = 0.000001;
 private double tolerance = 0.001;

 @Test
 public void testFindRoot() throws Exception {
 // Test case for the function f(x) = x^3 - 2
 BentFunction function = new BentFunction("x, x*x*x - 2.0");
 BrentSolver solver = new BrentSolver();

 // Test with an interval known to contain a root
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xLow, double fLow) {
 return (Math.abs(f) < tolerance) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
 }
 };

 double x0 = 1.0;
 double f0 = 0.0;
 double x1 = 3.0;
 double f1 = 12.0;

 solver.findRoot(function, checker, 100, x0, f0, x1, f1);

 Assert.assertFalse("findRoot should return false because maximal number of iterations is reached", solver.isRootFound());
 Assert.assertEquals("The root should be approximately 1.7320508075688772935", 1.7320508075688772935, solver.getRoot(), epsilon);

 // Test with an interval known to not contain a root
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xLow, double fLow) {
 return (Math.abs(f) > Math.abs(epsilon)) ? ConvergenceChecker.NOT_CONVERGED : ConvergenceChecker.LOW;
 }
 };

 x0 = 1.0;
 f0 = 0.0;
 x1 = 2.0;
 f1 = 0.0;

 solver.findRoot(function, checker, 100, x0, f0, x1, f1);

 Assert.assertFalse("findRoot should return false because no root is found in the given interval", solver.isRootFound());
 }

 @Test
 public void testFindRootWithNaN() throws Exception {
 // Test case for the function f(x) = 1/x
 BentFunction function = new BentFunction("x, 1.0 / x");
 BrentSolver solver = new BrentSolver();

 // Test with an interval known to not contain a root
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xLow, double fLow) {
 return (Double.isNaN(x) || Double.isNaN(f)) ? ConvergenceChecker.NOT_CONVERGED : ConvergenceChecker.LOW;
 }
 };

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = 0.0;
 double f1 = Double.NaN;

 solver.findRoot(function, checker, 100, x0, f0, x1, f1);

 Assert.assertFalse("findRoot should return false because no root is found in the given interval", solver.isRootFound());
 }

 @Test
 public void testFindRootWithEmptyInterval() throws Exception {
 // Test case for the function f(x) = x^3 - 2
 BentFunction function = new BentFunction("x, x*x*x - 2.0");
 BrentSolver solver = new BrentSolver();

 // Test with an empty interval
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xLow, double fLow) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };

 double x0 = 2.0;
 double f0 = 4.0;
 double x1 = 2.0;
 double f1 = 4.0;

 solver.findRoot(function, checker, 100, x0, f0, x1, f1);

 Assert.assertFalse("findRoot should return false because the interval is empty", solver.isRootFound());
 }

 @Test
 public void testFindRootWithDegeneratedInterval() throws Exception {
 // Test case for the function f(x) = x^3 - 2
 BentFunction function = new BentFunction("x, x*x*x - 2.0");
 BrentSolver solver = new BrentSolver();

 // Test with a degenerated interval
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xLow, double fLow) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };

 double x0 = 2.0;
 double f0 = 4.0;
 double x1 = 2.0;
 double f1 = 4.0;

 solver.findRoot(function, checker, 100, x0, f0, x1, f1);

 Assert.assertFalse("findRoot should return false because the interval is degenerated", solver.isRootFound());
 }

 @Test(expected = FunctionException.class)
 public void testFindRootWithException() throws Exception {
 // Test case for the function f(x) = 1/x
 BentFunction function = new BentFunction("x, 1.0 / x");
 BrentSolver solver = new BrentSolver();

 // Test with an interval known to not contain a root
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xLow, double fLow) {
 return ConvergenceChecker.LOW;
 }
 };

 double x0 = 0.0;
 double f0 = Double.POSITIVE_INFINITY;
 double x1 = 1.0;
 double f1 = 0.0;

 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (FunctionException e) {
 // expected
 }

 }

}