import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 0.001 && Math.abs(f2) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }

  @Test
  public void testFindRootWithSimpleFunction() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = -1.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThrowingException() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        if (x > 0) {
          throw new FunctionException("function error");
        }
        return Math.pow(x, 3) - 2;
      }
    };

    double x0 = -1.0;
    double x1 = 1.0;
    double root = -1.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      // expected
    }
    assertEquals(Double.NaN, solver.getRoot());
    assertEquals(false, solver.isFoundRoot());
  }

  @Test
  public void testFindRootWithFunctionHavingDifferentRoots() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 2;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root1 = 1.0;
    double root2 = 2.0;

    boolean result1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root1, solver.getRoot(), epsilon);
    assertEquals(true, result1);

    double x0_2 = 2.0;
    double x1_2 = 3.0;
    boolean result2 = solver.findRoot(function, checker, 100, x0_2, function.valueAt(x0_2), x1_2, function.valueAt(x1_2));
    assertEquals(root2, solver.getRoot(), epsilon);
    assertEquals(true, result2);
  }

  @Test
  public void testFindRootWithFunctionHavingOneRootAtTheEdge() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 0.001;
      }
    };

    double x0 = 1.0;
    double x1 = 1.1;
    double root = 1.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootWithFunctionHavingOneRootAtTheBeginning() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return -Math.pow(x, 2) - 0.001;
      }
    };

    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootWithFunctionHavingNoRoots() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 2;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(Double.NaN, solver.getRoot());
    assertEquals(false, result);
  }
}