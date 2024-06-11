package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction; // Assuming SimpleFunction is used as ComputableFunction in the application
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test function f(x) = x^3 - 2.
   * This function has one real root in [0, 2] interval.
   */
  @Test
  public void testFindRoot_FunctionXCubedMinusTwo() throws Exception {
    SimpleFunction function = new SimpleFunction(x -> Math.pow(x, 3) - 2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(xHigh - xLow) < 0.0001) ? ConvergenceChecker.BOTH : ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.pow(x0, 3) - 2;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 3) - 2;
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertEquals("Root not found", -1.0, solver.getRoot(), 0.001);
  }

  /**
   * Test function f(x) = sin(x) - x.
   * This function has one real root in [0, pi] interval.
   */
  @Test
  public void testFindRoot_FunctionSineMinusX() throws Exception {
    SimpleFunction function = x -> Math.sin(x) - x;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return Math.abs(fHigh) < 0.001 ? ConvergenceChecker.HIGH : ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.sin(x0) - x0;
    double x1 = Math.PI;
    double f1 = Math.sin(x1) - x1;
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertEquals("Root not found", Math.PI / 2, solver.getRoot(), 0.001);
  }

  /**
   * Test function f(x) = (x - 2) * (x - 4).
   * This function has two real roots.
   */
  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionXMinusTwoXMinusFour() throws Exception {
    SimpleFunction function = x -> (x - 2) * (x - 4);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = (x0 - 2) * (x0 - 4);
    double x1 = 3.0;
    double f1 = (x1 - 2) * (x1 - 4);
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    fail("Root not found");
  }

  /**
   * Test function f(x) = 1 / x.
   * This function has no real roots.
   */
  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionOneOverX() throws Exception {
    SimpleFunction function = x -> 1 / x;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = 1 / x0;
    double x1 = -1.0;
    double f1 = 1 / x1;
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    fail("Root not found");
  }

  /**
   * Test function f(x) = sqrt(x).
   * This function has no real roots for negative x's.
   */
  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionSquareRootX() throws Exception {
    SimpleFunction function = x -> Math.sqrt(x);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double f0 = Math.sqrt(x0);
    double x1 = 0.0;
    double f1 = Math.sqrt(x1);
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    fail("Root not found");
  }

  /**
   * Test function f(x) = sqrt(x) with a very wide interval.
   * This function has one real root for x > 0.
   */
  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionSquareRootX_WideInterval() throws Exception {
    SimpleFunction function = x -> Math.sqrt(x);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -100.0;
    double f0 = Math.sqrt(x0);
    double x1 = 100.0;
    double f1 = Math.sqrt(x1);
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    fail("Root not found");
  }

  /**
   * Test function f(x) = sqrt(x) with a very narrow interval.
   * This function has one real root for x > 0.
   */
  @Test
  public void testFindRoot_FunctionSquareRootX_NarrowInterval() throws Exception {
    SimpleFunction function = x -> Math.sqrt(x);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(xHigh - xLow) < 0.01) ? ConvergenceChecker.BOTH : ConvergenceChecker.NEITHER;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.01;
    double f0 = Math.sqrt(x0);
    double x1 = 0.02;
    double f1 = Math.sqrt(x1);
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertEquals("Root not found", Math.sqrt(0.015), solver.getRoot(), 0.01);
  }

}