import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Tests the findRoot method with a simple function for which we know the root
   */
  @Test
  public void testFindRoot() throws Exception {
    // define the function to be tested
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    // define the convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.NONE;
      }
    };

    // create the Brent solver
    BrentSolver solver = new BrentSolver();

    // define the interval
    double x0 = -3.0;
    double x1 = 3.0;

    // find the root
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // check the result
    assertTrue(result);

    // check the root value
    double expectedRoot = -2.0;
    double root = solver.getRoot();
    assertEquals(expectedRoot, root, 1e-5);
  }

  /**
   * Tests the findRoot method with a function for which we do not know the root
   */
  @Test
  public void testFindRootInvalidFunction() throws Exception {
    // define the function to be tested
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.log(Math.abs(x - 1.0));
      }
    };

    // define the convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.NONE;
      }
    };

    // create the Brent solver
    BrentSolver solver = new BrentSolver();

    // define the interval
    double x0 = 0.0;
    double x1 = 2.0;

    // find the root
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // check the result
    assertFalse(result);

    // check that the root has not been modified
    assertEquals(Double.NaN, solver.getRoot());
  }
}