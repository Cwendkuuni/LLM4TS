package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructionWithDoubleValue() {
        Fraction f = new Fraction(1.5);
        assertEquals(1, f.getDenominator());
        assertEquals(3, f.getNumerator());
        assertEquals(1.5, f.doubleValue(), 0.0001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithLargeDoubleValue() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructionWithEpsilonAndMaxIterations() {
        Fraction f = new Fraction(1.5, 0.001, 50);
        assertEquals(1, f.getDenominator());
        assertEquals(3, f.getNumerator());
        assertEquals(1.5, f.doubleValue(), 0.0001);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithInvalidInputForFractionConversion() {
        new Fraction(-1.5, 0.001, 50);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithTooLargeNumeratorAndDenominator() {
        new Fraction(1.0 / 0.0, 1.0 / 0.0, 1, 100);
    }

    @Test
    public void testConstructionWithIntValue() {
        Fraction f = new Fraction(4);
        assertEquals(1, f.getDenominator());
        assertEquals(4, f.getNumerator());
    }

    @Test
    public void testConstructionWithIntNumeratorAndIntDenominator() {
        Fraction f = new Fraction(1, 2);
        assertEquals(1, f.getDenominator());
        assertEquals(1, f.getNumerator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeNumeratorAndDenominator() {
        new Fraction(-1, -1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithOverFlowInNumeratorAndDenominator() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testAbsMethod() {
        Fraction f = new Fraction(-3, 2);
        assertEquals(3, f.abs().getDenominator());
        assertEquals(-3, f.abs().getNumerator());
    }

    @Test
    public void testCompareToMethod() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 1);
        Fraction f3 = new Fraction(1, 2);
        int result1 = f1.compareTo(f2);
        int result2 = f1.compareTo(f3);
        assertEquals(-1, result1);
        assertEquals(0, result2);
    }

    @Test
    public void testGetDenominatorMethod() {
        Fraction f = new Fraction(1, 2);
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testGetNumeratorMethod() {
        Fraction f = new Fraction(1, 2);
        assertEquals(1, f.getNumerator());
    }

    @Test
    public void testHashCodeMethod() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 1);
        int hashCode1 = f1.hashCode();
        int hashCode2 = f2.hashCode();
        int hashCode3 = f3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testIntValueMethod() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0, f.intValue());
    }

    @Test
    public void testLongValueMethod() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0L, f.longValue());
    }

    @Test
    public void testNegateMethod() {
        Fraction f = new Fraction(-1, 1);
        assertEquals(1, f.negate().getDenominator());
        assertEquals(-1, f.negate().getNumerator());
    }

    @Test
    public void testReciprocalMethod() {
        Fraction f = new Fraction(1, 2);
        Fraction reciprocal = f.reciprocal();
        assertEquals(2, reciprocal.getDenominator());
        assertEquals(1, reciprocal.getNumerator());
    }

    @Test
    public void testAddMethod() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.add(f2);
        assertEquals(6, sum.getDenominator());
        assertEquals(5, sum.getNumerator());
    }

    @Test
    public void testAddWithIntValue() {
        Fraction f = new Fraction(1, 2);
        Fraction sum = f.add(2);
        assertEquals(2, sum.getDenominator());
        assertEquals(3, sum.getNumerator());
    }

    @Test
    public void testSubtractMethod() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction difference = f1.subtract(f2);
        assertEquals(-1, difference.getDenominator());
        assertEquals(1, difference.getNumerator());
    }

    @Test
    public void testSubtractWithIntValue() {
        Fraction f = new Fraction(1, 2);
        Fraction difference = f.subtract(2);
        assertEquals(-4, difference.getDenominator());
        assertEquals(-3, difference.getNumerator());
    }

    @Test
    public void testAddSubMethodWithFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.addSub(f2, true);
        assertEquals(6, sum.getDenominator());
        assertEquals(5, sum.getNumerator());

        Fraction difference = f1.addSub(f2, false);
        assertEquals(-1, difference.getDenominator());
        assertEquals(1, difference.getNumerator());
    }

    @Test
    public void testAddSubMethodWithIntValue() {
        Fraction f = new Fraction(1, 2);
        Fraction sum = f.addSub(2, true);
        assertEquals(2, sum.getDenominator());
        assertEquals(3, sum.getNumerator());

        Fraction difference = f.addSub(2, false);
        assertEquals(-1, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testMultiplyMethod() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction product = f1.multiply(f2);
        assertEquals(6, product.getDenominator());
        assertEquals(2, product.getNumerator());
    }

    @Test
    public void testMultiplyWithIntValue() {
        Fraction f = new Fraction(1, 2);
        Fraction product = f.multiply(3);
        assertEquals(2, product.getDenominator());
        assertEquals(3, product.getNumerator());
    }

    @Test
    public void testDivideMethod() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 2);
        Fraction quotient = f1.divide(f2);
        assertEquals(3, quotient.getDenominator());
        assertEquals(2, quotient.getNumerator());
    }

    @Test
    public void testDivideWithIntValue() {
        Fraction f = new Fraction(1, 2);
        Fraction quotient = f.divide(3);
        assertEquals(-1, quotient.getDenominator());
        assertEquals(-1, quotient.getNumerator());
    }

    @Test
    public void testPercentageValueMethod() {
        Fraction f = new Fraction(1, 2);
        double percentageValue = f.percentageValue();
        assertEquals(50.0, percentageValue, 0.01);
    }

    @Test
    public void testToStringMethod() {
        Fraction f = new Fraction(1, 3);
        assertEquals("1/3", f.toString());
    }

    @Test
    public void testGetField() {
        Fraction f = new Fraction(1, 2);
        FractionField field = f.getField();
        assertEquals(FractionField.getInstance(), field);
    }
}