import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.LinearFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private LinearFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return (f1 * f2 > 0) ? ConvergenceChecker.DID_NOT_CONVERGE : ConvergenceChecker.CONVERGED;
      }
    };
    function = new LinearFunction(1.0, 2.0);
  }

  @Test
  public void testFindRoot_SimpleCase() {
    double x0 = 0.0;
    double x1 = 1.0;
    try {
      boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(result);
      assertEquals(0.5, solver.getRoot());
    } catch (FunctionException e) {
      fail("FunctionException should not be thrown in this case.");
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionThrowsException() {
    double x0 = 0.0;
    double x1 = 1.0;
    LinearFunction function = new LinearFunction(1.0, 1.0 / 0.0);

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("FunctionException should be thrown in this case.");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRoot_EdgeCase() {
    double x0 = Double.NEGATIVE_INFINITY;
    double x1 = 0.0;
    double x2 = Double.POSITIVE_INFINITY;
    try {
      boolean result = solver.findRoot(function, checker, 100, x0, Double.NEGATIVE_INFINITY, x1, 0.0);
      assertFalse(result);
    } catch (FunctionException e) {
      // expected, as the interval is not guaranteed to contain a root
    }

    try {
      result = solver.findRoot(function, checker, 100, x1, 0.0, x2, Double.POSITIVE_INFINITY);
      assertFalse(result);
    } catch (FunctionException e) {
      // expected, as the interval is not guaranteed to contain a root
    }
  }
}