import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot_FunctionWithRoot() {
    // Given
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.LOW;
      }
    };

    // When
    boolean result = solver.findRoot(function, checker, 100, -2.0, -4.0, 2.0, 0.0);

    // Then
    Assert.assertTrue(result);
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithoutRoot() {
    // Given
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.LOW;
      }
    };

    // When
    boolean result = solver.findRoot(function, checker, 100, -10.0, 10.0, 0.0, 0.0);

    // Then
    Assert.assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithException() {
    // Given
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        if (x > 1.0) {
          throw new ArithmeticException("Division by zero");
        }
        return x / (x - 1.0);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.LOW;
      }
    };

    // When
    solver.findRoot(function, checker, 100, 0.5, 1.0, 1.5, 1.0);

    // Then
  }

  // Add more test cases here

}