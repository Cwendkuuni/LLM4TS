package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() {
        Fraction fraction = new Fraction(4.0 / 3.0);
        assertEquals(new Fraction(1, 3), fraction);
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(new Fraction(4, 1), fraction);
    }

    @Test
    public void testFractionConstructionWithIntNumeratorAndIntDenominator() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(new Fraction(2, 3), fraction);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 6);
        Fraction expected = new Fraction(5, 6);
        Fraction actual = fraction.abs();
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        double expected = 0.75;
        double actual = fraction.doubleValue();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(3, 2);
        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(5, 6);
        Fraction expected = new Fraction(-5, 6);
        Fraction actual = fraction.negate();
        assertEquals(expected, actual);
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(5, 6);
        Fraction expected = new Fraction(3, 5);
        Fraction actual = fraction.reciprocal();
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(7, 12);
        Fraction actual = fraction1.add(fraction2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction expected = new Fraction(2, 3);
        Fraction actual = fraction1.subtract(fraction2);
        assertEquals(expected, actual);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(6, 12);
        Fraction actual = fraction1.multiply(fraction2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction expected = new Fraction(2, 1);
        Fraction actual = fraction1.divide(fraction2);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(5, 6);
        String expected = "5/6";
        String actual = fraction.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithMaxDenominator() {
        new Fraction(1, Integer.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithMaxIterations() {
        new Fraction(1, 1, 1000000, 1);
    }

    @Test
    public void testFractionConstructionWithEpsilon() {
        Fraction fraction = new Fraction(Math.sqrt(2), 1.0E-6, 100);
        Fraction expected = new Fraction(1, 1, 1, 1).sqrt();
        Fraction actual = fraction;
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithNegativeValue() {
        new Fraction(-1.5);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithOverflowValue() {
        new Fraction(1.5E1008);
    }

}