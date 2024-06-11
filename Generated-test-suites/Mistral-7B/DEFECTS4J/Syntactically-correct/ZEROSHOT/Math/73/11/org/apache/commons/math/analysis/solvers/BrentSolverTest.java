import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < Math.abs(fLow)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fLow) < Math.abs(fHigh)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDETERMINED;
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
  public void testFindRoot_Simple() {
    ComputableFunction function = new RationalFunction("x^2 - 2");
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;
    boolean result = solver.findRoot(function, checker, maxIter, x0, -1.0, x1, 1.0);
    assertTrue(result);
    double root = solver.getRoot();
    assertEquals(Math.sqrt(2.0), root, 1e-6);
  }

  @Test
  public void testFindRoot_Divergence() {
    ComputableFunction function = new RationalFunction("x^2 - 2");
    double x0 = 0.0;
    double x1 = 3.0;
    int maxIter = 100;
    assertFalse(solver.findRoot(function, checker, maxIter, x0, -1.0, x1, 1.0));
    assertNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_Complex() {
    ComputableFunction function = new RationalFunction("x^2 - 2 * i * x + 1");
    double x0 = -1.0;
    double x1 = 1.0;
    int maxIter = 100;
    boolean result = solver.findRoot(function, checker, maxIter, x0, -1.0, x1, 1.0);
    assertTrue(result);
    double root = solver.getRoot();
    assertEquals(Math.sqrt(1.0 + Math.pow(0.5, 2.0)), Math.abs(root), 1e-6);
    assertEquals(Math.acos(0.5) / 2.0, Math.atan2(root.imag(), root.real()), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_Exception() {
    ComputableFunction function = new RationalFunction("x^2 / 0");
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;
    solver.findRoot(function, checker, maxIter, x0, -1.0, x1, 1.0);
  }

}