import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalars.FunctionUtils;
import org.spaceroots.mantissa.functions.scalars.examples.SquareRootFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SquareRootFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    function = new SquareRootFunction();
  }

  @Test
  public void testFindRoot_SimpleCase() {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.4142135625;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root not correct", expectedRoot, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRoot_EdgeCase_PositiveX() {
    double x0 = 100.0;
    double x1 = 100.01;
    double expectedRoot = 10.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root not correct", expectedRoot, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRoot_EdgeCase_NegativeX() {
    double x0 = -100.0;
    double x1 = -100.01;
    double expectedRoot = -10.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root not correct", expectedRoot, solver.getRoot(), 0.000001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_ErrorCase_NegativeFunction() {
    double x0 = 0.0;
    double x1 = 0.01;
    function = new SquareRootFunctionNegative();

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_ErrorCase_FunctionThrowsException() {
    double x0 = 0.0;
    double x1 = 0.01;
    function = new FunctionWithException();

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}

class FunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new FunctionException("Function exception");
  }
}

class SquareRootFunctionNegative implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.sqrt(Math.negateExact(x));
  }
}