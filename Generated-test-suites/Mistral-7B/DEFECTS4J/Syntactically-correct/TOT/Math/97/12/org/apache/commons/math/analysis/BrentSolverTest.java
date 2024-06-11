package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionTests;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

/**
 * Tests for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test with a simple function (x^2-1)
    ComputableFunction function = FunctionTests.squareMinusOne;
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();
    BrentSolver solver = new BrentSolver();

    // Test with an interval containing a root
    double x0 = -2.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertNotNaN(solver.getRoot());
    Assert.assertEquals(0.0, Math.abs(solver.getRoot() - 0.0), 1e-9);

    // Test with an interval not containing a root
    x0 = 0.0;
    x1 = 1.0;
    solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(Double.isNaN(solver.getRoot()));

    // Test with a function that has no roots
    ComputableFunction noRootFunction = FunctionTests.exponentialMinusOne;
    solver.findRoot(noRootFunction, checker, 50, 0.0, noRootFunction.valueAt(0.0), 1.0, noRootFunction.valueAt(1.0));
    Assert.assertTrue(Double.isNaN(solver.getRoot()));

    // Test with a function that throws an exception
    ComputableFunction exceptionFunction = () -> { throw new ArithmeticException("Divide by zero"); };
    Assert.assertThrows(FunctionException.class, () -> solver.findRoot(exceptionFunction, checker, 50, 0.0, Double.NEGATIVE_INFINITY, 0.0, Double.POSITIVE_INFINITY));
  }

}