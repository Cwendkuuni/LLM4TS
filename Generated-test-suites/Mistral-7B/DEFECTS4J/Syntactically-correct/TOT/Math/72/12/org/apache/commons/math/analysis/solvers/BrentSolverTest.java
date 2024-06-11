import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.test.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceChecker.ABSOLUTE;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    function = new MockComputableFunction(0.0, 1.0);
  }

  @Test
  public void testFindRootSimpleCase() throws FunctionException {
    double x0 = 0.5;
    double x1 = 1.0;
    double root = 0.75;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootZeroFunction() throws FunctionException {
    double x0 = 0.5;
    double x1 = 1.0;
    ComputableFunction function = new MockComputableFunction(0.0, 0.0);

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertFalse(result);
  }

  @Test
  public void testFindRootFunctionWithTwoRoots() throws FunctionException {
    double x0 = 0.5;
    double x1 = 1.5;
    ComputableFunction function = new MockComputableFunction(1.0, 1.0, 2.0);

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootDivisionByZeroFunction() throws FunctionException {
    double x0 = 0.5;
    double x1 = 1.0;
    ComputableFunction function = new MockComputableFunction(0.0, 0.0, Double.doubleQuote("Division by zero is not allowed"));

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}