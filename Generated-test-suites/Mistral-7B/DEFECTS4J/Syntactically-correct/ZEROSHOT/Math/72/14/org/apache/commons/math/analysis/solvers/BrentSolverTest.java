import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} .
   */
  @Test
  public void testFindRoot() {
    // Test function f(x) = x^3 - 2
    ComputableFunction function = new FunctionUtils.PowerFunction(3.0);
    ConvergenceChecker checker = new ConvergenceChecker.AbsDiff();
    BrentSolver solver = new BrentSolver();

    // Test root at x = -1.5
    double x0 = -1.5;
    double x1 = -1.0;
    boolean expectedResult = true;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      double root = solver.getRoot();
      Assert.assertNotEquals(Double.NaN, root);
      Assert.assertEquals(-1.28866251, root, 0.00001);
      Assert.assertTrue(expectedResult);
    } catch (FunctionException e) {
      Assert.fail("Exception should not be thrown: " + e.getMessage());
    }

    // Test root at x = 1.5
    solver.reset();
    x0 = 1.5;
    x1 = 2.0;
    expectedResult = true;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      root = solver.getRoot();
      Assert.assertEquals(1.4622133, root, 0.00001);
      Assert.assertTrue(expectedResult);
    } catch (FunctionException e) {
      Assert.fail("Exception should not be thrown: " + e.getMessage());
    }

    // Test root at x = 0
    solver.reset();
    x0 = 0.0;
    x1 = 1.0;
    expectedResult = true;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      root = solver.getRoot();
      Assert.assertEquals(0.0, root, 0.00001);
      Assert.assertTrue(expectedResult);
    } catch (FunctionException e) {
      Assert.fail("Exception should not be thrown: " + e.getMessage());
    }

    // Test failure case: root not found within the given interval
    solver.reset();
    x0 = 0.0;
    x1 = 2.0;
    expectedResult = false;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.fail("Exception should have been thrown");
    } catch (FunctionException e) {
      Assert.assertEquals("ROOT_NOT_FOUND", e.getMessage());
      Assert.assertTrue(expectedResult);
    }
  }

}