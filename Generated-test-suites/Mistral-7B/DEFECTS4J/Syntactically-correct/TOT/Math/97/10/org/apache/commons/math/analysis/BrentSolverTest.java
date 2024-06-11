import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() {
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      double x0 = -2.5;
      double x1 = 2.5;
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      double root = solver.getRoot();
      assertEquals("Root must be around -1.0", -0.999, root, 0.01);
    } catch (FunctionException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFindRoot_FunctionWithTwoRoots() {
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1, 2) - 0.25;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      double x0 = -2;
      double x1 = 2;
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      double root = solver.getRoot();
      double expectedRoot = (Math.random() > 0.5) ? -1.1 : 1.1;
      assertEquals("Root must be around " + expectedRoot, expectedRoot, root, 0.1);
    } catch (FunctionException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFindRoot_FunctionWithNoRoots() {
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) * Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Double.isNaN(fx)) {
          return ConvergenceChecker.NOT_CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      double x0 = 0;
      double x1 = 2 * Math.PI;
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("No root found, but method returned a root.");
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testFindRoot_FunctionWithOutOfRangeValues() {
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      double x0 = -100;
      double x1 = 100;
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected an exception due to out of range values.");
    } catch (FunctionException e) {
      // Expected
    }
  }
}