import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.BentFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.ConvergenceChecker;

/**
 * Test cases for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test finding root of a simple function.
   */
  @Test
  public void testFindRootSimpleFunction() throws Exception {
    // Given
    ComputableFunction function = new BentFunction(-3.0, 3.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 100, -2.0, -1.5, 0.0, 1.0);

    // Then
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.01);
  }

  /**
   * Test finding root of a function with two roots.
   */
  @Test
  public void testFindRootFunctionWithTwoRoots() throws Exception {
    // Given
    ComputableFunction function = new BentFunction(-3.0, 3.0, Math.sin);
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 100, -3.0, 3.0, 0.0, 1.0);

    // Then
    assertTrue(result);
    assertTrue(Math.abs(solver.getRoot() - 1.2732) < 0.01);
    assertTrue(Math.abs(Math.sin(solver.getRoot())) < 0.001);
    assertTrue(Math.abs(solver.getRoot() - (-1.2732)) < 0.01);
    assertTrue(Math.abs(Math.sin(solver.getRoot())) < 0.001);
  }

  /**
   * Test finding root of a function with no root.
   */
  @Test
  public void testFindRootFunctionWithNoRoot() throws Exception {
    // Given
    ComputableFunction function = new BentFunction(-3.0, 3.0, x -> Math.pow(x, 2) - 4);
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 100, -3.0, 3.0, 0.0, 1.0);

    // Then
    assertFalse(result);
  }

  /**
   * Test finding root of a function with an exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithException() throws Exception {
    // Given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Exception message");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    // When
    solver.findRoot(function, checker, 100, 0.0, 1.0, 0.0, 1.0);

    // Then
    // not reached
  }
}