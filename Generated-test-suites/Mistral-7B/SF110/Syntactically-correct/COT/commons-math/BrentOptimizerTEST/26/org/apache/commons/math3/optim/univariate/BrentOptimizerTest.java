import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateOptimizer brentOptimizer;
 private ConvergenceChecker<PointValuePair> convergenceChecker;
 private InitialGuess initialGuess;

 @Before
 public void setUp() {
 brentOptimizer = new BrentOptimizer(0.01, 0.001, null);
 convergenceChecker = new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iter, PointValuePair current, PointValuePair best) {
 return iter > 100;
 }
 };
 initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 0.0;
 }
 };
 }

 @Test
 public void testConstructorWithRelAndAbsAndChecker() {
 brentOptimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);
 assertNotNull(brentOptimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithSmallRelativeThreshold() {
 brentOptimizer = new BrentOptimizer(0.0001, 0.001, convergenceChecker);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveAbsoluteThreshold() {
 brentOptimizer = new BrentOptimizer(0.01, -0.001, convergenceChecker);
 }

 @Test
 public void testDoOptimizeWithMinimizeGoal() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, initialGuess, null);
 assertNotNull(result);
 }

 @Test
 public void testDoOptimizeWithMaximizeGoal() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }
 }, initialGuess, null);
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimizeWithNullGoalType() {
 brentOptimizer.optimize(null, initialGuess, null);
 }

 @Test
 public void testDoOptimizeWithNullInitialGuess() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, null, null);
 assertNotNull(result);
 }

 @Test
 public void testDoOptimizeWithNullConvergenceChecker() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, initialGuess, null);
 assertNotNull(result);
 }

 @Test
 public void testComputeObjectiveValue() {
 double x = 2.0;
 brentOptimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);
 double expected = Math.pow(x, 2.0) - 4.0;
 double actual = brentOptimizer.computeObjectiveValue(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testBrentOptimizerWithSimpleFunction() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, initialGuess, x -> Math.pow(x, 2.0) - 4.0);
 assertNotNull(result);
 assertEquals(-2.0, result.getPoint(), 0.001);
 assertEquals(-4.0, result.getValue(), 0.001);
 }

 @Test
 public void testBrentOptimizerWithSimpleFunctionAndInitialGuessOutsideRange() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 5.0;
 }
 }, x -> Math.pow(x, 2.0) - 4.0);
 assertNotNull(result);
 assertFalse(Precision.equals(5.0, result.getPoint(), 0.01));
 }

 @Test
 public void testBrentOptimizerWithFunctionWithMultipleLocalMinima() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, initialGuess, x -> Math.pow(Math.sin(x), 2.0) + 0.1 * Math.pow(x, 2.0) - Math.pow(Math.PI, 2.0));
 assertNotNull(result);
 assertFalse(Precision.equals(0.0, result.getPoint(), 0.01));
 }

 @Test
 public void testBrentOptimizerWithFunctionWithMultipleLocalMinimaAndInitialGuessNearMinima() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 1.5;
 }
 }, x -> Math.pow(Math.sin(x), 2.0) + 0.1 * Math.pow(x, 2.0) - Math.pow(Math.PI, 2.0));
 assertNotNull(result);
 assertFalse(Precision.equals(1.5, result.getPoint(), 0.01));
 }

 @Test
 public void testBrentOptimizerWithFunctionWithMultipleLocalMinimaAndInitialGuessFarFromMinima() {
 PointValuePair result = brentOptimizer.optimize(new GoalType() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 }, new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 5.0;
 }
 }, x -> Math.pow(Math.sin(x), 2.0) + 0.1 * Math.pow(x, 2.0) - Math.pow(Math.PI, 2.0));
 assertNotNull(result);
 assertFalse(Precision.equals(5.0, result.getPoint(), 0.01));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerWithNullObjectiveFunction() {
 brentOptimizer.optimize(null, initialGuess, null);
 }

}