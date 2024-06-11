package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

	@Test
	public void testHashDouble() {
		double value = 3.14;
		int expectedHash = 1234567; // Arbitrary value for testing
		int actualHash = MathUtils.hash(value);
		assertEquals(expectedHash, actualHash);
	}

	@Test
	public void testHashDoubleArray() {
		double[] values = { 1.2, 2.7, 3.14 };
		int expectedHash = 1234567; // Arbitrary value for testing
		int actualHash = MathUtils.hash(values);
		assertEquals(expectedHash, actualHash);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testNormalizeAngleInvalidArgument() {
		MathUtils.checkFinite(Double.POSITIVE_INFINITY);
		MathUtils.checkFinite(new double[] { Double.NaN });

		double a = Double.POSITIVE_INFINITY;
		double center = 0.0;
		try {
			MathUtils.normalizeAngle(a, center);
		} catch (NotFiniteNumberException e) {
			// Expected
			throw e;
		}
	}

	@Test
	public void testNormalizeAngleSimpleCases() {
		double a = 0.0;
		double center = 0.0;
		double expected = 0.0;
		assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.0);

		a = Math.PI;
		expected = -Math.PI;
		assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.0);
	}

	@Test
	public void testReduceSimpleCases() {
		double a = 1.5;
		double period = 2.0;
		double offset = 0.5;
		double expected = 0.5;
		assertEquals(expected, MathUtils.reduce(a, period, offset), 0.0);

		a = 4.5;
		expected = 1.5;
		assertEquals(expected, MathUtils.reduce(a, period, offset), 0.0);
	}

	@Test
	public void testCopySignByte() {
		byte magnitude = 5;
		byte sign = 1;
		byte expected = 5;
		byte actual = MathUtils.copySign(magnitude, sign);
		assertEquals(expected, actual);

		magnitude = -5;
		sign = 1;
		expected = -5;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);

		magnitude = 5;
		sign = -1;
		expected = -5;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);
	}

	@Test
	public void testCopySignShort() {
		short magnitude = 5;
		short sign = 1;
		short expected = 5;
		short actual = MathUtils.copySign(magnitude, sign);
		assertEquals(expected, actual);

		magnitude = -32767;
		sign = 1;
		expected = -32767;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);

		magnitude = 5;
		sign = -1;
		expected = -5;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);
	}

	@Test
	public void testCopySignInt() {
		int magnitude = 5;
		int sign = 1;
		int expected = 5;
		int actual = MathUtils.copySign(magnitude, sign);
		assertEquals(expected, actual);

		magnitude = Integer.MIN_VALUE;
		sign = 1;
		expected = Integer.MIN_VALUE;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);

		magnitude = 5;
		sign = -1;
		expected = -5;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);
	}

	@Test
	public void testCopySignLong() {
		long magnitude = 5L;
		long sign = 1;
		long expected = 5L;
		long actual = MathUtils.copySign(magnitude, sign);
		assertEquals(expected, actual);

		magnitude = Long.MIN_VALUE;
		sign = 1;
		expected = Long.MIN_VALUE;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);

		magnitude = 5L;
		sign = -1;
		expected = -5L;
		try {
			actual = MathUtils.copySign(magnitude, sign);
		} catch (MathArithmeticException e) {
			// This exception should not be thrown in this test
			fail("Unexpected MathArithmeticException: " + e.getMessage());
		}
		assertEquals(expected, actual);
	}

	@Test
	public void testCheckFiniteDouble() {
		double value = 3.14;
		MathUtils.checkFinite(value);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFiniteDoubleNaN() {
		MathUtils.checkFinite(Double.NaN);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFiniteDoubleInfinity() {
		MathUtils.checkFinite(Double.POSITIVE_INFINITY);
	}

	@Test
	public void testCheckFiniteDoubleArray() {
		double[] values = { 1.2, 2.7, 3.14 };
		MathUtils.checkFinite(values);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFiniteDoubleArrayNaN() {
		double[] values = { 1.2, Double.NaN, 3.14 };
		MathUtils.checkFinite(values);
	}

	@Test(expected = NullArgumentException.class)
	public void testCheckNotNullNullArgument() {
		MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
	}

	@Test(expected = NullArgumentException.class)
	public void testCheckNotNullNullObject() {
		MathUtils.checkNotNull(null);
	}

	@Test
	public void testCheckNotNullNotNullObject() {
		Object obj = new Object();
		MathUtils.checkNotNull(obj);
	}
}