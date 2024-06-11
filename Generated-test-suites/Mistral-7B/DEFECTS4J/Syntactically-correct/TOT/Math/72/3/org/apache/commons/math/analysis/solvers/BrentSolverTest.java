package org.spaceroots.mantissa.roots;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test method for the findRoot method with a simple function, a small interval, and a large number of iterations.
   */
  @Test
  public void testFindRootSimpleFunction() {
    double functionValue;
    double x = 1.0;
    double expectedRoot = Math.PI / 2.0;

    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, x - 1.0, function.valueAt(x), x + 1.0, function.valueAt(x + 1.0));

    Assert.assertTrue(result);
    Assert.assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test method for the findRoot method with a simple function, an edge interval, and a large number of iterations.
   */
  @Test
  public void testFindRootEdgeIntervalSimpleFunction() {
    double functionValue;
    double x = 0.0;
    double expectedRoot = 0.0;

    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, x - 1.0, function.valueAt(x), x + 1.0, function.valueAt(x + 1.0));

    Assert.assertTrue(result);
    Assert.assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test method for the findRoot method with a complex function, a small interval, and a moderate number of iterations.
   */
  @Test
  public void testFindRootComplexFunctionSmallInterval() {
    double functionValue;
    double x = 1.0;
    double expectedRoot = Math.log(2.0);

    ComputableFunction function = new FunctionUtils.ExpFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 50, x - 0.1, function.valueAt(x), x + 0.1, function.valueAt(x + 1.0));

    Assert.assertTrue(result);
    Assert.assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test method for the findRoot method with a complex function, an edge interval, and a moderate number of iterations.
   */
  @Test
  public void testFindRootComplexFunctionEdgeInterval() {
    double functionValue;
    double x = 0.0;
    double expectedRoot = 0.0;

    ComputableFunction function = new FunctionUtils.ExpFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 50, x - 1.0, function.valueAt(x), x + 1.0, function.valueAt(x + 1.0));

    Assert.assertTrue(result);
    Assert.assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test method for the findRoot method with a function that has no roots in the given interval.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoots() {
    double functionValue;
    double x = 1.0;
    double x0 = 0.0;
    double x1 = 2.0;

    ComputableFunction function = new FunctionUtils.ExpFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertFalse(result);
  }
}