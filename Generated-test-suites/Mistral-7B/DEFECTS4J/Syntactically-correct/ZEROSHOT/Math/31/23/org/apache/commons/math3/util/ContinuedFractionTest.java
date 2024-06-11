import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;

import org.junit.Test;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return x;
    default:
     return -1.0 / (x + n);
   }
  }

  @Override
  protected double getB(int n, double x) {
   return 1.0;
  }
 }

 @Test
 public void testSquareRoot() {
  ContinuedFraction c = new TestContinuedFraction();

  double epsilon = 1e-12;
  double x = 2.0;

  double result = c.evaluate(x);
  double expected = FastMath.sqrt(x);

  assertEquals(expected, result, epsilon);
 }

 @Test(expected = ConvergenceException.class)
 public void testDivergence() {
  ContinuedFraction c = new TestContinuedFraction();

  double x = -1.0;

  c.evaluate(x, 1e-10, 1000);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxIterations() {
  ContinuedFraction c = new TestContinuedFraction();

  double x = 1.0;

  c.evaluate(x, 1e-10, 1);
 }

}