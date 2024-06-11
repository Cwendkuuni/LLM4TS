import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;
  private SimpleFunction simpleFunction;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    simpleFunction = new SimpleFunction();
  }

  /**
   * Test finding root in interval when function is monotonous.
   */
  @Test
  public void testFindRootMonotonousFunction() {
    try {
      boolean rootFound = brentSolver.findRoot(simpleFunction, convergenceChecker, 100, 1.0, 2.0);
      assertTrue(rootFound);
      double root = brentSolver.getRoot();
      assertEquals(1.449, root, 0.001);
    } catch (FunctionException e) {
      fail("Should not throw FunctionException");
    }
  }

  /**
   * Test finding root in interval when function is not monotonous.
   */
  @Test
  public void testFindRootNonMonotonousFunction() {
    try {
      SimpleFunction nonMonotonousFunction = new NonMonotonousFunction();
      boolean rootFound = brentSolver.findRoot(nonMonotonousFunction, convergenceChecker, 100, 1.0, 2.0);
      assertFalse(rootFound);
    } catch (FunctionException e) {
      // Expected, since the test function is not defined in the given interval
    }
  }

  /**
   * Test error handling when interval does not contain root.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRootInInterval() {
    try {
      SimpleFunction nonRootFunction = new NonRootFunction();
      brentSolver.findRoot(nonRootFunction, convergenceChecker, 100, 1.0, 2.0);
    } catch (FunctionException e) {
      // Expected
    }
  }

  /**
   * Simple function for testing.
   */
  private static class SimpleFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return Math.pow(x, 2) - 2.0;
    }
  }

  /**
   * Non-monotonous function for testing.
   */
  private static class NonMonotonousFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      if (x < 1.2) {
        return Math.sin(x) * Math.pow(x, 2) - 1.5;
      } else {
        return Math.cos(x) * Math.pow(x, 2) + 1.5;
      }
    }
  }

  /**
   * Function with no root in the given interval.
   */
  private static class NonRootFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return Math.pow(x, 2) - 3.0;
    }
  }
}