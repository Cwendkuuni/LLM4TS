package org.junit.tests;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    

    @Test
    public void testConstructorWithDouble() throws Exception {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOutOfRange() throws Exception {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructorWithInt() {
        Fraction fraction = new Fraction(1);
        assertEquals(1, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(0, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeDenominator() {
        new Fraction(1, -1);
    }

    @Test
    public void testConstructorWithNegativeNumber() {
        Fraction fraction = new Fraction(-3);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(-5, 6);
        Fraction absFraction = fraction.abs();
        assertEquals(5, absFraction.getNumerator());
        assertEquals(6, absFraction.getDenominator());
    }

    @Test
    public void testCompareToMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.doubleValue();
        assertEquals(0.75, result, 0.001);
    }

    @Test
    public void testEqualsMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.hashCode();
        assertEquals(37 * (629 + 1) + 2, result);
    }

    @Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1, fraction.intValue());
    }

    @Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1L, fraction.longValue());
    }

    @Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(3, 4);
        Fraction negateFraction = fraction.negate();
        assertEquals(-3, negateFraction.getNumerator());
        assertEquals(4, negateFraction.getDenominator());
    }

    @Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(2, 3);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(3, reciprocalFraction.getNumerator());
        assertEquals(2, reciprocalFraction.getDenominator());
    }

    @Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(2, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testAddMethodWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction sum = fraction.add(2);
        assertEquals(3, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(2, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testSubtractMethodWithInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction difference = fraction.subtract(1);
        assertEquals(2, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testAddSubMethodWithNullFraction() {
        Fraction fraction = new Fraction(1, 2);
        try {
            fraction.addSub(null, true);
        } catch (NullArgumentException e) {
            assertEquals("FRACTION", e.getMessage());
        }
    }

    @Test
    public void testAddSubMethodWithNegativeIsAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction difference = fraction1.addSub(fraction2, false);
        assertEquals(-1, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testMultiplyMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testMultiplyMethodWithInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction product = fraction.multiply(3);
        assertEquals(3, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testDivideMethod() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(1, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test
    public void testDivideMethodWithInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction quotient = fraction.divide(2);
        assertEquals(1, quotient.getNumerator());
        assertEquals(1, quotient.getDenominator());
    }

    @Test
    public void testPercentageValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.percentageValue();
        assertEquals(75.0, result, 0.01);
    }

    @Test
    public void testGetReducedFractionMethod() {
        Fraction fraction = new Fraction(12, 24);
        Fraction reducedFraction = Fraction.getReducedFraction(12, 24);
        assertEquals(1, reducedFraction.getNumerator());
        assertEquals(4, reducedFraction.getDenominator());
    }

    @Test
    public void testToStringMethod() {
        Fraction fraction = new Fraction(1, 2);
        String result = fraction.toString();
        assertEquals("1/2", result);
    }

    
}