package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xMax, double fxMax) {
        return (fx > 0.0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NORMAL;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionThrowsException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 0.0) {
          throw new FunctionException("Function exception");
        }
        return Math.sin(x);
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xMax, double fxMax) {
        return ConvergenceChecker.NORMAL;
      }
    };

    double x0 = 1.0;
    double x1 = 0.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_ComplexFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return FunctionUtils.sin(x) * Math.exp(-x) - 2.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xMax, double fxMax) {
        return (Math.abs(fx) < 1e-6) ? ConvergenceChecker.LOW : ConvergenceChecker.NORMAL;
      }
    };

    double x0 = 1.0;
    double x1 = -1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(-1.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_IntervalIsEmpty() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xMax, double fxMax) {
        return ConvergenceChecker.NORMAL;
      }
    };

    double x0 = 0.0;
    double x1 = 0.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    // the test should not find a root and the interval should be empty
    Assert.assertTrue(Double.isNaN(solver.getRoot()));
  }
}