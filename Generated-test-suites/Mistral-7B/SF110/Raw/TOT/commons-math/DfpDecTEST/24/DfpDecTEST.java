package org.apache.commons.math3.dfp.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.apache.commons.math3.dfp.DfpUtil;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

public class DfpDecTest extends TestCase {

	private static final DfpFieldFactory FIELD_FACTORY = DfpFieldFactory.getDfp128FieldInstance();
	private static final DfpDec ZERO = new DfpDec(FIELD_FACTORY);
	private static final DfpDec ONE = new DfpDec(FIELD_FACTORY, 1);

	public static Test suite() {
		return new TestSuite(DfpDecTest.class);
	}

	public DfpDecTest(String name) {
		super(name);
	}

	// Test constructor with double argument
	public void testConstructorWithDoubleArgument() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY, 3.14);
		assertEquals(3.14, dfpDec.doubleValue(), 0.0001);
	}

	// Test constructor with Dfp argument
	public void testConstructorWithDfpArgument() {
		Dfp dfp = new DfpDec(FIELD_FACTORY, 5.5);
		DfpDec dfpDec = new DfpDec(dfp);
		assertEquals(5.5, dfpDec.doubleValue(), 0.0001);
	}

	// Test newInstance method with int argument
	public void testNewInstanceWithIntArgument() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY, 10);
		DfpDec newInstance = dfpDec.newInstance(1234);
		assertEquals(1234, newInstance.intValue());
	}

	// Test newInstance method with Dfp argument
	public void testNewInstanceWithDfpArgument() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY, 3.14);
		DfpDec newInstance = dfpDec.newInstance(ONE);
		assertEquals(4.14, newInstance.doubleValue(), 0.0001);
	}

	// Test round method with different rounding modes
	public void testRoundMethod() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY, 1.5);
		assertEquals(1, dfpDec.round(0));
		assertEquals(2, dfpDec.round(1));
		assertEquals(2, dfpDec.round(2));
		assertEquals(2, dfpDec.round(3));
		assertEquals(2, dfpDec.round(4));
		assertEquals(2, dfpDec.round(5));
		assertEquals(3, dfpDec.round(6));
		assertEquals(3, dfpDec.round(7));
		assertEquals(4, dfpDec.round(8));
		assertEquals(4, dfpDec.round(9));
		assertEquals(5, dfpDec.round(10));
		assertEquals(5, dfpDec.round(11));
		assertEquals(6, dfpDec.round(12));
		assertEquals(6, dfpDec.round(13));
		assertEquals(7, dfpDec.round(14));
		assertEquals(7, dfpDec.round(15));
		assertEquals(8, dfpDec.round(16));
		assertEquals(8, dfpDec.round(17));
		assertEquals(9, dfpDec.round(18));
		assertEquals(9, dfpDec.round(19));
		assertEquals(10, dfpDec.round(20));
		assertEquals(10, dfpDec.round(21));
	}

	// Test nextAfter method with different inputs
	public void testNextAfterMethod() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY, 0.5);
		DfpDec result = dfpDec.nextAfter(ONE);
		assertEquals(1.5, result.doubleValue(), 0.0001);
		assertEquals(ONE, result.compareTo(ONE));

		dfpDec = new DfpDec(FIELD_FACTORY, -0.5);
		result = dfpDec.nextAfter(ONE);
		assertEquals(-0.5, result.doubleValue(), 0.0001);
		assertEquals(-1, result.compareTo(ONE));

		dfpDec = new DfpDec(FIELD_FACTORY, Double.NEGATIVE_INFINITY);
		result = dfpDec.nextAfter(ZERO);
		assertEquals(Double.NEGATIVE_INFINITY, result.doubleValue(), 0.0);
		assertEquals(-1, result.compareTo(ZERO));

		dfpDec = new DfpDec(FIELD_FACTORY, Double.POSITIVE_INFINITY);
		result = dfpDec.nextAfter(ZERO);
		assertEquals(Double.POSITIVE_INFINITY, result.doubleValue(), 0.0);
		assertEquals(1, result.compareTo(ZERO));

		dfpDec = new DfpDec(FIELD_FACTORY, ZERO);
		result = dfpDec.nextAfter(ONE);
		assertEquals(1.0E-324, result.doubleValue(), 0.0);
		assertEquals(1, result.compareTo(ONE));
	}

	// Test round method with overflow and underflow
	public void testRoundMethodWithOverflowAndUnderflow() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY, Double.MAX_VALUE);
		assertEquals(Double.POSITIVE_INFINITY, dfpDec.doubleValue(), 0.0);
		assertEquals(4, dfpDec.round(0));
		assertEquals(4, dfpDec.round(1));
		assertEquals(4, dfpDec.round(2));
		assertEquals(4, dfpDec.round(3));
		assertEquals(4, dfpDec.round(4));
		assertEquals(4, dfpDec.round(5));
		assertEquals(4, dfpDec.round(6));
		assertEquals(4, dfpDec.round(7));
		assertEquals(4, dfpDec.round(8));
		assertEquals(4, dfpDec.round(9));
		assertEquals(5, dfpDec.round(10));
		assertEquals(5, dfpDec.round(11));
		assertEquals(5, dfpDec.round(12));
		assertEquals(5, dfpDec.round(13));
		assertEquals(5, dfpDec.round(14));
		assertEquals(5, dfpDec.round(15));
		assertEquals(5, dfpDec.round(16));
		assertEquals(5, dfpDec.round(17));
		assertEquals(6, dfpDec.round(18));
		assertEquals(6, dfpDec.round(19));
		assertEquals(16, dfpDec.round(20));
		assertEquals(16, dfpDec.round(21));

		dfpDec = new DfpDec(FIELD_FACTORY, Double.MIN_VALUE);
		assertEquals(Double.NEGATIVE_INFINITY, dfpDec.doubleValue(), 0.0);
		assertEquals(4, dfpDec.round(0));
		assertEquals(4, dfpDec.round(1));
		assertEquals(4, dfpDec.round(2));
		assertEquals(4, dfpDec.round(3));
		assertEquals(4, dfpDec.round(4));
		assertEquals(4, dfpDec.round(5));
		assertEquals(4, dfpDec.round(6));
		assertEquals(4, dfpDec.round(7));
		assertEquals(4, dfpDec.round(8));
		assertEquals(4, dfpDec.round(9));
		assertEquals(3, dfpDec.round(10));
		assertEquals(3, dfpDec.round(11));
		assertEquals(3, dfpDec.round(12));
		assertEquals(3, dfpDec.round(13));
		assertEquals(3, dfpDec.round(14));
		assertEquals(3, dfpDec.round(15));
		assertEquals(3, dfpDec.round(16));
		assertEquals(3, dfpDec.round(17));
		assertEquals(2, dfpDec.round(18));
		assertEquals(2, dfpDec.round(19));
		assertEquals(2, dfpDec.round(20));
		assertEquals(2, dfpDec.round(21));
	}

	// Test newInstance method with invalid Dfp argument
	public void testNewInstanceWithInvalidDfpArgument() {
		DfpDec dfpDec = new DfpDec(FIELD_FACTORY);
		try {
			dfpDec.newInstance(new DfpDec(FIELD_FACTORY, Integer.MAX_VALUE));
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			dfpDec.newInstance(new DfpDec(FIELD_FACTORY, Integer.MIN_VALUE));
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			dfpDec.newInstance(new DfpDec(FIELD_FACTORY, Double.NaN));
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			dfpDec.newInstance(new DfpDec(FIELD_FACTORY, Double.NEGATIVE_INFINITY));
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
}