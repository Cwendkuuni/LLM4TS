package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;

/**
 * Test case for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test the BrentSolver with a simple function.
   */
  @Test
  public void testBrentSolverWithSimpleFunction() {
    ComputableFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLast, double fxLast) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -3.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();
    assertEquals("Root should be 1.", 1.0, root, 0.01);
  }

  /**
   * Test the BrentSolver with a function having two roots.
   */
  @Test
  public void testBrentSolverWithFunctionTwoRoots() {
    ComputableFunction function = new RationalFunction("x^3 - 3 * x^2 + 2");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLast, double fxLast) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();
    double root2 = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0))
        .getRoot();

    assertEquals("Root 1 should be around 1.3.", 1.3, root1, 0.1);
    assertEquals("Root 2 should be around 1.5.", 1.5, root2, 0.1);
  }

  /**
   * Test the BrentSolver when the function has no real roots.
   */
  @Test
  public void testBrentSolverWithFunctionNoRealRoots() {
    ComputableFunction function = new RationalFunction("x^2 + 1");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLast, double fxLast) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 2.0;
    assertFalse("Test should fail to find a root.", solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());
  }

  /**
   * Test the BrentSolver with a function which throws an exception.
   */
  @Test(expected = FunctionException.class)
  public void testBrentSolverWithExceptionFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test exception.");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLast, double fxLast) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

}