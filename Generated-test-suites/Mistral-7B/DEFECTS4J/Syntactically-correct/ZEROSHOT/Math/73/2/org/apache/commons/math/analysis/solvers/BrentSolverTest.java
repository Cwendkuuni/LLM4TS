import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionException;
import org.spaceroots.mantissa.functions.scalar.PolynomialFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = mock(ConvergenceChecker.class);
    function = new PolynomialFunction(x -> x * x - 4.0);
  }

  @Test
  public void testFindRoot_SimpleCase() throws FunctionException {
    when(checker.converged(0.0, 16.0, 2.0, -8.0)).thenReturn(ConvergenceChecker.LOW);
    boolean result = solver.findRoot(function, checker, 10, 0.0, 0.0, 2.0, 16.0);
    assertEquals(2.0, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test
  public void testFindRoot_NoRootsFound() throws FunctionException {
    when(checker.converged(0.0, 1.0, 2.0, 0.1)).thenReturn(ConvergenceChecker.NONE);
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 10, 0.0, 0.0, 2.0, 1.0));
    assertEquals(Double.NaN, solver.getRoot());
    assertEquals(false, solver.isRootFound());
  }

  @Test
  public void testFindRoot_FunctionException() throws FunctionException {
    when(checker.converged(0.0, 1.0, 2.0, 0.1)).thenThrow(new FunctionException("Test FunctionException"));
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 10, 0.0, 0.0, 2.0, 1.0));
    assertEquals(Double.NaN, solver.getRoot());
    assertEquals(false, solver.isRootFound());
  }

  @Test
  public void testFindRoot_MaxIterationsExceeded() throws FunctionException {
    when(checker.converged(0.0, 1.0, 2.0, 0.1)).thenReturn(ConvergenceChecker.NONE);
    assertFalse(solver.findRoot(function, checker, 0, 0.0, 0.0, 2.0, 1.0));
    assertEquals(Double.NaN, solver.getRoot());
    assertEquals(false, solver.isRootFound());
  }

  @Test
  public void testGetRoot() {
    assertEquals(Double.NaN, solver.getRoot());
  }

  @Test
  public void testIsRootFound() {
    assertEquals(false, solver.isRootFound());
  }

}