package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroDenominatorException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionFromDoubleWithValidInput() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionFromDoubleWithEpsilonAndMaxIterations() {
        Fraction fraction = new Fraction(1.5, 0.0001, 10);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionFromDoubleWithValidMaxDenominator() {
        Fraction fraction = new Fraction(1.5, 10);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromDoubleWithOverflow() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromIntWithValidInput() {
        Fraction fraction = new Fraction(2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionFromIntWithNegativeDenominator() {
        try {
            new Fraction(-2, 3);
            fail("Expected ZeroDenominatorException");
        } catch (ZeroDenominatorException e) {
            // Expected
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionFromIntWithOverflow() {
        new Fraction(Integer.MIN_VALUE);
    }

    @Test
    public void testFractionGcd() {
        Fraction fraction1 = new Fraction(2, 4);
        Fraction fraction2 = new Fraction(3, 6);
        assertEquals(1, ArithmeticUtils.gcd(fraction1.getNumerator(), fraction2.getNumerator()));
        assertEquals(1, ArithmeticUtils.gcd(fraction1.getDenominator(), fraction2.getDenominator()));
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 1);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-2, 1);
        Fraction result = fraction.abs();
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(5, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionAddNull() {
        new Fraction(1, 2).add(null);
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testFractionMultiplyByInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(3);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionMultiplyNull() {
        new Fraction(1, 2).multiply(null);
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(3, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionDivideNull() {
        new Fraction(1, 2).divide(null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionDivideByZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction zero = Fraction.ZERO;
        fraction.divide(zero);
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(66.66666666666667, fraction.percentageValue(), 0.00001);
    }

    @Test
    public void testFractionGetReducedFraction() {
        Fraction fraction = new Fraction(6, 15);
        Fraction result = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(1, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(1, 4);
        assertEquals("1/4", fraction.toString());
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField field = fraction.getField();
        assertEquals(FractionField.getInstance(), field);
    }
}