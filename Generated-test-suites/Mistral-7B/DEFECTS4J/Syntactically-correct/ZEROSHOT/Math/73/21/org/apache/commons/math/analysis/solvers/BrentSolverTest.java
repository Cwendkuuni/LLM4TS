import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    // Test function f(x) = x^3 - 2*x^2 + x + 1
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return FunctionUtils.pow3(x) - 2 * FunctionUtils.pow2(x) + x + 1;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.INDEFINITE;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // Check result
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(1.8623496829288345, solver.getRoot(), 1e-7);

    // Test function f(x) = sin(x)
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        if (Math.abs(f) < 1e-8) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(x1 - x) < 1e-8) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.INDEFINITE;
        }
      }
    };

    x0 = 0.0;
    x1 = Math.PI / 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // Check result
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(1.5707963267948966, solver.getRoot(), 1e-7);

  }

}