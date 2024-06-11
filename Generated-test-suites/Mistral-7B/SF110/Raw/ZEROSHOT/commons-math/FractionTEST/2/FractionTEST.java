package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorFromDouble() throws Exception {
        Fraction f = new Fraction(2.5);
        assertEquals(5, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorFromDoubleWithOverflow() throws Exception {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructorFromDoubleWithEpsilon() throws Exception {
        Fraction f = new Fraction(2.5, 1e-10, 100);
        assertEquals(5, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorFromDoubleWithMaxDenominator() throws Exception {
        new Fraction(2.5, 10);
    }

    @Test
    public void testConstructorFromInt() {
        Fraction f = new Fraction(3);
        assertEquals(3, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorFromIntDenominator() {
        Fraction f = new Fraction(3, 5);
        assertEquals(3, f.getNumerator());
        assertEquals(5, f.getDenominator());
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
    public void testAbs() {
        Fraction f = new Fraction(-2, 3);
        Fraction expected = new Fraction(2, 3);
        Fraction result = f.abs();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        int cmp = f1.compareTo(f2);
        assertEquals(-1, cmp);
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        double value = f.doubleValue();
        assertEquals(0.5, value, 1e-16);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        boolean result = f1.equals(f2);
        assertTrue(result);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 2);
        float value = f.floatValue();
        assertEquals(0.5f, value, 1e-16f);
    }

    @Test
    public void testHashCode() {
        Fraction f = new Fraction(1, 2);
        int hashCode = f.hashCode();
        int expectedHashCode = 1038322897; // computed using the hashCode() method source code
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(1, 2);
        int value = f.intValue();
        assertEquals(0, value);
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(1, 2);
        long value = f.longValue();
        assertEquals(0L, value);
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(2, 3);
        Fraction expected = new Fraction(-2, 3);
        Fraction result = f.negate();
        assertEquals(expected, result);
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.reciprocal();
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.add(f2);
        Fraction expected = new Fraction(5, 6);
        Fraction result = f1.add(f2);
        assertEquals(expected, result);
        assertEquals(expected, sum);
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 2);
        Fraction sum = f.add(1);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.add(1);
        assertEquals(expected, result);
        assertEquals(expected, sum);
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction difference = f1.subtract(f2);
        Fraction expected = new Fraction(1, 6);
        Fraction result = f1.subtract(f2);
        assertEquals(expected, result);
        assertEquals(expected, difference);
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(1, 2);
        Fraction difference = f.subtract(1);
        Fraction expected = new Fraction(-1, 2);
        Fraction result = f.subtract(1);
        assertEquals(expected, result);
        assertEquals(expected, difference);
    }

    @Test
    public void testAddSub() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.addSub(f2, true);
        Fraction difference = f1.addSub(f2, false);
        Fraction expectedSum = new Fraction(5, 6);
        Fraction expectedDifference = new Fraction(2, 6);
        Fraction resultSum = f1.addSub(f2, true);
        Fraction resultDifference = f1.addSub(f2, false);
        assertEquals(expectedSum, resultSum);
        assertEquals(expectedSum, sum);
        assertEquals(expectedDifference, resultDifference);
        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction product = f1.multiply(f2);
        Fraction expected = new Fraction(1, 6);
        Fraction result = f1.multiply(f2);
        assertEquals(expected, result);
        assertEquals(expected, product);
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(1, 2);
        Fraction product = f.multiply(3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.multiply(3);
        assertEquals(expected, result);
        assertEquals(expected, product);
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction quotient = f1.divide(f2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f1.divide(f2);
        assertEquals(expected, result);
        assertEquals(expected, quotient);
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(1, 2);
        Fraction quotient = f.divide(3);
        Fraction expected = new Fraction(1, 6);
        Fraction result = f.divide(3);
        assertEquals(expected, result);
        assertEquals(expected, quotient);
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        double percentageValue = f.percentageValue();
        assertEquals(50.0, percentageValue, 1e-16);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction f = new Fraction(3, 5);
        Fraction expected = new Fraction(1, 2);
        Fraction result = Fraction.getReducedFraction(3, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        Fraction f = new Fraction(1, 2);
        String toString = f.toString();
        assertEquals("1/2", toString);
    }

    @Test
    public void testField() {
        Fraction f = new Fraction(1, 2);
        FractionField field = f.getField();
        assertNotNull(field);
        assertEquals(FractionField.getInstance(), field);
    }
}