package org.junit.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @org.junit.Test
    public void testConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOutOfRange() {
        new Fraction(Double.MAX_VALUE);
    }

    @org.junit.Test
    public void testConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @org.junit.Test
    public void testConstructorWithIntAndInt() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(-2, 0);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeDenominatorAndPositiveNumerator() {
        new Fraction(-2, -3);
    }

    @org.junit.Test
    public void testConstructorWithNegativeDenominatorAndNegativeNumerator() {
        Fraction fraction = new Fraction(-2, -3);
        assertEquals(-2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @org.junit.Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(-2, 3);
        Fraction absFraction = fraction.abs();
        assertEquals(2, absFraction.getNumerator());
        assertEquals(3, absFraction.getDenominator());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        Fraction fraction = new Fraction(1, 1);
        fraction.compareTo(null);
    }

    @org.junit.Test
    public void testCompareToSameFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(0, comparisonResult);
    }

    @org.junit.Test
    public void testCompareToGreaterFraction() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(1, comparisonResult);
    }

    @org.junit.Test
    public void testCompareToSmallerFraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 2);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);
    }

    @org.junit.Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        double value = fraction.doubleValue();
        assertEquals(0.6666666666666666, value, 0.00000000001);
    }

    @org.junit.Test
    public void testEqualsObjectNull() {
        Fraction fraction = new Fraction(1, 1);
        assertFalse(fraction.equals(null));
    }

    @org.junit.Test
    public void testEqualsObjectSameFraction() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 1);
        assertTrue(fraction1.equals(fraction2));
    }

    @org.junit.Test
    public void testEqualsObjectDifferentFraction() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(2, 1);
        assertFalse(fraction1.equals(fraction2));
    }

    @org.junit.Test
    public void testFloatValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        float value = fraction.floatValue();
        assertEquals(0.6666667f, value, 0.0000001f);
    }

    @org.junit.Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(3, fraction.getDenominator());
    }

    @org.junit.Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
    }

    @org.junit.Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(2, 3);
        int hashCode = fraction.hashCode();
        System.out.println("Hashcode: " + hashCode);
        Fraction fraction2 = new Fraction(3, 2);
        int hashCode2 = fraction2.hashCode();
        System.out.println("Hashcode2: " + hashCode2);
        assertNotEquals(hashCode, hashCode2);
    }

    @org.junit.Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.intValue());
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testIntValueMethodWithNegativeResult() {
        Fraction fraction = new Fraction(2, 3);
        fraction.intValue();
        new Fraction(-2, 3);
    }

    @org.junit.Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        long value = fraction.longValue();
        assertEquals(2L, value);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testLongValueMethodWithNegativeResult() {
        Fraction fraction = new Fraction(2, 3);
        fraction.longValue();
        new Fraction(-2, 3);
    }

    @org.junit.Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(2, 3);
        Fraction negateFraction = fraction.negate();
        assertEquals(-2, negateFraction.getNumerator());
        assertEquals(3, negateFraction.getDenominator());
    }

    @org.junit.Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(2, 3);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(3, reciprocalFraction.getNumerator());
        assertEquals(2, reciprocalFraction.getDenominator());
    }

    @org.junit.Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @org.junit.Test
    public void testAddMethodWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction sum = fraction.add(1);
        assertEquals(5, sum.getNumerator());
        assertEquals(3, sum.getDenominator());
    }

    @org.junit.Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(1, difference.getNumerator());
        assertEquals(3, difference.getDenominator());
    }

    @org.junit.Test
    public void testSubtractMethodWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction difference = fraction.subtract(1);
        assertEquals(1, difference.getNumerator());
        assertEquals(3, difference.getDenominator());
    }

    @org.junit.Test
    public void testAddSubMethodWithNullFraction() {
        Fraction fraction = new Fraction(2, 3);
        try {
            fraction.addSub(null, true);
        } catch (NullArgumentException e) {
            assertTrue(true);
        }
    }

    @org.junit.Test
    public void testAddSubMethodWithSameFraction() {
        Fraction fraction = new Fraction(2, 3);
        Fraction sum = fraction.addSub(fraction, true);
        assertEquals(4, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @org.junit.Test
    public void testAddSubMethodWithOppositeFractions() {
        Fraction fraction = new Fraction(2, 3);
        Fraction sum = fraction.addSub(fraction, false);
        assertEquals(0, sum.getNumerator());
        assertEquals(3, sum.getDenominator());
    }

    @org.junit.Test
    public void testMultiplyMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(3, product.getNumerator());
        assertEquals(4, product.getDenominator());
    }

    @org.junit.Test
    public void testMultiplyMethodWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction product = fraction.multiply(2);
        assertEquals(4, product.getNumerator());
        assertEquals(3, product.getDenominator());
    }

    @org.junit.Test
    public void testDivideMethod() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 2);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(2, quotient.getNumerator());
        assertEquals(1, quotient.getDenominator());
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testDivideMethodWithZeroDenominatorFraction() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(0, 1);
        fraction1.divide(fraction2);
    }

    @org.junit.Test
    public void testDivideMethodWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction quotient = fraction.divide(2);
        assertEquals(1, quotient.getNumerator());
        assertEquals(3, quotient.getDenominator());
    }

    @org.junit.Test
    public void testPercentageValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        double percentageValue = fraction.percentageValue();
        assertEquals(75.0, percentageValue, 0.1);
    }

    @org.junit.Test
    public void testGetFieldMethod() {
        Fraction fraction = new Fraction(2, 3);
        FractionField field = fraction.getField();
        assertEquals(FractionField.getInstance(), field);
    }
}