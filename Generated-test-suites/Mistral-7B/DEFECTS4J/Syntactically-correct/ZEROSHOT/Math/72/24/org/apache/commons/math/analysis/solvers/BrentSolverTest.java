import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SinFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SinFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.UNKNOWN;
      }
    };
    function = new SinFunction();
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
    function = null;
  }

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = 2.0;
    int maxIter = 100;

    boolean result = solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 1.0);
    assertEquals("Root not found", true, result);
    assertEquals("Root should be approx. 1.27324074074", 1.27324074074, solver.getRoot(), 0.00001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNonConvergentFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;

    // This function is not convergent for the given interval
    SinFunction nonConvergentFunction = new SinFunction();
    nonConvergentFunction.setInverse(true);

    solver.findRoot(nonConvergentFunction, checker, maxIter, x0, 0.0, x1, 1.0);
  }

  @Test
  public void testFindRootWithFunctionWithMultipleRoots() throws FunctionException {
    double x0 = -2.0;
    double x1 = 2.0;
    int maxIter = 100;

    boolean result = solver.findRoot(function, checker, maxIter, x0, 4.0, x1, 0.0);
    assertEquals("Root not found", true, result);
    assertEquals("Root should be approx. 0.0", 0.0, solver.getRoot(), 0.00001);
  }

  @Test
  public void testFindRootWithFunctionWithNoRoots() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;

    CosFunction cosFunction = new CosFunction();

    boolean result = solver.findRoot(cosFunction, checker, maxIter, x0, 0.0, x1, 0.0);
    assertEquals("Root found when none should be", false, result);
  }

}

class CosFunction extends SinFunction {
  @Override
  public double valueAt(double x) {
    return Math.cos(x);
  }
}