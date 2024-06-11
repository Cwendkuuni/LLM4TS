package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() {
        Fraction fraction = new Fraction(0.5);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(1.25);
        assertEquals(1, fraction.getDenominator());
        assertEquals(2, fraction.getNumerator());

        fraction = new Fraction(-0.5);
        assertEquals(1, fraction.getDenominator());
        assertEquals(-1, fraction.getNumerator());

        try {
            new Fraction(-1.5);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructionWithDoubleAndOptions() {
        Fraction fraction = new Fraction(0.5, 1.0E-6, 1000);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(1.25, 1.0E-6, 1000, 100);
        assertEquals(1, fraction.getDenominator());
        assertEquals(2, fraction.getNumerator());

        fraction = new Fraction(-0.5, 1.0E-6, 1000, 100);
        assertEquals(1, fraction.getDenominator());
        assertEquals(-1, fraction.getNumerator());

        try {
            new Fraction(-1.5, 1.0E-6, 1000, 100);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructionWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(0.5, 10);
        assertEquals(2, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(1.25, 10);
        assertEquals(4, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(-0.5, 10);
        assertEquals(2, fraction.getDenominator());
        assertEquals(-1, fraction.getNumerator());

        try {
            new Fraction(-1.5, 10);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction fraction = new Fraction(1);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(-1);
        assertEquals(1, fraction.getDenominator());
        assertEquals(-1, fraction.getNumerator());

        try {
            new Fraction(-1, 0);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructionWithIntAndInt() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        fraction = new Fraction(-1, 2);
        assertEquals(2, fraction.getDenominator());
        assertEquals(-1, fraction.getNumerator());

        fraction = new Fraction(0, 1);
        assertEquals(1, fraction.getDenominator());
        assertEquals(0, fraction.getNumerator());

        try {
            new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-1, 2);
        Fraction absoluteFraction = fraction.abs();
        assertEquals(1, absoluteFraction.getDenominator());
        assertEquals(1, absoluteFraction.getNumerator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);

        int compareResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, compareResult1);

        int compareResult2 = fraction2.compareTo(fraction1);
        assertEquals(1, compareResult2);

        int compareResult3 = fraction1.compareTo(fraction1);
        assertEquals(0, compareResult3);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.00001);

        fraction = new Fraction(-1, 2);
        assertEquals(-0.5, fraction.doubleValue(), 0.00001);

        fraction = new Fraction(1, 3);
        assertEquals(0.3333333333333333, fraction.doubleValue(), 0.0000000000000001);

        fraction = new Fraction(2, 3);
        assertEquals(0.6666666666666667, fraction.doubleValue(), 0.0000000000000001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 0.00001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction1.hashCode();
        int hashCode3 = fraction3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0, fraction.intValue());
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0L, fraction.longValue());
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(1, 2);
        Fraction negatedFraction = fraction.negate();
        assertEquals(-1, negatedFraction.getDenominator());
        assertEquals(-1, negatedFraction.getNumerator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(1, 2);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(2, reciprocalFraction.getDenominator());
        assertEquals(1, reciprocalFraction.getNumerator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(3, sum.getDenominator());
        assertEquals(2, sum.getNumerator());
    }

    @Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction sum = fraction.add(1);
        assertEquals(3, sum.getDenominator());
        assertEquals(1, sum.getNumerator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(6, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction difference = fraction.subtract(1);
        assertEquals(1, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.addSub(fraction2, true);
        assertEquals(3, sum.getDenominator());
        assertEquals(2, sum.getNumerator());

        Fraction difference = fraction1.addSub(fraction2, false);
        assertEquals(6, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(3, product.getDenominator());
        assertEquals(1, product.getNumerator());
    }

    @Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction product = fraction.multiply(3);
        assertEquals(6, product.getDenominator());
        assertEquals(3, product.getNumerator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(6, quotient.getDenominator());
        assertEquals(3, quotient.getNumerator());
    }

    @Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction quotient = fraction.divide(3);
        assertEquals(2, quotient.getDenominator());
        assertEquals(-1, quotient.getNumerator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(1, 3);
        assertEquals(33.33333333333333, fraction.percentageValue(), 0.0000000000000001);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField fractionField = fraction.getField();
        assertTrue(fractionField instanceof FractionField);
    }
}