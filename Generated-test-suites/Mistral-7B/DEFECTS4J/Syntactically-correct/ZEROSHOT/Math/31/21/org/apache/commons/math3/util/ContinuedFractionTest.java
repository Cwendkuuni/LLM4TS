import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private static final double DEFAULT_EPSILON = 1e-9;

 @Test(expected = ConvergenceException.class)
 public void testEvaluateDivergence() {
 ContinuedFraction cf = new DivergingContinuedFraction();
 try {
 df.evaluate(0.0, DEFAULT_EPSILON, Integer.MAX_VALUE);
 } catch (MaxCountExceededException e) {
 // expected, since the max iterations is exceeded
 }
 df.evaluate(0.0, DEFAULT_EPSILON, 1);
 }

 @Test
 public void testEvaluateSimple() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 double result = cf.evaluate(2.0);
 assertEquals("Test evaluate simple square root", Math.sqrt(2.0), result, 1e-6);
 }

 @Test
 public void testEvaluateEpsilon() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 double result = cf.evaluate(2.0, 1e-12, Integer.MAX_VALUE);
 assertEquals("Test evaluate simple square root with high precision", Math.sqrt(2.0), result, 1e-13);
 }

 @Test
 public void testEvaluateMaxIterations() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 try {
 df.evaluate(2.0, DEFAULT_EPSILON, 0);
 } catch (IllegalArgumentException e) {
 // expected, since the number of iterations must be greater than zero
 }

 try {
 df.evaluate(2.0, DEFAULT_EPSILON, Integer.MIN_VALUE);
 } catch (IllegalArgumentException e) {
 // expected, since the number of iterations must be greater than zero
 }

 try {
 df.evaluate(2.0, DEFAULT_EPSILON, Integer.MAX_VALUE / 2);
 } catch (MaxCountExceededException e) {
 // expected, since the number of iterations is too low
 }
 }

 @Test
 public void testEvaluateNegativeX() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 double result = cf.evaluate(-2.0);
 assertEquals("Test evaluate negative x", -Math.sqrt(2.0), result, 1e-6);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateNegativeXDivergence() {
 ContinuedFraction cf = new DivergingNegativeContinuedFraction();
 cf.evaluate(-2.0, DEFAULT_EPSILON, Integer.MAX_VALUE);
 }

 @Test
 public void testGetA() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 assertEquals("Test getA(0, 2.0)", 2.0, cf.getA(0, 2.0), 1e-6);
 }

 @Test
 public void testGetB() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 assertEquals("Test getB(0, 2.0)", 1.0, cf.getB(0, 2.0), 1e-6);
 }

 @Test
 public void testGetAAndB() {
 ContinuedFraction cf = new SquareRootContinuedFraction();
 double x = 2.0;
 double a = cf.getA(1, x);
 double b = cf.getB(1, x);
 assertEquals("Test getA(1, 2.0)", 2.0, a, 1e-6);
 assertEquals("Test getB(1, 2.0)", 1.0, b, 1e-6);
 }

}

class DivergingContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return Double.NaN;
 }

 @Override
 protected double getB(int n, double x) {
 return Double.NaN;
 }
}

class DivergingNegativeContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 throw new RuntimeException("Not implemented");
 }

 @Override
 protected double getB(int n, double x) {
 throw new RuntimeException("Not implemented");
 }

 @Override
 public double evaluate(double x, double epsilon, int maxIterations) {
 double result = super.evaluate(x, epsilon, maxIterations);
 if (Double.isNaN(result)) {
 throw new ConvergenceException(LocalizedFormats.DIVERGING_CONTINUED_FRACTION, x);
 }
 return result;
 }
}