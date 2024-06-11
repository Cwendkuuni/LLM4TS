package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

 @Test
 public void testHash_double() {
 Double value = new Double(1.0);
 int expectedHashCode = value.hashCode();
 int actualHashCode = MathUtils.hash(value);
 assertEquals("Hash code should be the same", expectedHashCode, actualHashCode);
 }

 @Test(expected = NullArgumentException.class)
 public void testHash_nullDoubleArrayThrowsException() {
 MathUtils.hash(null);
 }

 @Test
 public void testHash_doubleArray() {
 double[] value = {1.0, 2.0, 3.0};
 int expectedHashCode = Arrays.hashCode(value);
 int actualHashCode = MathUtils.hash(value);
 assertEquals("Hash codes should be the same", expectedHashCode, actualHashCode);
 }

 @Test
 public void testNormalizeAngle_double_double() {
 double a = 3.14159265;
 double center = 0.0;
 double expectedNormalizedAngle = -3.14159265;
 double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
 assertEquals("Normalized angle should be correct", expectedNormalizedAngle, actualNormalizedAngle);

 a = 6.5;
 center = 3.14159265;
 double expectedNormalizedAngle2 = 0.15707963;
 double actualNormalizedAngle2 = MathUtils.normalizeAngle(a, center);
 assertEquals("Normalized angle should be correct", expectedNormalizedAngle2, actualNormalizedAngle2);

 a = 6.5;
 center = 6.5;
 double expectedNormalizedAngle3 = 0.0;
 double actualNormalizedAngle3 = MathUtils.normalizeAngle(a, center);
 assertEquals("Normalized angle should be correct", expectedNormalizedAngle3, actualNormalizedAngle3);
}

 @Test
 public void testReduce_double_double_double() {
 double a = 3.5;
 double period = 2.0;
 double offset = 1.5;
 double expectedReducedValue = 0.5;
 double actualReducedValue = MathUtils.reduce(a, period, offset);
 assertEquals("Reduced value should be correct", expectedReducedValue, actualReducedValue);
}

 @Test(expected = MathArithmeticException.class)
 public void testCopySign_byte_byteThrowsException() {
 byte magnitude = 1;
 byte sign = -129;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySign_short_shortThrowsException() {
 short magnitude = 1;
 short sign = -32769;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySign_int_int() {
 int magnitude = 1;
 int sign = -1;
 int expectedCopiedSign = -1;
 int actualCopiedSign = MathUtils.copySign(magnitude, sign);
 assertEquals("Copied sign should be correct", expectedCopiedSign, actualCopiedSign);

 magnitude = Integer.MIN_VALUE;
 sign = 1;
 MathUtils.checkFinite(magnitude); // Check that no exception is thrown
 int expectedCopiedSign2 = Integer.MIN_VALUE;
 int actualCopiedSign2 = MathUtils.copySign(magnitude, sign);
 assertEquals("Copied sign should be correct", expectedCopiedSign2, actualCopiedSign2);
}

 @Test
 public void testCopySign_long_long() {
 long magnitude = 1L;
 long sign = -1L;
 long expectedCopiedSign = -1L;
 long actualCopiedSign = MathUtils.copySign(magnitude, sign);
 assertEquals("Copied sign should be correct", expectedCopiedSign, actualCopiedSign);

 magnitude = Long.MIN_VALUE;
 sign = 1L;
 MathUtils.checkFinite(magnitude); // Check that no exception is thrown
 long expectedCopiedSign2 = Long.MIN_VALUE;
 long actualCopiedSign2 = MathUtils.copySign(magnitude, sign);
 assertEquals("Copied sign should be correct", expectedCopiedSign2, actualCopiedSign2);
}

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFinite_doubleThrowsException() {
 MathUtils.checkFinite(Double.POSITIVE_INFINITY);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFinite_doubleArrayThrowsException() {
 double[] array = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
 MathUtils.checkFinite(array);
 }

 @Test
 public void testCheckFinite_doubleArrayNoException() {
 double[] array = {1.0, 2.0, 3.0};
 MathUtils.checkFinite(array);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNull_nullObjectThrowsException() {
 MathUtils.checkNotNull(null, LocalizedFormats.OBJECT_NAME, "ObjectName");
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNull_nullObjectWithMessageThrowsException() {
 MathUtils.checkNotNull(null, "ObjectName is null");
 }

 @Test
 public void testCheckNotNull_nonNullObject() {
 Object object = new Object();
 MathUtils.checkNotNull(object);
 }
}