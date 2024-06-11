package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        Fraction f = new Fraction(2.5);
        assertEquals(2, f.getNumerator());
        assertEquals(5, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleThrowsExceptionForLargeValue() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructorWithInt() {
        Fraction f = new Fraction(4);
        assertEquals(4, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeDenominator() {
        new Fraction(1, -2);
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, f.abs());
    }

    @Test
    public void testAdd() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);
        Fraction sum = a.add(b);
        Fraction expected = new Fraction(7, 4);
        assertEquals(expected, sum);
    }

    @Test
    public void testAddWithInt() {
        Fraction a = new Fraction(1, 2);
        Fraction sum = a.add(3);
        Fraction expected = new Fraction(5, 2);
        assertEquals(expected, sum);
    }

    @Test
    public void testSubtract() {
        Fraction a = new Fraction(5, 2);
        Fraction b = new Fraction(3, 2);
        Fraction difference = a.subtract(b);
        Fraction expected = new Fraction(2, 2);
        assertEquals(expected, difference);
    }

    @Test
    public void testSubtractWithInt() {
        Fraction a = new Fraction(5, 2);
        Fraction difference = a.subtract(3);
        Fraction expected = new Fraction(2, 2);
        assertEquals(expected, difference);
    }

    @Test
    public void testMultiply() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction product = a.multiply(b);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, product);
    }

    @Test
    public void testMultiplyWithInt() {
        Fraction a = new Fraction(1, 2);
        Fraction product = a.multiply(3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, product);
    }

    @Test
    public void testReciprocal() {
        Fraction a = new Fraction(2, 3);
        Fraction reciprocal = a.reciprocal();
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, reciprocal);
    }

    @Test
    public void testNegate() {
        Fraction a = new Fraction(3, 2);
        Fraction negated = a.negate();
        Fraction expected = new Fraction(-3, 2);
        assertEquals(expected, negated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNull() {
        Fraction a = new Fraction(1, 2);
        a.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractWithNull() {
        Fraction a = new Fraction(1, 2);
        a.subtract(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyWithNull() {
        Fraction a = new Fraction(1, 2);
        a.multiply(null);
    }

    @Test
    public void testCompareTo() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 2);
        int comparison = a.compareTo(b);
        assertEquals(-1, comparison);
    }

    @Test
    public void testCompareToEquals() {
        Fraction a = new Fraction(1, 2);
        int comparison = a.compareTo(a);
        assertEquals(0, comparison);
    }
}