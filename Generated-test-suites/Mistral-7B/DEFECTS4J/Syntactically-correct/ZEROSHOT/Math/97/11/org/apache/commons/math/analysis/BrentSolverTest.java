import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.impl.SinFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SinFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        // we are only interested in the convergence state, not in the actual value
        if (Math.abs(fx1) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
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
  public void testFindRootSimpleCase() {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2.0;

    assertTrue(solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootNoRoot() {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = Double.NaN;

    assertFalse(solver.findRoot(new ConstantFunction(0.0), checker, 50, x0, 0.0, x1, 0.0));
    assertEquals(root, solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.sqrt(2.0);

    function = new SquareRootFunction(); // function with a zero in the interval

    solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootMaxIter() {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.sqrt(2.0);

    solver = new BrentSolver();
    solver.setMaxIter(0);

    assertFalse(solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

}

class ConstantFunction implements ComputableFunction {
  private double value;

  public ConstantFunction(double value) {
    this.value = value;
  }

  @Override
  public double valueAt(double x) {
    return value;
  }
}

class SquareRootFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.sqrt(x);
  }
}