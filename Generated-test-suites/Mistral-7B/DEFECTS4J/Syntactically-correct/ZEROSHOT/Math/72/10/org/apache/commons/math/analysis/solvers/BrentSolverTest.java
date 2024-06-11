import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.functions.SinusFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private double epsilon = 1e-10;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testSolveX2() throws Exception {
    ComputableFunction function = new SinusFunction();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    double root = solver.getRoot();
    double expectedRoot = Math.PI;
    assertArrayEquals(new double[]{expectedRoot}, new double[]{root}, 0.001);
  }

  @Test
  public void testSolveX3() throws Exception {
    ComputableFunction function = new SinusFunction();
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    double root = solver.getRoot();
    double expectedRoot = Math.PI;
    assertArrayEquals(new double[]{expectedRoot}, new double[]{root}, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testSolveX4() throws Exception {
    ComputableFunction function = new SinusFunction();
    double x0 = 0.0;
    double f0 = Double.NaN;
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    double root = solver.getRoot();
    double expectedRoot = Math.PI;
    assertArrayEquals(new double[]{expectedRoot}, new double[]{root}, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testSolveX5() throws Exception {
    ComputableFunction function = new SinusFunction();
    double x0 = 0.0;
    double f0 = 0.0;
    double x1 = 1.0;
    double f1 = Double.NaN;

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    double root = solver.getRoot();
    double expectedRoot = Math.PI;
    assertArrayEquals(new double[]{expectedRoot}, new double[]{root}, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testSolveX6() throws Exception {
    ComputableFunction function = new SinusFunction();
    double x0 = 1.0;
    double f0 = Double.NaN;
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    double root = solver.getRoot();
    double expectedRoot = Math.PI;
    assertArrayEquals(new double[]{expectedRoot}, new double[]{root}, 0.001);
  }
}