package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() {
        Fraction f = new Fraction(1.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.75, f.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleOutOfRange() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testFractionConstructionWithDoubleAndEpsilon() {
        Fraction f = new Fraction(1.5, 0.001, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.75, f.doubleValue(), 0.001);
    }

    @Test
    public void testFractionConstructionWithDoubleAndMaxDenominator() {
        Fraction f = new Fraction(1.5, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.75, f.doubleValue(), 0.001);
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction f = new Fraction(3);
        assertEquals(3, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructionWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructionWithNegativeDenominatorAndPositiveNumerator() {
        new Fraction(1, -1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructionWithNegativeNumeratorAndPositiveDenominator() {
        new Fraction(-1, 1);
    }

    @Test
    public void testFractionAbs() {
        Fraction f = new Fraction(-3, 4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
        Fraction absF = f.abs();
        assertEquals(3, absF.getNumerator());
        assertEquals(4, absF.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction f3 = new Fraction(1, 2);
        int c1 = f1.compareTo(f2);
        int c2 = f2.compareTo(f1);
        int c3 = f1.compareTo(f1);
        assertEquals(-1, c1);
        assertEquals(1, c2);
        assertEquals(0, c3);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.001);
    }

    @Test
    public void testFractionEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);
        assertTrue(f1.equals(f1));
        assertTrue(f1.equals(f2));
        assertFalse(f1.equals(f3));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5f, f.floatValue(), 0.001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction f = new Fraction(1, 2);
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction f = new Fraction(1, 2);
        assertEquals(1, f.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);
        assertEquals(f1.hashCode(), f1.hashCode());
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1.hashCode(), f3.hashCode());
    }

    @Test
    public void testFractionIntValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0, f.intValue());
    }

    @Test
    public void testFractionLongValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0L, f.longValue());
    }

    @Test
    public void testFractionNegate() {
        Fraction f = new Fraction(-3, 4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
        Fraction negateF = f.negate();
        assertEquals(3, negateF.getNumerator());
        assertEquals(4, negateF.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction f = new Fraction(2, 3);
        assertEquals(3, f.getDenominator());
        assertEquals(2, f.getNumerator());
        Fraction reciprocalF = f.reciprocal();
        assertEquals(2, reciprocalF.getDenominator());
        assertEquals(3, reciprocalF.getNumerator());
    }

    @Test
    public void testFractionAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.add(f2);
        assertEquals(2, sum.getDenominator());
        assertEquals(2, sum.getNumerator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction difference = f1.subtract(f2);
        assertEquals(1, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testFractionAddSubWithInt() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction sum = f1.add(2);
        assertEquals(1, sum.getDenominator());
        assertEquals(3, sum.getNumerator());
        Fraction diff = f1.subtract(2);
        assertEquals(1, diff.getDenominator());
        assertEquals(-1, diff.getNumerator());
    }

    @Test
    public void testFractionAddSub() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction sum = f1.add(f2);
        assertEquals(2, sum.getDenominator());
        assertEquals(2, sum.getNumerator());
        Fraction difference = f1.subtract(f2);
        assertEquals(1, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction product = f1.multiply(f2);
        assertEquals(1, product.getDenominator());
        assertEquals(2, product.getNumerator());
    }

    @Test
    public void testFractionMultiplyWithInt() {
        Fraction f = new Fraction(1, 2);
        Fraction product = f.multiply(3);
        assertEquals(3, product.getDenominator());
        assertEquals(3, product.getNumerator());
    }

    @Test
    public void testFractionDivide() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction quotient = f1.divide(f2);
        assertEquals(3, quotient.getDenominator());
        assertEquals(1, quotient.getNumerator());
    }

    @Test
    public void testFractionDivideWithInt() {
        Fraction f = new Fraction(1, 2);
        Fraction quotient = f.divide(3);
        assertEquals(3, quotient.getDenominator());
        assertEquals(-1, quotient.getNumerator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction f = new Fraction(1, 4);
        assertEquals(25.0, f.percentageValue(), 0.01);
    }

    @Test
    public void testFractionGetReducedFraction() {
        Fraction f = new Fraction(6, 8);
        Fraction reducedFraction = Fraction.getReducedFraction(f.getNumerator(), f.getDenominator());
        assertEquals(3, reducedFraction.getNumerator());
        assertEquals(4, reducedFraction.getDenominator());
    }

    @Test
    public void testFractionToString() {
        Fraction f = new Fraction(1, 2);
        assertEquals("1/2", f.toString());
    }
}