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
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // use a simple check for this test
        return Math.abs(xHigh - xLow) < 0.0001 ? ConvergenceChecker.LOW : ConvergenceChecker.UNDECIDED;
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 2;
      }
    };
  }

  @Test
  public void testFindRoot_SimpleCase() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;
    double root = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(1.5, root, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionThrowsException() throws FunctionException {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function exception");
      }
    };
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;
    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_EdgeCase_ZeroInterval() throws FunctionException {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    double x0 = 0.0;
    double x1 = 0.0;
    double root = Double.NaN;
    int maxIter = 100;
    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(false, result);
    assertEquals(root, solver.getRoot());
  }

  @Test
  public void testFindRoot_ErrorCase_RootOutsideInterval() throws FunctionException {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    double x0 = 1.0;
    double x1 = 0.5;
    double root = -0.5;
    int maxIter = 100;
    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(false, result);
    assertEquals(root, solver.getRoot());
  }

  @Test
  public void testFindRoot_ErrorCase_RootInsideInterval() throws FunctionException {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.0;
    int maxIter = 100;
    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot());
  }

}