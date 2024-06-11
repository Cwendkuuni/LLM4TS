package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOverflow() throws FractionConversionException {
        new Fraction(3.0E+308);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleZeroDenominator() throws FractionConversionException {
        new Fraction(-0.5);
    }

    @Test
    public void testConstructorWithDoubleEpsilonAndMaxIterations() throws FractionConversionException {
        Fraction fraction = new Fraction(Math.PI, 1.0E-9, 100);
        assertEquals(3141592653589793238L, fraction.getNumerator());
        assertEquals(2147483647L, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithDoubleMaxDenominator() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5, 10);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeDenominator() {
        new Fraction(1, -3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithIntegerDivisionByZero() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithIntegerOverflow() {
        new Fraction(Integer.MAX_VALUE + 1, 1);
    }

    @Test
    public void testConstructorWithTwoIntegers() {
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
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(4, 2);
        Fraction fraction3 = new Fraction(3, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult1);

        int comparisonResult2 = fraction1.compareTo(fraction3);
        assertEquals(0, comparisonResult2);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1.5, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(3, 2);
        Fraction fraction3 = new Fraction(4, 2);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals((float) 1.5f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 2);
        int hashCode = fraction.hashCode();
        assertEquals(37 * (629 + 3) + 2, hashCode);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.negate();
        assertEquals(-3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.reciprocal();
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(4, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(11, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.add(2);
        assertEquals(7, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.subtract(1);
        assertEquals(2, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 3);
        boolean isAdd = true;
        Fraction result = fraction1.addSub(fraction2, isAdd);
        assertEquals(7, result.getNumerator());
        assertEquals(6, result.getDenominator());

        isAdd = false;
        result = fraction1.addSub(fraction2, isAdd);
        assertEquals(-2, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(5, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.multiply(5);
        assertEquals(15, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(9, 4);
        Fraction fraction2 = new Fraction(3, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(9, 4);
        Fraction result = fraction.divide(3);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = new Fraction(15, 24);
        Fraction result = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(5, 7);
        assertEquals("5/7", fraction.toString());
    }
}