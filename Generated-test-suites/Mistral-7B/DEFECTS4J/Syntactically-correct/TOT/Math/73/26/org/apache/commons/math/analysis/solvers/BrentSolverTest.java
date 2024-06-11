package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactories;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  @Test
  public void testFindRootPolynomialFunction() {
    ComputableFunction function = FunctionFactories.polynomialFunction(-1, 1, 4);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xNext, double fxNext) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double root = 1.0;
    double x0 = 0.0;
    double x1 = 2.0;
    double[] expectedRoots = {1.0};
    double[] expectedFValues = {4.0};
    try {
      assertTrue(solver.findRoot(function, checker, 50, x0, FunctionFactories.polynomialFunctionValueAt(function, x0), x1, FunctionFactories.polynomialFunctionValueAt(function, x1)));
      assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      // This should not happen as findRoot should return true if a root is found
      assertFalse(true);
    }
  }

  @Test
  public void testFindRootFunctionWithMultipleRoots() {
    ComputableFunction function = FunctionFactories.sinusFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xNext, double fxNext) {
        return ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double root = Math.PI / 2.0;
    double x0 = 0.0;
    double x1 = Math.PI / 2.0 + epsilon;
    double[] expectedRoots = {Math.PI / 2.0, -Math.PI / 2.0};
    double[] expectedFValues = {0.0, 0.0};
    try {
      for (int i = 0; i < 2; ++i) {
        assertTrue(solver.findRoot(function, checker, 50, x0, FunctionFactories.sinusFunctionValueAt(function, x0), x1, FunctionFactories.sinusFunctionValueAt(function, x1)));
        double currentRoot = solver.getRoot();
        assertEquals(expectedRoots[i % 2], currentRoot, epsilon);
        solver.reset();
        x1 = currentRoot + epsilon;
      }
    } catch (FunctionException e) {
      // This should not happen as findRoot should return true if a root is found
      assertFalse(true);
    }
  }

  @Test
  public void testFindRootFunctionWithNoRoots() {
    ComputableFunction function = FunctionFactories.polynomialFunction(-1, 1, 0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xNext, double fxNext) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 1.0;
    double x2 = 2.0;
    try {
      assertFalse(solver.findRoot(function, checker, 50, x0, FunctionFactories.polynomialFunctionValueAt(function, x0), x1, FunctionFactories.polynomialFunctionValueAt(function, x1)));
      assertFalse(solver.findRoot(function, checker, 50, x0, FunctionFactories.polynomialFunctionValueAt(function, x0), x2, FunctionFactories.polynomialFunctionValueAt(function, x2)));
      assertFalse(solver.findRoot(function, checker, 50, x1, FunctionFactories.polynomialFunctionValueAt(function, x1), x2, FunctionFactories.polynomialFunctionValueAt(function, x2)));
    } catch (FunctionException e) {
      // This should not happen
      assertFalse(true);
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("FunctionException for test purpose", x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xNext, double fxNext) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 1.0;
    try {
      solver.findRoot(function, checker, 50, x0, FunctionFactories.functionExceptionValueAt(function, x0), x1, FunctionFactories.functionExceptionValueAt(function, x1));
    } catch (FunctionException e) {
      // Expected
    }
  }

}