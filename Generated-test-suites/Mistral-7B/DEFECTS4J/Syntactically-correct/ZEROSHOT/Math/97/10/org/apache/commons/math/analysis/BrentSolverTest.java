import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

/**
 * JUnit 4 test file for the BrentSolver class.
 */
public class BrentSolverTest {

  private static final double EPSILON = 1e-9;
  private static final double MAX_ITERATIONS = 100;

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = x -> Math.pow(x, 3) - 2;
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xi, double fi) {
        if (Math.abs(fx) < EPSILON && Math.abs(fi) < EPSILON) {
          return ConvergenceChecker.Both;
        }
        return ConvergenceChecker.None;
      }
    };
  }

  @After
  public void tearDown() {
    solver = null;
    function = null;
    checker = null;
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = -2.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, MAX_ITERATIONS, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(-1.0, solver.getRoot(), EPSILON);
  }

}