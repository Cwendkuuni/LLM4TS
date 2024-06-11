package org.spaceroots.mantissa.roots.test;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private FunctionUtils functionUtils;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    functionUtils = new FunctionUtils();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        double error = Math.abs(x2 - x);
        if (error < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < Math.abs(fx)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
  }

  /**
   * Tests finding root of function f(x) = x^3 - 2 in the interval [1, 2].
   */
  @Test
  public void testFindRootFunctionX3Minus2() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x * x - 2;
      }
    };

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(x1, solver.getRoot(), epsilon);
  }

  /**
   * Tests finding root of function f(x) = sin(x) in the interval [0, 1].
   */
  @Test
  public void testFindRootFunctionSin() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(result);
    // The exact value of sin(1) is not known as a constant value,
    // instead we can use a tolerance value to check the result.
    double tolerance = 0.001;
    assertEquals(Math.sin(x1), solver.getRoot(), tolerance);
  }

  /**
   * Tests error handling when finding root of function f(x) = 1/x in the interval [1, 1.1].
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionOneOverX() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        if (x == 0.0) {
          throw new ArithmeticException("Division by zero");
        }
        return 1.0 / x;
      }
    };

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.1;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }
}