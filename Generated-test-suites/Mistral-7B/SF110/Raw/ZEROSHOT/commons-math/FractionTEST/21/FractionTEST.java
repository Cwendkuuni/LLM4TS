package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        try {
            Fraction frac = new Fraction(0.5);
            assertEquals(1, frac.getDenominator());
            assertEquals(1, frac.getNumerator());
        } catch (FractionConversionException e) {
            fail("Exception should not be thrown");
        }

        try {
            Fraction frac = new Fraction(-0.5);
            assertEquals(-1, frac.getDenominator());
            assertEquals(1, frac.getNumerator());
        } catch (FractionConversionException e) {
            fail("Exception should not be thrown");
        }

        try {
            Fraction frac = new Fraction(1.5);
            assertEquals(1, frac.getDenominator());
            assertEquals(3, frac.getNumerator());
        } catch (FractionConversionException e) {
            fail("Exception should not be thrown");
        }

        try {
            Fraction frac = new Fraction(2.3);
            assertNotNull(frac);
            assertNotSame(0, frac.getDenominator());
            assertNotSame(0, frac.getNumerator());
        } catch (FractionConversionException e) {
            fail("Exception should not be thrown");
        }

        try {
            Fraction frac = new Fraction(Double.POSITIVE_INFINITY);
            fail("Exception should be thrown");
        } catch (FractionConversionException ignored) {
            // expected
        }

        try {
            Fraction frac = new Fraction(Double.NEGATIVE_INFINITY);
            fail("Exception should be thrown");
        } catch (FractionConversionException ignored) {
            // expected
        }

        try {
            Fraction frac = new Fraction(NaN.VALUE);
            fail("Exception should be thrown");
        } catch (FractionConversionException ignored) {
            // expected
        }

        try {
            new Fraction(-0.5, 1);
            fail("Exception should be thrown");
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(3, 0);
            fail("Exception should be thrown");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithDoubleAndOptions() {
        Fraction frac = new Fraction(1.5, 1e-10, 100);
        assertEquals(1, frac.getDenominator());
        assertEquals(3, frac.getNumerator());
    }

    @Test
    public void testConstructorWithDoubleAndMaxDenominator() {
        Fraction frac = new Fraction(1.5, 100);
        assertEquals(3, frac.getDenominator());
        assertEquals(1, frac.getNumerator());
    }

    @Test
    public void testConstructorWithInt() {
        Fraction frac = new Fraction(3);
        assertEquals(1, frac.getDenominator());
        assertEquals(3, frac.getNumerator());
    }

    @Test
    public void testConstructorWithIntAndInt() {
        Fraction frac = new Fraction(3, 2);
        assertEquals(2, frac.getDenominator());
        assertEquals(3, frac.getNumerator());
    }

    @Test
    public void testNegate() {
        Fraction frac = new Fraction(1, 2);
        Fraction negated = frac.negate();
        assertEquals(-1, negated.getNumerator());
        assertEquals(2, negated.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction frac = new Fraction(2, 3);
        Fraction reciprocal = frac.reciprocal();
        assertEquals(3, reciprocal.getNumerator());
        assertEquals(2, reciprocal.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 3);
        Fraction sum = frac1.add(frac2);
        assertEquals(2, sum.getDenominator());
        assertEquals(3, sum.getNumerator());
    }

    @Test
    public void testAddInt() {
        Fraction frac = new Fraction(1, 2);
        Fraction sum = frac.add(1);
        assertEquals(1, sum.getDenominator());
        assertEquals(3, sum.getNumerator());
    }

    @Test
    public void testSubtract() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 3);
        Fraction difference = frac1.subtract(frac2);
        assertEquals(6, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testSubtractInt() {
        Fraction frac = new Fraction(1, 2);
        Fraction difference = frac.subtract(1);
        assertEquals(1, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testAddSubWithBoolean() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 3);
        Fraction sum = frac1.addSub(frac2, true);
        assertEquals(2, sum.getDenominator());
        assertEquals(3, sum.getNumerator());

        Fraction difference = frac1.addSub(frac2, false);
        assertEquals(6, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testMultiply() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 3);
        Fraction product = frac1.multiply(frac2);
        assertEquals(2, product.getDenominator());
        assertEquals(3, product.getNumerator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction frac = new Fraction(1, 2);
        Fraction product = frac.multiply(3);
        assertEquals(3, product.getDenominator());
        assertEquals(3, product.getNumerator());
    }

    @Test
    public void testDivide() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 3);
        Fraction quotient = frac1.divide(frac2);
        assertEquals(6, quotient.getDenominator());
        assertEquals(1, quotient.getNumerator());
    }

    @Test
    public void testDivideInt() {
        Fraction frac = new Fraction(1, 2);
        Fraction quotient = frac.divide(3);
        assertEquals(2, quotient.getDenominator());
        assertEquals(-1, quotient.getNumerator());
    }

    @Test
    public void testPercentageValue() {
        Fraction frac = new Fraction(1, 3);
        assertEquals(33.333333333333336, frac.percentageValue(), 0.000000000000001);
    }

    @Test
    public void testCompareTo() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 3);
        Fraction frac3 = new Fraction(1, 2);
        int comparisonResult1 = frac1.compareTo(frac2);
        int comparisonResult2 = frac2.compareTo(frac1);
        int comparisonResult3 = frac1.compareTo(frac1);

        assertEquals(-1, comparisonResult1);
        assertEquals(1, comparisonResult2);
        assertEquals(0, comparisonResult3);
    }

    @Test
    public void testGetDenominatorAndNumerator() {
        Fraction frac = new Fraction(1, 2);
        assertEquals(2, frac.getDenominator());
        assertEquals(1, frac.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction frac1 = new Fraction(1, 2);
        Fraction frac2 = new Fraction(1, 2);
        Fraction frac3 = new Fraction(1, 3);

        assertEquals(frac1.hashCode(), frac1.hashCode());
        assertEquals(frac1.hashCode(), frac2.hashCode());
        assertNotSame(frac1.hashCode(), frac3.hashCode());
    }

    @Test
    public void testToString() {
        Fraction frac = new Fraction(1, 2);
        assertEquals("1/2", frac.toString());
    }

    @Test
    public void testGetReducedFraction() {
        Fraction frac = new Fraction(3, 4);
        Fraction reducedFraction = Fraction.getReducedFraction(3, 4);
        assertEquals(reducedFraction, new Fraction(1, 2));
    }

    @Test
    public void testGetField() {
        Fraction frac = new Fraction(1, 2);
        FractionField field = frac.getField();
        assertEquals(FractionField.getInstance().getClass(), field.getClass());
    }
}