import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionFactory functionFactory;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < Math.abs(fHigh)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < Double.MIN_VALUE) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDEFINED;
        }
      }
    };
    functionFactory = new FunctionFactory();
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
    functionFactory = null;
  }

  @Test
  public void testFindRoot_functionWithOneRoot() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction((x) -> Math.pow(x, 2) - 1.0);
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(0.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_functionWithTwoRoots_oneIsOutOfInterval() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction((x) -> Math.pow(x - 2.0, 2) - 4.0);
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;
    assertFalse(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRoot_functionWithTwoRoots_oneIsInsideInterval() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction((x) -> Math.pow(x - 2.0, 2) - 4.0);
    double x0 = 1.0;
    double x1 = 4.0;
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionWithNoRoots() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction((x) -> Math.pow(x, 2) + 1.0);
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;
    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}