import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for BrentSolver class.
 */
public class BrentSolverTest {

  @Test
  public void testFindRootSimple() {
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NEITHER;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = Math.sin(x0);
    double x1 = -1.0;
    double fx1 = Math.sin(x1);

    try {
      solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
      double root = solver.getRoot();
      Assert.assertEquals(0.0, root, epsilon);
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test
      Assert.fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootZeroFunction() {
    ComputableFunction function = new FunctionConstants();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double fx0 = 0.0;
    double x1 = 0.1;
    double fx1 = 0.1;

    try {
      solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
      double root = solver.getRoot();
      Assert.assertEquals(0.0, root, epsilon);
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test
      Assert.fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootFunctionWithTwoRoots() {
    ComputableFunction function = new FunctionConstants();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double fx0 = Math.sin(x0);
    double x1 = 1.0;
    double fx1 = Math.sin(x1);

    try {
      solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
      double root = solver.getRoot();
      Assert.assertEquals(0.0, root % (2 * Math.PI), epsilon);
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test
      Assert.fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRootDivergentFunction() {
    ComputableFunction function = new FunctionConstants() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 0) {
          throw new FunctionException("Error");
        }
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NEITHER;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double fx0 = Math.sin(x0);
    double x1 = 1.0;
    double fx1 = Math.sin(x1);

    try {
      solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
    } catch (FunctionException e) {
      // Expected FunctionException
    }
  }

}