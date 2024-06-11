import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        if (fx * fxLow > 0) {
          return ConvergenceChecker.NEITHER;
        } else {
          if (Math.abs(fx) < Double.MIN_VALUE) {
            return ConvergenceChecker.LOW;
          } else {
            return ConvergenceChecker.HIGH;
          }
        }
      }
    };
  }

  /**
   * Test finding a root with a simple function
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    ComputableFunction function = new DummyFunction(1.0);

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(1.5, solver.getRoot(), 0.001);
  }

  /**
   * Test finding a root with a function with multiple roots
   */
  @Test
  public void testFindRootFunctionMultipleRoots() throws FunctionException {
    ComputableFunction function = new DummyFunction(-1.0);

    double x0 = -2.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(-1.0, solver.getRoot(), 0.001);
  }

  /**
   * Test finding a root with a function that doesn't have a root in the interval
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionNoRoot() throws FunctionException {
    ComputableFunction function = new DummyFunction(1.0);

    double x0 = 1.0;
    double x1 = 0.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test finding a root with a function that throws an exception
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    ComputableFunction function = new DummyFunctionThrowingException();

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}

class DummyFunction implements ComputableFunction {

  private double a;

  public DummyFunction(double a) {
    this.a = a;
  }

  @Override
  public double valueAt(double x) {
    return a * Math.sin(Math.PI * x / 2.0);
  }

}

class DummyFunctionThrowingException implements ComputableFunction {

  @Override
  public double valueAt(double x) {
    throw new FunctionException("Exception message");
  }

}