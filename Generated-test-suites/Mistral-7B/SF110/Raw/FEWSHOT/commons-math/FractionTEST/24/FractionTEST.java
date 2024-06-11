package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testConstructorDouble() {
        Fraction fraction = new Fraction(1.23);
        assertEquals(1, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
        assertEquals(0.2462, fraction.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleOverflow() {
        new Fraction(Double.MAX_VALUE + 1);
    }

    @Test
    public void testConstructorDoubleEpsilon() {
        Fraction fraction = new Fraction(1.23, 0.001, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
        assertEquals(0.2462, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testConstructorDoubleMaxDenominator() {
        Fraction fraction = new Fraction(1.23, 1, 10);
        assertEquals(1, fraction.getNumerator());
        assertEquals(10, fraction.getDenominator());
        assertEquals(0.123, fraction.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleMaxIterations() {
        new Fraction(1.23, 0.001, 1, 1);
    }

    @Test
    public void testConstructorInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
        assertEquals(4.0, fraction.doubleValue(), 0.0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroDenominator() {
        new Fraction(4, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorOverflow() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testConstructorIntInt() {
        Fraction fraction = new Fraction(4, 5);
        assertEquals(4, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
        assertEquals(0.8, fraction.doubleValue(), 0.01);
    }

    @Test
    public void testConstructorNegativeIntPositiveInt() {
        Fraction fraction = new Fraction(-3, 4);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        assertEquals(-0.75, fraction.doubleValue(), 0.01);
    }

    @Test
    public void testConstructorPositiveIntNegativeInt() {
        Fraction fraction = new Fraction(3, -4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(-4, fraction.getDenominator());
        assertEquals(-0.75, fraction.doubleValue(), 0.01);
    }

    @Test
    public void testConstructorGcd() {
        Fraction fraction = new Fraction(15, 20);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
        assertEquals(0.6, fraction.doubleValue(), 0.01);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction result = fraction.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(1, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult1);

        int comparisonResult2 = fraction2.compareTo(fraction1);
        assertEquals(1, comparisonResult2);

        int comparisonResult3 = fraction1.compareTo(fraction1);
        assertEquals(0, comparisonResult3);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.01);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        boolean result1 = fraction1.equals(fraction2);
        assertTrue(result1);

        boolean result2 = fraction1.equals(fraction3);
        assertFalse(result2);
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75f, fraction.floatValue(), 0.01f);
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
        int hashCode = fraction.hashCode();
        assertEquals(37 * (629 + 3) + 4, hashCode);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3 / 4, fraction.intValue(), 0);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3L / 4L, fraction.longValue(), 0L);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.negate();
        assertEquals(-3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.reciprocal();
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testAddFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction result = fraction1.add(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(8, result.getDenominator());
        assertEquals(0.625, result.doubleValue(), 0.01);
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.add(2);
        assertEquals(7, result.getNumerator());
        assertEquals(4, result.getDenominator());
        assertEquals(1.75, result.doubleValue(), 0.01);
    }

    @Test
    public void testSubtractFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
        assertEquals(0.25, result.doubleValue(), 0.01);
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.subtract(2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
        assertEquals(0.16666666666666668, result.doubleValue(), 0.0000001);
    }

    @Test
    public void testAddSubFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction result1 = fraction1.add(fraction2);
        Fraction result2 = fraction1.subtract(fraction2);

        assertEquals(5, result1.getNumerator());
        assertEquals(8, result1.getDenominator());
        assertEquals(0.625, result1.doubleValue(), 0.01);

        assertEquals(-1, result2.getNumerator());
        assertEquals(6, result2.getDenominator());
        assertEquals(-0.16666666666666668, result2.doubleValue(), 0.0000001);
    }

    @Test
    public void testMultiplyFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
        assertEquals(0.75, result.doubleValue(), 0.01);
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.multiply(2);
        assertEquals(6, result.getNumerator());
        assertEquals(4, result.getDenominator());
        assertEquals(1.5, result.doubleValue(), 0.01);
    }

    @Test
    public void testDivideFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(12, result.getDenominator());
        assertEquals(0.08333333333333332, result.doubleValue(), 0.0000001);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.divide(2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
        assertEquals(0.16666666666666668, result.doubleValue(), 0.0000001);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(37.5, fraction.percentageValue(), 0.1);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 15;
        int denominator = 20;
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(3, result.getNumerator());
        assertEquals(5, result.getDenominator());
        assertEquals(0.6, result.doubleValue(), 0.01);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("3/4", fraction.toString());
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(FractionField.getInstance(), fraction.getField());
    }
}