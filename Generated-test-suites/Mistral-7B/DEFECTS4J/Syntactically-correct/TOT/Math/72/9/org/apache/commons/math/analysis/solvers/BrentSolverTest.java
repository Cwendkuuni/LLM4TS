package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRootWithSineFunction() throws FunctionException {
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue("Root not found in the given interval", result);
    assertEquals("Root is not correct", root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThrowsException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Division by zero");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullFunction() throws FunctionException {
    ComputableFunction function = null;
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test(expected = NullPointerException.class)
  public void testFindRootWithNullChecker() throws FunctionException {
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = null;
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootWithLargeInterval() throws FunctionException {
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 2 * Math.PI;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue("Root not found in the given interval", result);
    assertEquals("Root is not correct", Math.PI, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithConvergedChecker() throws FunctionException {
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Converged();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue("Root not found in the given interval", result);
    assertEquals("Root is not correct", Math.PI, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithMaxIterationReached() throws FunctionException {
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.MaxIterationReached();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    boolean result = solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse("Root found in less than 1 iteration", result);
  }
}