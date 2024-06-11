import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private ComputableFunction function;
  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
      }
    };
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.UNDECIDED;
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
    double x0 = 1.0;
    double x1 = 2.0;
    boolean foundRoot = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, foundRoot);
    assertEquals(1.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    ConvergenceChecker checkerException = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        throw new RuntimeException("Convergence checker exception");
      }
    };
    solver.findRoot(function, checkerException, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}