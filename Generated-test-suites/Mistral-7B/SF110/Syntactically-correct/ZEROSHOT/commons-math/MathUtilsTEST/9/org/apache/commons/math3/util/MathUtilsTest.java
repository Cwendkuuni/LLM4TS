package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 final double value = 1.23;
 final int expectedHash = new Double(value).hashCode();
 assertEquals(expectedHash, MathUtils.hash(value));
 }

 @Test
 public void testHashDoubleArray() {
 final double[] values = {1.23, 2.46, 3.69};
 final int expectedHash = Arrays.hashCode(values);
 assertEquals(expectedHash, MathUtils.hash(values));
 }

 @Test
 public void testNormalizeAngle() {
 final double a = 3.141592653589793;
 final double center = 0.0;
 final double expectedResult = -3.141592653589793;
 assertEquals(expectedResult, MathUtils.normalizeAngle(a, center), 0.001);

 final double a2 = 6.283185307179586;
 final double center2 = 3.141592653589793;
 final double expectedResult2 = 0.0;
 assertEquals(expectedResult2, MathUtils.normalizeAngle(a2, center2), 0.001);
 }

 @Test
 public void testReduce() {
 final double a = 5.5;
 final double period = 2.0;
 final double offset = 1.5;
 final double expectedResult = 1.5;
 assertEquals(expectedResult, MathUtils.reduce(a, period, offset), 0.001);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteNegative() {
 final byte magnitude = -5;
 final byte sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteOverflow() {
 final byte magnitude = 127;
 final byte sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignShort() {
 final short magnitude = -32767;
 final short sign = 1;
 try {
 MathUtils.copySign(magnitude, sign);
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testCopySignShortNegative() {
 final short magnitude = -5;
 final short sign = 1;
 final short expectedResult = magnitude;
 assertEquals(expectedResult, MathUtils.copySign(magnitude, sign));
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortOverflow() {
 final short magnitude = 32767;
 final short sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignInt() {
 final int magnitude = -2147483647;
 final int sign = 1;
 try {
 MathUtils.copySign(magnitude, sign);
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testCopySignIntNegative() {
 final int magnitude = -5;
 final int sign = 1;
 final int expectedResult = magnitude;
 assertEquals(expectedResult, MathUtils.copySign(magnitude, sign));
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntOverflow() {
 final int magnitude = Integer.MAX_VALUE;
 final int sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignLong() {
 final long magnitude = -9223372036854775807L;
 final long sign = 1;
 try {
 MathUtils.copySign(magnitude, sign);
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testCopySignLongNegative() {
 final long magnitude = -5;
 final long sign = 1;
 final long expectedResult = magnitude;
 assertEquals(expectedResult, MathUtils.copySign(magnitude, sign));
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongOverflow() {
 final long magnitude = Long.MAX_VALUE;
 final long sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCheckFiniteDouble() {
 final double value = 1.23;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 final double value = Double.NaN;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleInfinite() {
 final double value = Double.POSITIVE_INFINITY;
 MathUtils.checkFinite(value);
 }

 @Test
 public void testCheckFiniteDoubleArray() {
 final double[] values = {1.23, 2.46, 3.69};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayNaN() {
 final double[] values = {Double.NaN};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayInfinite() {
 final double[] values = {Double.POSITIVE_INFINITY};
 MathUtils.checkFinite(values);
 }

 @Test
 public void testCheckNotNullObject() {
 final Object obj = new Object();
 MathUtils.checkNotNull(obj);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullObjectNull() {
 final Object obj = null;
 MathUtils.checkNotNull(obj);
 }

 @Test
 public void testCheckNotNullObjectPattern() {
 final Object obj = new Object();
 MathUtils.checkNotNull(obj, LocalizedFormats.INVALID_ARGUMENT, "Object cannot be null");
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullObjectPatternNull() {
 final Object obj = null;
 MathUtils.checkNotNull(obj, LocalizedFormats.INVALID_ARGUMENT, "Object cannot be null");
 }
}