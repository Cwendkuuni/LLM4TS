package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.spaceroots.mantissa.functions.scalar.LinearFunction; // For the test function
import org.spaceroots.mantissa.roots.RootsFinder;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.ConvergenceChecker;
import org.spaceroots.mantissa.functions.LinearConvergenceChecker;

/**
 * This class implements test cases for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /** Test function to test {@link BrentSolver}. */
  private ComputableFunction function = new LinearFunction(1.0, 2.0);

  /** Convergence checker for testing. */
  private ConvergenceChecker checker = new LinearConvergenceChecker(0.01);

  /** Test for successful root finding with {@link BrentSolver}. */
  @Test
  public void testSuccessfulRootFinding() {
    // Given
    RootsFinder solver = new BrentSolver();

    // When
    double x0 = 1.5;
    double x1 = 2.5;
    boolean foundRoot = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // Then
    assertTrue(foundRoot);
    assertEquals(1.718281828, solver.getRoot(), 0.001);
  }

  /** Test for root finding failure with {@link BrentSolver}. */
  @Test
  public void testRootFindingFailure() {
    // Given
    RootsFinder solver = new BrentSolver();

    // When
    double x0 = 0.0;
    double x1 = 1.0;
    boolean foundRoot = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // Then
    assertFalse(foundRoot);
  }

  /** Test for root finding with extreme input values. */
  @Test
  public void testExtremeInputValues() {
    // Given
    RootsFinder solver = new BrentSolver();

    // When
    double x0 = Double.NEGATIVE_INFINITY;
    double x1 = Double.POSITIVE_INFINITY;
    boolean foundRoot = solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);

    // Then
    assertFalse(foundRoot);
  }

  /** Test for root finding with input values close to each other. */
  @Test
  public void testInputValuesCloseToEachOther() {
    // Given
    RootsFinder solver = new BrentSolver();

    // When
    double x0 = 0.0;
    double x1 = 0.0001;
    boolean foundRoot = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // Then
    assertFalse(foundRoot);
  }

  /** Test for root finding with input values providing the same function value. */
  @Test
  public void testInputValuesProvidingSameFunctionValue() {
    // Given
    ComputableFunction function2 = new LinearFunction(1.0, 0.0);
    RootsFinder solver = new BrentSolver();

    // When
    double x0 = 1.5;
    double x1 = 1.5;
    boolean foundRoot = solver.findRoot(function2, checker, 100, x0, function2.valueAt(x0), x1, function2.valueAt(x1));

    // Then
    assertFalse(foundRoot);
  }

  /** Test for root finding with a function throwing an exception. */
  @Test(expected = FunctionException.class)
  public void testFunctionException() throws FunctionException {
    // Given
    ComputableFunction functionWithException = mock(ComputableFunction.class);
    when(functionWithException.valueAt(ArgumentCaptor.capture(Double.class)))
        .thenThrow(new FunctionException("Test Exception"));
    RootsFinder solver = new BrentSolver();

    // When
    solver.findRoot(functionWithException, checker, 100, 0.0, 0.0, 0.0, 0.0);

  }

}