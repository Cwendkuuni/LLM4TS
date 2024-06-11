import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SineFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(xPrev - x) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    function = new SineFunction();
  }

  @Test
  public void testFindRoot_simpleCase() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootInsideInterval() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootBetweenTwoIterations() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.sqrt(2.0) / 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_functionThrowsException() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    SineFunction brokenFunction = new SineFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function error");
      }
    };

    assertThrows(FunctionException.class, () -> solver.findRoot(brokenFunction, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertNotNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_maxIterationsReached() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.PI;

    ConvergenceChecker maxIterChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    solver.findRoot(function, maxIterChecker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertFalse(solver.getRoot() != Double.NaN);
  }

  @Test
  public void testFindRoot_rootOutsideInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = -1.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertFalse(solver.getRoot() != Double.NaN);
  }
}