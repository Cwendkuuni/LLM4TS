import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private MockFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 0.0001 && Math.abs(f2) < 0.0001) {
          return ConvergenceChecker.BOTH;
        } else if (Math.abs(f1) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    function = new MockFunction();
  }

  @Test
  public void testRootAtZero() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(0.0, solver.getRoot(), 1e-5);
  }

  @Test
  public void testRootAtOne() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    function.setFunction((x) -> x - 1.0);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(1.0, solver.getRoot(), 1e-5);
  }

  @Test
  public void testRootBetweenZeroAndOne() throws FunctionException {
    double x0 = 0.1;
    double x1 = 0.3;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    function.setFunction((x) -> Math.sin(Math.PI * x / 2.0));

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 0.25) < 1e-3);
  }

  @Test
  public void testRootNotInInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    function.setFunction((x) -> Math.sin(Math.PI * x) + 1.0);

    assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertNull(solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testDivideByZero() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    function.setFunction((x) -> 1.0 / (x - 1.0));

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }
}