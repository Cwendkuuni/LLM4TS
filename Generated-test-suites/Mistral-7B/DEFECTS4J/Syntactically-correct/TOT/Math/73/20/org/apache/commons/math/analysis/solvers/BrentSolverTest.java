import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.PolynomialFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private PolynomialFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return (fx1 < 0 && fx2 > 0) ? ConvergenceChecker.LOW : (fx1 > 0 && fx2 < 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.ITERATION;
      }
    };
    function = new PolynomialFunction(3.0, 2.0, -1.0);
  }

  /**
   * Test finding a root in a simple interval
   */
  @Test
  public void testSimpleRoot() {
    double x0 = 1.0;
    double x1 = 2.0;
    try {
      boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(result);
      double root = solver.getRoot();
      assertEquals(1.5, root, 0.001);
    } catch (FunctionException e) {
      fail("FunctionException should not be thrown");
    }
  }

  /**
   * Test finding a root at the edge of the interval
   */
  @Test
  public void testEdgeRoot() {
    double x0 = 1.0;
    double x1 = 2.0;
    try {
      solver.findRoot(function, checker, 100, x0, Double.NEGATIVE_INFINITY, x1, Double.POSITIVE_INFINITY);
      fail("FunctionException should be thrown");
    } catch (FunctionException e) {
      // expected
    }

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    double root = solver.getRoot();
    assertEquals(1.5, root, 0.001);
  }

  /**
   * Test finding a root outside the interval
   */
  @Test
  public void testOutsideRoot() {
    double x0 = 1.0;
    double x1 = 2.0;
    try {
      solver.findRoot(function, checker, 100, 0.0, function.valueAt(0.0), x1, function.valueAt(x1));
      fail("FunctionException should be thrown");
    } catch (FunctionException e) {
      // expected
    }

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), 0.0, function.valueAt(0.0));
      fail("FunctionException should be thrown");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test handling of non-convergent function
   */
  @Test
  public void testNonConvergentFunction() {
    double x0 = 1.0;
    double x1 = 2.0;
    function = new PolynomialFunction(3.0, 2.0, 0.0);

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("FunctionException should be thrown");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test handling of an invalid ConvergenceChecker
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidChecker() {
    ConvergenceChecker invalidChecker = null;
    solver.findRoot(function, invalidChecker, 100, 1.0, 0.0, 2.0, 0.0);
  }

  /**
   * Test handling of a null function
   */
  @Test(expected = NullPointerException.class)
  public void testNullFunction() {
    solver.findRoot(null, checker, 100, 1.0, 0.0, 2.0, 0.0);
  }
}