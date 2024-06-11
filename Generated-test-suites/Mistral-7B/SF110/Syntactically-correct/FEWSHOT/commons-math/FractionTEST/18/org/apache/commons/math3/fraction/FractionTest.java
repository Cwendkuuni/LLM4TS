package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() {
        Fraction f = new Fraction(1.5);
        assertEquals(3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleOutOfRange() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testFractionConstructionWithDoubleAndEpsilon() {
        Fraction f = new Fraction(1.5, 0.001, 100);
        assertEquals(3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleAndEpsilonOutOfRange() {
        new Fraction(Double.MAX_VALUE, 0.001, 100);
    }

    @Test
    public void testFractionConstructionWithDoubleAndMaxDenominator() {
        Fraction f = new Fraction(1.5, 100);
        assertEquals(3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleAndMaxDenominatorOutOfRange() {
        new Fraction(Double.MAX_VALUE, 100);
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction f = new Fraction(3);
        assertEquals(3, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testFractionConstructionWithIntAndDenominator() {
        Fraction f = new Fraction(3, 2);
        assertEquals(3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructionWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructionWithNegativeDenominator() {
        new Fraction(1, -1);
    }

    @Test
    public void testFractionAbs() {
        Fraction f = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.abs();
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(1, 2);
        int result1 = f1.compareTo(f2);
        int result2 = f1.compareTo(f3);
        int expected1 = -1;
        int expected2 = 0;
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction f = new Fraction(2, 3);
        double expected = 0.6666666666666666;
        double result = f.doubleValue();
        assertEquals(expected, result, 0.000000000000001);
    }

    @Test
    public void testFractionEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);
        boolean result1 = f1.equals(f2);
        boolean result2 = f1.equals(f3);
        boolean expected1 = true;
        boolean expected2 = false;
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }

    @Test
    public void testFractionHashCode() {
        Fraction f = new Fraction(1, 2);
        int expected = 37 * (629 + f.getNumerator()) + f.getDenominator();
        int result = f.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionIntValue() {
        Fraction f = new Fraction(2, 3);
        int expected = 2;
        int result = f.intValue();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionLongValue() {
        Fraction f = new Fraction(2, 3);
        long expected = 2;
        long result = f.longValue();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionNegate() {
        Fraction f = new Fraction(3, 2);
        Fraction expected = new Fraction(-3, 2);
        Fraction result = f.negate();
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.reciprocal();
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionAddWithNull() {
        new Fraction().add(null);
    }

    @Test
    public void testFractionAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(4, 6);
        Fraction expected = new Fraction(7, 6);
        Fraction result = f1.add(f2);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
        Fraction result2 = f1.add(f3);
        Fraction expected2 = new Fraction(10, 6);
        assertEquals(expected2.getNumerator(), result2.getNumerator());
        assertEquals(expected2.getDenominator(), result2.getDenominator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(3, 4);
        Fraction expected = new Fraction(0, 4);
        Fraction result = f1.subtract(f2);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
        Fraction result2 = f1.subtract(f3);
        Fraction expected2 = new Fraction(0, 4);
        assertEquals(expected2.getNumerator(), result2.getNumerator());
        assertEquals(expected2.getDenominator(), result2.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionAddSubWithInvalidFraction() {
        new Fraction().addSub(null, true);
    }

    @Test
    public void testFractionAddSub() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(4, 6);
        Fraction result = f1.addSub(f2, true);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
        Fraction expected2 = new Fraction(-1, 6);
        Fraction result2 = f1.addSub(f2, false);
        assertEquals(expected2.getNumerator(), result2.getNumerator());
        assertEquals(expected2.getDenominator(), result2.getDenominator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 8);
        Fraction result = f1.multiply(f2);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMultiplyWithInvalidFraction() {
        new Fraction(1, 2).multiply(null);
    }

    @Test
    public void testFractionDivide() {
        Fraction f1 = new Fraction(3, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 1);
        Fraction result = f1.divide(f2);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionDivideByZero() {
        new Fraction(1, 2).divide(new Fraction(0, 1));
    }

    @Test
    public void testFractionDivide() {
        Fraction f1 = new Fraction(3, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 1);
        Fraction result = f1.divide(f2);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction f = new Fraction(3, 4);
        double expected = 75.0;
        double result = f.percentageValue();
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testFractionGetReducedFraction() {
        int numerator = 15;
        int denominator = 21;
        Fraction expected = new Fraction(1, 1);
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionGetReducedFractionWithOverflow() {
        int numerator = Integer.MIN_VALUE;
        int denominator = 2;
        Fraction.getReducedFraction(numerator, denominator);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionGetReducedFractionWithNegativeDenominator() {
        int numerator = 2;
        int denominator = -2;
        Fraction.getReducedFraction(numerator, denominator);
    }

    @Test
    public void testFractionToString() {
        Fraction f = new Fraction(3, 4);
        String expected = "3/4";
        String result = f.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionGetField() {
        Fraction f = new Fraction(1, 2);
        FractionField expected = FractionField.getInstance();
        FractionField result = f.getField();
        assertEquals(expected, result);
    }
}