import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonlinearOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.FunctionEvaluation;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConverter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
 UnivariateFunction f = new FunctionWithRoots();
 try {
 PointValuePair result = optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals(0.0, result.getValue(), 1e-5);
 } catch (MaxCountExceededException | NonlinearOptimizationException | NotStrictlyPositiveException | NumberIsTooSmallException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testBrentOptimizerEmptyChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new GoalTypeChecker());
 UnivariateFunction f = new FunctionWithRoots();
 try {
 PointValuePair result = optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals(0.0, result.getValue(), 1e-5);
 } catch (MaxCountExceededException | NonlinearOptimizationException | NotStrictlyPositiveException | NumberIsTooSmallException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testBrentOptimizerMinimize() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new GoalTypeChecker(GoalType.MINIMIZE));
 UnivariateFunction f = new FunctionWithRoots();
 try {
 PointValuePair result = optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals(0.0, result.getValue(), 1e-5);
 } catch (MaxCountExceededException | NonlinearOptimizationException | NotStrictlyPositiveException | NumberIsTooSmallException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testBrentOptimizerMaximize() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new GoalTypeChecker(GoalType.MAXIMIZE));
 UnivariateFunction f = new FunctionWithRootsNegated();
 try {
 PointValuePair result = optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
 assertEquals(1.0, result.getValue(), 1e-5);
 } catch (MaxCountExceededException | NonlinearOptimizationException | NotStrictlyPositiveException | NumberIsTooSmallException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test (expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerNegativeRelThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(-1.0e-5, 1.0e-5, null);
 UnivariateFunction f = new FunctionWithRoots();
 try {
 optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 fail();
 } catch (MaxCountExceededException | NonlinearOptimizationException | NotStrictlyPositiveException | NumberIsTooSmallException e) {
 // expected exception
 }
 }

 @Test (expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerZeroAbsThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 0.0, null);
 UnivariateFunction f = new FunctionWithRoots();
 try {
 optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 fail();
 } catch (MaxCountExceededException | NonlinearOptimizationException | NotStrictlyPositiveException | NumberIsTooSmallException e) {
 // expected exception
 }
 }

 @Test
 public void testBrentOptimizerMaxCountExceeded() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new ConvergenceChecker() {
 @Override
 public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
 return iter > 1000;
 }
 });
 UnivariateFunction f = new FunctionWithRoots();
 try {
 optimizer.optimize(new UnivariateFunctionWrapper(f), new InitialGuess(0.5));
 fail();
 } catch (MaxCountExceededException e) {
 // expected exception
 }
 }
}

class FunctionWithRoots implements UnivariateFunction {

 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2.0;
 }

 @Override
 public double derivative(double x) {
 return 3.0 * Math.pow(x, 2);
 }
}

class FunctionWithRootsNegated implements UnivariateFunction {

 @Override
 public double value(double x) {
 return -(Math.pow(x, 3) - 2.0);
 }

 @Override
 public double derivative(double x) {
 return -3.0 * Math.pow(x, 2);
 }
}