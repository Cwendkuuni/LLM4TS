package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfBounds() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testFractionConstructorWithDoubleAndEpsilon() throws FractionConversionException {
        Fraction fraction = new Fraction(1.41421356, 1e-6, 1000);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
        assertEquals(0.58778525, fraction.doubleValue(), 1e-6);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleAndMaxDenominator() throws FractionConversionException {
        new Fraction(1.5, 1, 1);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithIntAndInt() {
        Fraction fraction = new Fraction(4, 5);
        assertEquals(4, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);
        int comparisonResult1 = fraction1.compareTo(fraction2);
        int comparisonResult2 = fraction2.compareTo(fraction1);
        int comparisonResult3 = fraction1.compareTo(fraction1);
        assertEquals(-1, comparisonResult1);
        assertEquals(1, comparisonResult2);
        assertEquals(0, comparisonResult3);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.0);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(2, 3);
        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 0.0f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(2, 3);
        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction1.hashCode();
        int hashCode3 = fraction3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction negateFraction = fraction.negate();
        assertEquals(3, negateFraction.getNumerator());
        assertEquals(2, negateFraction.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(3, 2);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(2, reciprocalFraction.getNumerator());
        assertEquals(3, reciprocalFraction.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.add(fraction2);
        assertEquals(3, sumFraction.getNumerator());
        assertEquals(6, sumFraction.getDenominator());
    }

    @Test
    public void testFractionAddWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction sumFraction = fraction.add(1);
        assertEquals(3, sumFraction.getNumerator());
        assertEquals(2, sumFraction.getDenominator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction differenceFraction = fraction1.subtract(fraction2);
        assertEquals(-1, differenceFraction.getNumerator());
        assertEquals(6, differenceFraction.getDenominator());
    }

    @Test
    public void testFractionSubtractWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction differenceFraction = fraction.subtract(1);
        assertEquals(-1, differenceFraction.getNumerator());
        assertEquals(2, differenceFraction.getDenominator());
    }

    @Test
    public void testFractionAddSubWithNull() {
        Fraction fraction = new Fraction(1, 2);
        try {
            fraction.addSub(null, true);
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            fraction.addSub(null, false);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction productFraction = fraction1.multiply(fraction2);
        assertEquals(1, productFraction.getNumerator());
        assertEquals(6, productFraction.getDenominator());
    }

    @Test
    public void testFractionMultiplyWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction productFraction = fraction.multiply(2);
        assertEquals(2, productFraction.getNumerator());
        assertEquals(2, productFraction.getDenominator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotientFraction = fraction1.divide(fraction2);
        assertEquals(3, quotientFraction.getNumerator());
        assertEquals(2, quotientFraction.getDenominator());
    }

    @Test
    public void testFractionDivideWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction quotientFraction = fraction.divide(2);
        assertEquals(-1, quotientFraction.getNumerator());
        assertEquals(2, quotientFraction.getDenominator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(1, 3);
        assertEquals(33.333333333333336, fraction.percentageValue(), 0.000000001);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        assertSame(FractionField.getInstance(), fraction.getField());
    }

    @Test
    public void testFractionConstantValues() {
        assertEquals(2, Fraction.TWO.getNumerator());
        assertEquals(1, Fraction.ONE.getNumerator());
        assertEquals(0, Fraction.ZERO.getNumerator());
        assertEquals(4, Fraction.FOUR_FIFTHS.getNumerator());
        assertEquals(1, Fraction.ONE_FIFTH.getNumerator());
        assertEquals(1, Fraction.ONE_HALF.getNumerator());
        assertEquals(1, Fraction.ONE_QUARTER.getNumerator());
        assertEquals(1, Fraction.ONE_THIRD.getNumerator());
        assertEquals(3, Fraction.THREE_FIFTHS.getNumerator());
        assertEquals(3, Fraction.THREE_QUARTERS.getNumerator());
        assertEquals(2, Fraction.TWO_FIFTHS.getNumerator());
        assertEquals(2, Fraction.TWO_QUARTERS.getNumerator());
        assertEquals(2, Fraction.TWO_THIRDS.getNumerator());
        assertEquals(-1, Fraction.MINUS_ONE.getNumerator());
    }
}