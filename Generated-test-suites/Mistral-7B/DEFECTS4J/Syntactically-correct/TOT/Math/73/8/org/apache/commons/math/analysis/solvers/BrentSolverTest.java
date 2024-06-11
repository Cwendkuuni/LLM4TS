import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRootSimpleFunction() {
    // Given
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        if (Math.abs(f) < 0.0001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 50, -2.0, 2.0, -1.0, 1.0);

    // Then
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootThrowsFunctionException() {
    // Given
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Test exception");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When and Then
    solver.findRoot(function, checker, 50, 0.0, 1.0, -1.0, 0.0);
  }

  @Test
  public void testFindRootFailureConvergence() {
    // Given
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 50, 0.0, 1.0, -1.0, 0.0);

    // Then
    Assert.assertFalse(result);
  }

  @Test
  public void testFindRootZeroFunction() {
    // Given
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 50, 0.0, 1.0, -1.0, 0.0);

    // Then
    Assert.assertFalse(result);
  }

  @Test
  public void testFindRootBoundaryCondition() {
    // Given
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        if (Math.abs(f) < 0.0001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 50, -2.5, 2.5, -2.5, 2.5);

    // Then
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
  }
}