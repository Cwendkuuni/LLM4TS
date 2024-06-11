import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 private ComputableFunction function;
 private ConvergenceChecker checker;
 private BrentSolver solver;

 @Test
 public void testBrentSolver() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.LOW;
 }
 };

 function = new RationalFunction("x^3 - 2*x^2 + x - 1");

 double root = 1.0; // known root
 try {
 solver.findRoot(function, checker, 100, 1.0, 2.0, 2.0, 3.0);
 Assert.assertEquals(root, solver.getRoot(), 0.001);
 } catch (FunctionException e) {
 e.printStackTrace();
 Assert.fail("Exception was thrown: " + e.getMessage());
 }
 }

 @Test(expected = FunctionException.class)
 public void testFunctionException() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.LOW;
 }
 };

 function = new RationalFunction("x^3 - 2*x^2 + 3*x - 1"); // function with no root
 try {
 solver.findRoot(function, checker, 100, 1.0, 2.0, 2.0, 3.0);
 Assert.fail("No FunctionException was thrown");
 } catch (FunctionException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 solver = new BrentSolver();
 checker = null;
 function = new RationalFunction("x^2 - 4");

 try {
 solver.findRoot(function, checker, 100, 1.0, 2.0, 2.0, 3.0);
 Assert.fail("No IllegalArgumentException was thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException2() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {}; // empty checker
 function = new RationalFunction("x^2 - 4");

 try {
 solver.findRoot(function, checker, 100, 1.0, 2.0, 2.0, 3.0);
 Assert.fail("No IllegalArgumentException was thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}