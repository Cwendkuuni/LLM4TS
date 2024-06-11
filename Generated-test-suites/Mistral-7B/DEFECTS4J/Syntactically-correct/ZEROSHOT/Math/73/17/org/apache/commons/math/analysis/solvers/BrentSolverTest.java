import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionGenerator;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionGenerator functionGenerator;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        double error = Math.abs(x2 - x);
        if (error < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (error > 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    functionGenerator = new FunctionGenerator();
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
    functionGenerator = null;
  }

  @Test
  public void testFindRoot_simpleCase() throws FunctionException {
    ComputableFunction function = functionGenerator.sinFunction();
    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = functionGenerator.divideFunction(1, 0);
    double x0 = 1.0;
    double x1 = 0.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_maxIterations() throws FunctionException {
    ComputableFunction function = functionGenerator.sinFunction();
    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;
    solver.setMaxIterations(0);
    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRoot_convergenceChecker() throws FunctionException {
    ComputableFunction function = functionGenerator.sqrtFunction();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        double error = Math.abs(x2 - x);
        if (error < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (error > 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_customIterations() throws FunctionException {
    ComputableFunction function = functionGenerator.sinFunction();
    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;
    int customIterations = 5;
    assertTrue(solver.findRoot(function, checker, customIterations, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

}