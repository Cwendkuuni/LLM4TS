package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 Double value = new Double(1.0);
 int expected = value.hashCode();
 int actual = MathUtils.hash(value.doubleValue());
 assertEquals(expected, actual);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = new double[]{1.0, 2.0, 3.0};
 int expected = Arrays.hashCode(values);
 int actual = MathUtils.hash(values);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double input = 3.141592653589793; // Pi
 double center = 0.0;
 double expected = -3.141592653589793; // -Pi
 double actual = MathUtils.normalizeAngle(input, center);
 assertEquals(expected, actual, 0.00001);

 input = 6.283185307179586; // 2 * Pi
 center = 3.141592653589793; // Pi
 expected = 0.0;
 actual = MathUtils.normalizeAngle(input, center);
 assertEquals(expected, actual, 0.00001);

 input = 0.0;
 center = 3.141592653589793; // Pi
 expected = 3.141592653589793; // Pi - 2 * Pi
 actual = MathUtils.normalizeAngle(input, center);
 assertEquals(expected, actual, 0.00001);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testReduceWithInfiniteArgument() {
 double a = Double.POSITIVE_INFINITY;
 double period = 1.0;
 double offset = 0.0;
 MathUtils.reduce(a, period, offset);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testReduceWithNaNArgument() {
 double a = Double.NaN;
 double period = 1.0;
 double offset = 0.0;
 MathUtils.reduce(a, period, offset);
 }

 @Test
 public void testReduce() {
 double a = 3.0;
 double period = 1.0;
 double offset = 0.5;
 double expected = 1.5;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.00001);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteNegativeAndPositive() {
 byte magnitude = -1;
 byte sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignBytePositiveAndNegative() {
 byte magnitude = 1;
 byte sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 MathUtils checkFinite = new MathUtils();
 double input = Double.NaN;
 checkFinite.checkFinite(input);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleInfinite() {
 MathUtils checkFinite = new MathUtils();
 double input = Double.POSITIVE_INFINITY;
 checkFinite.checkFinite(input);
 }

 @Test
 public void testCheckFiniteDoubleFinite() {
 MathUtils checkFinite = new MathUtils();
 double input = 1.0;
 checkFinite.checkFinite(input);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 MathUtils utils = null;
 utils.checkNotNull(null);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullArray() {
 MathUtils utils = new MathUtils();
 utils.checkFinite(null);
 }

 @Test
 public void testCheckFiniteArrayFinite() {
 MathUtils utils = new MathUtils();
 double[] values = new double[]{1.0, 2.0, 3.0};
 utils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteArrayNaN() {
 MathUtils utils = new MathUtils();
 double[] values = new double[]{1.0, Double.NaN, 3.0};
 utils.checkFinite(values);
 }
}