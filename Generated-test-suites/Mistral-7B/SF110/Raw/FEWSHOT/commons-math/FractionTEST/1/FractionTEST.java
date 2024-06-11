package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOverflow() throws FractionConversionException {
        Fraction fraction = new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testFractionConstructorWithDoubleEpsilon() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5, 0.001, 1000);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleMaxDenominator() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5, 1, 10);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithIntAndDenominator() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeDenominator() {
        new Fraction(1, -2);
    }

    @Test
    public void testFractionConstructorWithMinValueNumeratorAndDenominator() {
        Fraction fraction = new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals(-2147483648, fraction.getNumerator());
        assertEquals(-1, fraction.getDenominator());
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-1, 2);
        Fraction result = fraction.abs();
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 2);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 0.0001f);
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(1, 2);
        int hashCode = fraction.hashCode();
        Fraction fraction2 = new Fraction(1, 2);
        assertEquals(hashCode, fraction2.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-1, 2);
        Fraction result = fraction.negate();
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(1);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(1);
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddSubWithNull() {
        Fraction fraction = new Fraction(1, 2);
        try {
            Fraction result = fraction.addSub(null, true);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddSubWithNullAndIsAddFalse() {
        Fraction fraction = new Fraction(1, 2);
        try {
            Fraction result = fraction.addSub(null, false);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(2);
        assertEquals(2, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(2);
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = new Fraction(15, 25);
        Fraction result = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(1, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField field = fraction.getField();
        assertEquals(field.getZero(), new Fraction(0, 1));
    }
}