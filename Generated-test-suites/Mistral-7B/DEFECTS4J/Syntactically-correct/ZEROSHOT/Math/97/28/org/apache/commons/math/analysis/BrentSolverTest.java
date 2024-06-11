import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.NOT_YET;
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
  }

  @Test
  public void testFindRoot() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, rootFound);
    assertEquals(-1.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootDivergent() throws Exception {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;

    boolean rootFound = solver.findRoot(new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return 1.0 / x;
      }
    }, checker, maxIter, x0, Double.POSITIVE_INFINITY, x1, Double.NEGATIVE_INFINITY);
    assertEquals(false, rootFound);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;

    ConvergenceChecker checkerException = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        throw new RuntimeException();
      }
    };

    solver.findRoot(function, checkerException, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}