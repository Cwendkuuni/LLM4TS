package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor_double() throws Exception {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_invalid() throws Exception {
        new Fraction(-1.5);
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(3);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
        assertEquals(3.0, fraction.doubleValue(), 0.0);
    }

    @Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_overflow() {
        new Fraction(Integer.MIN_VALUE, 1);
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
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        int comparisonResult = fraction1.compareTo(fraction2);
        int expectedComparisonResult = 1;
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        double expectedValue = 0.5;
        double resultValue = fraction.doubleValue();
        assertEquals(expectedValue, resultValue, 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        boolean expectedResult = true;
        boolean result = fraction1.equals(fraction2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        float expectedValue = 0.5f;
        float resultValue = fraction.floatValue();
        assertEquals(expectedValue, resultValue, 0.001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        int expectedValue = 2;
        int resultValue = fraction.getDenominator();
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        int expectedValue = 1;
        int resultValue = fraction.getNumerator();
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(1, 2);
        int expectedHashCode = 97285415;
        int resultHashCode = fraction.hashCode();
        assertEquals(expectedHashCode, resultHashCode);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        int expectedValue = 0;
        int resultValue = fraction.intValue();
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        long expectedValue = 0;
        long resultValue = fraction.longValue();
        assertEquals(expectedValue, resultValue);
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

    @Test(expected = NullPointerException.class)
    public void testAdd_null() {
        Fraction fraction = new Fraction(1, 2);
        fraction.add(null);
    }

    @Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(1);
        Fraction expected = new Fraction(2, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSub_Fraction_isAdd() throws Exception {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(5, 6);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSub_Fraction_isSubtract() throws Exception {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 5);
        Fraction result = fraction1.addSub(fraction2, false);
        assertEquals(expected, result);
    }

    @Test(expected = FractionConversionException.class)
    public void testAddSub_Fraction_overflow() throws Exception {
        Fraction fraction1 = new Fraction(Integer.MAX_VALUE);
        Fraction fraction2 = new Fraction(1);
        fraction1.addSub(fraction2, true);
    }

    @Test(expected = FractionConversionException.class)
    public void testAddSub_Fraction_overflow_subtract() throws Exception {
        Fraction fraction1 = new Fraction(Integer.MIN_VALUE);
        Fraction fraction2 = new Fraction(1);
        fraction1.addSub(fraction2, false);
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testMultiply_null() {
        Fraction fraction = new Fraction(1, 2);
        fraction.multiply(null);
    }

    @Test(expected = FractionConversionException.class)
    public void testMultiply_overflow() throws Exception {
        Fraction fraction1 = new Fraction(Integer.MAX_VALUE);
        Fraction fraction2 = new Fraction(1);
        fraction1.multiply(fraction2);
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(3);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDivide_null() {
        Fraction fraction = new Fraction(1, 2);
        fraction.divide(null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivide_zeroDivision() {
        Fraction fraction = new Fraction(1, 2);
        fraction.divide(new Fraction(0, 1));
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        double expectedValue = 75.0;
        double resultValue = fraction.percentageValue();
        assertEquals(expectedValue, resultValue, 0.001);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = -12345;
        int denominator = 87654321;
        Fraction expected = new Fraction(5, 15112);
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFraction_zeroDenominator() {
        int numerator = 1;
        int denominator = 0;
        Fraction.getReducedFraction(numerator, denominator);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        String expectedString = "3/4";
        String resultString = fraction.toString();
        assertEquals(expectedString, resultString);
    }
}