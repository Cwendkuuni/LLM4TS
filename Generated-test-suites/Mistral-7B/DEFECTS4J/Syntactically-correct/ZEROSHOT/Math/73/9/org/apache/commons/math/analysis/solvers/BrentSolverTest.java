import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionFactory functionFactory;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        if (Math.abs(fx) < 0.001d) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };
    functionFactory = new FunctionFactory();
  }

  @Test
  public void testSimpleRoot() throws Exception {
    ComputableFunction function = functionFactory.sin();
    double x0 = 1.0;
    double x1 = 0.0;
    double root = 0.0;
    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testComplexRoot() throws Exception {
    ComputableFunction function = functionFactory.sinxSqrt();
    double x0 = 1.5;
    double x1 = 2.0;
    double root = 1.5708;
    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.01);
  }

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws Exception {
    ComputableFunction function = functionFactory.divideByZero();
    double x0 = 1.0;
    double x1 = 1.0;
    double root = 0.0;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testMaxIterations() throws Exception {
    ComputableFunction function = functionFactory.sinxSqrt();
    double x0 = 1.5;
    double x1 = 2.0;
    double root = 1.5708;
    solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(Double.isFinite(solver.getRoot()));
  }

}