package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.math.BigInteger;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(2.5);
        assertEquals(2, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleThrowsException() throws FractionConversionException {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleNegativeThrowsException() throws FractionConversionException {
        new Fraction(-2.5);
    }

    @Test
    public void testFractionConstructorWithDoubleEpsilonAndMaxIterations() throws FractionConversionException {
        Fraction fraction = new Fraction(Math.sqrt(2), 0.0001, 100);
        assertEquals(1, fraction.getDenominator());
        assertEquals((int) (Math.sqrt(2) * 2), fraction.getNumerator());
    }

    @Test
    public void testFractionConstructorWithDoubleMaxDenominator() throws FractionConversionException {
        Fraction fraction = new Fraction(3.0 / 7.0, 10);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleMaxDenominatorThrowsException() throws FractionConversionException {
        new Fraction(3.0 / 7.0, Integer.MAX_VALUE);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithIntDenominator() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals(4, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominatorThrowsException() {
        new Fraction(4, 0);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-5, 2);
        Fraction absFraction = fraction.abs();
        assertEquals(5, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(1, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult1);

        int comparisonResult2 = fraction1.compareTo(fraction1);
        assertEquals(0, comparisonResult2);

        int comparisonResult3 = fraction2.compareTo(fraction1);
        assertEquals(1, comparisonResult3);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(0.6666666666666666, fraction.doubleValue(), 0.00000000000001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals((float) 0.6666666666666666f, fraction.floatValue(), 0.00000000000001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction2.hashCode();
        int hashCode3 = fraction3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3 / 4, fraction.intValue(), 0);
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3L / 4L, fraction.longValue());
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(-5, 2);
        Fraction negateFraction = fraction.negate();
        assertEquals(5, negateFraction.getNumerator());
        assertEquals(2, negateFraction.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(3, reciprocalFraction.getNumerator());
        assertEquals(2, reciprocalFraction.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(7, sum.getNumerator());
        assertEquals(8, sum.getDenominator());
    }

    @Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction sum = fraction.add(3);
        assertEquals(7, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(-2, difference.getNumerator());
        assertEquals(8, difference.getDenominator());
    }

    @Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction difference = fraction.subtract(3);
        assertEquals(-5, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(-3, 4);
        Fraction sum = fraction1.addSub(fraction2, true);
        assertEquals(11, sum.getNumerator());
        assertEquals(8, sum.getDenominator());

        Fraction difference = fraction1.addSub(fraction2, false);
        assertEquals(-2, difference.getNumerator());
        assertEquals(8, difference.getDenominator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(3, product.getNumerator());
        assertEquals(4, product.getDenominator());
    }

    @Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction product = fraction.multiply(3);
        assertEquals(3, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(1, quotient.getNumerator());
        assertEquals(12, quotient.getDenominator());
    }

    @Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction quotient = fraction.divide(3);
        assertEquals(-1, quotient.getNumerator());
        assertEquals(6, quotient.getDenominator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testFractionGetReducedFraction() {
        Fraction fraction = new Fraction(11, 33);
        Fraction reducedFraction = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(1, reducedFraction.getDenominator());
        assertEquals(11, reducedFraction.getNumerator());
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(1, 3);
        assertEquals("1/3", fraction.toString());
    }

    @Test
    public void testFractionFromString() throws FractionConversionException {
        Fraction fraction = new Fraction("1/2");
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromStringInvalidFormatThrowsException() throws FractionConversionException {
        new Fraction("abc");
    }
}