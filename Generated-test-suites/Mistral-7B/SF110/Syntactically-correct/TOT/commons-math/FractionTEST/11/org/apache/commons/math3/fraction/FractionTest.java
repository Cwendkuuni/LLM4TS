package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
        Assert.assertEquals(0.5, fraction.doubleValue(), 0.001);

        fraction = new Fraction(0.25);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());
        Assert.assertEquals(0.25, fraction.doubleValue(), 0.001);

        try {
            new Fraction(-1.5);
            Assert.fail();
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(2.5);
            Assert.fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructorWithDoubleEpsilonMaxIterations() {
        Fraction fraction = new Fraction(1.5, 0.001, 100);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
        Assert.assertEquals(0.5, fraction.doubleValue(), 0.001);

        fraction = new Fraction(0.25, 0.001, 100);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());
        Assert.assertEquals(0.25, fraction.doubleValue(), 0.001);

        try {
            new Fraction(2.5, 0.001, 100);
            Assert.fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructorWithDoubleMaxDenominator() {
        Fraction fraction = new Fraction(1.5, 10);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
        Assert.assertEquals(0.5, fraction.doubleValue(), 0.001);

        fraction = new Fraction(0.25, 10);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());
        Assert.assertEquals(0.25, fraction.doubleValue(), 0.001);

        try {
            new Fraction(2.5, 10);
            Assert.fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        Assert.assertEquals(4, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(1, 3);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());

        fraction = new Fraction(-3, 2);
        Assert.assertEquals(-3, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithIntDenominator() {
        Fraction fraction = new Fraction(4, 3);
        Assert.assertEquals(4, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());

        try {
            new Fraction(0, 2);
            Assert.fail();
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(Integer.MIN_VALUE, 1);
            Assert.fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction absFraction = fraction.abs();
        Assert.assertEquals(3, absFraction.getNumerator());
        Assert.assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);

        Assert.assertEquals(-1, fraction1.compareTo(fraction2));
        Assert.assertEquals(0, fraction1.compareTo(fraction3));
        Assert.assertEquals(1, fraction2.compareTo(fraction1));
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(1, 3);
        Assert.assertEquals(0.3333333333333333, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        Object obj = new Object();

        Assert.assertTrue(fraction1.equals(fraction1));
        Assert.assertTrue(fraction1.equals(fraction2));
        Assert.assertFalse(fraction1.equals(fraction3));
        Assert.assertFalse(fraction1.equals(obj));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(1, 3);
        Assert.assertEquals(0.333333f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(1, 3);
        Assert.assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(1, 3);
        Assert.assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(-1959942129, fraction.hashCode());
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(2, 3);
        Assert.assertEquals(2, fraction.intValue());
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 2);
        Assert.assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(1, 3);
        Fraction negateFraction = fraction.negate();
        Assert.assertEquals(-1, negateFraction.getNumerator());
        Assert.assertEquals(3, negateFraction.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(1, 3);
        Fraction reciprocalFraction = fraction.reciprocal();
        Assert.assertEquals(3, reciprocalFraction.getNumerator());
        Assert.assertEquals(1, reciprocalFraction.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.add(fraction2);
        Assert.assertEquals(2, sumFraction.getNumerator());
        Assert.assertEquals(6, sumFraction.getDenominator());
    }

    @Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(1, 3);
        Fraction sumFraction = fraction.add(2);
        Assert.assertEquals(5, sumFraction.getNumerator());
        Assert.assertEquals(3, sumFraction.getDenominator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction differenceFraction = fraction1.subtract(fraction2);
        Assert.assertEquals(1, differenceFraction.getNumerator());
        Assert.assertEquals(6, differenceFraction.getDenominator());
    }

    @Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(1, 3);
        Fraction differenceFraction = fraction.subtract(2);
        Assert.assertEquals(-1, differenceFraction.getNumerator());
        Assert.assertEquals(3, differenceFraction.getDenominator());
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.addSub(fraction2, true);
        Assert.assertEquals(2, sumFraction.getNumerator());
        Assert.assertEquals(6, sumFraction.getDenominator());

        Fraction differenceFraction = fraction1.addSub(fraction2, false);
        Assert.assertEquals(1, differenceFraction.getNumerator());
        Assert.assertEquals(6, differenceFraction.getDenominator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction productFraction = fraction1.multiply(fraction2);
        Assert.assertEquals(1, productFraction.getNumerator());
        Assert.assertEquals(6, productFraction.getDenominator());
    }

    @Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(1, 3);
        Fraction productFraction = fraction.multiply(2);
        Assert.assertEquals(2, productFraction.getNumerator());
        Assert.assertEquals(3, productFraction.getDenominator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotientFraction = fraction1.divide(fraction2);
        Assert.assertEquals(3, quotientFraction.getNumerator());
        Assert.assertEquals(2, quotientFraction.getDenominator());
    }

    @Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(1, 3);
        Fraction quotientFraction = fraction.divide(2);
        Assert.assertEquals(-1, quotientFraction.getNumerator());
        Assert.assertEquals(3, quotientFraction.getDenominator());
    }
}