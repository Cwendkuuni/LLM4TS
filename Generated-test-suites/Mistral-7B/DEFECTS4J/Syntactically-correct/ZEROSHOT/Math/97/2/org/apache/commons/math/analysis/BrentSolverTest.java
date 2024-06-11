import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.RootChecker;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    // Test function f(x) = x^3 - 2
    ComputableFunction function = FunctionFactory.createFunction(x -> Math.pow(x, 3) - 2);

    ConvergenceChecker checker = new RootChecker();

    BrentSolver solver = new BrentSolver();

    boolean rootFound = solver.findRoot(function, checker, 100, -2.0, 2.0, -1.0, 1.0);

    Assert.assertTrue(rootFound);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

    // Test function f(x) = sin(x)
    function = FunctionFactory.createFunction(Math::sin);

    checker = new RootChecker();

    solver = new BrentSolver();

    rootFound = solver.findRoot(function, checker, 100, -3.141592653589793, 3.141592653589793, -3.2, 3.2);

    Assert.assertTrue(rootFound);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

  }
}