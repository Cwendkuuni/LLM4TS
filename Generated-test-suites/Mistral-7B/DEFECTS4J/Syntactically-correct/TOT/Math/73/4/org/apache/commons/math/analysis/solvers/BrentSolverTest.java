import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.SineFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot_FunctionWithSimpleRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    SineFunction function = new SineFunction();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = Math.PI / 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithNoRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    FunctionNoRoot function = new FunctionNoRoot();
    double x0 = 1.0;
    double x1 = 2.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException to be thrown.");
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testFindRoot_FunctionWithTwoRoots() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    SquareFunction function = new SquareFunction();
    double x0 = 0.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double expectedRoot1 = 1.0;
    double expectedRoot2 = 2.0;

    assertEquals(expectedRoot1, solver.getRoot(), epsilon);

    solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));

    assertEquals(expectedRoot2, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithLargeInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    CosineFunction function = new CosineFunction();
    double x0 = -10.0;
    double x1 = 10.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(Math.PI / 2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithLargeValue() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    ExponentialFunction function = new ExponentialFunction();
    double x0 = 0.0;
    double x1 = 10.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double expectedRoot = Math.E;

    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithSmallValue() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
  public int converged(double x, double fx, double xPrev, double fxPrev) {
    return ConvergenceChecker.LOW;
  }
};

TanFunction function = new TanFunction();
double x0 = 0.0;
double x1 = 1.0 / 10.0;

solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

double expectedRoot = Math.PI / 4.0;

assertEquals(expectedRoot, solver.getRoot(), epsilon);
}

interface ComputableFunction {
  double valueAt(double x) throws FunctionException;
}

class FunctionNoRoot implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new FunctionException("Function has no root.");
  }
}

class SquareFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(x, 2);
  }
}

class CosineFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.cos(x);
  }
}

class ExponentialFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.exp(x);
  }
}

class TanFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.tan(x);
  }
}

interface ConvergenceChecker {
  int converged(double x, double fx, double xPrev, double fxPrev);

  static final int LOW = 0;
  static final int HIGH = 1;
}

}