import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolverRootsFinder() throws Exception {

    // Test case 1: simple root
    double x0 = 1.0;
    double x1 = 3.0;
    ComputableFunction function = new SimpleFunction("x^2 - 2") {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp, double fp) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(1.5, solver.getRoot(), 1e-6);

    // Test case 2: multiple roots
    x0 = -2.0;
    x1 = 2.0;
    function = new SimpleFunction("(x - 1)^3 - 1") {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 3) - 1;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp, double fp) {
        return ConvergenceChecker.HIGH;
      }
    };

    solver = new BrentSolver();
    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(-1.0, solver.getRoot(), 1e-6);
    Assert.assertEquals(1.0, solver.getRoot(), 1e-6);

    // Test case 3: interval does not contain any root
    x0 = -5.0;
    x1 = 5.0;
    function = new SimpleFunction("x^3 + 1") {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) + 1;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp, double fp) {
        return ConvergenceChecker.LOW;
      }
    };

    solver = new BrentSolver();
    Assert.assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(Double.NaN, solver.getRoot());

    // Test case 4: root outside the interval
    x0 = 0.0;
    x1 = 1.0;
    function = new SimpleFunction("x^3 - 2") {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp, double fp) {
        return ConvergenceChecker.LOW;
      }
    };

    solver = new BrentSolver();
    Assert.assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(Double.NaN, solver.getRoot());

  }

}