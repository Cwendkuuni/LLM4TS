package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor_double() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_overflow() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_epsilon() {
        new Fraction(0.1, 1.0E-10, 100);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_maxIterations() {
        new Fraction(Math.PI, 10);
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction result = fraction.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareTo_equal() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_greater() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(1, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testEquals_false() {
        Fraction fraction1 = new Fraction(1, 2);
        Object obj = new Object();
        assertFalse(fraction1.equals(obj));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertEquals(fraction1.hashCode(), fraction2.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testIntValue_negative() {
        Fraction fraction = new Fraction(-1, 1);
        assertEquals(-1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.reciprocal();
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtract_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(2);
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(3, result.getNumerator());
        assertEquals(6, result.getDenominator());

        result = fraction1.addSub(fraction2, false);
        assertEquals(-2, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(2);
        assertEquals(2, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(2);
        assertEquals(1, result.getNumerator());
        assertEquals(-2, result.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 3);
        assertEquals(33.333333333333336, fraction.percentageValue(), 0.000000000000001);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = new Fraction(5, 8);
        Fraction result = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }
}