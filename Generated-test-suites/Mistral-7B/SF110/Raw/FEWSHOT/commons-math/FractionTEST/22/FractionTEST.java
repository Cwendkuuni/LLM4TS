package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.FractionField;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorDouble() {
        try {
            Fraction fraction = new Fraction(0.5);
            assertEquals(1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(0.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(-0.5);
            assertEquals(-1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(-0.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(1.5);
            assertEquals(1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(1.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(2.5);
            assertEquals(1, fraction.getDenominator());
            assertEquals(2, fraction.getNumerator());
            assertEquals(2.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(Double.NaN);
            fail("Expected FractionConversionException but got: " + fraction);
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            Fraction fraction = new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException but got: " + fraction);
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            Fraction fraction = new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException but got: " + fraction);
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructorDoubleWithEpsilonAndMaxIterations() {
        try {
            Fraction fraction = new Fraction(0.5, 0.001, 1000);
            assertEquals(1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(0.5, fraction.doubleValue(), 0.001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(-0.5, 0.001, 1000);
            assertEquals(-1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(-0.5, fraction.doubleValue(), 0.001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(1.5, 0.001, 1000);
            assertEquals(1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(1.5, fraction.doubleValue(), 0.001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(2.5, 0.001, 1000);
            assertEquals(1, fraction.getDenominator());
            assertEquals(2, fraction.getNumerator());
            assertEquals(2.5, fraction.doubleValue(), 0.001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(2.5, 0.001, 10);
            assertEquals(1, fraction.getDenominator());
            assertEquals(2, fraction.getNumerator());
            assertEquals(2.5, fraction.doubleValue(), 0.002);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFractionConstructorDoubleWithMaxDenominator() {
        try {
            Fraction fraction = new Fraction(0.5, 1000000);
            assertEquals(2, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(0.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(-0.5, 1000000);
            assertEquals(-2, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(-0.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(1.5, 1000000);
            assertEquals(3, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(1.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Fraction fraction = new Fraction(2.5, 1000000);
            assertEquals(4, fraction.getDenominator());
            assertEquals(2, fraction.getNumerator());
            assertEquals(2.5, fraction.doubleValue(), 0.0001);
        } catch (FractionConversionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFractionConstructorInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(1, fraction.getDenominator());
        assertEquals(4, fraction.getNumerator());
    }

    @Test
    public void testFractionConstructorIntInt() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(3, fraction.getDenominator());
        assertEquals(2, fraction.getNumerator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorIntIntWithZeroDenominator() {
        new Fraction(2, 0);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.abs();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 1);

        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction3);
        int result4 = fraction3.compareTo(fraction1);

        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(1, result3);
        assertEquals(-1, result4);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        double result = fraction.doubleValue();
        assertEquals(0.6666666666666666, result, 0.000000000000001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(3, 2);
        Fraction fraction3 = new Fraction(2, 3);
        Object object = new Object();

        boolean result1 = fraction1.equals(fraction2);
        boolean result2 = fraction1.equals(fraction3);
        boolean result3 = fraction1.equals(object);

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(2, 3);
        float result = fraction.floatValue();
        assertEquals(0.666667f, result, 0.000001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(2, 3);
        int result = fraction.getDenominator();
        assertEquals(3, result);
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(2, 3);
        int result = fraction.getNumerator();
        assertEquals(2, result);
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction = new Fraction(3, 2);
        int result = fraction.hashCode();
        assertEquals(-1831869265, result);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(3, 2);
        int result = fraction.intValue();
        assertEquals(1, result);
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 2);
        long result = fraction.longValue();
        assertEquals(2, result);
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(3, 2);
        Fraction expected = new Fraction(-3, 2);
        Fraction result = fraction.negate();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(3, 2);
        Fraction expected = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(4, 2);
        Fraction result = fraction1.add(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(3, 2);
        int number = 1;
        Fraction expected = new Fraction(7, 2);
        Fraction result = fraction.add(number);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(1, 2);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(3, 2);
        int number = 1;
        Fraction expected = new Fraction(1, 2);
        Fraction result = fraction.subtract(number);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 1);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(expected, result);

        expected = new Fraction(-1, 2);
        result = fraction1.addSub(fraction2, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 1);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(3, 2);
        int number = 1;
        Fraction expected = new Fraction(3, 1);
        Fraction result = fraction.multiply(number);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 1);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(3, 2);
        int number = 1;
        Fraction expected = new Fraction(3, 1);
        Fraction result = fraction.divide(number);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.percentageValue();
        assertEquals(75.0, result, 0.0);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(3, 2);
        FractionField expected = FractionField.getInstance();
        FractionField result = fraction.getField();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(3, 2);
        String result = fraction.toString();
        assertEquals("3/2", result);
    }
}