package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructionWithDouble() {
        Fraction f = new Fraction(1.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());

        Fraction f1 = new Fraction(-1.5);
        assertEquals(-1, f1.getNumerator());
        assertEquals(2, f1.getDenominator());

        try {
            new Fraction(-2.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionWithDoubleAndEpsilon() {
        Fraction f = new Fraction(1.5, 0.001, 1000);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());

        Fraction f1 = new Fraction(-1.5, 0.001, 1000);
        assertEquals(-1, f1.getNumerator());
        assertEquals(2, f1.getDenominator());

        try {
            new Fraction(-2.5, 0.001, 1000);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionWithDoubleAndMaxDenominator() {
        Fraction f = new Fraction(1.5, 10);
        assertEquals(1, f.getNumerator());
        assertEquals(5, f.getDenominator());

        Fraction f1 = new Fraction(-1.5, 10);
        assertEquals(-1, f1.getNumerator());
        assertEquals(5, f1.getDenominator());

        try {
            new Fraction(-2.5, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionWithInt() {
        Fraction f = new Fraction(4);
        assertEquals(4, f.getNumerator());
        assertEquals(1, f.getDenominator());

        Fraction f1 = new Fraction(-4);
        assertEquals(-4, f1.getNumerator());
        assertEquals(1, f1.getDenominator());
    }

    @Test
    public void testConstructionWithIntAndInt() {
        Fraction f = new Fraction(4, 3);
        assertEquals(4, f.getNumerator());
        assertEquals(3, f.getDenominator());

        Fraction f1 = new Fraction(-4, 3);
        assertEquals(-4, f1.getNumerator());
        assertEquals(3, f1.getDenominator());

        try {
            new Fraction(0, -1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(-1, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, f.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(3, 3);

        int result1 = f1.compareTo(f2);
        assertEquals(-1, result1);

        int result2 = f1.compareTo(f1);
        assertEquals(0, result2);

        int result3 = f1.compareTo(f3);
        assertEquals(1, result3);
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1.5, f.doubleValue(), 0.001);

        Fraction f1 = new Fraction(-3, 2);
        assertEquals(-1.5, f1.doubleValue(), 0.001);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);

        assertTrue(f1.equals(f2));

        assertFalse(f1.equals(f3));

        assertFalse(f1.equals(null));

        assertFalse(f1.equals(new Object()));
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(3, 2);
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(3, 2);
        assertEquals(3, f.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);

        assertEquals(f1.hashCode(), f1.hashCode());

        assertEquals(f1.hashCode(), f2.hashCode());

        assertNotEquals(f1.hashCode(), f3.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1, f.intValue());

        Fraction f1 = new Fraction(-3, 2);
        assertEquals(-1, f1.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1L, f.longValue());

        Fraction f1 = new Fraction(-3, 2);
        assertEquals(-1L, f1.longValue());
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(3, 2);
        Fraction expected = new Fraction(-3, 2);
        assertEquals(expected, f.negate());
    }

    @Test(expected = FractionConversionException.class)
    public void testNegativeDoubleConversion() {
        new Fraction(-1.5);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction f = new Fraction(1, 1);
        Fraction f1 = new Fraction(0, 1);
        f.divide(f1);
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, f.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(7, 4);
        assertEquals(expected, f1.add(f2));
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, f.add(1));
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(-1, 4);
        assertEquals(expected, f1.subtract(f2));
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(-1, 2);
        assertEquals(expected, f.subtract(1));
    }

    @Test
    public void testAddSub() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected1 = new Fraction(7, 4);
        Fraction expected2 = new Fraction(-1, 4);
        assertEquals(expected1, f1.add(f2));
        assertEquals(expected2, f1.subtract(f2));
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 8);
        assertEquals(expected, f1.multiply(f2));
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, f.multiply(1, 1, 2));
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(3, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 1);
        assertEquals(expected, f1.divide(f2));
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(3, 2);
        Fraction expected = new Fraction(3, 1);
        assertEquals(expected, f.divide(1, 1, 2));
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 0.01);
    }

    @Test(expected = FractionConversionException.class)
    public void testPercentageValueException() {
        new Fraction(0).percentageValue();
    }

    @Test
    public void testGetField() {
        Fraction f = new Fraction(1, 2);
        assertEquals(FractionField.getInstance(), f.getField());
    }
}