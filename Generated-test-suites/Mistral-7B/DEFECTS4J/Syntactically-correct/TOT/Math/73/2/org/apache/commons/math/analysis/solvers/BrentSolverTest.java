package org.spaceroots.mantissa.roots.test;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.CubicFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private RootsFinder brentSolver;
  private ConvergenceChecker convergenceChecker;

  @Before
  public void setup() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int convergence(double x1, double fx1, double x2, double fx2) {
        if (Math.abs(fx1) < Math.abs(fx2)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < Double.MIN_VALUE) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testFindRoot_FunctionWithOneRootInInterval_Success() throws FunctionException {
    ComputableFunction function = new CubicFunction(0.0);
    double x0 = -2.0;
    double x1 = 2.0;

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithOneRootOutsideInterval_Failure() {
    ComputableFunction function = new CubicFunction(0.0);
    double x0 = -5.0;
    double x1 = -3.0;

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRoot_FunctionWithOneRootAtEndpoint_Success() throws FunctionException {
    ComputableFunction function = new CubicFunction(0.0);
    double x0 = 0.0;
    double x1 = 0.0;

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots_Failure() throws FunctionException {
    ComputableFunction function = new CubicFunction(-1.0);
    double x0 = -3.0;
    double x1 = 2.0;

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRoot_FunctionWithDivergingRoot_Failure() throws FunctionException {
    ComputableFunction function = new CubicFunction(FunctionUtils.SQUARE);
    double x0 = 0.0;
    double x1 = 1.0;

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRoot_FunctionWithNanValues_Failure() throws FunctionException {
    ComputableFunction function = new CubicFunction(FunctionUtils.SIN);
    double x0 = 0.0;
    double x1 = 0.0;

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, Double.NaN, x1, Double.NaN));
  }

  @Test
  public void testFindRoot_FunctionWithRootsTooClose_Failure() throws FunctionException {
    ComputableFunction function = new CubicFunction(-1.0);
    double x0 = -1.0;
    double x1 = -1.01;

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }
}