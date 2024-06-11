import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   return (n % 2 == 0) ? 1.0 : x;
  }

  @Override
  protected double getB(int n, double x) {
   return (n % 2 == 1) ? 1.0 : 0.0;
  }
 }

 @Test
 public void testEvaluate() {
  ContinuedFraction cf = new TestContinuedFraction();
  double sqrt2 = 1.4142135623731e+01;

  assertEquals(1.0, cf.evaluate(0.0), 1e-9);
  assertEquals(1.0, cf.evaluate(1.0), 1e-9);
  assertEquals(sqrt2, cf.evaluate(sqrt2), 1e-6);
  assertEquals(sqrt2, cf.evaluate(sqrt2, 1e-6), 1e-6);
  assertEquals(sqrt2, cf.evaluate(sqrt2, 1e-6, 30), 1e-6);

  try {
   cf.evaluate(sqrt2, 1e-12, 10);
   fail("Expected MaxCountExceededException");
  } catch (MaxCountExceededException e) {
   // expected
  }

  try {
   cf.evaluate(-1.0, 1e-10, 10);
   fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
   // expected
  }
 }

 @Test
 public void testEvaluateInfiniteDivergence() {
  ContinuedFraction cf = new TestContinuedFraction() {
   @Override
   protected double getA(int n, double x) {
    return 1.0 / x;
   }

   @Override
   protected double getB(int n, double x) {
    return 1.0;
   }
  };

  try {
   cf.evaluate(0.0, 1e-10, 10);
   fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
   // expected
  }
 }

 @Test
 public void testEvaluateNaN() {
  ContinuedFraction cf = new TestContinuedFraction() {
   @Override
   protected double getA(int n, double x) {
    return x * x;
   }

   @Override
   protected double getB(int n, double x) {
    return 2.0 * x;
   }
  };

  try {
   cf.evaluate(-1.0, 1e-10, 10);
   fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
   // expected
  }
 }

 @Test
 public void testEvaluateWithEpsilon() {
  ContinuedFraction cf = new TestContinuedFraction() {
   @Override
   protected double getA(int n, double x) {
    return Math.sqrt(x);
   }

   @Override
   protected double getB(int n, double x) {
    return 1.0;
   }
  };

  assertEquals(1.5, cf.evaluate(2.25), 1e-6);
  assertEquals(1.5, cf.evaluate(2.25, 1e-6), 1e-6);
 }

 @Test
 public void testEvaluateWithMaxIterations() {
  ContinuedFraction cf = new TestContinuedFraction() {
   @Override
   protected double getA(int n, double x) {
    return Math.pow(2, n) / Math.PI;
   }

   @Override
   protected double getB(int n, double x) {
    return -Math.pow(-1, n);
   }
  };

  assertEquals(1.6487212707375825682688552817, cf.evaluate(1.0), 1e-15);
  assertEquals(1.6487212707375825682688552817, cf.evaluate(1.0, 1e-15), 1e-15);
  assertEquals(1.6487212707375825682688552817, cf.evaluate(1.0, 1e-15, 1000), 1e-15);
 }

}