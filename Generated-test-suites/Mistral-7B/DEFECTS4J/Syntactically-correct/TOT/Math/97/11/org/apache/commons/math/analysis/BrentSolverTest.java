package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for {@link BrentSolver} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class BrentSolverTest {

  @Mock
  private ComputableFunction function;

  @Mock
  private ConvergenceChecker checker;

  @InjectMocks
  private BrentSolver solver;

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that returns 0 at the root.
   */
  @Test
  public void testFindRootWithFunctionReturningZeroAtRoot() throws FunctionException {
    // given : a function that returns 0 at the root and a checker that always returns Converged.LOW
    Mockito.when(function.valueAt(Mockito.anyDouble()))
        .thenReturn(0.0);
    Mockito.when(checker.converged(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble(),
        ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
        .thenReturn(ConvergenceChecker.LOW);

    // when : find a root in the interval [-1, 1]
    double x0 = -1.0;
    double f0 = 0.0;
    double x1 = 1.0;
    double f1 = 0.0;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    // then : the root has been found
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has a root in the interval and a checker that never converges.
   */
  @Test
  public void testFindRootWithFunctionNotConvergingChecker() throws FunctionException {
    // given : a function with a root in the interval [-1, 1] and a checker that never converges
    Mockito.when(function.valueAt(Mockito.anyDouble()))
        .then((invocationOnMock) -> Math.sin(invocationOnMock.getArgument(0)));
    Mockito.when(checker.converged(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble(),
        ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
        .thenReturn(ConvergenceChecker.NOT_CONVERGED);

    // when : find a root in the interval [-1, 1]
    double x0 = -1.0;
    double f0 = Math.sin(x0);
    double x1 = 1.0;
    double f1 = Math.sin(x1);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    // then : the method has not found a root
    assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has no root in the interval and a checker that always converges.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionNotHavingARootInInterval() throws FunctionException {
    // given : a function that has no root in the interval [-1, 1] and a checker that always converges
    Mockito.when(function.valueAt(Mockito.anyDouble()))
        .then((invocationOnMock) -> Math.pow(invocationOnMock.getArgument(0), 2) - 1.0);
    Mockito.when(checker.converged(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble(),
        ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
        .thenReturn(ConvergenceChecker.CONVERGED);

    // when : find a root in the interval [-1, 1]
    double x0 = -1.0;
    double f0 = Math.pow(x0, 2) - 1.0;
    double x1 = 1.0;
    double f1 = Math.pow(x1, 2) - 1.0;
    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    // then : an exception should be thrown
  }
}