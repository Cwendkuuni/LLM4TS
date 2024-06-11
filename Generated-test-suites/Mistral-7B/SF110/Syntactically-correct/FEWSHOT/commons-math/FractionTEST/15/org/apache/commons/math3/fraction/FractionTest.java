package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructors() {
        try {
            Fraction fraction = new Fraction(-1.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        Fraction fraction1 = new Fraction(1.5);
        Fraction fraction2 = new Fraction(1.5, 1.0E-5, 100);
        Fraction fraction3 = new Fraction(1.5, 5);

        assertEquals(1.5, fraction1.doubleValue(), 0.001);
        assertEquals(1, fraction2.getNumerator());
        assertEquals(5, fraction3.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        Fraction result = fraction.abs();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 3);

        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction1);

        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(0, result3);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        double expected = 0.75;
        double result = fraction.doubleValue();

        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);

        boolean result1 = fraction1.equals(fraction2);
        boolean result2 = fraction1.equals(fraction3);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 5);
        int expected = 5;
        int result = fraction.getDenominator();

        assertEquals(expected, result);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 3;
        int result = fraction.getNumerator();

        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 1622632755;
        int result = fraction.hashCode();

        assertEquals(expected, result);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 3 * 25 / 4;
        int result = fraction.intValue();

        assertEquals(expected, result);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(5, 2);
        long expected = 5 * 25 / 2;
        long result = fraction.longValue();

        assertEquals(expected, result);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(-3, 4);
        Fraction result = fraction.negate();

        assertEquals(expected, result);
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(4, 3);
        Fraction result = fraction.reciprocal();

        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(7, 8);
        Fraction result = fraction1.add(fraction2);

        assertEquals(expected, result);
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        int value = 3;
        Fraction expected = new Fraction(5, 2);
        Fraction result = fraction.add(value);

        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 2);
        Fraction result = fraction1.subtract(fraction2);

        assertEquals(expected, result);
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(3, 2);
        int value = 1;
        Fraction expected = new Fraction(1, 2);
        Fraction result = fraction.subtract(value);

        assertEquals(expected, result);
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        boolean isAdd = true;
        Fraction expected = new Fraction(7, 8);
        Fraction result = fraction1.addSub(fraction2, isAdd);

        assertEquals(expected, result);

        isAdd = false;
        expected = new Fraction(-2, 8);
        result = fraction1.addSub(fraction2, isAdd);

        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 8);
        Fraction result = fraction1.multiply(fraction2);

        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        int value = 3;
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.multiply(value);

        assertEquals(expected, result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction1.divide(fraction2);

        assertEquals(expected, result);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 4);
        int value = 2;
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.divide(value);

        assertEquals(expected, result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        double expected = 50.0;
        double result = fraction.percentageValue();

        assertEquals(expected, result, 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 17;
        int denominator = 29;
        Fraction expected = new Fraction(1, 2);
        Fraction result = Fraction.getReducedFraction(numerator, denominator);

        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3/4";
        String result = fraction.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField expected = FractionField.getInstance();
        FractionField result = fraction.getField();

        assertEquals(expected, result);
    }
}