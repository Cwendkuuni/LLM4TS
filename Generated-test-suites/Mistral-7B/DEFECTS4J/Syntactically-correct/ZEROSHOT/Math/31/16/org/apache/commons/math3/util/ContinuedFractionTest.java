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
        return 1;
      case 1:
        return x;
      default:
        return (n % 2 == 0) ? 1 : -1;
    }
  }

  @Override
  protected double getB(int n, double x) {
    switch (n) {
      case 0:
        return 0;
      case 1:
        return 1;
      default:
        return (n % 2 == 0) ? 1 : 0;
    }
  }
 }

 @Test
 public void testSquareRoot() {
  ContinuedFraction cfr = new TestContinuedFraction();
  double epsilon = 1e-9;

  double x = 2.0;
  double value = cfr.evaluate(x, epsilon);
  assertEquals(Math.sqrt(x), value, epsilon);
  x = 1.0;
  value = cfr.evaluate(x, epsilon);
  assertEquals(Math.sqrt(x), value, epsilon);
  x = 0.25;
  value = cfr.evaluate(x, epsilon);
  assertEquals(Math.sqrt(x), value, epsilon);

  x = 3.0;
  try {
    value = cfr.evaluate(x, epsilon);
    fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
    // expected
  }

  x = -2.0;
  try {
    value = cfr.evaluate(x, epsilon);
    fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
    // expected
  }
 }

 @Test
 public void testGoldenRatio() {
  ContinuedFraction cfr = new TestContinuedFraction();
  double epsilon = 1e-10;

  double x = 1.0;
  double value = cfr.evaluate(x, epsilon);
  assertEquals(0.5, value, epsilon);

  x = 1.61803398875; // golden ratio
  value = cfr.evaluate(x, epsilon);
  assertEquals(1.61803398875, value, epsilon);

  x = -1.61803398875;
  try {
    value = cfr.evaluate(x, epsilon);
    fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
    // expected
  }
 }

 @Test
 public void testFibonacci() {
  ContinuedFraction cfr = new TestContinuedFraction();
  double epsilon = 1e-15;

  double x = 1.0;
  double value = cfr.evaluate(x, epsilon);
  assertEquals(1.0, value, epsilon);

  x = (FastMath.sqrt(5) + 1) / 2.0;
  value = cfr.evaluate(x, epsilon);
  assertEquals(Math.pow(1.61803398875, x), value, epsilon);

  x = -1.0;
  try {
    value = cfr.evaluate(x, epsilon);
    fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
    // expected
  }
 }

 @Test
 public void testTestTest() {
  ContinuedFraction cfr = new TestContinuedFraction();
  double epsilon = 1e-9;

  double x = 0.5;
  double value = cfr.evaluate(x, epsilon);
  assertEquals(1.0 / Math.sqrt(2.0), value, epsilon);

  try {
    value = cfr.evaluate(-1.0, epsilon);
    fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
    // expected
  }

  try {
    value = cfr.evaluate(0.0, epsilon);
    fail("Expected ConvergenceException");
  } catch (ConvergenceException e) {
    // expected
  }
 }

 @Test
 public void testTestMaxIterations() {
  ContinuedFraction cfr = new TestContinuedFraction();
  double epsilon = 1e-9;
  int maxIterations = 3;

  double x = 0.5;
  double value = cfr.evaluate(x, epsilon, maxIterations);
  assertEquals(1.0 / Math.sqrt(2.0), value, epsilon);

  try {
    value = cfr.evaluate(-1.0, epsilon, maxIterations);
    fail("Expected MaxCountExceededException");
  } catch (MaxCountExceededException e) {
    // expected
  }
 }
}