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
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.LOW; // assuming all tests pass
      }
    };
    function = new FunctionsTest.SinFunction();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    ComputableFunction function = () -> { throw new FunctionException("Test Exception"); };

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootDivergence() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Double.NaN;
    ComputableFunction function = () -> { return Math.log(Math.abs(x0 - 1.0)); };

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot());
  }

}