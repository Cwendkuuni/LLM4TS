package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOverflow() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleEpsilon() {
        double epsilon = 1.0e-10;
        new Fraction(Math.PI, epsilon);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleMaxIterations() {
        int maxIterations = 10;
        new Fraction(Math.E, maxIterations);
    }

    @Test
    public void testConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithIntDenominator() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeNumAndDenominator() {
        new Fraction(-1, -2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeNumAndPositiveDenominator() {
        new Fraction(-1, 2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeNumAndMinNegativeDenominator() {
        new Fraction(-1, Integer.MIN_VALUE);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-1, 1);
        assertEquals(1, fraction.abs().getNumerator());
        assertEquals(1, fraction.abs().getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);

        assertEquals(-1, fraction1.compareTo(fraction2));
        assertEquals(1, fraction2.compareTo(fraction1));
        assertEquals(0, fraction1.compareTo(fraction3));
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 1e-10);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction1.equals(null));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 1e-10f);
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
        int expected = 37 * (629 + fraction.getNumerator()) + fraction.getDenominator();
        assertEquals(expected, fraction.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-1, 1);
        assertEquals(1, fraction.negate().getNumerator());
        assertEquals(1, fraction.negate().getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(1, 2);
        Fraction reciprocal = fraction.reciprocal();
        assertEquals(2, reciprocal.getNumerator());
        assertEquals(1, reciprocal.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction sum = fraction.add(1);
        assertEquals(3, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(-1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction difference = fraction.subtract(1);
        assertEquals(-1, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.addSub(fraction2, true);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());

        sum = fraction1.addSub(fraction2, false);
        assertEquals(-2, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction product = fraction.multiply(2);
        assertEquals(2, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(3, quotient.getNumerator());
        assertEquals(6, quotient.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction quotient = fraction.divide(2);
        assertEquals(0, quotient.getNumerator());
        assertEquals(1, quotient.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = new Fraction(5, 12);
        Fraction reducedFraction = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(1, reducedFraction.getNumerator());
        assertEquals(4, reducedFraction.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }
}