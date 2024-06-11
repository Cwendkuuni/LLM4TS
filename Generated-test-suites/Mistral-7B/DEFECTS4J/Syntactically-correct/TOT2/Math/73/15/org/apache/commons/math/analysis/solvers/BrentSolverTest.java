import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.DefaultConvergenceChecker;
import org.spaceroots.mantissa.roots.SimpleFunction;
import org.spaceroots.mantissa.roots.FunctionWithNoRoots;

public class BrentSolverTest {

 @Test
 public void testFindRootWithSimpleFunctionAndChecker() {
 ComputableFunction function = new SimpleFunction();
 ConvergenceChecker checker = new DefaultConvergenceChecker();

 BrentSolver solver = new BrentSolver();

 double x0 = 1.0;
 double x1 = 3.0;
 double root = 2.0;

 boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

 assertEquals(root, solver.getRoot(), epsilon);
 }

 // Edge case test for findRoot method with a function that has no roots in the given interval
 @Test
 public void testFindRootWithFunctionNoRoots() {
 ComputableFunction function = new FunctionWithNoRoots();
 ConvergenceChecker checker = new DefaultConvergenceChecker();

 BrentSolver solver = new BrentSolver();

 double x0 = 0.0;
 double x1 = 1.0;

 boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

 assertFalse(result);
 }

 // Error scenario test for findRoot method with a null function
 @Test(expected = NullPointerException.class)
 public void testFindRootWithNullFunction() {
 ConvergenceChecker checker = new DefaultConvergenceChecker();

 BrentSolver solver = new BrentSolver();

 ComputableFunction function = null;

 solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);
 }

 // Basic test case for getRoot method with a known root value
 @Test
 public void testGetRootWithKnownRootValue() {
 BrentSolver solver = new BrentSolver();
 solver.findRoot(new SimpleFunction(), new DefaultConvergenceChecker(), 100, 1.0, 2.0, 3.0, 1.0);

 double expectedRoot = 2.0;
 double actualRoot = solver.getRoot();

 assertEquals(expectedRoot, actualRoot, epsilon);
 }

 // Edge case test for getRoot method with a solver that didn't find a root
 @Test
 public void testGetRootWithNoRootFound() {
 BrentSolver solver = new BrentSolver();
 solver.findRoot(new FunctionWithNoRoots(), new DefaultConvergenceChecker(), 100, 0.0, 0.0, 1.0, 1.0);

 double actualRoot = solver.getRoot();
 assertNaN(actualRoot);
 }

 // Error scenario test for getRoot method with a null solver object
 @Test(expected = NullPointerException.class)
 public void testGetRootWithNullSolver() {
 BrentSolver solver = null;

 double actualRoot = solver.getRoot();
 }

}