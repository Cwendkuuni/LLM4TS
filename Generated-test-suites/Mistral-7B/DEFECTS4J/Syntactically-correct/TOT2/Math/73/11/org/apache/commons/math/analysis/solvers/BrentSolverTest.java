import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() {
    // Given
    ComputableFunction function = FunctionFactory.sineFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.PI / 2.0;

    // When
    boolean result = solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0);

    // Then
    Assert.assertTrue(result);
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  // Add test cases for other methods and edge cases here

  // Test edge cases and error scenarios
  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Exception message");
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, null, 10, 0.0, 0.0, 0.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_illegalArgumentException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, null, -1, 0.0, 0.0, 0.0, 0.0);
  }

  // Complete test file
  
  // Add other test cases here
  
}