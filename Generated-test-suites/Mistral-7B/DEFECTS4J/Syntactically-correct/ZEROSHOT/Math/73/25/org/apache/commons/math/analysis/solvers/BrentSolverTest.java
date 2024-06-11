import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {
    // Test case 1: x^3 - 1 = 0
    ComputableFunction function = new RationalFunction("3.0 * x^3 - 1.0");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        if (Math.abs(fx) < 0.1) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 10, -1.5, -1.5, -1.0, -0.5);
    Assert.assertEquals(0.0, solver.getRoot(), 0.001);

    // Test case 2: sqrt(x) - 2 = 0
    function = new RationalFunction("x - 4.0");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        if (Math.abs(fx) < 0.1) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 10, 1.0, 1.5, 2.0, 2.5);
    Assert.assertEquals(2.0, solver.getRoot(), 0.001);

    // Test case 3: x^2 - 2 = 0
    function = new RationalFunction("x^2 - 2.0");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 10, -2.0, -1.5, -1.0, -0.5);
    Assert.assertEquals(-1.0, solver.getRoot(), 0.001);
    Assert.assertEquals(1.0, solver.getRoot(), 0.001);

    // Test case 4: x^2 - 2 = 0 with large interval
    function = new RationalFunction("x^2 - 2.0");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 10, -10.0, 10.0, 0.0, 10.0);
    Assert.assertEquals(-1.0, solver.getRoot(), 0.001);
    Assert.assertEquals(1.0, solver.getRoot(), 0.001);

    // Test case 5: x^4 - 2 = 0
    function = new RationalFunction("x^4 - 2.0");
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 10, -2.0, -1.5, -1.0, -0.5);
    Assert.assertEquals(-0.752745995, solver.getRoot(), 0.001);
    Assert.assertEquals(0.752745995, solver.getRoot(), 0.001);
  }

}