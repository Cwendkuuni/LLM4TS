import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionFactory functionFactory;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fLow) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    functionFactory = new FunctionFactory();
  }

  @Test
  public void testFindRootSimple() {
    ComputableFunction function = functionFactory.createFunction(Math::pow, new double[]{3.0, 4.0});
    double x0 = 1.0;
    double x1 = 2.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(1.73205080757, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootMultipleRoots() {
    ComputableFunction function = functionFactory.createFunction(Math::sin, new double[]{0.0, 3.0});
    double x0 = -2.0;
    double x1 = 2.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(0.0, solver.getRoot(), 0.001);
    assertEquals(Math.PI, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootFail() {
    ComputableFunction function = functionFactory.createFunction(x -> x * x, new double[]{0.0, 0.0});
    boolean result = solver.findRoot(function, checker, 100, 0.0, 0.0, 0.0, 0.0);
    assertEquals(false, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    ComputableFunction function = functionFactory.createFunction(Math::log, new double[]{0.0, Double.NEGATIVE_INFINITY});
    boolean result = solver.findRoot(function, checker, 100, 1.0, Math.log(1.0), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
  }

}