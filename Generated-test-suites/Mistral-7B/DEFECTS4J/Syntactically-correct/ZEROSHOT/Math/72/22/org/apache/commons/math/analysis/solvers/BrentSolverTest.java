import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {
    // Test with a simple function
    ComputableFunction function = new RationalFunction("1.0", "x", "2.0");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 2.0;
    double fx1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    assertEquals(true, result);
    assertEquals(1.4142135625, solver.getRoot(), 0.00001);

    // Test with a function having multiple roots
    function = new RationalFunction("x^3 - 4.0", "x");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONVERGED_TO_ONE_ROOT;
      }
    };
    solver = new BrentSolver();
    x0 = 1.0;
    fx0 = function.valueAt(x0);
    x1 = 3.0;
    fx1 = function.valueAt(x1);
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    assertEquals(true, result);
    assertEquals(1.5, solver.getRoot(), 0.001);

    // Test with a function having no roots in the given interval
    function = new RationalFunction("x^2 + 1.0", "x");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.DIVERGED;
      }
    };
    solver = new BrentSolver();
    x0 = -2.0;
    fx0 = function.valueAt(x0);
    x1 = 2.0;
    fx1 = function.valueAt(x1);
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    assertEquals(false, result);

    // Test with a function having a root at a boundary of the interval
    function = new RationalFunction("x^3 - 2.0", "x");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };
    solver = new BrentSolver();
    x0 = -1.0;
    fx0 = function.valueAt(x0);
    x1 = 1.0;
    fx1 = function.valueAt(x1);
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    assertEquals(true, result);
    assertEquals(-0.5, solver.getRoot(), 0.001);

    // Test with a function having a root at the midpoint of the interval
    function = new RationalFunction("x^3 - 2.0", "x");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.HIGH;
      }
    };
    solver = new BrentSolver();
    x0 = -1.0;
    fx0 = function.valueAt(x0);
    x1 = 1.0;
    fx1 = function.valueAt(x1);
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    assertEquals(true, result);
    assertEquals(0.5, solver.getRoot(), 0.001);

  }

}