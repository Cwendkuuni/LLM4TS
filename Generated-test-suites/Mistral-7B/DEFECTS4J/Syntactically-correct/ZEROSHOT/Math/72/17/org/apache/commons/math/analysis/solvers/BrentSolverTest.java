import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xprev, double fxprev) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fxprev) > Math.abs(fx)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NEITHER;
        }
      }
    };
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
  }

  @Test
  public void testFindRoot_functionWithOneRoot() throws FunctionException {
    ComputableFunction function = FunctionFactory.sin();
    double a = 0.1;
    double b = 1.0;
    double xRoot = 0.54030230586875567;

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));

    assertEquals(xRoot, solver.getRoot(), 0.00001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionWithNoRoot() throws FunctionException {
    ComputableFunction function = FunctionFactory.cos();
    double a = 0.1;
    double b = 1.0;

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionWithMultipleRoots() throws FunctionException {
    ComputableFunction function = FunctionFactory.exp();
    double a = 0.1;
    double b = 1.0;

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));
  }
}