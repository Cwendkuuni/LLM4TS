import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.Functions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

/**
 * Tests for the BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < Double.MIN_VALUE && Math.abs(f2) < Double.MIN_VALUE) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = Functions.sin();
  }

  /**
   * Test finding a root in an interval with known root.
   */
  @Test
  public void testFindRootInIntervalWithKnownRoot() {
    // given
    double x0 = 0.0;
    double x1 = Math.PI / 2;
    double expectedRoot = Math.PI / 2;

    // when
    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // then
    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test finding a root in an interval with no root.
   */
  @Test
  public void testFindRootInIntervalWithNoRoot() {
    // given
    double x0 = 0.0;
    double x1 = 1.0;

    // when
    boolean result = solver.findRoot(Functions.constant(1.0), checker, 10, x0, FunctionValues.ONE, x1, FunctionValues.ONE);

    // then
    assertFalse(result);
  }

  /**
   * Test finding a root in an interval with root at boundary.
   */
  @Test
  public void testFindRootInIntervalWithRootAtBoundary() {
    // given
    double x0 = 0.0;
    double x1 = 0.0;
    double expectedRoot = 0.0;

    // when
    boolean result = solver.findRoot(Functions.constant(0.0), checker, 10, x0, FunctionValues.ZERO, x1, FunctionValues.ZERO);

    // then
    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test finding a root in an interval with large interval size.
   */
  @Test
  public void testFindRootInIntervalWithLargeIntervalSize() {
    // given
    double x0 = -1000.0;
    double x1 = 1000.0;
    double expectedRoot = 0.0;

    // when
    boolean result = solver.findRoot(Functions.sqrt(), checker, 10, x0, FunctionValues.NEGATIVE_ONE_HUNDRED, x1, FunctionValues.ONE_HUNDRED);

    // then
    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test finding a root in an interval with function throwing an exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootInIntervalWithFunctionException() {
    // given
    ComputableFunction function = () -> { throw new FunctionException("Test exception"); };

    // when
    solver.findRoot(function, checker, 10, 0.0, Double.NaN, 0.0, Double.NaN);

    // then (exception should be thrown)
  }
}