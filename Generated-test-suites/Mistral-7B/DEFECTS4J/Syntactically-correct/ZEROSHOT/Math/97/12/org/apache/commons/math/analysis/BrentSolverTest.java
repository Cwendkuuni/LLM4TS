import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;
  private RationalFunction function;

  @Before
  public void setup() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < Math.abs(f2)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f1) > Math.abs(f2)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NEITHER;
        }
      }
    };
    function = new RationalFunction("x^2 - 4");
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;

    boolean rootFound = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(2.0, brentSolver.getRoot(), 0.001);
    assertEquals(true, rootFound);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;

    RationalFunction function = new RationalFunction("x^2 - 1");
    brentSolver.findRoot(function, convergenceChecker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullFunction() {
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      // empty implementation for testing purpose
    };

    brentSolver.findRoot(null, convergenceChecker, maxIter, x0, 0.0, x1, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullChecker() {
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;

    BrentSolver brentSolver = new BrentSolver();
    RationalFunction function = new RationalFunction("x^2 - 4");

    brentSolver.findRoot(function, null, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNegativeMaxIter() {
    double x0 = 1.0;
    double x1 = 3.0;

    BrentSolver brentSolver = new BrentSolver();
    RationalFunction function = new RationalFunction("x^2 - 4");
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      // empty implementation for testing purpose
    };

    brentSolver.findRoot(function, convergenceChecker, -1, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testGetRoot() {
    double x0 = 1.0;
    double x1 = 3.0;
    try {
      brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      // ignored
    }

    assertEquals(2.0, brentSolver.getRoot(), 0.001);
  }

}