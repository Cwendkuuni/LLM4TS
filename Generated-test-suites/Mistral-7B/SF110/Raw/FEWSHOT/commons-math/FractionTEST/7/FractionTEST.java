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

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(2, 0);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, fraction.abs());
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
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.001);
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
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 37 * (629 + fraction.getNumerator()) + fraction.getDenominator();
        assertEquals(expected, fraction.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testIntValue_positive() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testLongValue_positive() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        Fraction expected = new Fraction(2, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(2);
        Fraction expected = new Fraction(-1, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSub_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, true);
        Fraction expected = new Fraction(2, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSub_Fraction_false() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, false);
        Fraction expected = new Fraction(-1, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(2);
        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.divide(fraction2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(2);
        Fraction expected = new Fraction(1, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue(), 0.1);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = -2147483648;
        int denominator = 1;
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testGetReducedFraction_positive() {
        int numerator = 3;
        int denominator = 4;
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        String result = fraction.toString();
        String expected = "3/4";
        assertEquals(expected, result);
    }

    @Test
    public void testToString_zero() {
        Fraction fraction = Fraction.ZERO;
        String result = fraction.toString();
        String expected = "0";
        assertEquals(expected, result);
    }
}