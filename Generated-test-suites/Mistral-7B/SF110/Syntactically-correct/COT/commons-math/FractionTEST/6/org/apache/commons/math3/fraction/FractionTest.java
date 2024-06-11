package org.junit.tests;

import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    // Test constructor from double value
    
    @org.junit.Test
    public void testFractionFromDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionFromDoubleOverflow() {
        new Fraction(Double.MAX_VALUE);
    }

    // Test constructor from double value with epsilon and maxIterations

    @org.junit.Test
    public void testFractionFromDoubleWithEpsilon() {
        Fraction fraction = new Fraction(1.5, 0.001, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionFromDoubleWithMaxDenominator() {
        new Fraction(1.5, 0.001, Integer.MAX_VALUE, 100);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionFromDoubleWithMaxIterations() {
        new Fraction(1.5, 0.001, Integer.MAX_VALUE);
    }

    // Test constructor from int value

    @org.junit.Test
    public void testFractionFromInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testFractionFromZeroDenominator() {
        new Fraction(4, 0);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testFractionFromNegativeDenominator() {
        new Fraction(4, -1);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testFractionFromOverflow() {
        new Fraction(Integer.MIN_VALUE);
        new Fraction(Integer.MIN_VALUE, 2);
    }

    // Test constructor from int num and int den

    @org.junit.Test
    public void testFractionFromIntNumIntDen() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testFractionFromZeroDenominator2() {
        new Fraction(2, 0);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testFractionFromNegativeDenominator2() {
        new Fraction(2, -1);
    }

    // Test abs() method

    @org.junit.Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(4, absFraction.getDenominator());
    }

    // Test compareTo() method

    @org.junit.Test
    public void testCompareToEqual() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 1);
        int result = fraction1.compareTo(fraction2);
        assertEquals(0, result);
    }

    @org.junit.Test
    public void testCompareToLessThan() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testCompareToGreaterThan() {
        Fraction fraction1 = new Fraction(2, 2);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(1, result);
    }

    // Test doubleValue() method

    @org.junit.Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.doubleValue();
        assertEquals(0.75, result, 0.001);
    }

    // Test equals() method

    @org.junit.Test
    public void testEqualsSameObject() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = fraction1;
        assertTrue(fraction1.equals(fraction2));
    }

    @org.junit.Test
    public void testEqualsEqualFractions() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 1);
        assertTrue(fraction1.equals(fraction2));
    }

    @org.junit.Test
    public void testEqualsNotEqualFractions() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 2);
        assertFalse(fraction1.equals(fraction2));
    }

    @org.junit.Test
    public void testEqualsNullObject() {
        Fraction fraction = new Fraction(1, 1);
        assertFalse(fraction.equals(null));
    }

    // Test floatValue() method

    @org.junit.Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        float result = fraction.floatValue();
        assertEquals(0.75f, result, 0.001f);
    }

    // Test getDenominator() method

    @org.junit.Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    // Test getNumerator() method

    @org.junit.Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    // Test hashCode() method

    @org.junit.Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        int result = fraction.hashCode();
        int expected = 37 * (629 + fraction.getNumerator()) + fraction.getDenominator();
        assertEquals(expected, result);
    }

    // Test intValue() method

    @org.junit.Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0, fraction.intValue() % 3);
        assertEquals(1, fraction.intValue() % 4);
    }

    // Test longValue() method

    @org.junit.Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        long result = fraction.longValue();
        assertEquals(0L, result % 3);
        assertEquals(1L, result % 4);
    }

    // Test negate() method

    @org.junit.Test
    public void testNegate() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction negateFraction = fraction.negate();
        assertEquals(3, negateFraction.getNumerator());
        assertEquals(4, negateFraction.getDenominator());
    }

    // Test reciprocal() method

    @org.junit.Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(4, reciprocalFraction.getNumerator());
        assertEquals(3, reciprocalFraction.getDenominator());
    }

    // Test add() method

    @org.junit.Test
    public void testAddSameFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction sumFraction = fraction1.add(fraction2);
        assertEquals(2, sumFraction.getNumerator());
        assertEquals(2, sumFraction.getDenominator());
    }

    @org.junit.Test
    public void testAddDifferentFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.add(fraction2);
        assertEquals(3, sumFraction.getNumerator());
        assertEquals(6, sumFraction.getDenominator());
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testAddNullFraction() {
        Fraction fraction = new Fraction(1, 2);
        fraction.add(null);
    }

    // Test subtract() method

    @org.junit.Test
    public void testSubtractSameFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction differenceFraction = fraction1.subtract(fraction2);
        assertEquals(0, differenceFraction.getNumerator());
        assertEquals(2, differenceFraction.getDenominator());
    }

    @org.junit.Test
    public void testSubtractDifferentFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction differenceFraction = fraction1.subtract(fraction2);
        assertEquals(-1, differenceFraction.getNumerator());
        assertEquals(6, differenceFraction.getDenominator());
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testSubtractNullFraction() {
        Fraction fraction = new Fraction(1, 2);
        fraction.subtract(null);
    }

    // Test addSub() method

    @org.junit.Test
    public void testAddSubSameSign() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.addSub(fraction2, true);
        assertEquals(2, sumFraction.getNumerator());
        assertEquals(6, sumFraction.getDenominator());
    }

    @org.junit.Test
    public void testAddSubDifferentSign() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction differenceFraction = fraction1.addSub(fraction2, false);
        assertEquals(-2, differenceFraction.getNumerator());
        assertEquals(6, differenceFraction.getDenominator());
    }

    // Test multiply() method

    @org.junit.Test
    public void testMultiplySameFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction productFraction = fraction1.multiply(fraction2);
        assertEquals(1, productFraction.getNumerator());
        assertEquals(4, productFraction.getDenominator());
    }

    @org.junit.Test
    public void testMultiplyDifferentFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction productFraction = fraction1.multiply(fraction2);
        assertEquals(1, productFraction.getNumerator());
        assertEquals(6, productFraction.getDenominator());
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testMultiplyNullFraction() {
        Fraction fraction = new Fraction(1, 2);
        fraction.multiply(null);
    }

    // Test divide() method

    @org.junit.Test(expected = NullArgumentException.class)
    public void testDivideByNullFraction() {
        Fraction fraction = new Fraction(1, 2);
        fraction.divide(null);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFraction() {
        Fraction fraction = new Fraction(1, 2);
        fraction.divide(new Fraction(0, 1));
    }

    @org.junit.Test
    public void testDivideDifferentFractions() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotientFraction = fraction1.divide(fraction2);
        assertEquals(3, quotientFraction.getNumerator());
        assertEquals(6, quotientFraction.getDenominator());
    }

    // Test toString() method

    @org.junit.Test
    public void testToString() {
        Fraction fraction = new Fraction(2, 3);
        String result = fraction.toString();
        assertEquals("2/3", result);
    }

    // Test static methods

    @org.junit.Test
    public void testGetReducedFraction() {
        int numerator = 12;
        int denominator = 15;
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(4, reducedFraction.getNumerator());
        assertEquals(3, reducedFraction.getDenominator());
    }
}