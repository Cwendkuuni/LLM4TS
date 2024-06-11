import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private MockFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fx2) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new MockFunction();
  }

  @Test
  public void testFindRootSimple() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootComplex() throws Exception {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    function = new MockFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        throw new FunctionException("Test exception");
      }
    };

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}