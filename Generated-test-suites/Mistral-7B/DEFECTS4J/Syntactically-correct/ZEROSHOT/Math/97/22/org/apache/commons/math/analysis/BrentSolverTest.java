import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xp, double fp) {
        return ConvergenceChecker.CONTINUE;
      }
    };
    function = new RationalFunction("x^3 - 2 * x^2 + 2 * x - 1");
  }

  @Test
  public void testFindRoot() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertTrue(Double.isFinite(root));
    assertEquals(-1.0, root, 1e-6);
  }

  @Test
  public void testFindRoot_rootOutsideInterval() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRoot_rootInsideInterval() throws Exception {
    double x0 = 1.5;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertTrue(Double.isFinite(root));
    assertEquals(-0.582598867137367, root, 1e-6);
  }

  @Test
  public void testFindRoot_rootEqualsBoundary() throws Exception {
    double x0 = 1.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertTrue(Double.isFinite(root));
    assertEquals(1.0, root, 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = () -> {
      throw new FunctionException();
    };
    solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);
  }
}