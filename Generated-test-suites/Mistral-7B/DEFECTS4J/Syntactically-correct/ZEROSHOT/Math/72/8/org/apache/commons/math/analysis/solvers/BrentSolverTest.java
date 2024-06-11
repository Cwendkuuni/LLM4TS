import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    function = FunctionUtils.sqrt;
  }

  @Test
  public void testFindRoot_simple() throws FunctionException {
    double x0 = 2.0;
    double f0 = 4.0;
    double x1 = 3.0;
    double f1 = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(Math.sqrt(2.0), solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_convergence() throws FunctionException {
    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 3.0;
    double f1 = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertFalse(result);
  }

  @Test
  public void testFindRoot_bracketing() throws FunctionException {
    double x0 = 0.0;
    double f0 = 0.25;
    double x1 = 1.0;
    double f1 = 0.1;

    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(0.5, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = () -> { throw new FunctionException("test"); };
    solver.findRoot(function, checker, 1, 0.0, 0.0, 0.0, 0.0);
    assertFalse(solver.isRootFound());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionExceptionThrows() throws FunctionException {
    ComputableFunction function = () -> { throw new FunctionException("test"); };
    solver.findRoot(function, checker, 1, 0.0, 0.0, 0.0, 0.0);
  }

}