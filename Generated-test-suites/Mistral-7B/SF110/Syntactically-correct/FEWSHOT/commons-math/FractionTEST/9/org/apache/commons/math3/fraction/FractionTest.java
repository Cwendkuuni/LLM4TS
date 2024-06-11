package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor_double() {
        Fraction fraction = new Fraction(1.0 / 2.0);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(-1.0 / 0.0);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_double_epsilon_maxIterations() {
        Fraction fraction = new Fraction(1.0 / 3.0, 1.0e-10, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        try {
            new Fraction(1.0 / 3.0, 1.0e-10, 10, 5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_double_maxDenominator() {
        Fraction fraction = new Fraction(1.0 / 3.0, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        try {
            new Fraction(1.0 / 3.0, 10000);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(4, 2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        try {
            new Fraction(Integer.MIN_VALUE, 1);
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

        try {
            new Fraction(0, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(-1, 1);
            fail("Expected Fraction");
        } catch (Fraction e) {
            Fraction negate = e.negate();
            assertEquals(-1, negate.getNumerator());
            assertEquals(1, negate.getDenominator());
        }
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction abs = fraction.abs();
        assertEquals(3, abs.getNumerator());
        assertEquals(2, abs.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(5, 3);
        Fraction fraction3 = new Fraction(3, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult1);

        int comparisonResult2 = fraction1.compareTo(fraction1);
        assertEquals(0, comparisonResult2);

        int comparisonResult3 = fraction2.compareTo(fraction1);
        assertEquals(1, comparisonResult3);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 2);
        double value = fraction.doubleValue();
        assertEquals(1.5, value);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(3, 2);
        Fraction fraction3 = new Fraction(4, 2);
        Object obj = new Object();

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction1.equals(obj));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 2);
        float value = fraction.floatValue();
        assertEquals(1.5f, value);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 2);
        int denominator = fraction.getDenominator();
        assertEquals(2, denominator);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 2);
        int numerator = fraction.getNumerator();
        assertEquals(3, numerator);
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 2);
        int hashCode = fraction.hashCode();
        assertEquals(-1421167884, hashCode);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 2);
        int value = fraction.intValue();
        assertEquals(1, value);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 2);
        long value = fraction.longValue();
        assertEquals(1L, value);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 2);
        Fraction negate = fraction.negate();
        assertEquals(-3, negate.getNumerator());
        assertEquals(2, negate.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 2);
        Fraction reciprocal = fraction.reciprocal();
        assertEquals(2, reciprocal.getNumerator());
        assertEquals(3, reciprocal.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(5, sum.getNumerator());
        assertEquals(4, sum.getDenominator());
    }

    @Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(3, 2);
        Fraction sum = fraction.add(2);
        assertEquals(7, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(13, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test
    public void testSubtract_int() {
        Fraction fraction = new Fraction(3, 2);
        Fraction difference = fraction.subtract(1);
        assertEquals(2, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(2, 3);
        boolean isAdd = true;
        Fraction result = fraction1.addSub(fraction2, isAdd);
        assertEquals(7, result.getNumerator());
        assertEquals(6, result.getDenominator());

        isAdd = false;
        result = fraction1.addSub(fraction2, isAdd);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(6, product.getNumerator());
        assertEquals(12, product.getDenominator());
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(3, 2);
        Fraction product = fraction.multiply(5);
        assertEquals(15, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(2, quotient.getNumerator());
        assertEquals(1, quotient.getDenominator());
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(3, 2);
        Fraction quotient = fraction.divide(2);
        assertEquals(1, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 2);
        double percentageValue = fraction.percentageValue();
        assertEquals(150.0, percentageValue);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 12;
        int denominator = 21;
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(2, reducedFraction.getNumerator());
        assertEquals(7, reducedFraction.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        String toString = fraction.toString();
        assertEquals("1/2", toString);
    }
}