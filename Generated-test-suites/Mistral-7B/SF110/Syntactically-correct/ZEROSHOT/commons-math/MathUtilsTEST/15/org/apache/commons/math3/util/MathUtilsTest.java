package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

 @org.junit.Test
 public void testHashDouble() {
 final double value = 1.23;
 final int expected = new Double(value).hashCode();
 final int actual = MathUtils.hash(value);
 assertEquals("hash of double value", expected, actual);
 }

 @org.junit.Test
 public void testHashDoubleArray() {
 final double[] values = {1.23, 2.46, 3.6};
 final int expected = Arrays.hashCode(values);
 final int actual = MathUtils.hash(values);
 assertEquals("hash of double array", expected, actual);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testNormalizeAngleWithNaN() {
 final double a = Double.NaN;
 final double center = MathUtils.TWO_PI / 2.0;
 MathUtils.normalizeAngle(a, center);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testNormalizeAngleWithInfinite() {
 final double a = Double.POSITIVE_INFINITY;
 final double center = MathUtils.TWO_PI / 2.0;
 MathUtils.normalizeAngle(a, center);
 }

 @org.junit.Test
 public void testNormalizeAngle() {
 final double a = 3.141592653589793;
 final double center = MathUtils.TWO_PI / 2.0;
 final double expected = Math.PI;
 final double actual = MathUtils.normalizeAngle(a, center);
 assertEquals("normalizeAngle", expected, actual, 1e-10);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testReduceWithNaN() {
 final double a = Double.NaN;
 final double period = 2.0;
 final double offset = 1.0;
 MathUtils.reduce(a, period, offset);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testReduceWithNegativePeriod() {
 final double a = 0.0;
 final double period = -2.0;
 final double offset = 1.0;
 MathUtils.reduce(a, period, offset);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testReduceWithInfinitePeriod() {
 final double a = 0.0;
 final double period = Double.POSITIVE_INFINITY;
 final double offset = 1.0;
 MathUtils.reduce(a, period, offset);
 }

 @org.junit.Test
 public void testReduce() {
 final double a = 1.5 * MathUtils.TWO_PI;
 final double period = 2.0;
 final double offset = 1.0;
 final double expected = a - 2.0 * Math.floor((a - offset) / 2.0) - offset;
 final double actual = MathUtils.reduce(a, period, offset);
 assertEquals("reduce", expected, actual, 1e-10);
 }

 @org.junit.Test(expected = MathArithmeticException.class)
 public void testCopySignByteWithNonConformantSignsAndPositiveMagnitude() {
 final byte sign = -1;
 final byte magnitude = 5;
 MathUtils.copySign(magnitude, sign);
 }

 @org.junit.Test(expected = MathArithmeticException.class)
 public void testCopySignByteWithNonConformantSignsAndNegativeMagnitude() {
 final byte sign = -1;
 final byte magnitude = -129;
 MathUtils.copySign(magnitude, sign);
 }

 @org.junit.Test
 public void testCopySignByteWithConformantSigns() {
 final byte sign = 1;
 final byte magnitude = 5;
 final byte expected = 5;
 final byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign byte", expected, actual);
 }

 @org.junit.Test(expected = MathArithmeticException.class)
 public void testCopySignShortWithNonConformantSignsAndPositiveMagnitude() {
 final short sign = -1;
 final short magnitude = 32767;
 MathUtils.copySign(magnitude, sign);
 }

 @org.junit.Test(expected = MathArithmeticException.class)
 public void testCopySignShortWithNonConformantSignsAndNegativeMagnitude() {
 final short sign = -1;
 final short magnitude = -32768;
 MathUtils.copySign(magnitude, sign);
 }

 @org.junit.Test
 public void testCopySignShortWithConformantSigns() {
 final short sign = 1;
 final short magnitude = 32767;
 final short expected = 32767;
 final short actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign short", expected, actual);
 }

 @org.junit.Test(expected = MathArithmeticException.class)
 public void testCopySignIntWithNonConformantSignsAndPositiveMagnitude() {
 final int sign = -1;
 final int magnitude = Integer.MAX_VALUE;
 MathUtils.copySign(magnitude, sign);
 }

 @org.junit.Test(expected = MathArithmeticException.class)
 public void testCopySignIntWithNonConformantSignsAndNegativeMagnitude() {
 final int sign = -1;
 final int magnitude = Integer.MIN_VALUE;
 MathUtils.copySign(magnitude, sign);
 }

 @org.junit.Test
 public void testCopySignIntWithConformantSigns() {
 final int sign = 1;
 final int magnitude = Integer.MAX_VALUE;
 final int expected = Integer.MAX_VALUE;
 final int actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign int", expected, actual);
 }

 @org.junit.Test
 public void testCopySignLongWithConformantSigns() {
 final long sign = 1;
 final long magnitude = Long.MAX_VALUE;
 final long expected = Long.MAX_VALUE;
 final long actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign long", expected, actual);
 }

 @org.junit.Test
 public void testCheckFiniteDouble() {
 final double x = 1.23;
 MathUtils.checkFinite(x);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 final double x = Double.NaN;
 MathUtils.checkFinite(x);
 }

 @org.junit.Test
 public void testCheckFiniteDoubleArray() {
 final double[] val = {1.23, 2.46, 3.6};
 MathUtils.checkFinite(val);
 }

 @org.junit.Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayNaN() {
 final double[] val = {1.23, Double.NaN, 3.6};
 MathUtils.checkFinite(val);
 }

 @org.junit.Test
 public void testCheckNotNullObject() {
 final Object o = new Object();
 MathUtils.checkNotNull(o);
 }

 @org.junit.Test(expected = NullArgumentException.class)
 public void testCheckNotNullObjectNull() {
 final Object o = null;
 MathUtils.checkNotNull(o);
 }

 @org.junit.Test(expected = NullArgumentException.class)
 public void testCheckNotNullObjectWithPattern() {
 final Object o = null;
 final Localizable pattern = LocalizedFormats.MESSAGE_NOT_VALID;
 MathUtils.checkNotNull(o, pattern);
 }

 @org.junit.Test(expected = NullArgumentException.class)
 public void testCheckNotNullObjectWithPatternAndArgs() {
 final Object o = null;
 final Localizable pattern = LocalizedFormats.MESSAGE_NOT_VALID;
 final Object arg1 = "arg1";
 final Object arg2 = "arg2";
 MathUtils.checkNotNull(o, pattern, arg1, arg2);
 }
}