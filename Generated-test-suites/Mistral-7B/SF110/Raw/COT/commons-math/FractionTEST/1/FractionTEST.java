package org.junit.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testConstructor_double() {
        Fraction fraction = new Fraction(0.5);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_exception() {
        new Fraction(-1.0);
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(1);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_denominatorZero() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_overflow() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testConstructor_int_numNegative() {
        Fraction fraction = new Fraction(-2);
        assertEquals(-1, fraction.getDenominator());
        assertEquals(-2, fraction.getNumerator());
    }

    @Test
    public void testConstructor_int_numPositiveDenominatorPositive() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(3, fraction.getDenominator());
        assertEquals(2, fraction.getNumerator());
    }

    @Test
    public void testConstructor_int_numPositiveDenominatorNegative() {
        Fraction fraction = new Fraction(2, -3);
        assertEquals(-3, fraction.getDenominator());
        assertEquals(2, fraction.getNumerator());
    }

    @Test
    public void testGcd() {
        assertEquals(1, Fraction.gcd(1, 1));
        assertEquals(1, Fraction.gcd(2, 2));
        assertEquals(1, Fraction.gcd(3, 3));
        assertEquals(1, Fraction.gcd(-2, -2));
        assertEquals(-1, Fraction.gcd(-2, 2));
        assertEquals(2, Fraction.gcd(-2, 4));
        assertEquals(-1, Fraction.gcd(2, -4));
        assertEquals(3, Fraction.gcd(-3, 3));
        assertEquals(-1, Fraction.gcd(3, -3));
        assertEquals(1, Fraction.gcd(2, 4));
        assertEquals(1, Fraction.gcd(-2, 4));
        assertEquals(-1, Fraction.gcd(2, -4));
        assertEquals(1, Fraction.gcd(4, 2));
        assertEquals(-1, Fraction.gcd(2, 4));
        assertEquals(1, Fraction.gcd(4, -2));
        assertEquals(-1, Fraction.gcd(-4, 2));
        assertEquals(1, Fraction.gcd(2, 4));
        assertEquals(-1, Fraction.gcd(2, -4));
        assertEquals(1, Fraction.gcd(4, 2));
        assertEquals(-1, Fraction.gcd(-4, 2));
        assertEquals(1, Fraction.gcd(-4, -2));
        assertEquals(1, Fraction.gcd(4, -2));
        assertEquals(-1, Fraction.gcd(-4, -2));
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-2, 1);
        Fraction negatedFraction = fraction.negate();
        assertEquals(2, negatedFraction.getNumerator());
        assertEquals(1, negatedFraction.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 1);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(1, reciprocalFraction.getNumerator());
        assertEquals(2, reciprocalFraction.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(2, sum.getDenominator());
        assertEquals(3, sum.getNumerator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(2, 1);
        Fraction fraction2 = new Fraction(1, 1);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(1, difference.getDenominator());
        assertEquals(1, difference.getNumerator());
    }

    @Test
    public void testAddSub_booleanTrue() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.addSub(fraction2, true);
        assertEquals(2, sum.getDenominator());
        assertEquals(3, sum.getNumerator());
    }

    @Test
    public void testAddSub_booleanFalse() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.addSub(fraction2, false);
        assertEquals(1, difference.getDenominator());
        assertEquals(-1, difference.getNumerator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(3, product.getDenominator());
        assertEquals(2, product.getNumerator());
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction product = fraction.multiply(3);
        assertEquals(3, product.getDenominator());
        assertEquals(3, product.getNumerator());
    }

    @Test
    public void testMultiply_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(3, product.getDenominator());
        assertEquals(2, product.getNumerator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(2, 1);
        Fraction fraction2 = new Fraction(1, 1);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(1, quotient.getDenominator());
        assertEquals(2, quotient.getNumerator());
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction quotient = fraction.divide(3);
        assertEquals(1, quotient.getDenominator());
        assertEquals(-1, quotient.getNumerator());
    }

    @Test
    public void testDivide_Fraction() {
        Fraction fraction1 = new Fraction(2, 1);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(1, quotient.getDenominator());
        assertEquals(2, quotient.getNumerator());
    }
}