package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() {
        try {
            Fraction fraction = new Fraction(0.5);
            assertEquals(1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
        } catch (FractionConversionException e) {
            fail("Expected no exception for double 0.5");
        }

        try {
            Fraction fraction = new Fraction(-0.5);
            assertEquals(-1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
        } catch (FractionConversionException e) {
            fail("Expected no exception for double -0.5");
        }

        try {
            Fraction fraction = new Fraction(1.5);
            assertEquals(1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(3, fraction.hashCode());
        } catch (FractionConversionException e) {
            fail("Expected no exception for double 1.5");
        }

        try {
            Fraction fraction = new Fraction(-1.5);
            assertEquals(-1, fraction.getDenominator());
            assertEquals(1, fraction.getNumerator());
            assertEquals(-3, fraction.hashCode());
        } catch (FractionConversionException e) {
            fail("Expected no exception for double -1.5");
        }

        try {
            Fraction fraction = new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException for Double.POSITIVE_INFINITY");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            Fraction fraction = new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException for Double.NEGATIVE_INFINITY");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            Fraction fraction = new Fraction(Double.NaN);
            fail("Expected FractionConversionException for Double.NaN");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            Fraction fraction = new Fraction(-0.0);
            fail("Expected FractionConversionException for negative zero");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            Fraction fraction = new Fraction(1.0 / 0.0);
            fail("Expected FractionConversionException for division by zero");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructionWithDoubleAndParameters() {
        Fraction fraction = new Fraction(1.0, 1.0E-5, Integer.MAX_VALUE, 100);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.hashCode());
    }

    @Test
    public void testFractionConstructionWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(1.0, Integer.MAX_VALUE);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.hashCode());
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction fraction = new Fraction(42);
        assertEquals(1, fraction.getDenominator());
        assertEquals(42, fraction.getNumerator());
    }

    @Test
    public void testFractionConstructionWithIntAndInt() {
        Fraction fraction = new Fraction(42, 13);
        assertEquals(13, fraction.getDenominator());
        assertEquals(42, fraction.getNumerator());
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-42, 13);
        Fraction result = fraction.abs();
        assertEquals(42, result.getNumerator());
        assertEquals(13, result.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction fraction3 = new Fraction(2, 3);
        Fraction fraction4 = new Fraction(1, 2);

        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);

        result = fraction1.compareTo(fraction1);
        assertEquals(0, result);

        result = fraction1.compareTo(fraction3);
        assertEquals(-1, result);

        result = fraction2.compareTo(fraction1);
        assertEquals(1, result);

        result = fraction2.compareTo(fraction2);
        assertEquals(0, result);

        result = fraction2.compareTo(fraction3);
        assertEquals(-1, result);

        result = fraction3.compareTo(fraction1);
        assertEquals(1, result);

        result = fraction3.compareTo(fraction2);
        assertEquals(1, result);

        result = fraction4.compareTo(fraction1);
        assertEquals(0, result);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        double result = fraction.doubleValue();
        assertEquals(0.5, result, 0.0);
    }

    @Test
    public void testFractionEqualsObject() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        Object obj = new Object();

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction1.equals(obj));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        float result = fraction.floatValue();
        assertEquals(0.5f, result, 0.0f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        int result1 = fraction1.hashCode();
        int result2 = fraction1.hashCode();
        int result3 = fraction2.hashCode();

        assertEquals(3, result1);
        assertEquals(3, result2);
        assertEquals(-3, result3);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.intValue();
        assertEquals(0, result);
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(1, 2);
        long result = fraction.longValue();
        assertEquals(0L, result);
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(-1, 1);
        Fraction result = fraction.negate();
        assertEquals(1, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testFractionAddFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction result = fraction1.add(fraction2);
        assertEquals(3, result.getDenominator());
        assertEquals(2, result.getNumerator());
    }

    @Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(1, 2);

        Fraction result = fraction.add(1);
        assertEquals(3, result.getDenominator());
        assertEquals(1, result.getNumerator());
    }

    @Test
    public void testFractionSubtractFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(1, 2);

        Fraction result = fraction.subtract(1);
        assertEquals(1, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionAddSubtractFractionWithIsAddTrue() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(3, result.getDenominator());
        assertEquals(2, result.getNumerator());
    }

    @Test
    public void testFractionAddSubtractFractionWithIsAddFalse() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction result = fraction1.addSub(fraction2, false);
        assertEquals(1, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionMultiplyFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getDenominator());
        assertEquals(2, result.getNumerator());
    }

    @Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);

        Fraction result = fraction.multiply(2);
        assertEquals(2, result.getDenominator());
        assertEquals(2, result.getNumerator());
    }

    @Test
    public void testFractionDivideFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction result = fraction1.divide(fraction2);
        assertEquals(3, result.getDenominator());
        assertEquals(1, result.getNumerator());
    }

    @Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(1, 2);

        Fraction result = fraction.divide(2);
        assertEquals(1, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        double result = fraction.percentageValue();
        assertEquals(50.0, result, 0.0);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField result = fraction.getField();
        assertEquals(FractionField.getInstance(), result);
    }
}