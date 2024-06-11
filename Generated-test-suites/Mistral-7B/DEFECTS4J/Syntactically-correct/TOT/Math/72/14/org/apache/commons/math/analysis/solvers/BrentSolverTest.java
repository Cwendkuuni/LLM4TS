package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.SinFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Unit tests for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test finding a root using the Brent solver for a function known to have a root.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = Math.PI / 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with no roots.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testNoRoots() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNull(solver.getRoot());
  }

  /**
   * Test finding a root using the Brent solver for a function with multiple roots.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testMultipleRoots() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 2.0 * Math.PI;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(Math.PI, solver.getRoot(), 1e-6);

    solver.reset();

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(-Math.PI, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root at the edge of the interval.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootAtEdge() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = 1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root at a boundary.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootAtBoundary() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 0.0;
    double root = -1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a large interval.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testLargeInterval() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(-x) - 0.1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = -5.0;
    double x1 = 5.0;
    double root = FunctionConstants.E / 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a large root.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testLargeRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(x) - 10.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = -5.0;
    double x1 = 5.0;
    double root = 5.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root very close to a boundary.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootNearBoundary() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 0.0;
    double root = -0.1;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root very close to another root.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootsCloseTogether() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 0.5;
    double root1 = 0.2;
    double root2 = 0.4;

    // find root1
    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root1, solver.getRoot(), 1e-6);

    // reset the solver
    solver.reset();

    // find root2
    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root2, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root at an endpoint of the interval.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootAtEndpoint() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root at a midpoint of the interval.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootAtMidpoint() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.25;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root very close to a boundary and a large interval.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootAtEdgeWithLargeInterval() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 2.0;
    double root = -0.1;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root very close to another root and a large interval.
   * @throws Exception if an error occurs during testing
   */
  @Test
  public void testRootsCloseTogetherWithLargeInterval() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root1 = 0.2;
    double root2 = 0.4;

    // find root1
    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root1, solver.getRoot(), 1e-6);

    // reset the solver
    solver.reset();

    // find root2
    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotNull(solver.getRoot());
    Assert.assertEquals(root2, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root using the Brent solver for a function with a root that is not found within the interval.
   * @throws Exception if an error occurs during testing
   */
  @Test(expected = FunctionException.class)
  public void testNoRootInInterval() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(-x) - 0.1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.AbsoluteError(1e-6);
    BrentSolver solver = new BrentSolver();

    double x0 = -5.0;
    double x1 = 0.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }
}