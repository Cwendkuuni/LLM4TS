package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructionWithDouble() throws FractionConversionException {
        Fraction f = new Fraction(1.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.75, f.doubleValue(), 1e-6);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithDoubleOutOfRangeDenominator() throws FractionConversionException {
        new Fraction(-1.5);
    }

    @Test
    public void testConstructionWithDoubleAndEpsilon() throws FractionConversionException {
        Fraction f = new Fraction(2.3, 1e-6, 100);
        assertEquals(46320, f.getNumerator());
        assertEquals(19683, f.getDenominator());
        assertEquals(0.11936533527446032, f.doubleValue(), 1e-8);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithDoubleAndMaxDenominator() throws FractionConversionException {
        new Fraction(1.5, 1, 3);
    }

    @Test
    public void testConstructionWithInt() {
        Fraction f = new Fraction(3);
        assertEquals(3, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeDenominator() {
        new Fraction(-3);
    }

    @Test
    public void testConstructionWithIntNumeratorAndDenominator() {
        Fraction f = new Fraction(4, 5);
        assertEquals(4, f.getNumerator());
        assertEquals(5, f.getDenominator());
        assertEquals(0.8, f.doubleValue(), 1e-6);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeNumeratorAndPositiveDenominator() {
        new Fraction(-3, 2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithZeroDenominator() {
        new Fraction(4, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructionWithLargeNumbers() {
        new Fraction(Long.MAX_VALUE, 2);
    }

    @Test(expected = NumberFormatException.class)
    public void testToString() {
        Fraction f = new Fraction(1, 3);
        String s = f.toString();
        assertEquals("1/3", s);
    }

    @Test
    public void testComparison() {
        Fraction a = new Fraction(5, 2);
        Fraction b = new Fraction(10, 4);
        Fraction c = new Fraction(5, 2);
        Fraction d = new Fraction(3, 2);

        assertTrue(a.compareTo(c) == 0);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertTrue(a.compareTo(d) > 0);
        assertTrue(d.compareTo(a) < 0);
    }

    @Test
    public void testAbs() {
        Fraction a = new Fraction(-3, 2);
        Fraction b = a.abs();
        assertEquals(3, b.getNumerator());
        assertEquals(2, b.getDenominator());
    }

    @Test
    public void testEquals() {
        Fraction a = new Fraction(5, 2);
        Fraction b = new Fraction(5, 2);
        Fraction c = new Fraction(6, 2);
        Fraction d = new Object();

        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
    }

    @Test
    public void testDoubleValue() {
        Fraction a = new Fraction(1, 2);
        assertEquals(0.5, a.doubleValue(), 1e-6);
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegateWithOverflow() {
        new Fraction(-Long.MAX_VALUE);
    }

    @Test
    public void testNegate() {
        Fraction a = new Fraction(3, 2);
        Fraction b = a.negate();
        assertEquals(-3, b.getNumerator());
        assertEquals(2, b.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction a = new Fraction(3, 2);
        Fraction b = a.reciprocal();
        assertEquals(2, b.getNumerator());
        assertEquals(3, b.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(1, 4);
        Fraction c = new Fraction(5, 12);
        Fraction d = new Fraction(1, 1);

        assertEquals(c, a.add(b));
        assertEquals(d, a.add(d));
        assertNotNull(a.add(null));
    }

    @Test
    public void testSubtract() {
        Fraction a = new Fraction(3, 2);
        Fraction b = new Fraction(1, 4);
        Fraction c = new Fraction(1, 6);
        Fraction d = new Fraction(1, 1);

        assertEquals(c, a.subtract(b));
        assertEquals(d, a.subtract(d));
        assertNotNull(a.subtract(null));
    }

    @Test
    public void testAddSubWithNull() {
        Fraction a = new Fraction(1, 3);
        Fraction b = null;

        assertEquals(a, a.add(b));
        assertEquals(a, a.subtract(b));
    }

    @Test
    public void testAddSub() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(1, 4);
        Fraction c = new Fraction(5, 12);
        Fraction d = new Fraction(1, 1);

        assertEquals(c, a.add(b));
        assertEquals(d, a.subtract(d));
        assertEquals(a, a.add(d));
        assertEquals(a, a.subtract(d));
    }

    @Test
    public void testAddSubWithLargeNumbers() {
        Fraction a = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 1);
        Fraction b = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 2);
        Fraction c = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 3);
        Fraction d = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 1);

        assertEquals(c, a.add(b));
        assertEquals(d, a.subtract(d));
    }

    @Test
    public void testMultiply() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(1, 4);
        Fraction c = new Fraction(3, 2);
        Fraction d = new Fraction(1, 1);

        assertEquals(c, a.multiply(b));
        assertEquals(d, a.multiply(d));
        assertNotNull(a.multiply(null));
    }

    @Test
    public void testMultiplyWithLargeNumbers() {
        Fraction a = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 1);
        Fraction b = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 2);
        Fraction c = new Fraction(BigInteger.valueOf(Long.MAX_VALUE), 3);

        assertEquals(c, a.multiply(b));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction a = new Fraction(1, 2);
        a.divide(new Fraction(0, 1));
    }

    @Test
    public void testDivide() {
        Fraction a = new Fraction(3, 2);
        Fraction b = new Fraction(1, 4);
        Fraction c = new Fraction(1, 2);
        Fraction d = new Fraction(1, 1);

        assertEquals(c, a.divide(b));
        assertEquals(d, a.divide(d));
        assertNull(a.divide(null));
    }

    @Test
    public void testPercentageValue() {
        Fraction a = new Fraction(3, 4);
        assertEquals(75.0, a.percentageValue(), 1e-2);
    }

    @Test
    public void testGetField() {
        Fraction a = new Fraction(1, 2);
        FractionField field = a.getField();
        assertEquals(a.getClass(), field.getFractionClass());
    }

    @Test
    public void testGetReducedFraction() {
        int num = 315;
        int denom = 16;
        Fraction f = Fraction.getReducedFraction(num, denom);
        assertEquals(21, f.getNumerator());
        assertEquals(5, f.getDenominator());
    }
}