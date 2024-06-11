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
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-10 && Math.abs(f2) < 1e-10) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    functionFactory = new FunctionFactory();
  }

  @Test
  public void testSimpleRoot() throws Exception {
    ComputableFunction function = functionFactory.createFunction(x -> x * x - 2.0);
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;
    assertEquals(-1.0, root, 1e-6);
  }

  @Test
  public void testRootsAtZero() throws Exception {
    ComputableFunction function = functionFactory.createFunction(x -> Math.sin(x) - x);
    double x0 = 0.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;
    assertEquals(0.0, root, 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Test function exception.");
      }
    };
    solver.findRoot(function, checker, 50, 0.0, 0.0, 1.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFunction() throws Exception {
    ComputableFunction function = null;
    solver.findRoot(function, checker, 50, 0.0, 0.0, 1.0, 0.0);
  }

}