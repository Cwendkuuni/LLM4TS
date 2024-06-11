package org.junit.tests;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfRange() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(3);
        Assert.assertEquals(3, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(1, -2);
        Fraction expected = new Fraction(1, 2);
        Fraction actual = fraction.abs();
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test
    public void testCompareToMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Assert.assertThat(fraction1.compareTo(fraction2), lessThan(0));
    }

    @Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(0.5, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testEqualsMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Assert.assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(-1112273197, fraction.hashCode());
    }

    @Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(1, -2);
        Fraction expected = new Fraction(-1, 2);
        Fraction actual = fraction.negate();
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 1);
        Fraction actual = fraction.reciprocal();
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(5, 6);
        Fraction actual = fraction1.add(fraction2);
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test
    public void testAddMethodWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction actual = fraction.add(1);
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        Fraction actual = fraction1.subtract(fraction2);
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test
    public void testSubtractMethodWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(0, 2);
        Fraction actual = fraction.subtract(1);
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    // Add more test cases for other methods, such as multiply, divide, percentageValue, getReducedFraction, and toString.

    @Test
    public void testNegativeInputForAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(-1, 2);
        Fraction expected = new Fraction(0, 2);
        Fraction actual = fraction1.add(fraction2);
        Assert.assertEquals(expected.getNumerator(), actual.getNumerator());
        Assert.assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentForAddSubtractMethod() {
        Fraction fraction = new Fraction(1, 2);
        Fraction nullArgument = null;
        fraction.add(nullArgument);
        fraction.subtract(nullArgument);
    }

    @Test
    public void testMaxDenominatorAndMaxIterationsForFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5, 10, 5, 100);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testOverflowForFractionConstructorWithDouble() {
        new Fraction(1.0E+308, 1.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testOverflowForFractionConstructorWithInt() {
        new Fraction(Integer.MAX_VALUE, 1);
    }
}