import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePairChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionConvertedNumberFormatException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + Math.sin(x) * Math.cos(x) + 1.5 * x - 2.5;
 }
 };

 @Test
 public void testBrentOptimizer() {
 // Test with default constructor and no ConvergenceChecker
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
 UnivariateOptimizerResult result = optimizer.optimize(function, new SimpleBounds(0.0, 10.0), new MaxEval(1000));
 assertEquals(0.0, result.getPoint(), 0.001);
 assertEquals(-2.67848051, result.getValue(), 0.001);

 // Test with custom InitialGuess
 BrentOptimizer optimizerWithGuess = new BrentOptimizer(0.001, 0.01, new InitialGuess() {
 @Override
 public double getInitialPoint() {
 return 2.5;
 }
 });
 UnivariateOptimizerResult resultWithGuess = optimizerWithGuess.optimize(function, new SimpleBounds(0.0, 10.0), new MaxEval(1000));
 assertEquals(2.5, resultWithGuess.getPoint(), 0.0);
 assertEquals(-2.67848051, resultWithGuess.getValue(), 0.001);

 // Test with PointValuePairChecker
 PointValuePairChecker checker = new PointValuePairChecker() {
 @Override
 public boolean check(int iteration, PointValuePair x, PointValuePair fx) {
 return x.getValue() <= fx.getValue();
 }
 };
 BrentOptimizer optimizerWithChecker = new BrentOptimizer(0.001, 0.01, checker);
 UnivariateOptimizerResult resultWithChecker = optimizerWithChecker.optimize(function, new SimpleBounds(0.0, 10.0), new MaxEval(1000));
 assertEquals(0.0, resultWithChecker.getPoint(), 0.001);
 assertEquals(-2.67848051, resultWithChecker.getValue(), 0.001);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeRelativeThreshold() {
 new BrentOptimizer(-0.001, 0.01);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeAbsoluteThreshold() {
 new BrentOptimizer(0.001, -0.01);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSmallRelativeThreshold() {
 new BrentOptimizer(0.0001, 0.01);
 }

 @Test(expected = PrecisionConvertedNumberFormatException.class)
 public void testInvalidInitialGuess() {
 new BrentOptimizer(0.001, 0.01, new InitialGuess() {
 @Override
 public double getInitialPoint() {
 throw new PrecisionConvertedNumberFormatException("Invalid initial guess");
 }
 });
 }
}