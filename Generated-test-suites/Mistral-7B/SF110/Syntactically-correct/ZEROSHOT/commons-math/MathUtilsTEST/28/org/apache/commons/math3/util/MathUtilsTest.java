package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

	@Test
	public void testHashDouble() {
		double value = 1.2;
		int expected = Double.hashCode(value);
		int actual = MathUtils.hash(value);
		assertEquals("Hash code of a double should be the same", expected, actual);
	}

	@Test
	public void testHashDoubleArray() {
		double[] values = {1.2, 2.3, 3.4};
		int expected = Arrays.hashCode(values);
		int actual = MathUtils.hash(values);
		assertEquals("Hash code of a double array should be the same", expected, actual);
	}

	@Test(expected = NullArgumentException.class)
	public void testNormalizeAngleNullInput() {
		MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, "angle");
	}

	@Test
	public void testNormalizeAngle() {
		double angle = 3.14;
		double expected = -3.14;
		double actual = MathUtils.normalizeAngle(angle, 0);
		assertEquals("Normalize angle test failed", expected, actual, 1e-12);

		angle = 6.28;
		expected = 0;
		actual = MathUtils.normalizeAngle(angle, 0);
		assertEquals("Normalize angle test failed", expected, actual, 1e-12);

		angle = -6.28;
		expected = 6.28;
		actual = MathUtils.normalizeAngle(angle, 0);
		assertEquals("Normalize angle test failed", expected, actual, 1e-12);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testReduceNaNInput() {
		MathUtils.checkFinite(Double.NaN);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testReduceInfiniteInput() {
		MathUtils.checkFinite(Double.POSITIVE_INFINITY);
		MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
	}

	@Test
	public void testReduce() {
		double a = 1.5;
		double period = 2.5;
		double offset = 0.5;
		double expected = 0.7;
		double actual = MathUtils.reduce(a, period, offset);
		assertEquals("Reduce test failed", expected, actual, 1e-12);
	}

	@Test(expected = MathArithmeticException.class)
	public void testCopySignByteNegativeMagnitudePositiveSign() {
		byte magnitude = -1;
		byte sign = 1;
		MathUtils.copySign(magnitude, sign);
	}

	@Test
	public void testCopySignByteValidInput() {
		byte magnitude = 1;
		byte sign = 1;
		byte expected = magnitude;
		byte actual = MathUtils.copySign(magnitude, sign);
		assertEquals("Copy sign test failed", expected, actual);
	}

	@Test(expected = MathArithmeticException.class)
	public void testCopySignByteNegativeMagnitudeNegativeSign() {
		byte magnitude = -1;
		byte sign = -1;
		MathUtils.copySign(magnitude, sign);
	}

	@Test
	public void testCopySignBytePositiveMagnitudeNegativeSign() {
		byte magnitude = 1;
		byte sign = -1;
		byte expected = -1;
		byte actual = MathUtils.copySign(magnitude, sign);
		assertEquals("Copy sign test failed", expected, actual);
	}

	@Test
	public void testCheckFiniteDouble() {
		MathUtils.checkFinite(1.2);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFiniteNaN() {
		MathUtils.checkFinite(Double.NaN);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFiniteInfinity() {
		MathUtils.checkFinite(Double.POSITIVE_INFINITY);
		MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
	}

	@Test
	public void testCheckNotNullNullInput() {
		String message = "Object should not be null";
		MathUtils.checkNotNull(null, LocalizedFormats.OBJECT_NULL, message);
	}

	@Test
	public void testCheckNotNullValidInput() {
		Object obj = new Object();
		MathUtils.checkNotNull(obj);
	}

	@Test(expected = NullArgumentException.class)
	public void testCheckNotNullNullArg() {
		MathUtils.checkNotNull(null);
	}
}