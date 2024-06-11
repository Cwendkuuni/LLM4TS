import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        double diff = Math.abs(fx - fxPrevious);
        if (diff < 1e-12) {
          return ConvergenceChecker.LOW;
        } else if (diff < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }

  @After
  public void tearDown() {
    solver = null;
  }

  @Test
  public void testFindRootSimple() {
    RationalFunction function = new RationalFunction("x^2 - 4", "x");
    double x0 = 1.0;
    double x1 = 3.0;
    assertNotNull(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootThreeRealRoots() {
    RationalFunction function = new RationalFunction("x^3 - 3*x^2 + 2*x - 1", "x");
    double x0 = 0.5;
    double x1 = 1.5;
    assertNotNull(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(-1.0 / 3.0, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootNoRoots() {
    RationalFunction function = new RationalFunction("x^2 + 1", "x");
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}