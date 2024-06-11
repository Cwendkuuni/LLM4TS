import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new DummyFunction(3.0);
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThrowingException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new ExceptionFunction();
    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.fail("FunctionException should have been thrown");
  }

  @Test
  public void testFindRootWithFunctionConvergingSlowly() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return (Math.abs(f1) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new SlowConvergingFunction();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.4;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

}

class DummyFunction implements ComputableFunction {
  private double a;

  public DummyFunction(double a) {
    this.a = a;
  }

  @Override
  public double valueAt(double x) {
    return x - a;
  }
}

class ExceptionFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new FunctionException("Test exception");
  }
}

class SlowConvergingFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(Math.E, -x) - Math.log(x);
  }
}