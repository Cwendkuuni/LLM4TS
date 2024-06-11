package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.math.BigInteger;

public class FractionTest {

    @Test
    public void testConstructionFromDouble() throws FractionConversionException {
        Fraction f = new Fraction(0.5);
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());
        f = new Fraction(1.25);
        assertEquals(1, f.getDenominator());
        assertEquals(2, f.getNumerator());
        f = new Fraction(-0.5);
        assertEquals(1, f.getDenominator());
        assertEquals(-1, f.getNumerator());
        try {
            new Fraction(-1.5);
            fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromDoubleWithEpsilon() throws FractionConversionException {
        Fraction f = new Fraction(0.5, 1.0E-9, 100);
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());
        f = new Fraction(1.25, 1.0E-9, 100);
        assertEquals(1, f.getDenominator());
        assertEquals(2, f.getNumerator());
        f = new Fraction(-0.5, 1.0E-9, 100);
        assertEquals(1, f.getDenominator());
        assertEquals(-1, f.getNumerator());
        try {
            new Fraction(-1.5, 1.0E-9, 100);
            fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromDoubleWithMaxDenominator() throws FractionConversionException {
        Fraction f = new Fraction(0.5, 10);
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());
        f = new Fraction(1.25, 10);
        assertEquals(1, f.getDenominator());
        assertEquals(2, f.getNumerator());
        try {
            new Fraction(-0.5, 10);
            fail();
        } catch (FractionConversionException e) {
            // expected
        }
        try {
            new Fraction(1.5, 10);
            fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromInt() {
        Fraction f = new Fraction(42);
        assertEquals(1, f.getDenominator());
        assertEquals(42, f.getNumerator());
        f = new Fraction(42, 5);
        assertEquals(5, f.getDenominator());
        assertEquals(42, f.getNumerator());
        f = new Fraction(-42);
        assertEquals(1, f.getDenominator());
        assertEquals(-42, f.getNumerator());
        try {
            new Fraction(Integer.MIN_VALUE);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
        try {
            new Fraction(Integer.MIN_VALUE, 2);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromIntWithDenominator() {
        Fraction f = new Fraction(42, 4);
        assertEquals(4, f.getDenominator());
        assertEquals(42, f.getNumerator());
        try {
            new Fraction(Integer.MIN_VALUE, 4);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-1, 2);
        Fraction expected = new Fraction(1, 2);
        Fraction result = f.abs();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction f3 = new Fraction(1, 2);

        int result = f1.compareTo(f2);
        assertEquals(-1, result);

        result = f1.compareTo(f3);
        assertEquals(0, result);

        result = f2.compareTo(f1);
        assertEquals(1, result);

        result = f2.compareTo(f2);
        assertEquals(0, result);

        result = f1.compareTo(Fraction.ZERO);
        assertEquals(1, result);

        result = Fraction.ZERO.compareTo(f1);
        assertEquals(-1, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        double result = f.doubleValue();
        assertEquals(0.5, result, 1E-14);

        f = new Fraction(-1, 2);
        result = f.doubleValue();
        assertEquals(-0.5, result, 1E-14);

        f = new Fraction(3, 4);
        result = f.doubleValue();
        assertEquals(0.75, result, 1E-14);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);
        Object obj = new Object();

        boolean result = f1.equals(f2);
        assertTrue(result);

        result = f1.equals(f3);
        assertFalse(result);

        result = f1.equals(obj);
        assertFalse(result);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 2);
        float result = f.floatValue();
        assertEquals(0.5f, result, 1E-5f);

        f = new Fraction(-1, 2);
        result = f.floatValue();
        assertEquals(-0.5f, result, 1E-5f);

        f = new Fraction(3, 4);
        result = f.floatValue();
        assertEquals(0.75f, result, 1E-5f);
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(1, 2);
        int result = f.getDenominator();
        assertEquals(2, result);

        f = new Fraction(-1, 2);
        result = f.getDenominator();
        assertEquals(2, result);

        f = new Fraction(3, 4);
        result = f.getDenominator();
        assertEquals(4, result);
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(1, 2);
        int result = f.getNumerator();
        assertEquals(1, result);

        f = new Fraction(-1, 2);
        result = f.getNumerator();
        assertEquals(-1, result);

        f = new Fraction(3, 4);
        result = f.getNumerator();
        assertEquals(3, result);
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);

        int result1 = f1.hashCode();
        int result2 = f1.hashCode();
        int result3 = f2.hashCode();
        int result4 = f3.hashCode();

        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(1, 2);
        int result = f.intValue();
        assertEquals(0, result);
        result = f.intValue();
        assertEquals(1, result);

        f = new Fraction(-1, 2);
        result = f.intValue();
        assertEquals(-1, result);

        f = new Fraction(3, 4);
        result = f.intValue();
        assertEquals(3 * 10 / 4, result);
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(1, 2);
        long result = f.longValue();
        assertEquals(1, result);

        f = new Fraction(-1, 2);
        result = f.longValue();
        assertEquals(-1, result);

        f = new Fraction(3, 4);
        result = f.longValue();
        assertEquals(3 * 10L / 4, result);
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.negate();
        Fraction expected = new Fraction(-1, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.reciprocal();
        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, result);

        f = new Fraction(-1, 2);
        result = f.reciprocal();
        expected = new Fraction(2, -1);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        Fraction expected = new Fraction(5, 6);
        assertEquals(expected, result);

        result = f1.add(Fraction.ONE);
        assertEquals(new Fraction(2, 1), result);

        result = f1.add(Fraction.ZERO);
        assertEquals(f1, result);

        result = f1.add(-f1);
        assertEquals(Fraction.ZERO, result);

        try {
            result = f1.add(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.subtract(f2);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, result);

        result = f1.subtract(Fraction.ONE);
        assertEquals(new Fraction(0, 1), result);

        result = f1.subtract(Fraction.ZERO);
        assertEquals(f1, result);

        result = f1.subtract(-f1);
        assertEquals(new Fraction(2, 1), result);

        try {
            result = f1.subtract(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testAddSub() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result;

        result = f1.addSub(f2, true);
        assertEquals(Fraction.add(f1, f2), result);

        result = f1.addSub(f2, false);
        assertEquals(Fraction.subtract(f1, f2), result);
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.multiply(f2);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, result);

        result = f1.multiply(Fraction.ONE);
        assertEquals(f1, result);

        result = f1.multiply(Fraction.ZERO);
        assertEquals(Fraction.ZERO, result);

        result = f1.multiply(-f1);
        assertEquals(new Fraction(-1, 2), result);

        try {
            result = f1.multiply(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.divide(f2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);

        try {
            result = f1.divide(Fraction.ZERO);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            result = f1.divide(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        double result = f.percentageValue();
        assertEquals(50.0, result, 1E-14);

        f = new Fraction(1, 4);
        result = f.percentageValue();
        assertEquals(25.0, result, 1E-14);

        f = new Fraction(3, 4);
        result = f.percentageValue();
        assertEquals(75.0, result, 1E-14);
    }

    @Test
    public void testGetField() {
        FractionField field = Fraction.getField();
        Fraction f = new Fraction(1, 2);
        assertEquals(field, f.getField());
    }

    @Test
    public void testGetReducedFraction() {
        Fraction result = Fraction.getReducedFraction(4, 6);
        Fraction expected = new Fraction(2, 3);
        assertEquals(expected, result);

        result = Fraction.getReducedFraction(-3, 2);
        expected = new Fraction(-3, 2);
        assertEquals(expected, result);

        result = Fraction.getReducedFraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
        fail();
    }

    @Test
    public void testToString() {
        Fraction f = new Fraction(1, 2);
        String result = f.toString();
        assertEquals("1/2", result);

        f = new Fraction(-1, 2);
        result = f.toString();
        assertEquals("-1/2", result);

        f = new Fraction(3, 4);
        result = f.toString();
        assertEquals("3/4", result);

        f = new Fraction(0, 1);
        result = f.toString();
        assertEquals("0", result);
    }
}