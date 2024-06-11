import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x0, double fx0) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fx) > 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }

  @Test
  public void testRootFind() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(2.0, solver.getRoot(), 0.001);
    assertEquals(true, root);
  }

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function error");
      }
    };
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, 1.0, 0.0, 2.0, 0.0));
  }

  @Test
  public void testMaxIterations() {
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(solver.getRoot() != Double.NaN);
  }

  @Test
  public void testConvergenceCheckerFailed() {
    double x0 = 1.0;
    double x1 = 2.0;
    ConvergenceChecker checkerFail = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x0, double fx0) {
        return ConvergenceChecker.NONE;
      }
    };
    solver.findRoot(function, checkerFail, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(solver.getRoot() != Double.NaN);
  }
}