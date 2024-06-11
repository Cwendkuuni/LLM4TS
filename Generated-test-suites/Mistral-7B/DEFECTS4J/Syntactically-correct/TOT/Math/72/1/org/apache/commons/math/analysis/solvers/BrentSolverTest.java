import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.AbstractFunction;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.ExponentialFunction;
import org.spaceroots.mantissa.functions.scalar.LinearFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new LinearFunction(1.0, 2.0, 3.0);
  }

  @Test
  public void testFindRootLinearFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = 3.0;
    double root = 1.0;
    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, brentSolver.getRoot(), 0.001);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootExponentialFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.5;
    boolean result = brentSolver.findRoot(new ExponentialFunction(1.0), checker, 100, x0, Double.NEGATIVE_INFINITY, x1, Double.POSITIVE_INFINITY);
    assertEquals(root, brentSolver.getRoot(), 0.01);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Test Exception");
      }
    };
    boolean result = brentSolver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootNullFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    ComputableFunction function = null;
    boolean result = brentSolver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootNullChecker() {
    double x0 = 0.0;
    double x1 = 1.0;
    ConvergenceChecker checker = null;
    boolean result = brentSolver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }
}