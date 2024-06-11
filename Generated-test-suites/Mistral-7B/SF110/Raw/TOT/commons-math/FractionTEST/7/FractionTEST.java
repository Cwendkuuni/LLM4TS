package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructor_double() {
        Fraction f = new Fraction(1.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.75, f.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_bigValue() {
        new Fraction(1.2e308);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_epsilonTooSmall() {
        new Fraction(1.0, 0.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_maxIterations() {
        new Fraction(Math.PI, 10);
    }

    @Test
    public void testConstructor_int() {
        Fraction f = new Fraction(4);
        assertEquals(4, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructor_int_int() {
        Fraction f = new Fraction(2, 3);
        assertEquals(2, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(2, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_negative() {
        new Fraction(-2, 3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_overflow() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-3, 2);
        Fraction result = f.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction f3 = new Fraction(3, 2);

        int comparison = f1.compareTo(f2);
        assertEquals(-1, comparison);

        comparison = f1.compareTo(f1);
        assertEquals(0, comparison);

        comparison = f1.compareTo(f3);
        assertEquals(-1, comparison);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 1);

        assertTrue(f1.equals(f1));
        assertTrue(f1.equals(f2));
        assertFalse(f1.equals(f3));
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(1, 2);
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(1, 2);
        assertEquals(1, f.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 1);

        assertEquals(f1.hashCode(), f1.hashCode());
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1.hashCode(), f3.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1, f.intValue());
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(-3, 2);
        Fraction result = f.negate();
        assertEquals(-3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.add(f2);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());

        sum = f1.add(new Fraction(1));
        assertEquals(2, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction difference = f1.subtract(f2);
        assertEquals(1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());

        difference = f1.subtract(new Fraction(1));
        assertEquals(-1, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction product = f1.multiply(f2);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 2);
        Fraction quotient = f1.divide(f2);
        assertEquals(3, quotient.getNumerator());
        assertEquals(1, quotient.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 0.01);
    }

    @Test
    public void testAddSub_FractionFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.addSub(f2, true); // addition
        assertEquals(3, result.getNumerator());
        assertEquals(6, result.getDenominator());

        result = f1.addSub(f2, false); // subtraction
        assertEquals(-1, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test
    public void testMultiply_FractionFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }
}