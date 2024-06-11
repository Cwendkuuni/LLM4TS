package org.spaceroots.mantissa.roots.test;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.ConvergenceException;

public class BrentSolverTest {

  @Test
  public void testBrentSolverConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRootWithValidInput() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    // Given
    when(function.valueAt(anyDouble())).thenAnswer(invocation -> invocation.getArguments()[0] / 2.0);
    when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(ConvergenceChecker.NONE);

    // When
    boolean result = solver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 3.0);

    // Then
    assertTrue(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionException() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    // Given
    doThrow(new FunctionException("FunctionException message")).when(function).valueAt(anyDouble());

    // When & Then
    solver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 3.0);
  }

  @Test
  public void testFindRootWithConvergenceCheckerException() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    // Given
    when(function.valueAt(anyDouble())).thenAnswer(invocation -> invocation.getArguments()[0] / 2.0);
    when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenThrow(new ConvergenceException("ConvergenceException message"));

    // When & Then
    solver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 3.0);
  }

  @Test
  public void testFindRootWithMaxIterationReached() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    // Given
    when(function.valueAt(anyDouble())).thenAnswer(invocation -> invocation.getArguments()[0] / 2.0);
    doReturn(ConvergenceChecker.MAX_ITERATION).when(checker).converged(anyDouble(), anyDouble(), anyDouble(), anyDouble());

    // When
    boolean result = solver.findRoot(function, checker, 1, 1.0, 2.0, 3.0, 3.0);

    // Then
    assertFalse(result);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(mock(ComputableFunction.class), mock(ConvergenceChecker.class), 10, 1.0, 2.0, 3.0, 3.0);

    // Given
    double expectedRoot = solver.getRoot();

    // Then
    assertEquals(expectedRoot, 2.0, 0.001); // Adjust the expected root value based on the test case used in findRoot test.
  }

  // Add more test cases as needed.
}