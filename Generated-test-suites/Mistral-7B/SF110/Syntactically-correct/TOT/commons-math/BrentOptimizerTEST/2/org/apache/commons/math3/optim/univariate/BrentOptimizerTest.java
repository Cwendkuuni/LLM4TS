import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @BeforeClass
 public static void setup() {
 System.out.println("Running BrentOptimizer tests...");
 }

 @AfterClass
 public static void teardown() {
 System.out.println("BrentOptimizer tests completed.");
 }

 @Test
 public void testBrentOptimizerWithValidArguments() throws GoalTypeException, NotStrictlyPositiveException, ConvergenceException, NumberIsTooSmallException {
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }

 @Override
 public double gradient(double x) {
 return -2 * Math.sin(x) * Math.cos(x);
 }

 @Override
 public double lowerBound(double x) {
 return -5.0;
 }

 @Override
 public double upperBound(double x) {
 return 5.0;
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(-1.4142135625, solution.getPoint(), 1.0e-6);
 assertEquals(0.0, solution.getValue(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithInvalidLowerBound() throws NotStrictlyPositiveException {
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }

 @Override
 public double gradient(double x) {
 return -2 * Math.sin(x) * Math.cos(x);
 }

 @Override
 public double lowerBound(double x) {
 return -1.0;
 }

 @Override
 public double upperBound(double x) {
 return 5.0;
 }
 };

 assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(1.0e-6, 1.0e-6, problem.getChecker()));
 }

 @Test
 public void testBrentOptimizerWithInvalidUpperBound() throws NotStrictlyPositiveException {
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }

 @Override
 public double gradient(double x) {
 return -2 * Math.sin(x) * Math.cos(x);
 }

 @Override
 public double lowerBound(double x) {
 return -5.0;
 }

 @Override
 public double upperBound(double x) {
 return 0.0;
 }
 };

 assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(1.0e-6, 1.0e-6, problem.getChecker()));
 }

 @Test
 public void testBrentOptimizerWithInvalidRelativeTolerance() throws NumberIsTooSmallException {
 BrentOptimizer optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6);
 assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(new DummyOptimizationProblem()));
 }

 @Test
 public void testBrentOptimizerWithInvalidAbsoluteTolerance() throws NumberIsTooSmallException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6);
 assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(new DummyOptimizationProblem()));
 }

 @Test
 public void testBrentOptimizerWithInvalidGoalType() throws GoalTypeException {
 assertThrows(GoalTypeException.class, () -> new BrentOptimizer(1.0e-6, 1.0e-6, new OptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }
 }));
}

 @Test
 public void testBrentOptimizerWithConvergenceChecker() throws GoalTypeException, NotStrictlyPositiveException, ConvergenceException, NumberIsTooSmallException {
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }

 @Override
 public double gradient(double x) {
 return -2 * Math.sin(x) * Math.cos(x);
 }

 @Override
 public double lowerBound(double x) {
 return -5.0;
 }

 @Override
 public double upperBound(double x) {
 return 5.0;
 }

 @Override
 public ConvergenceChecker<UnivariatePointValuePair> getChecker() {
 return new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() == previous.getValue();
 }
 };
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, problem.getChecker());
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(-1.4142135625, solution.getPoint(), 1.0e-6);
 assertEquals(0.0, solution.getValue(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithDummyOptimizationProblem() throws GoalTypeException, NotStrictlyPositiveException, ConvergenceException, NumberIsTooSmallException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariatePointValuePair solution = optimizer.optimize(new DummyOptimizationProblem());
 assertNull(solution);
 }
}