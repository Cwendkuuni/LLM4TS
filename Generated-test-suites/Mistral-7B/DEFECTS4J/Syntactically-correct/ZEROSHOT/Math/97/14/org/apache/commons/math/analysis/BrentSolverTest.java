import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {

      @Override
      public int converged(double x, double fx, double xtol) {
        return ConvergenceChecker.UNDETERMINED;
      }

      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double tolerance = 1e-12;
        return fLow * fHigh < 0 ? ConvergenceChecker.UNDETERMINED : (Math.abs(fLow) < tolerance || Math.abs(fHigh) < tolerance) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }

    };
  }

  @Test
  public void testFindRootSimple() {
    ComputableFunction function = FunctionFactory.newFunction("x * x - 2", x -> x * x - 2);
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.4142135625;

    boolean rootFound = solver.findRoot(function, checker, 10, x0, x0 * x0 - 2, x1, x1 * x1 - 2);
    assertTrue(rootFound);
    assertEquals(expectedRoot, solver.getRoot(), 1e-12);
  }

  @Test
  public void testFindRootComplex() {
    ComputableFunction function = FunctionFactory.newFunction("sin(x) / x", x -> Math.sin(x) / x);
    double x0 = 0.1;
    double x1 = 2.0;
    double expectedRoot = 1.2732053858;

    boolean rootFound = solver.findRoot(function, checker, 100, x0, Math.sin(x0) / x0, x1, Math.sin(x1) / x1);
    assertTrue(rootFound);
    assertEquals(expectedRoot, solver.getRoot(), 1e-10);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new RuntimeException();
      }
    };
    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);
  }

}