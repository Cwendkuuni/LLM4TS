import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        if (Math.abs(fx1) < 0.001 && Math.abs(fx2) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
  }

  @Test
  public void testFindRootInInterval() throws Exception {
    double x0 = -1.5;
    double x1 = 1.5;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(-0.7071067811865476, solver.getRoot(), 0.001);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootInIntervalException() throws Exception {
    double x0 = -1.5;
    double x1 = 1.5;
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function exception");
      }
    };
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootAtZero() throws Exception {
    double x0 = 0.0;
    double x1 = 0.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(0.0, solver.getRoot(), 0.001);
    assertEquals(true, result);
  }

}