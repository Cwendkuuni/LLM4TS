import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NO;
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 4 * x + 4;
      }
    };
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = -2.0;
    double x1 = 2.0;
    int maxIter = 100;

    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    double root = solver.getRoot();
    assertEquals(0.25112516220744877, root, 0.0001);
  }

  @Test
  public void testFindRootInvalidFunction() {
    double x0 = 0.0;
    double x1 = 0.0;
    int maxIter = 100;

    try {
      solver.findRoot(null, checker, maxIter, x0, Double.NaN, x1, Double.NaN);
      assert False();
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testFindRootInvalidCheckers() {
    double x0 = 0.0;
    double x1 = 0.0;
    int maxIter = 100;

    ConvergenceChecker invalidChecker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };

    try {
      solver.findRoot(function, invalidChecker, maxIter, x0, FunctionConstants.E, x1, FunctionConstants.E);
      assert False();
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRootMaxIterations() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    int maxIter = 0;

    assertFalse(solver.findRoot(function, checker, maxIter, x0, FunctionConstants.E, x1, FunctionConstants.E));
  }
}