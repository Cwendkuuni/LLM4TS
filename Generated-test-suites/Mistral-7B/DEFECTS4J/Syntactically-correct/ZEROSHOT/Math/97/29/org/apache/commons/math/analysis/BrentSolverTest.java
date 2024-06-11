import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.RootsChecker;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    // Test function f(x) = x^3 - 4
    ComputableFunction function = FunctionFactory.newFunction("f", x -> Math.pow(x, 3) - 4);
    ConvergenceChecker checker = new RootsChecker(1e-12);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertNotNull(root);
    Assert.assertEquals(1.666666672336986, root, 1e-13);

    // Test function f(x) = sin(x) - x
    function = FunctionFactory.newFunction("f", x -> Math.sin(x) - x);
    checker = new RootsChecker(1e-12);
    solver = new BrentSolver();

    x0 = 0.0;
    x1 = 1.0;
    root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertNotNull(root);
    Assert.assertEquals(0.8414709848078965, root, 1e-13);

    // Test function f(x) = exp(x) - x - 1
    function = FunctionFactory.newFunction("f", x -> Math.exp(x) - x - 1);
    checker = new RootsChecker(1e-12);
    solver = new BrentSolver();

    x0 = -2.0;
    x1 = 0.0;
    root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertNotNull(root);
    Assert.assertEquals(-0.567143290406358, root, 1e-13);

    // Test function f(x) = exp(x) - x - 1 with initial bounds reversed
    function = FunctionFactory.newFunction("f", x -> Math.exp(x) - x - 1);
    checker = new RootsChecker(1e-12);
    solver = new BrentSolver();

    x0 = 0.0;
    x1 = -2.0;
    root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertNotNull(root);
    Assert.assertEquals(-0.567143290406358, root, 1e-13);

    // Test function f(x) = sin(x) - x with incorrect bounds
    function = FunctionFactory.newFunction("f", x -> Math.sin(x) - x);
    checker = new RootsChecker(1e-12);
    solver = new BrentSolver();

    x0 = 0.0;
    x1 = 1.0;
    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      Assert.assertEquals("Roots are not bracketed.", e.getMessage());
    }
  }

}