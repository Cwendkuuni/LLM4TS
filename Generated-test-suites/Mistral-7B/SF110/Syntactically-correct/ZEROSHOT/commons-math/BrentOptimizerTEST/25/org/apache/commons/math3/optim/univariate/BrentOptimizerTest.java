import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BrentOptimizerTest {

 private FunctionFunction1D testFunction;
 private BrentOptimizer optimizer;

 @Before
 public void setUp() {
 testFunction = new QuadraticFunction(-1.0, 1.0, 1.0);
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 }

 @Test
 public void testOptimize() {
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 1.0);
 double expected = 0.4242640687119285;
 assertEquals(expected, result.getPoint(), 1e-7);
 assertEquals(expected * expected, result.getValue(), 1e-7);
 }

 @Test (expected = NumberIsTooSmallException.class)
 public void testMinRelativeToleranceTooSmall() {
 optimizer = new BrentOptimizer(0.1, 1.0e-6);
 optimizer.optimize(testFunction, 0.0, 1.0);
 }

 @Test (expected = NotStrictlyPositiveException.class)
 public void testAbsoluteThresholdNotPositive() {
 optimizer = new BrentOptimizer(1.0, -1.0);
 optimizer.optimize(testFunction, 0.0, 1.0);
 }

 @Test
 public void testConvergenceChecker() {
 ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iterationNumber, UnivariatePointValuePair currentPosition, UnivariatePointValuePair newPosition) {
 return FastMath.abs(currentPosition.getPoint() - newPosition.getPoint()) < 1e-6;
 }
 };
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 1.0);
 }

 @Test
 public void testGoalTypeMinimize() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 optimizer.setGoalType(GoalType.MINIMIZE);
 result = optimizer.optimize(testFunction, 0.0, 1.0);
 assertEquals(-expected, result.getValue(), 1e-7);
 }

 @Test
 public void testGoalTypeMaximize() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 result = optimizer.optimize(testFunction, 0.0, 1.0);
 assertEquals(expected, result.getValue(), 1e-7);
 }

 @Test
 public void testStartValueBeyondBounds() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 ResultVector resultVector = new ResultVector(new double[] { 2.0 }); ;
 ResultVector expectedResultVector = new ResultVector(new double[] { 0.4242640687119285 });
 OptimizationProblem optimizationProblem = new OptimizationProblem() {
 @Override
 public FunctionFunction1D getFunctionToMinimize() {
 return testFunction;
 }

 @Override
 public double[] getLowerBounds() {
 return new double[] { Double.NEGATIVE_INFINITY };
 }

 @Override
 public double[] getUpperBounds() {
 return new double[] { 1.0 };
 }

 @Override
 public double[] getStartingPoint() {
 return new double[] { 2.0 };
 }
 };
 OptimizationResult optimizationResult = optimizer.optimize(optimizationProblem);
 assertEquals(expectedResultVector, optimizationResult.getResultVector());
 }
}