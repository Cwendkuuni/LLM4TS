package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionField;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructionFromDouble() {
        Fraction f = new Fraction(1.5);
        assertEquals(3, f.getDenominator());
        assertEquals(1, f.getNumerator());

        try {
            new Fraction(-1.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(1.5, 0.0);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(Double.MAX_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(Double.MIN_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromDoubleWithEpsilonAndMaxIterations() {
        Fraction f = new Fraction(1.5, 0.001, 100);
        assertEquals(3, f.getDenominator());
        assertEquals(1, f.getNumerator());

        try {
            new Fraction(-1.5, 0.001, 100);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(1.5, 0.0, Integer.MIN_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(1.5, 0.0, 1);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromDoubleWithMaxDenominator() {
        Fraction f = new Fraction(1.5, 10);
        assertEquals(10, f.getDenominator());
        assertEquals(3, f.getNumerator());

        try {
            new Fraction(-1.5, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(1.5, Integer.MIN_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(1.5, 1);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructionFromInt() {
        Fraction f = new Fraction(1);
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());

        Fraction f2 = new Fraction(-1);
        assertEquals(1, f2.getDenominator());
        assertEquals(-1, f2.getNumerator());

        Fraction f3 = new Fraction(0);
        assertEquals(1, f3.getDenominator());
        assertEquals(0, f3.getNumerator());

        Fraction f4 = new Fraction(1, 2);
        assertEquals(2, f4.getDenominator());
        assertEquals(1, f4.getNumerator());

        Fraction f5 = new Fraction(-1, 2);
        assertEquals(2, f5.getDenominator());
        assertEquals(-1, f5.getNumerator());
    }

    @Test
    public void testConstructionFromIntWithDenominator() {
        Fraction f = new Fraction(1, 2);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());

        try {
            new Fraction(1, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(1, Integer.MIN_VALUE);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, f.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(2, 3);

        int result1 = f1.compareTo(f2);
        assertEquals(-1, result1);

        int result2 = f1.compareTo(f1);
        assertEquals(0, result2);

        int result3 = f2.compareTo(f1);
        assertEquals(1, result3);

        int result4 = f1.compareTo(f3);
        assertEquals(-1, result4);

        int result5 = f3.compareTo(f2);
        assertEquals(1, result5);

        int result6 = f2.compareTo(f3);
        assertEquals(-1, result6);
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.0001);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);

        assertTrue(f1.equals(f1));
        assertTrue(f1.equals(f2));
        assertFalse(f1.equals(f3));
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5f, f.floatValue(), 0.001f);
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
        Fraction f = new Fraction(1, 2);
        Fraction g = new Fraction(1, 2);
        Fraction h = new Fraction(1, 3);

        int hashCode1 = f.hashCode();
        int hashCode2 = g.hashCode();
        int hashCode3 = h.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0, f.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0L, f.longValue());
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(-1, 1);
        Fraction expected = new Fraction(1, 1);
        assertEquals(expected, f.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, f.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction expected = new Fraction(5, 6);
        Fraction result = f1.add(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.add(1);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(1, 6);
        Fraction result = f1.subtract(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(1, 4);
        Fraction result = f.subtract(1);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSubWithNull() {
        Fraction f = new Fraction(1, 2);
        try {
            f.add(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        try {
            f.subtract(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddSubWithZero() {
        Fraction f = new Fraction(1, 2);
        Fraction zero = Fraction.ZERO;

        Fraction expected = f;
        Fraction result = f.add(zero);
        assertEquals(expected, result);

        expected = f.negate();
        result = zero.subtract(f);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSubWithSameFraction() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 2);
        Fraction result = f.add(f);
        assertEquals(expected, result);

        expected = new Fraction(-1, 2);
        result = f.subtract(f);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 8);
        Fraction result = f1.multiply(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(5, 2);
        Fraction result = f.multiply(2);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f1.divide(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 1);
        Fraction result = f.divide(2);
        assertEquals(expected, result);
    }

    @Test
    public void testDivideByZero() {
        Fraction f = new Fraction(1, 2);
        try {
            f.divide(Fraction.ZERO);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(3, 4);
        assertEquals(75.0, f.percentageValue(), 0.01);
    }

    @Test
    public void testGetField() {
        Fraction f = new Fraction(1, 2);
        FractionField field = f.getField();
        assertEquals(FractionField.getInstance(), field);
    }
}