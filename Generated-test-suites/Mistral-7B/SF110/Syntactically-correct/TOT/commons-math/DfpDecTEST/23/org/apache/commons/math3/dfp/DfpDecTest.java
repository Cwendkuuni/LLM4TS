package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFieldElement;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpRoundingMode;
import org.apache.commons.math3.dfp.Precision;
import org.apache.commons.math3.exception.ArithmeticException;
import org.apache.commons.math3.exception.DivisionByZeroException;
import org.apache.commons.math3.exception.IEEEException;

public class DfpDecTest {

    private static final DfpField FACTORY = new DfpField(36, DfpRoundingMode.HALF_EVEN);

    @org.junit.Test
    public void testNewInstance() {
        DfpDec d1 = new DfpDec(FACTORY);
        DfpDec d2 = DfpDec.newInstance(d1);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceInt() {
        DfpDec d1 = new DfpDec(FACTORY, 42);
        DfpDec d2 = DfpDec.newInstance(d1.intValue());
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceLong() {
        DfpDec d1 = new DfpDec(FACTORY, 42L);
        DfpDec d2 = DfpDec.newInstance(d1.longValue());
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceDouble() {
        DfpDec d1 = new DfpDec(FACTORY, 42.65);
        DfpDec d2 = DfpDec.newInstance(d1.doubleValue());
        assertEquals(d1, d2, 1e-14);
    }

    @org.junit.Test
    public void testNewInstanceDfp() {
        DfpDec d1 = new DfpDec(FACTORY, new DfpDec(FACTORY, 42));
        DfpDec d2 = DfpDec.newInstance(d1);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceString() {
        DfpDec d1 = new DfpDec(FACTORY, "42.65");
        DfpDec d2 = DfpDec.newInstance(d1.toString());
        assertEquals(d1, d2, 1e-14);
    }

    @org.junit.Test
    public void testNewInstanceStringWithExponent() {
        DfpDec d1 = new DfpDec(FACTORY, "42.65E-3");
        DfpDec d2 = DfpDec.newInstance(d1.toString());
        assertEquals(d1, d2, 1e-14);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testNewInstanceStringInvalid() {
        DfpDec d1 = new DfpDec(FACTORY, "foo");
    }

    @org.junit.Test
    public void testNewInstanceInvalid() {
        DfpDec d1 = new DfpDec(FACTORY, null);
        assertFalse(d1 instanceof DfpDec);
    }

    @org.junit.Test
    public void testRounding() {
        DfpDec d1 = new DfpDec(FACTORY, 123.4567);
        int result = d1.round(DfpRoundingMode.HALF_UP.getValue());
        assertEquals(1, result);
        assertEquals(123.457, d1.doubleValue(), 1e-14);

        result = d1.round(DfpRoundingMode.HALF_DOWN.getValue());
        assertEquals(0, result);
        assertEquals(123.456, d1.doubleValue(), 1e-14);

        result = d1.round(DfpRoundingMode.CEIL.getValue());
        assertEquals(1, result);
        assertEquals(123.457, d1.doubleValue(), 1e-14);

        result = d1.round(DfpRoundingMode.FLOOR.getValue());
        assertEquals(0, result);
        assertEquals(123.456, d1.doubleValue(), 1e-14);

        result = d1.round(DfpRoundingMode.UP.getValue());
        assertEquals(1, result);
        assertEquals(123.457, d1.doubleValue(), 1e-14);

        result = d1.round(DfpRoundingMode.DOWN.getValue());
        assertEquals(0, result);
        assertEquals(123.456, d1.doubleValue(), 1e-14);
    }

    @org.junit.Test
    public void testNextAfter() {
        DfpDec x1 = new DfpDec(FACTORY, 0.1234567);
        DfpDec x2 = new DfpDec(FACTORY, 1.23456789);
        DfpDec y1 = x1.nextAfter(x2);
        DfpDec y2 = x2.nextAfter(x1);

        assertEquals(y1, y2);
        assertEquals(x2, y1.previous(1));
    }

    @org.junit.Test
    public void testNextAfterZero() {
        DfpDec x = new DfpDec(FACTORY, 0);
        DfpDec y = x.nextAfter(x);
        assertEquals(x, y);
    }

    @org.junit.Test
    public void testNextAfterNaN() {
        DfpDec x = new DfpDec(FACTORY, Double.NaN);
        DfpDec y = x.nextAfter(x);
        assertEquals(x, y);
    }

    @org.junit.Test
    public void testNextAfterNegativeZero() {
        DfpDec x = new DfpDec(FACTORY, -0);
        DfpDec y = x.nextAfter(x);
        assertEquals(-0, y.doubleValue(), 0);
    }

    @org.junit.Test
    public void testNextAfterPositiveZero() {
        DfpDec x = new DfpDec(FACTORY, 0);
        DfpDec y = x.nextAfter(x);
        assertEquals(0, y.doubleValue(), 0);
    }

    @org.junit.Test
    public void testNextAfterNegative() {
        DfpDec x = new DfpDec(FACTORY, -123.456);
        DfpDec y = x.nextAfter(x);
        assertEquals(-123.455, y.doubleValue(), 1e-14);
    }

    @org.junit.Test
    public void testNextAfterPositive() {
        DfpDec x = new DfpDec(FACTORY, 123.456);
        DfpDec y = x.nextAfter(x);
        assertEquals(123.457, y.doubleValue(), 1e-14);
    }

    @org.junit.Test
    public void testNextAfterBig() {
        DfpDec x = new DfpDec(FACTORY, Precision.LARGE_VALUE);
        DfpDec y = x.nextAfter(x);
        assertTrue(y.compareTo(x) > 0);
    }

    @org.junit.Test
    public void testNextAfterBigNegative() {
        DfpDec x = new DfpDec(FACTORY, Precision.LARGE_NEGATIVE_VALUE);
        DfpDec y = x.nextAfter(x);
        assertTrue(y.compareTo(x) < 0);
    }

    @org.junit.Test
    public void testNewInstanceWithIEEEFloatingPoint() {
        DfpDec d1 = new DfpDec(FACTORY, 1.7976931348623157e308);
        DfpDec d2 = DfpDec.newInstance(Double.longBitsToDouble(0xfff0000000000000L | 0x7ff0000000000001L));
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceWithComplex() {
        Complex complex = new Complex(1.0, 2.0);
        DfpDec d1 = new DfpDec(FACTORY, complex);
        DfpDec d2 = DfpDec.newInstance(complex);
        assertEquals(d1, d2);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testNewInstanceWithComplexNaN() {
        Complex complex = new Complex(0.0, Double.NaN);
        DfpDec d1 = new DfpDec(FACTORY, complex);
    }

    @org.junit.Test(expected = IEEEException.class)
    public void testNewInstanceWithDenormalizedNumber() {
        DfpDec d1 = new DfpDec(FACTORY, 0.0000000000000001111111111111111111111111111111111111111111111111111111111111111);
    }

    @org.junit.Test(expected = IEEEException.class)
    public void testNewInstanceWithSubnormalNumber() {
        DfpDec d1 = new DfpDec(FACTORY, 1.40112e-45);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeExponent() {
        DfpDec d1 = new DfpDec(FACTORY, new Complex(1.0, 2.0), -3);
    }

    @org.junit.Test
    public void testNewInstanceWithZeroExponent() {
        DfpDec d1 = new DfpDec(FACTORY, new Complex(1.0, 2.0), 0);
        DfpDec d2 = DfpDec.newInstance(new Complex(1.0, 2.0), 0);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceWithPositiveExponent() {
        DfpDec d1 = new DfpDec(FACTORY, new Complex(1.0, 2.0), 3);
        DfpDec d2 = DfpDec.newInstance(new Complex(1.0, 2.0), 3);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testNewInstanceWithComplexNaN2() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        DfpDec d1 = new DfpDec(FACTORY, complex);
        DfpDec d2 = DfpDec.newInstance(complex);
        assertEquals(d1, d2);
    }

    @org.junit.Test(expected = IEEEException.class)
    public void testNewInstanceWithComplexInvalid() {
        Complex complex = new Complex(Double.NaN, 1.0);
        DfpDec d1 = new DfpDec(FACTORY, complex);
    }

    @org.junit.Test(expected = DivisionByZeroException.class)
    public void testDivideByZero() {
        DfpDec d1 = new DfpDec(FACTORY, 1);
        DfpDec d2 = new DfpDec(FACTORY, 0);
        DfpDec d3 = d1.divide(d2);
    }

    @org.junit.Test
    public void testDivide() {
        DfpDec d1 = new DfpDec(FACTORY, 4);
        DfpDec d2 = new DfpDec(FACTORY, 2);
        DfpDec d3 = d1.divide(d2);
        assertEquals(2, d3.intValue());
    }

    @org.junit.Test
    public void testDivideWithNegativeSign() {
        DfpDec d1 = new DfpDec(FACTORY, -4);
        DfpDec d2 = new DfpDec(FACTORY, 2);
        DfpDec d3 = d1.divide(d2);
        assertEquals(-2, d3.intValue());
    }

    @org.junit.Test
    public void testDivideWithSameSign() {
        DfpDec d1 = new DfpDec(FACTORY, 4);
        DfpDec d2 = new DfpDec(FACTORY, 4);
        DfpDec d3 = d1.divide(d2);
        assertEquals(1, d3.intValue());
    }

    @org.junit.Test
    public void testDivideWithNegativeSignAndSameValue() {
        DfpDec d1 = new DfpDec(FACTORY, -4);
        DfpDec d2 = new DfpDec(FACTORY, -4);
        DfpDec d3 = d1.divide(d2);
        assertEquals(-1, d3.intValue());
    }

    @org.junit.Test
    public void testDivideWithLargeDividend() {
        DfpDec d1 = new DfpDec(FACTORY, Precision.LARGE_VALUE);
        DfpDec d2 = new DfpDec(FACTORY, 1);
        DfpDec d3 = d1.divide(d2);
        assertTrue(d3.compareTo(Precision.LARGE_VALUE) < 0);
    }

    @org.junit.Test
    public void testDivideWithLargeDividendAndLargeDivisor() {
        DfpDec d1 = new DfpDec(FACTORY, Precision.LARGE_VALUE);
        DfpDec d2 = new DfpDec(FACTORY, Precision.LARGE_VALUE);
        DfpDec d3 = d1.divide(d2);
        assertTrue(d3.compareTo(ComplexFieldElement.ONE) == 0);
    }

    @org.junit.Test
    public void testDivideWithLargeDenominator() {
        DfpDec d1 = new DfpDec(FACTORY, 1);
        DfpDec d2 = new DfpDec(FACTORY, Precision.LARGE_NEGATIVE_VALUE);
        DfpDec d3 = d1.divide(d2);
        assertTrue(d3.compareTo(ComplexFieldElement.ZERO) > 0);
    }

    @org.junit.Test
    public void testDivideWithLargeDenominatorNegative() {
        DfpDec d1 = new DfpDec(FACTORY, 1);
        DfpDec d2 = new DfpDec(FACTORY, Precision.LARGE_NEGATIVE_VALUE);
        DfpDec d3 = d1.divide(d2);
        assertTrue(d3.compareTo(ComplexFieldElement.ZERO) < 0);
    }

    @org.junit.Test
    public void testDivideWithLargeDenominatorPositive() {
        DfpDec d1 = new DfpDec(FACTORY, 1);
        DfpDec d2 = new DfpDec(FACTORY, Precision.LARGE_VALUE);
        DfpDec d3 = d1.divide(d2);
        assertTrue(d3.compareTo(ComplexFieldElement.ZERO) < 0);
    }
}