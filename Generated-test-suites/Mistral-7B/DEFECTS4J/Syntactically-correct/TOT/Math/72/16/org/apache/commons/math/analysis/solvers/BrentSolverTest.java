import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.ScalarFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BrentSolver} class.
 */
public class BrentSolverTest {

 private BrentSolver solver;
 private ConvergenceChecker checker;
 private ScalarFunction function;

 @Before
 public void setup() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {
 @Override public int converged(double x1, double f1, double x2, double f2) {
 if (Math.abs(x1 - x2) < 0.001) {
 return ConvergenceChecker.LOW;
 } else if (Math.abs(f1) < 0.001) {
 return ConvergenceChecker.HIGH;
 } else {
 return ConvergenceChecker.NONE;
 }
 }
 };
 function = FunctionFactory.sinFunction();
 }

 /**
 * Test finding a root with a simple function.
 */
 @Test
 public void testFindRootSimpleFunction() {
 double x0 = 0.5;
 double x1 = 1.5;
 try {
 solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
 assertEquals(1.0, solver.getRoot(), 0.01);
 } catch (FunctionException e) {
 e.printStackTrace();
 assertTrue(false);
 }
 }

 /**
 * Test finding a root with a function with multiple roots.
 */
 @Test
 public void testFindRootMultipleRootsFunction() {
 double x0 = -1.5;
 double x1 = 1.5;
 try {
 solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
 assertEquals(-1.0, solver.getRoot(), 0.01);
 // The function has another root around x=0.5
 solver.reset();
 solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
 assertEquals(0.5, solver.getRoot(), 0.01);
 } catch (FunctionException e) {
 e.printStackTrace();
 assertTrue(false);
 }
 }

 /**
 * Test finding a root with a function that doesn't have a root in the interval.
 */
 @Test
 public void testFindRootNoRootFunction() {
 double x0 = 0.5;
 double x1 = 1.5;
 try {
 solver.findRoot(FunctionFactory.sqrtFunction(), checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
 assertFalse(solver.isRootFound());
 } catch (FunctionException e) {
 e.printStackTrace();
 assertTrue(false);
 }
 }

}