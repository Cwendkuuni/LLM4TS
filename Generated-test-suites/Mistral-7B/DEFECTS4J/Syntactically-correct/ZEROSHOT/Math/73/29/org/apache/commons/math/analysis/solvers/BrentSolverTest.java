import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {

    // Test function f(x) = x^3 - 2
    ComputableFunction function = new FunctionUtils.PowerFunction(3.0);
    ConvergenceChecker checker = new ConvergenceChecker.Linear();

    BrentSolver brentSolver = new BrentSolver();
    double root = brentSolver.findRoot(function, checker, 100, -2.0, -1.0)
        ? brentSolver.getRoot()
        : Double.NaN;

    Assert.assertEquals(1.0, root, epsilon);

    // Test function f(x) = x^2 - 2
    function = new FunctionUtils.PowerFunction(2.0);
    checker = new ConvergenceChecker.Quadratic();

    brentSolver = new BrentSolver();
    root = brentSolver.findRoot(function, checker, 100, -2.0, -1.0)
        ? brentSolver.getRoot()
        : Double.NaN;

    Assert.assertEquals(-1.0, root, epsilon);

    // Test function f(x) = sin(x)
    function = new FunctionUtils.SineFunction();
    checker = new ConvergenceChecker.Quadratic();

    brentSolver = new BrentSolver();
    root = brentSolver.findRoot(function, checker, 100, Math.PI, 0.0)
        ? brentSolver.getRoot()
        : Double.NaN;

    Assert.assertEquals(Math.PI, root, epsilon);

    // Test function f(x) = exp(x) - 2
    function = new FunctionUtils.ExpFunction();
    checker = new ConvergenceChecker.Logarithmic();

    brentSolver = new BrentSolver();
    root = brentSolver.findRoot(function, checker, 100, 0.0, 1.0)
        ? brentSolver.getRoot()
        : Double.NaN;

    Assert.assertEquals(Math.log(2.0) / 1.0, root, epsilon);

  }

}