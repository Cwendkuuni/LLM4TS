package org.spaceroots.mantissa.roots.test;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.RootsFinderException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private ComputableFunction function;
  private ConvergenceChecker checker;
  private BrentSolver solver;

  @Before
  public void setUp() {
    function = FunctionFactory.sineFunction();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    solver = new BrentSolver();
  }

  /**
   * Test finding a root using the Brent solver in a simple case.
   */
  @Test
  public void testFindRootSimple() throws RootsFinderException {
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(Math.PI / 2.0, solver.getRoot(), 0.0001);
  }

  /**
   * Test finding a root using the Brent solver when the function value is zero at the bounds.
   */
  @Test
  public void testFindRootZeroAtBounds() throws RootsFinderException {
    double x0 = 0.5;
    double x1 = 0.5;
    solver.findRoot(function, checker, 100, x0, 0, x1, 0);
    assertEquals(x0, solver.getRoot(), 0.0);
  }

  /**
   * Test finding a root using the Brent solver when the function value is negative at a bound.
   */
  @Test
  public void testFindRootNegativeAtBound() throws RootsFinderException {
    double x0 = -1.0;
    double x1 = 1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(-Math.PI / 2.0, solver.getRoot(), 0.0001);
  }

  /**
   * Test catching an exception thrown by the convergence checker.
   */
  @Test(expected = RootsFinderException.class)
  public void testFindRootException() throws RootsFinderException {
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        throw new RuntimeException();
      }
    }, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test catching an exception thrown by the function.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws RootsFinderException {
    ComputableFunction badFunction = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test");
      }
    };
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(badFunction, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }
}