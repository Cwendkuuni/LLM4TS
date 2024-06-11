import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.RootsChecker;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new TestFunctions.SinFunction();
    checker = new RootsChecker();
  }

  @After
  public void tearDown() {
    solver = null;
    function = null;
    checker = null;
  }

  @Test
  public void testFindRootSinFunction() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, FunctionConstants.SIN, x1, -FunctionConstants.SIN);
    assertEquals(true, foundRoot);
    assertEquals(1.5707963267948966, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootSinFunctionNegativeInterval() {
    double x0 = -1.0;
    double x1 = 0.0;
    int maxIter = 100;

    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, FunctionConstants.SIN, x1, FunctionConstants.SIN);
    assertEquals(true, foundRoot);
    assertEquals(-1.5707963267948966, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootCubicFunction() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    function = new TestFunctions.CubicFunction();
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, FunctionConstants.ZERO, x1, FunctionConstants.ZERO);
    assertEquals(true, foundRoot);
    assertEquals(1.324717957244746, solver.getRoot(), 0.0001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootDivisionByZeroFunction() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    function = new TestFunctions.DivisionByZeroFunction();
    solver.findRoot(function, checker, maxIter, x0, FunctionConstants.ZERO, x1, FunctionConstants.ZERO);
  }
}