import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^2 - 4");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return (fx > 0.0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double[] expectedRoots = {2.0, -2.0};
    for (double root : expectedRoots) {
      double[] interval = {root - 0.1, root + 0.1};
      solver.findRoot(function, checker, 100, interval[0], function.valueAt(interval[0]), interval[1], function.valueAt(interval[1]));
      assertEquals(root, solver.getRoot(), 1e-8);
      solver.reset(); // Reset root for next test
    }
  }

  @Test
  public void testFindRoot_ComplexFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("(x - 1.0) * (x - 3.0) * (x + 2.0)");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double[] expectedRoots = {1.0, 3.0};
    for (double root : expectedRoots) {
      double[] interval = {root - 0.3, root + 0.3};
      solver.findRoot(function, checker, 100, interval[0], function.valueAt(interval[0]), interval[1], function.valueAt(interval[1]));
      assertEquals(root, solver.getRoot(), 1e-8);
      solver.reset(); // Reset root for next test
    }
  }

  @Test
  public void testFindRoot_FunctionWithNoRoots() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^2 + 1");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double intervalStart = 0.0;
    double intervalEnd = 10.0;
    solver.findRoot(function, checker, 100, intervalStart, function.valueAt(intervalStart), intervalEnd, function.valueAt(intervalEnd));
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testFindRoot_FunctionWithInflectionPoint() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^3 - 2 * x^2");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double intervalStart = -1.5;
    double intervalEnd = 1.5;
    solver.findRoot(function, checker, 100, intervalStart, function.valueAt(intervalStart), intervalEnd, function.valueAt(intervalEnd));
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testFindRoot_MaxIterations() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^2 - 4");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double intervalStart = 1.0;
    double intervalEnd = 3.0;
    solver.findRoot(function, checker, 0, intervalStart, function.valueAt(intervalStart), intervalEnd, function.valueAt(intervalEnd));
    assertFalse(solver.isRootFound());
  }
}