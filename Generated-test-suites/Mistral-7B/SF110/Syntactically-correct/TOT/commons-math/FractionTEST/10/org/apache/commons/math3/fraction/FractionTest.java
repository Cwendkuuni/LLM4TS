package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructionFromDouble() {
        Fraction f = new Fraction(0.5);
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());
        f = new Fraction(1.5);
        assertEquals(1, f.getDenominator());
        assertEquals(3, f.getNumerator());
        f = new Fraction(-1.5);
        assertEquals(-1, f.getDenominator());
        assertEquals(-3, f.getNumerator());
        try {
            new Fraction(-0.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {}
        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {}
        try {
            new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {}
    }

    @Test
    public void testConstructionFromInt() {
        Fraction f = new Fraction(4);
        assertEquals(1, f.getDenominator());
        assertEquals(4, f.getNumerator());
        f = new Fraction(-2, 3);
        assertEquals(-1, f.getDenominator());
        assertEquals(-2, f.getNumerator());
        f = new Fraction(0);
        assertEquals(1, f.getDenominator());
        assertEquals(0, f.getNumerator());
        try {
            new Fraction(-4);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {}
        try {
            new Fraction(Integer.MIN_VALUE, 2);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {}
    }

    @Test
    public void testConstructionFromDoubleWithEpsilon() {
        Fraction f = new Fraction(1.4142135625);
        assertEquals(1, f.getDenominator());
        assertEquals(3, f.getNumerator());
        f = new Fraction(2.718281828459045);
        assertEquals(1, f.getDenominator());
        assertEquals(10, f.getNumerator());
        f = new Fraction(-3.14159265358979323846);
        assertEquals(-1, f.getDenominator());
        assertEquals(-11, f.getNumerator());
        try {
            new Fraction(Double.POSITIVE_INFINITY, 1.0E-5, 100);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {}
        try {
            new Fraction(-Double.POSITIVE_INFINITY, 1.0E-5, 100);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {}
    }

    @Test
    public void testConstructionFromDoubleWithMaxDenominator() {
        Fraction f = new Fraction(0.123456789);
        assertEquals(1, f.getDenominator());
        assertEquals(1234567, f.getNumerator());
        try {
            new Fraction(Double.POSITIVE_INFINITY, 1, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {}
    }

    @Test
    public void testZeroFraction() {
        Fraction f = Fraction.ZERO;
        assertEquals(1, f.getDenominator());
        assertEquals(0, f.getNumerator());
    }

    @Test
    public void testOneFraction() {
        Fraction f = Fraction.ONE;
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());
    }

    @Test
    public void testTwoFractions() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        Fraction sum = a.add(b);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
        assertEquals(2.5, sum.doubleValue(), 0.001);
    }

    @Test
    public void testSubtraction() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        Fraction difference = a.subtract(b);
        assertEquals(1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
        assertEquals(1.5, difference.doubleValue(), 0.001);
    }

    @Test
    public void testMultiplication() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        Fraction product = a.multiply(b);
        assertEquals(3, product.getNumerator());
        assertEquals(4, product.getDenominator());
        assertEquals(0.75, product.doubleValue(), 0.001);
    }

    @Test
    public void testDivision() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        Fraction quotient = a.divide(b);
        assertEquals(1, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
        assertEquals(1.5, quotient.doubleValue(), 0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        Fraction a = new Fraction(3, 4);
        Fraction b = Fraction.ZERO;
        a.divide(b);
    }

    @Test
    public void testNegate() {
        Fraction a = new Fraction(3, 4);
        Fraction negated = a.negate();
        assertEquals(-3, negated.getNumerator());
        assertEquals(4, negated.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction a = new Fraction(3, 4);
        Fraction reciprocal = a.reciprocal();
        assertEquals(1, reciprocal.getNumerator());
        assertEquals(3, reciprocal.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        int comparisonResult = a.compareTo(b);
        assertEquals(1, comparisonResult);
        Fraction c = new Fraction(1, 2);
        comparisonResult = a.compareTo(c);
        assertEquals(0, comparisonResult);
        Fraction d = new Fraction(4, 3);
        comparisonResult = a.compareTo(d);
        assertEquals(-1, comparisonResult);
        Fraction e = Fraction.ZERO;
        comparisonResult = a.compareTo(e);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testToString() {
        Fraction a = new Fraction(3, 4);
        assertEquals("3/4", a.toString());
        Fraction b = new Fraction(1, -2);
        assertEquals("-1/2", b.toString());
        Fraction c = new Fraction(-3, 4);
        assertEquals("-3/4", c.toString());
        Fraction d = new Fraction(0, 1);
        assertEquals("0", d.toString());
    }
}