import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
    if (n % 2 == 0) {
      return 1.0 / (2.0 * x + n);
    } else {
      return 1.0;
    }
  }

  @Override
  protected double getB(int n, double x) {
    if (n % 2 == 0) {
      return -1.0 / (2.0 * x + n);
    } else {
      return 2.0 * x;
    }
  }
 }

 @Test
 public void testEvaluate() {
  ContinuedFraction cf = new TestContinuedFraction();
  TestContinuedFraction testCF = (TestContinuedFraction) cf;
  double epsilon = 1e-9;

  // Test with default epsilon
  assertEquals(1.0 / 3.0, testCF.evaluate(1.0), epsilon);

  // Test with custom epsilon
  assertEquals(1.0 / 3.0, testCF.evaluate(1.0, epsilon), epsilon);

  // Test with custom maxIterations
  assertEquals(1.0 / 3.0, testCF.evaluate(1.0, epsilon, 10), epsilon);

  // Test with negative x
  assertThrows(UnsupportedOperationException.class, () -> testCF.evaluate(-1.0));

  // Test with infinite x
  assertThrows(UnsupportedOperationException.class, () -> testCF.evaluate(Double.POSITIVE_INFINITY));

  // Test with NaN x
  assertThrows(ArithmeticException.class, () -> testCF.evaluate(Double.NaN));

  // Test for convergence exception
  assertThrows(ConvergenceException.class, () -> testCF.evaluate(0.0, epsilon, 1));

  // Test for max count exceeded exception
  assertThrows(MaxCountExceededException.class, () -> testCF.evaluate(0.0, epsilon, 0));
 }

 @Test
 public void testEvaluateWithDifferentAAndB() {
  ContinuedFraction cf = new TestContinuedFractionWithDifferentAAndB();
  TestContinuedFraction testCF = (TestContinuedFractionWithDifferentAAndB) cf;
  double epsilon = 1e-9;

  // Test with default epsilon
  assertEquals(Math.sqrt(2.0), testCF.evaluate(1.0), epsilon);

  // Test with custom epsilon
  assertEquals(Math.sqrt(2.0), testCF.evaluate(1.0, epsilon), epsilon);
 }

 private static class TestContinuedFractionWithDifferentAAndB extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
    if (n % 2 == 0) {
      return Math.sqrt(2.0);
    } else {
      return 1.0;
    }
  }

  @Override
  protected double getB(int n, double x) {
    if (n % 2 == 0) {
      return 1.0 / (2.0 * x + n);
    } else {
      return 2.0 * x;
    }
  }
 }

}