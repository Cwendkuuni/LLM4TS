package org.junit.tests; // replace with your test package name if needed

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.FractionConversionException;

public class FractionTest {

    

    @org.junit.Test
    public void testConstructorWithDouble() throws Exception {
        Fraction fraction = new Fraction(1.5);
        assertEquals("1/2", fraction.toString());
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOverflow() throws Exception {
        new Fraction(Double.MAX_VALUE);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleUnderflow() throws Exception {
        new Fraction(-Double.MAX_VALUE);
    }

    @org.junit.Test
    public void testConstructorWithInt() {
        Fraction fraction = new Fraction(3);
        assertEquals("3/1", fraction.toString());
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @org.junit.Test
    public void testConstructorsWithNegativeNumAndDenominator() {
        Fraction fraction = new Fraction(-2, -3);
        assertEquals("-2/3", fraction.toString());
    }

    @org.junit.Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction actual = fraction.abs();
        assertEquals("3/2", actual.toString());
    }

    @org.junit.Test
    public void testCompareToMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);
    }

    @org.junit.Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        double value = fraction.doubleValue();
        assertEquals(0.6666666666666666, value, 0.000000000000001);
    }

    @org.junit.Test
    public void testEqualsMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @org.junit.Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(1, 2);
        int denominator = fraction.getDenominator();
        assertEquals(2, denominator);
    }

    @org.junit.Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(1, 2);
        int numerator = fraction.getNumerator();
        assertEquals(1, numerator);
    }

    @org.junit.Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(1, 2);
        int hashCode = fraction.hashCode();
        int expectedHashCode = 37 * (629 + 1) + 2;
        assertEquals(expectedHashCode, hashCode);
    }

    @org.junit.Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(3, 2);
        int value = fraction.intValue();
        assertEquals(1, value);
    }

    @org.junit.Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(3, 2);
        long value = fraction.longValue();
        assertEquals(1L, value);
    }

    @org.junit.Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction actual = fraction.negate();
        assertEquals("3/2", actual.toString());
    }

    @org.junit.Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(3, 2);
        Fraction expected = new Fraction(2, 3);
        Fraction actual = fraction.reciprocal();
        assertEquals("2/3", actual.toString());
    }

    @org.junit.Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        Fraction expected = new Fraction(5, 6);
        assertEquals("5/6", sum.toString());
    }

    @org.junit.Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction difference = fraction1.subtract(fraction2);
        Fraction expected = new Fraction(2, 1);
        assertEquals("2/1", difference.toString());
    }

    @org.junit.Test
    public void testMultiplyMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction product = fraction1.multiply(fraction2);
        Fraction expected = new Fraction(1, 6);
        assertEquals("1/6", product.toString());
    }

    @org.junit.Test
    public void testDivideMethod() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotient = fraction1.divide(fraction2);
        Fraction expected = new Fraction(3, 1);
        assertEquals("3/1", quotient.toString());
    }

    @org.junit.Test
    public void testPercentageValueMethod() {
        Fraction fraction = new Fraction(1, 3);
        double percentage = fraction.percentageValue();
        assertEquals(33.33333333333333, percentage, 0.000000000000001);
    }

    @org.junit.Test
    public void testReducedFractionMethod() {
        Fraction fraction = new Fraction(2, 4);
        Fraction expected = Fraction.ZERO;
        Fraction actual = Fraction.getReducedFraction(0, 2);
        assertEquals(expected, actual);

        fraction = new Fraction(1, 5);
        expected = new Fraction(1, 5);
        actual = Fraction.getReducedFraction(1, 5);
        assertEquals(expected, actual);

        fraction = new Fraction(2, -3);
        expected = new Fraction(-2, 3);
        actual = Fraction.getReducedFraction(2, -3);
        assertEquals(expected, actual);

        fraction = new Fraction(-3, 6);
        expected = new Fraction(-1, 2);
        actual = Fraction.getReducedFraction(-3, 6);
        assertEquals(expected, actual);

        fraction = new Fraction(11, 15);
        expected = new Fraction(2, 3);
        actual = Fraction.getReducedFraction(11, 15);
        assertEquals(expected, actual);

        fraction = new Fraction(1, 2);
        expected = Fraction.ONE;
        actual = Fraction.getReducedFraction(1, 2);
        assertEquals(expected, actual);
    }

    
}