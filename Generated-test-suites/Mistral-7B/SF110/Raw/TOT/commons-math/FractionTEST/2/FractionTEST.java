package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        Fraction f = new Fraction(2.5);
        Assert.assertEquals(2, f.getNumerator());
        Assert.assertEquals(5, f.getDenominator());
        Assert.assertEquals(1.0, f.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOverflow() {
        new Fraction(Double.MAX_VALUE * 2.0);
    }

    @Test
    public void testConstructorWithDoubleEpsilon() {
        Fraction f = new Fraction(0.123456789, 0.000001, 100);
        Assert.assertEquals(123456789, f.getNumerator());
        Assert.assertEquals(100000000, f.getDenominator());
        Assert.assertEquals(0.123456789, f.doubleValue(), 0.0000001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleMaxIterations() {
        new Fraction(1.0, 2.0, 1, 5);
    }

    @Test
    public void testConstructorWithInts() {
        Fraction f = new Fraction(4, 5);
        Assert.assertEquals(4, f.getNumerator());
        Assert.assertEquals(5, f.getDenominator());
        Assert.assertEquals(0.8, f.doubleValue(), 0.01);
    }

    @Test
    public void testConstructorWithNegativeInts() {
        Fraction f = new Fraction(-2, 3);
        Assert.assertEquals(-2, f.getNumerator());
        Assert.assertEquals(3, f.getDenominator());
        Assert.assertEquals(-0.666666667, f.doubleValue(), 0.01);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithOverflow() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-3, 4);
        Fraction expectedAbs = new Fraction(3, 4);
        Assert.assertEquals(expectedAbs, f.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(5, 6);
        Fraction f3 = new Fraction(3, 4);
        Assert.assertThat(f1.compareTo(f2), lessThan(0));
        Assert.assertThat(f1.compareTo(f3), equals(0));
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(0.75, f.doubleValue(), 0.01);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(2, 4);
        Fraction f4 = new Object();
        Assert.assertTrue(f1.equals(f2));
        Assert.assertFalse(f1.equals(f3));
        Assert.assertFalse(f1.equals(f4));
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(4, f.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(3, f.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(2, 4);
        Assert.assertEquals(f1.hashCode(), f2.hashCode());
        Assert.assertNotEquals(f1.hashCode(), f3.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(-1, f.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(-1L, f.longValue());
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(3, 4);
        Fraction expectedNegate = new Fraction(-3, 4);
        Assert.assertEquals(expectedNegate, f.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(3, 4);
        Fraction expectedReciprocal = new Fraction(4, 3);
        Assert.assertEquals(expectedReciprocal, f.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 4);
        Fraction expectedAdd = new Fraction(7, 12);
        Assert.assertEquals(expectedAdd, f1.add(f2));
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(3, 4);
        Fraction expectedAdd = new Fraction(13, 4);
        Assert.assertEquals(expectedAdd, f.add(2));
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 4);
        Fraction expectedSubtract = new Fraction(1, 12);
        Assert.assertEquals(expectedSubtract, f1.subtract(f2));
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(3, 4);
        Fraction expectedSubtract = new Fraction(-1, 4);
        Assert.assertEquals(expectedSubtract, f.subtract(1));
    }

    @Test
    public void testAddSubWithNull() {
        Fraction f = new Fraction(3, 4);
        Assert.assertNull(f.add(null));
        Assert.assertNull(f.subtract(null));
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction expectedMultiply = new Fraction(6, 12);
        Assert.assertEquals(expectedMultiply, f1.multiply(f2));
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(3, 4);
        Fraction expectedMultiply = new Fraction(12, 4);
        Assert.assertEquals(expectedMultiply, f.multiply(3));
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 3);
        Fraction expectedDivide = new Fraction(2, 3);
        Assert.assertEquals(expectedDivide, f1.divide(f2));
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(3, 4);
        Fraction expectedDivide = new Fraction(3, 4);
        Assert.assertEquals(expectedDivide, f.divide(1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction f = new Fraction(3, 4);
        f.divide(new Fraction(0, 1));
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(75.0, f.percentageValue(), 0.1);
    }
}