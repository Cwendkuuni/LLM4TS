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

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testConstructor_int_negativeDenominator() {
        Fraction fraction = new Fraction(-2, 3);
        assertEquals(-2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testConstructor_int_gcd() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(1, fraction.getDenominator());
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));

        Fraction fraction3 = new Fraction(2, 2);
        assertFalse(fraction1.equals(fraction3));

        Fraction fraction4 = new Fraction(1, 3);
        assertFalse(fraction1.equals(fraction4));
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction fraction3 = new Fraction(1, 2);

        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction3);

        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(0, result3);
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
        Fraction fraction = new Fraction(1, 2);
        int hashCode = fraction.hashCode();
        Fraction fraction2 = new Fraction(1, 2);
        int hashCode2 = fraction2.hashCode();

        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2, 3);
        Fraction result = fraction.abs();
        assertEquals(2, result.getNumerator());
        assertEquals(3, result.getDenominator());
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
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
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
        Fraction result = fraction.add(1);
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
        Fraction result = fraction.subtract(1);
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
        assertEquals(5, result.getDenominator());
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
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue());
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 7;
        int denominator = 14;
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(1, result.getDenominator());
        assertEquals(7, result.getNumerator());
    }
}