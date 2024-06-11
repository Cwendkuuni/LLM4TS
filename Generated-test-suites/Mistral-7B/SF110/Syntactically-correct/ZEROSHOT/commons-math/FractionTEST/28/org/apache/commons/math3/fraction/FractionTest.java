package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testConstructionFromDouble() {
        Fraction fraction = new Fraction(1.2345);
        assertEquals(12345, fraction.getNumerator());
        assertEquals(10000, fraction.getDenominator());

        try {
            new Fraction(-1.2345);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromDoubleWithEpsilon() {
        Fraction fraction = new Fraction(1.2345, 0.0001, 100);
        assertEquals(12345, fraction.getNumerator());
        assertEquals(10000, fraction.getDenominator());
    }

    @Test
    public void testConstructionFromDoubleWithMaxDenominator() {
        Fraction fraction = new Fraction(1.2345, 10);
        assertEquals(1234, fraction.getNumerator());
        assertEquals(10, fraction.getDenominator());
    }

    @Test
    public void testConstructionFromInt() {
        Fraction fraction = new Fraction(10);
        assertEquals(10, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructionFromIntAndDenominator() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(5, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeDenominator() {
        new Fraction(1, -2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeNumberAndPositiveDenominator() {
        new Fraction(-5, 3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeNumberAndNegativeDenominator() {
        new Fraction(-5, -3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithOverflow() {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithOverflowInNumeratorAndDenominator() {
        new Fraction(2147483648.0 / 2.0, 2147483648.0 / 2.0);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 3);
        Fraction result = fraction.abs();
        assertEquals(5, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(5, 3);
        Fraction fraction2 = new Fraction(3, 2);

        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(1, comparisonResult);

        comparisonResult = fraction2.compareTo(fraction1);
        assertEquals(-1, comparisonResult);

        comparisonResult = fraction1.compareTo(fraction1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(1.6666666666666666, fraction.doubleValue(), 0.00000000000001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(5, 3);
        Fraction fraction2 = new Fraction(5, 3);
        assertTrue(fraction1.equals(fraction2));

        Fraction fraction3 = new Fraction(5, 4);
        assertFalse(fraction1.equals(fraction3));

        Fraction fraction4 = new Fraction("5/3");
        assertTrue(fraction1.equals(fraction4));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals((float) 1.6666666666666666f, fraction.floatValue(), 0.00000001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(5, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(5, 3);
        int hashCode = fraction.hashCode();

        Fraction fraction2 = new Fraction(5, 3);
        assertEquals(hashCode, fraction2.hashCode());

        Fraction fraction3 = new Fraction(5, 4);
        assertNotEquals(hashCode, fraction3.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-5, 3);
        Fraction result = fraction.negate();

        assertEquals(5, result.getNumerator());
        assertEquals(3, result.getDenominator());
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

        assertEquals(3, result.getDenominator());
        assertEquals(2, result.getNumerator());
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(1);

        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
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

        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        result = fraction.subtract(1);

        assertEquals(0, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction sum = fraction1.add(fraction2);
        Fraction difference = fraction1.subtract(fraction2);

        assertEquals(sum.getNumerator(), difference.getNumerator() + sum.getDenominator() * difference.getNumerator());
        assertEquals(sum.getDenominator(), difference.getDenominator());
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
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(3);

        assertEquals(3, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.divide(fraction2);

        assertEquals(1.5, result.doubleValue(), 0.00001);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.divide(2);

        assertEquals(1.5, result.doubleValue(), 0.00001);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, 1));

        Fraction result = Fraction.getReducedFraction(1, 2);
        assertEquals(Fraction.ONE_HALF, result);

        result = Fraction.getReducedFraction(-3, 4);
        assertEquals(Fraction.getReducedFraction(3, 4), result);

        result = Fraction.getReducedFraction(-3, -4);
        assertEquals(Fraction.getReducedFraction(3, 4), result);

        result = Fraction.getReducedFraction(3000, 4000);
        assertEquals(Fraction.getReducedFraction(3, 4), result);

        result = Fraction.getReducedFraction(123456789123456789, 123456789123456789);
        assertEquals(Fraction.ONE, result);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals("16/3", fraction.toString());
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(5, 3);
        FractionField field = fraction.getField();

        assertEquals(Fraction.class, field.getFractionClass());
    }
}