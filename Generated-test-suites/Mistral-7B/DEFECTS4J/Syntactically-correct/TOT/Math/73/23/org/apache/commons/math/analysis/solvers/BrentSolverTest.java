import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionsTest;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int convergence(double x1, double f1, double x2, double f2) {
        if (Math.abs(x1 - x2) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f1 - f2) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDEFINED;
        }
      }
    };
    function = FunctionsTest.sinFunction;
  }

  @Test
  public void testFindRootInSimpleInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = Math.PI / 2.0; // Math.PI is a known root of sin function
    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.001);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootInImproperInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0; // The interval is improper, x1 is a root of sin function
    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootInEdgeInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0; // sin(0) = 0
    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.001);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootInLargeInterval() throws FunctionException {
    double x0 = -1000.0;
    double x1 = 1000.0;
    double root = Math.PI;
    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.01);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootInNonConvergentInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = Math.sqrt(2.0);
    boolean result = solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - root, 2) - 0.25;
      }
    }, checker, 50, x0, Math.pow(x0 - root, 2) - 0.25, x1, Math.pow(x1 - root, 2) - 0.25);
    assertEquals(root, solver.getRoot(), 0.01);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootInNonConvergentIntervalWithFunctionException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 1.5) {
          throw new FunctionException("Function is undefined in this interval.");
        }
        return Math.pow(x - 2.0, 2) - 0.25;
      }
    };
    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(false, result);
  }
}