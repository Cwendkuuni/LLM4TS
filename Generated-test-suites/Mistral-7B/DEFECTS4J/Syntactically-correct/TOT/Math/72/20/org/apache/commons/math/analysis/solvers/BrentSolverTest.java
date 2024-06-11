import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
  }

  @Test
  public void testFindRoot_FunctionWithSimpleRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 50;

    assertEquals(0.0, solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)), 0.001);
    assertEquals(0.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithNoRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 50;

    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x - 2.0;
      }
    };

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithDivergentBehavior() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 50;

    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        if (x > 1.0) {
          return x * x;
        } else {
          throw new ArithmeticException("Divide by zero");
        }
      }
    };

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots() throws FunctionException {
    double x0 = -2.0;
    double x1 = 2.0;
    int maxIter = 100;

    assertEquals(0.0, solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)), 0.01);
    assertEquals(0.0, solver.getRoot(), 0.01);

    function.setX(1.0);
    assertEquals(2.0, solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)), 0.01);
    assertEquals(2.0, solver.getRoot(), 0.01);
  }

}