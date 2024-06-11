import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.assertEquals;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

 private BrentSolver solver;
 private ConvergenceChecker checker;
 private MockFunction function;

 @Before
 public void setUp() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xprev, double fxprev) {
 return ConvergenceChecker.MID;
 }
 };
 function = new MockFunction();
 }

 @After
 public void tearDown() {
 solver = null;
 checker = null;
 function = null;
 }

 /**
 * Test finding root in a simple case
 */
 @Test
 public void testFindRootSimple() {
 double x0 = 0.0;
 double x1 = 3.0;
 double root = 1.0;
 double[] expected = {1.0};
 double[] args = {x0, x1};
 double[] results = solver.findRoot(function, checker, 1, args[0], function.valueAt(args[0]), args[1], function.valueAt(args[1]));
 assertEquals(true, results[0]);
 assertEquals(root, solver.getRoot(), epsilon);
 }

 /**
 * Test finding root in a complex case
 */
 @Test
 public void testFindRootComplex() {
 double x0 = 0.0;
 double x1 = 3.0;
 double root = 1.5;
 double[] expected = {1.5};
 double[] args = {x0, x1};
 function.setReturnValues(0.0, 1.0, 2.0, 3.0, 1.5, 1.0, 0.0);
 double[] results = solver.findRoot(function, checker, 1, args[0], function.valueAt(args[0]), args[1], function.valueAt(args[1]));
 assertEquals(true, results[0]);
 assertEquals(root, solver.getRoot(), epsilon);
 }

 /**
 * Test finding root in a case when function value at root is zero
 */
 @Test
 public void testFindRootZeroFunctionValue() {
 double x0 = 0.0;
 double x1 = 2.0;
 double root = 1.0;
 double[] expected = {1.0};
 double[] args = {x0, x1};
 function.setReturnValues(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0);
 double[] results = solver.findRoot(function, checker, 1, args[0], function.valueAt(args[0]), args[1], function.valueAt(args[1]));
 assertEquals(true, results[0]);
 assertEquals(root, solver.getRoot(), epsilon);
 }

 /**
 * Test finding root in a case when function is not defined in the interval
 */
 @Test(expected = FunctionException.class)
 public void testFindRootNotDefined() {
 double x0 = 0.0;
 double x1 = 2.0;
 double root = 1.0;
 double[] expected = {1.0};
 double[] args = {x0, x1};
 function.setThrowException(true);
 double[] results = solver.findRoot(function, checker, 1, args[0], function.valueAt(args[0]), args[1], function.valueAt(args[1]));
 }

}