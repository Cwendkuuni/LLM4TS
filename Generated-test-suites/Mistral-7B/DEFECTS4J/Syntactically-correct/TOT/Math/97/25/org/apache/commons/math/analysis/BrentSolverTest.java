import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.ConvergenceChecker.ConvergenceResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
      public ConvergenceResult converged(double x, double fx, double xLow, double fxLow) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceResult.LOW;
        } else if (Math.abs(x - xLow) < 0.0001) {
          return ConvergenceResult.HIGH;
        } else {
          return ConvergenceResult.NONE;
        }
      }
    };
    function = FunctionFactory.sinFunction();
  }

  @Test
  public void testFindRoot_simple() throws Exception {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2.0; // approximately 1.5707963267949

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_convergence() throws Exception {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2.0;

    // Test convergence at root
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x0, function.valueAt(x0)));
    assertEquals(root, solver.getRoot(), 0.0001);

    // Test convergence near root
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.0001);

    // Test convergence far from root
    double x2 = 5.0;
    assertTrue(solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x2, function.valueAt(x2)));
    assertEquals(root, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_divergence() throws Exception {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI; // incorrect root

    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws Exception {
    double x0 = 1.0;
    double x1 = 3.0;
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function failed");
      }
    };

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}