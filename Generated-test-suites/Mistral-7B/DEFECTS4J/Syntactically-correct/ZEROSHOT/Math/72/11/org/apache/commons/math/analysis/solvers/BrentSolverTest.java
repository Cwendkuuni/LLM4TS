import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.RootChecker;
import org.spaceroots.mantissa.functions.scalar.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;

public class BrentSolverTest {

  private RootsFinder solver;
  private FunctionFactory functionFactory;
  private RootChecker rootChecker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    functionFactory = new FunctionFactory();
    rootChecker = new RootChecker();
  }

  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(new SimpleFunction(-1, 1, x -> x * x - 4));
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;
    boolean result = solver.findRoot(function, rootChecker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(-1.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(new SimpleFunction(-1, 1, x -> Math.sin(x)));
    double x0 = 0.0;
    double x1 = Math.PI;
    int maxIter = 10;
    boolean result = solver.findRoot(function, rootChecker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertFalse(result);
  }

  @Test
  public void testFindRootConvergenceChecker() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(new SimpleFunction(-1, 1, x -> Math.abs(Math.sin(x) - 0.5)));
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 10;
    RootChecker rootChecker = new RootChecker() {
      @Override
      public int converged(double x, double f, double xp, double fp) {
        return ConvergenceChecker.MID;
      }
    };
    boolean result = solver.findRoot(function, rootChecker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(new SimpleFunction(-1, 1, x -> {
      throw new ArithmeticException("Division by zero");
    }));
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;
    solver.findRoot(function, rootChecker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}