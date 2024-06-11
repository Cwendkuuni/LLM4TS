import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction func = new SimpleFunction("x^2 - 2.0");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xprev, double fprev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(func, checker, 10, x0, func.valueAt(x0), x1, func.valueAt(x1));

    assert(root > 1.5 && root < 2.5);
  }
}