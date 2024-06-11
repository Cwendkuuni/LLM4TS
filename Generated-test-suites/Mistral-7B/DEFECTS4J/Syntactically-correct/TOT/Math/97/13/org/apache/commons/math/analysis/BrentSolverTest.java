import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.SineFunction;

import static org.junit.Assert.*;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SineFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return (f1 * f2 > 0) ? ConvergenceChecker.NONE : (Math.abs(x1 - x2) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };
    function = new SineFunction();
  }

  /**
   * Test root finding in a simple interval.
   */
  @Test
  public void testFindRootSimple() {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.01);
    assertTrue(result);
  }

  /**
   * Test root finding in an interval with a root at the boundary.
   */
  @Test
  public void testFindRootBoundary() {
    double x0 = 0.0;
    double x1 = 2.0;
    double root = 1.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.01);
    assertTrue(result);
  }

  /**
   * Test root finding in an interval with no root.
   */
  @Test
  public void testFindRootNoRoot() {
    double x0 = 0.0;
    double x1 = 4.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(result);
  }

  /**
   * Test root finding in an interval with a very large function value.
   */
  @Test
  public void testFindRootLargeFunctionValue() {
    double x0 = -1000.0;
    double x1 = 1000.0;
    boolean result = solver.findRoot(function, checker, 100, x0, Double.MAX_VALUE, x1, Double.MIN_VALUE);
    assertFalse(result);
  }

  /**
   * Test root finding in an interval with a function that throws an exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootThrowsException() {
    double x0 = 0.0;
    double x1 = 2.0;
    SolverExceptionFunction function = new SolverExceptionFunction();
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    fail("Exception expected but not thrown.");
  }

}

class SolverExceptionFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    throw new FunctionException("Test exception.");
  }
}