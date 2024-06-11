import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {

    // Create a test function
    ComputableFunction function = new RationalFunction("x^3 - 2");

    // Create a convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        if (Math.abs(fx) < 1e-8 * Math.abs(x1 - x)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx1) < 1e-8 * Math.abs(x1 - x)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Create a Brent solver
    BrentSolver solver = new BrentSolver();

    // Test finding a root using known values
    double x0 = -2.0;
    double x1 = 2.0;
    int maxIter = 50;
    Assert.assertTrue(solver.findRoot(function, checker, maxIter, x0, -Math.pow(x0, 3), x1, Math.pow(x1, 3)));
    Assert.assertEquals(0.0, solver.getRoot() - Math.pow(0.0, 3), 1e-8);

    // Test handling of invalid input
    x0 = Double.NaN;
    Assert.assertFalse(solver.findRoot(function, checker, maxIter, x0, Double.NaN, x1, Math.pow(x1, 3)));

    x1 = Double.NaN;
    Assert.assertFalse(solver.findRoot(function, checker, maxIter, x0, -Math.pow(x0, 3), x1, Double.NaN));

    maxIter = 0;
    Assert.assertFalse(solver.findRoot(function, checker, maxIter, x0, -Math.pow(x0, 3), x1, Math.pow(x1, 3)));

    // Test failure to find a root
    function = new RationalFunction("x^2 - 4 * x + 3");
    x0 = 1.0;
    x1 = 2.0;
    maxIter = 50;
    Assert.assertFalse(solver.findRoot(function, checker, maxIter, x0, -(Math.pow(x0, 2) + 2.0), x1, Math.pow(x1, 2) + 2.0));

  }

}