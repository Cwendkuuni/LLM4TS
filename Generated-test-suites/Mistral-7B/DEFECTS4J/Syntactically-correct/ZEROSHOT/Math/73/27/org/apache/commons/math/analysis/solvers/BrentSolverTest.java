import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

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
        return ConvergenceChecker.NONE;
      }
    };
    function = new RationalFunction("x^3 - 2*x^2 + 3*x - 2");
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertEquals(true, result);
    assertEquals(-0.416666666666667, solver.getRoot(), 0.000000001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNonConvergedFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    RationalFunction function = new RationalFunction("x^2 - 2");
    solver.findRoot(function, checker, 100, x0, 0.0, x1, 1.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNegativeInterval() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    RationalFunction function = new RationalFunction("x^3 - 2*x^2 + 3*x - 2");
    solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNonMonotonicFunction() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    RationalFunction function = new RationalFunction("x^3 + x^2 + 1");
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootWithMaxIter() throws FunctionException {
    double x0 = 0.0;
    double x1 = 2.0;
    RationalFunction function = new RationalFunction("sin(x) / x");
    boolean result = solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(false, result);
  }
}