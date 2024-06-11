import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstant;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private FunctionConstant function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new FunctionConstant(Math::sin);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.NONE;
      }
    };
  }

  @Test
  public void testFindRootSimple() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, 0.0, x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootNearRoot() throws FunctionException {
    double x0 = 1.5 * Math.PI / 2.0;
    double x1 = 1.5 * Math.PI / 2.0;
    double root = Math.PI / 2.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootDifferentSigns() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootMaxIter() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    assertFalse(solver.findRoot(function, checker, 0, x0, Math.sin(x0), x1, Math.sin(x1)));
  }

  @Test(expected = FunctionException.class)
  public void testFindRootDivisionByZero() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0;
    FunctionConstant function = new FunctionConstant(x -> 1.0 / x);

    solver.findRoot(function, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = FunctionException.class)
  public void testFindRootNullFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0;
    ComputableFunction function = null;

    solver.findRoot(function, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test
  public void testFindRootConvergenceChecker() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;
    ConvergenceChecker wrongChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.LOW;
      }
    };

    assertFalse(solver.findRoot(function, wrongChecker, 100, x0, Math.sin(x0), x1, Math.sin(x1)));
  }
}