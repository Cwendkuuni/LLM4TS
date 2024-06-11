import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;
 private UnivariateFunction function;

 @Before
 public void setUp() {
 function = new MyUnivariateFunction();
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 }

 @Test
 public void testMinimizeSimple() {
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
 double expected = 1.0;
 assertEquals(expected, result.getPoint(), 1e-6);
 assertEquals(expected, result.getValue(), 1e-6);
 }

 @Test
 public void testMinimizeWithConvergenceChecker() {
 ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new MyConvergenceChecker();
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, convergenceChecker);
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
 assertTrue(convergenceChecker.isConverged());
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testMinimizeWithSmallRelativeThreshold() {
 optimizer = new BrentOptimizer(1.0e-9, 1.0e-6, null);
 optimizer.optimize(function, 0.0, 10.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMinimizeWithNegativeAbsoluteThreshold() {
 optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
 optimizer.optimize(function, 0.0, 10.0);
 }

 @Test
 public void testMinimizeWithNonMinimum() {
 function = new MyUnivariateFunctionNonMinimum();
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
 assertNotNull(result);
 }

 @After
 public void tearDown() {
 optimizer = null;
 function = null;
 }

 static class MyUnivariateFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 }

 static class MyUnivariateFunctionNonMinimum implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + 1;
 }
 }

 static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iterationNumber, UnivariatePointValuePair currentValue, UnivariatePointValuePair previousValue) {
 return (currentValue.getValue() - previousValue.getValue()) <= 1.0e-6;
 }
 }
}