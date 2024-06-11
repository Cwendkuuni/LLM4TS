package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.PolynomialFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Tests for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Test finding a root of a polynomial function.
   */
  @Test
  public void testFindRootOfPolynomialFunction() {
    ComputableFunction function = new PolynomialFunction(3, -4, 1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double[] expectedRoots = {-1.0};
    for (double expectedRoot : expectedRoots) {
      try {
        double[] bounds = {expectedRoot - 0.1, expectedRoot + 0.1};
        double root = solver.findRoot(function, checker, 100, bounds[0],
            function.valueAt(bounds[0]), bounds[1], function.valueAt(bounds[1]))
            .getRoot();
        assertEquals(expectedRoot, root, 0.001);
      } catch (FunctionException e) {
        // should not happen
      }
    }
  }

  /**
   * Test finding a root outside the interval.
   */
  @Test
  public void testFindRootOutsideInterval() {
    ComputableFunction function = new PolynomialFunction(3, -4, 1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double[] bounds = {-1.0, 1.0};
    assertThrows(FunctionException.class, () -> {
      solver.findRoot(function, checker, 100, bounds[0], function.valueAt(bounds[0]),
          bounds[1], function.valueAt(bounds[1]));
    });
  }

  /**
   * Test finding a root with a non-convergent function.
   */
  @Test
  public void testFindRootNonConvergentFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new RuntimeException();
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double[] bounds = {0.0, 1.0};
    assertThrows(FunctionException.class, () -> {
      solver.findRoot(function, checker, 100, bounds[0],
          function.valueAt(bounds[0]), bounds[1], function.valueAt(bounds[1]));
    });
  }

  /**
   * Test finding a root with a non-monotonic function.
   */
  @Test
  public void testFindRootNonMonotonicFunction() {
    ComputableFunction function = new PolynomialFunction(1, 1, -1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double[] bounds = {0.0, 1.0};
    assertThrows(FunctionException.class, () -> {
      solver.findRoot(function, checker, 100, bounds[0], function.valueAt(bounds[0]),
          bounds[1], function.valueAt(bounds[1]));
    });
  }

}