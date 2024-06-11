package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructionFromDouble() throws FractionConversionException {
        Fraction fr = new Fraction(1.5);
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
        assertEquals(0.75d, fr.doubleValue(), 0.001d);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionFromTooLargeDouble() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructionFromInt() {
        Fraction fr = new Fraction(3);
        assertEquals(3, fr.getNumerator());
        assertEquals(1, fr.getDenominator());
    }

    @Test
    public void testConstructionFromIntDenominator() {
        Fraction fr = new Fraction(3, 4);
        assertEquals(3, fr.getNumerator());
        assertEquals(4, fr.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionFromNegativeDenominator() {
        new Fraction(1, -2);
    }

    @Test
    public void testAdd() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = a.add(b);
        assertEquals(2, c.getNumerator());
        assertEquals(6, c.getDenominator());
    }

    @Test
    public void testAddSelf() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 2);
        Fraction c = a.add(b);
        assertEquals(2, c.getNumerator());
        assertEquals(4, c.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(1, 3);
        Fraction c = a.subtract(b);
        assertEquals(1, c.getNumerator());
        assertEquals(3, c.getDenominator());
    }

    @Test
    public void testSubtractSelf() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 2);
        Fraction c = a.subtract(b);
        assertEquals(0, c.getNumerator());
        assertEquals(4, c.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);
        Fraction c = a.multiply(b);
        assertEquals(3, c.getNumerator());
        assertEquals(8, c.getDenominator());
    }

    @Test
    public void testMultiplySelf() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 2);
        Fraction c = a.multiply(b);
        assertEquals(1, c.getNumerator());
        assertEquals(4, c.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        Fraction c = a.divide(b);
        assertEquals(3, c.getNumerator());
        assertEquals(2, c.getDenominator());
    }

    @Test
    public void testDivideBySelf() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 2);
        Fraction c = a.divide(b);
        assertEquals(1, c.getNumerator());
        assertEquals(1, c.getDenominator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideByZero() {
        Fraction a = new Fraction(1, 2);
        a.divide(new Fraction(0, 1));
    }

    @Test
    public void testReciprocal() {
        Fraction a = new Fraction(3, 4);
        Fraction b = a.reciprocal();
        assertEquals(4, b.getNumerator());
        assertEquals(3, b.getDenominator());
    }

    @Test
    public void testNegate() {
        Fraction a = new Fraction(3, 4);
        Fraction b = a.negate();
        assertEquals(-3, b.getNumerator());
        assertEquals(4, b.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = new Fraction(1, 2);
        int result = a.compareTo(b);
        assertTrue(result < 0);
        result = a.compareTo(c);
        assertEquals(0, result);
    }

    @Test
    public void testAbs() {
        Fraction a = new Fraction(-1, 2);
        Fraction b = a.abs();
        assertEquals(1, b.getNumerator());
        assertEquals(2, b.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testOverflow() {
        new Fraction(Long.MAX_VALUE, 1);
    }

    @Test(expected = FractionConversionException.class)
    public void testDenominatorZero() {
        new Fraction(1, 0);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConversionException() {
        new Fraction(1.0d / 0.0d);
    }

    @Test
    public void testToString() {
        Fraction a = new Fraction(1, 2);
        assertEquals("1/2", a.toString());
        Fraction b = new Fraction(1, 3);
        assertEquals("1/3", b.toString());
    }

    @Test
    public void testEquals() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 2);
        Fraction c = new Fraction(1, 3);
        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
    }

    @Test
    public void testGetDenominator() {
        Fraction a = new Fraction(1, 2);
        assertEquals(2, a.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction a = new Fraction(1, 2);
        assertEquals(1, a.getNumerator());
    }

    @Test
    public void testGetField() {
        FractionField f = Fraction.getField();
        assertEquals(FractionField.getInstance(), f);
    }

    @Test
    public void testHashCode() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 2);
        Fraction c = new Fraction(1, 3);
        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    public void testValueComparison() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = new Fraction(1, 2);
        Fraction d = new Fraction(2, 2);
        Fraction e = new Fraction(1, 1);
        Fraction f = new Fraction(2, 3);
        Fraction g = new Fraction(1.5d);
        Fraction h = new Fraction(3, 2);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(a.compareTo(c) == 0);
        assertTrue(a.compareTo(d) < 0);
        assertTrue(a.compareTo(e) < 0);
        assertTrue(b.compareTo(c) > 0);
        assertTrue(b.compareTo(f) > 0);
        assertTrue(c.compareTo(g) == 0);
        assertTrue(d.compareTo(h) < 0);
    }

    @Test
    public void testPercentageValue() {
        Fraction a = new Fraction(1, 2);
        assertEquals(50.0, a.percentageValue(), 0.5);
        Fraction b = new Fraction(1, 3);
        assertEquals(33.333333333333336, b.percentageValue(), 1e-6);
    }
}