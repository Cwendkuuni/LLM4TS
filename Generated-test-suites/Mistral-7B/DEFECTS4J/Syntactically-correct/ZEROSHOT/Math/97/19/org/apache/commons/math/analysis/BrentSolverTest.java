import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        if (Math.abs(fx1) < 0.001 && Math.abs(fx2) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new RationalFunction("x^3 - 2*x^2 + 2*x - 1");
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();
    assertEquals(-0.3225787674396408, root, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    RationalFunction functionException = new RationalFunction("x^2 - 2");
    solver.findRoot(functionException, checker, 50, x0, functionException.valueAt(x0), x1, functionException.valueAt(x1));
  }

}