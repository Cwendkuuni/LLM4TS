import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private DummyFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (f1 * f2 > 0) {
          return LOW;
        } else {
          return HIGH;
        }
      }
    };
    function = new DummyFunction();
  }

  @Test
  public void testFindRoot_SimpleCase() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_RootIsX0() throws FunctionException {
    double x0 = 1.0;
    double x1 = 1.0;
    double root = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_RootIsX1() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 2.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_RootBetweenX0AndX1() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_RootOutsideX0X1() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 4.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertNull(solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    ComputableFunction function = () -> { throw new FunctionException(); };
    double f0 = Double.NaN;
    double f1 = Double.NaN;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

  @Test
  public void testGetRoot_RootIsSet() {
    double root = 2.0;
    solver.setRoot(root);

    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testGetRoot_RootIsNotSet() {
    assertNull(solver.getRoot());
  }

  @Test
  public void testSetRoot_RootIsSet() {
    double root = 2.0;
    solver.setRoot(root);

    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetRoot_IllegalArgumentException() {
    solver.setRoot(-1.0);
  }
}