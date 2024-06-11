import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.RootsChecker;
import org.spaceroots.mantissa.roots.BrentSolver;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {
    // ---------------------------------------------------------------------
    // Test function f(x) = x^3 - 2
    // ---------------------------------------------------------------------
    ComputableFunction function = new FunctionUtils.CubicFunction("-2.0");
    ConvergenceChecker checker = new RootsChecker.Default();

    BrentSolver solver = new BrentSolver();
    double x0 = -1.5;
    double x1 = 1.5;
    int maxIter = 10;

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertFalse(Double.isNaN(solver.getRoot()));
    Assert.assertEquals(-1.0, solver.getRoot(), 0.001);

    // ---------------------------------------------------------------------
    // Test function f(x) = sqrt(abs(x)) - x/2
    // ---------------------------------------------------------------------
    ComputableFunction functionSqrt = new FunctionUtils.SqrtAbsFunction();

    solver = new BrentSolver();
    double x0Sqrt = -1.5;
    double x1Sqrt = 1.5;
    int maxIterSqrt = 10;

    solver.findRoot(functionSqrt, checker, maxIterSqrt, x0Sqrt, functionSqrt.valueAt(x0Sqrt), x1Sqrt, functionSqrt.valueAt(x1Sqrt));

    Assert.assertFalse(Double.isNaN(solver.getRoot()));
    Assert.assertEquals(0.0, solver.getRoot(), 0.001);

    // ---------------------------------------------------------------------
    // Test function f(x) = sin(x)
    // ---------------------------------------------------------------------
    ComputableFunction functionSin = new FunctionUtils.SineFunction();

    solver = new BrentSolver();
    double x0Sin = -1.57;
    double x1Sin = 1.57;
    int maxIterSin = 10;

    solver.findRoot(functionSin, checker, maxIterSin, x0Sin, functionSin.valueAt(x0Sin), x1Sin, functionSin.valueAt(x1Sin));

    Assert.assertFalse(Double.isNaN(solver.getRoot()));
    Assert.assertEquals(0.0, solver.getRoot(), 0.001);

    // ---------------------------------------------------------------------
    // Test function f(x) = exp(x)
    // ---------------------------------------------------------------------
    ComputableFunction functionExp = new FunctionUtils.ExponentialFunction();

    solver = new BrentSolver();
    double x0Exp = -2.0;
    double x1Exp = -1.0;
    int maxIterExp = 10;

    solver.findRoot(functionExp, checker, maxIterExp, x0Exp, functionExp.valueAt(x0Exp), x1Exp, functionExp.valueAt(x1Exp));

    Assert.assertFalse(Double.isNaN(solver.getRoot()));
    Assert.assertEquals(-1.0, solver.getRoot(), 0.001);

  }

}