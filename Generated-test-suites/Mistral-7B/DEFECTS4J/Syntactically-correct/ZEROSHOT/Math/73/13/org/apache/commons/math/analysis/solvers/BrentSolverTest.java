import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xMin, double fMin) {
        if (Math.abs(f) < 1e-9) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    function = new FunctionUtils.PolynomialFunction(-1, 4);
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
    function = null;
  }

  @Test
  public void testBrentSolverSuccess() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-9);
  }

  @Test
  public void testBrentSolverFailure() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(result);
    assertNotNull(solver.getRoot());
  }

}