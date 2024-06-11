import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;
 private double relTol = 1.0e-6;
 private double absTol = 1.0e-6;
 private GoalType goalType = GoalType.MINIMIZE;
 private ConvergenceChecker<PointValuePair> convergenceChecker = null; // Add your custom ConvergenceChecker implementation here, if needed

 @Test
 public void testConstructorWithRelAndAbsTolAndChecker() {
 optimizer = new BrentOptimizer(relTol, absTol, convergenceChecker);
 assertNotNull(optimizer);
 }

 @Test
 public void testConstructorWithRelAndAbsTol() {
 optimizer = new BrentOptimizer(relTol, absTol);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithSmallRelativeTolerance() {
 optimizer = new BrentOptimizer(1.0e-10, absTol);
 fail("Should throw NumberIsTooSmallException with this relative tolerance.");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveAbsoluteTolerance() {
 optimizer = new BrentOptimizer(relTol, -1.0);
 fail("Should throw NotStrictlyPositiveException with this absolute tolerance.");
 }

 @Test
 public void testDoOptimize() throws GoalTypeException {
    double[] functionValues = new double[]{1.0, 0.5, 0.1};
    double[] xValues = new double[]{2.0, 1.5, 1.0};
    optimizer = new BrentOptimizer(relTol, absTol);
    PointValuePair solution = optimizer.optimize(new SimpleFunction(functionValues, xValues), goalType, null);
    double expectedSolution = xValues[1];
    assertEquals(expectedSolution, solution.getPoint(), 0.0);
    double expectedValue = functionValues[1];
    assertEquals(expectedValue, solution.getValue(), 0.0);
}

 @Test(expected = GoalTypeException.class)
 public void testDoOptimizeWithMaximizeGoalType() {
 optimizer = new BrentOptimizer(relTol, absTol);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 optimizer.optimize(new SimpleFunction(new double[]{1.0, 0.5, 0.1}, new double[]{2.0, 1.5, 1.0}), goalType, null);
 fail("Should throw GoalTypeException with this goal type.");
 }

 @Test
 public void testDoOptimizeWithDifferentInitialGuesses() {
 optimizer = new BrentOptimizer(relTol, absTol);
 double[] functionValues = new double[]{1.0, 0.5, 0.1};
 double[] xValues = new double[]{1.5, 1.2, 1.0};
 PointValuePair solution = optimizer.optimize(new SimpleFunction(functionValues, xValues), goalType, null);
 double expectedSolution = xValues[2];
 assertEquals(expectedSolution, solution.getPoint(), 0.0);
 double expectedValue = functionValues[2];
 assertEquals(expectedValue, solution.getValue(), 0.0);
 }

 @Test
 public void testDoOptimizeWithConvergenceChecker() {
 optimizer = new BrentOptimizer(relTol, absTol, convergenceChecker);
 double[] functionValues = new double[]{1.0, 0.5, 0.1};
 double[] xValues = new double[]{2.0, 1.5, 1.0};
 PointValuePair solution = optimizer.optimize(new SimpleFunction(functionValues, xValues), goalType, null);
 double expectedSolution = xValues[1];
 assertEquals(expectedSolution, solution.getPoint(), 0.0);
 double expectedValue = functionValues[1];
 assertEquals(expectedValue, solution.getValue(), 0.0);
 // Add your custom tests for convergence checker here, if needed
 }

 // Add more test cases as needed for edge cases or other exception handling scenarios

}