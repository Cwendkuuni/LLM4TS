import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.Constants;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Test 1 : function f(x) = x^3 - 2
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        if (Math.abs(f) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 20, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertEquals("Test 1 : Root is not expected to be NaN", -0.5, root, 0.001);

    // Test 2 : function f(x) = sin(x)
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        if (Math.abs(f) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();

    x0 = 0.0;
    x1 = 3.14;
    root = solver.findRoot(function, checker, 20, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertEquals("Test 2 : Root is not expected to be NaN", 1.27324064, root, 0.01);

    // Test 3 : function f(x) = exp(x) - 2
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(x) - 2;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        if (Math.abs(f) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();

    x0 = -1.0;
    x1 = 0.0;
    root = solver.findRoot(function, checker, 20, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertEquals("Test 3 : Root is not expected to be NaN", Constants.E, root, 0.001);

  }

}