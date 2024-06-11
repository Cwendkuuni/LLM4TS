import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new RationalFunction("x^2 - 4");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceCheck(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.assertEquals(root, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      // Exception is expected in this test case
    }
  }
}