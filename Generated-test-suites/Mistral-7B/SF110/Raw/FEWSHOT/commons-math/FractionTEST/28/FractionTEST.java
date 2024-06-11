package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructors() {
        // Test default constructor
        assertEquals(0, new Fraction().getNumerator());
        assertEquals(1, new Fraction().getDenominator());

        // Test double constructor
        Fraction f1 = new Fraction(2.5);
        assertEquals(2, f1.getNumerator());
        assertEquals(5, f1.getDenominator());

        // Test double constructor with epsilon and maxIterations
        Fraction f2 = new Fraction(2.5, 0.001, 100);
        assertEquals(2, f2.getNumerator());
        assertEquals(5, f2.getDenominator());

        // Test double constructor with maxDenominator
        Fraction f3 = new Fraction(2.5, 10);
        assertEquals(2, f3.getNumerator());
        assertEquals(10, f3.getDenominator());

        // Test long constructor
        Fraction f4 = new Fraction(3L);
        assertEquals(3, f4.getNumerator());
        assertEquals(1, f4.getDenominator());

        // Test int constructor
        Fraction f5 = new Fraction(4);
        assertEquals(4, f5.getNumerator());
        assertEquals(1, f5.getDenominator());

        // Test int constructor with denominator
        Fraction f6 = new Fraction(4, 2);
        assertEquals(2, f6.getNumerator());
        assertEquals(2, f6.getDenominator());
    }

    @Test
    public void testBasicOperations() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);

        // Test absolute value
        Fraction absA = a.abs();
        assertEquals(1, absA.getNumerator());
        assertEquals(2, absA.getDenominator());

        // Test comparison
        int cmp = a.compareTo(b);
        assertEquals(-1, cmp);

        // Test double value
        double dValA = a.doubleValue();
        assertEquals(0.5, dValA, 0.001);

        // Test equals
        Fraction c = new Fraction(1, 2);
        assertTrue(a.equals(c));

        // Test float value
        float fValA = a.floatValue();
        assertEquals(0.5F, fValA, 0.001F);

        // Test get denominator and numerator
        assertEquals(2, a.getDenominator());
        assertEquals(1, a.getNumerator());

        // Test hashCode
        int hashA = a.hashCode();
        assertEquals(-1031232366, hashA);

        // Test int value
        int iValA = a.intValue();
        assertEquals(0, iValA);

        // Test long value
        long lValA = a.longValue();
        assertEquals(0L, lValA);

        // Test negate
        Fraction negA = a.negate();
        assertEquals(-1, negA.getNumerator());
        assertEquals(2, negA.getDenominator());

        // Test reciprocal
        Fraction recipA = a.reciprocal();
        assertEquals(2, recipA.getNumerator());
        assertEquals(1, recipA.getDenominator());

        // Test addition
        Fraction sum = a.add(b);
        assertEquals(3, sum.getNumerator());
        assertEquals(4, sum.getDenominator());

        // Test addition with int
        Fraction sumInt = a.add(1);
        assertEquals(3, sumInt.getNumerator());
        assertEquals(2, sumInt.getDenominator());

        // Test subtraction
        Fraction diff = a.subtract(b);
        assertEquals(-1, diff.getNumerator());
        assertEquals(8, diff.getDenominator());

        // Test subtraction with int
        Fraction diffInt = a.subtract(1);
        assertEquals(-1, diffInt.getNumerator());
        assertEquals(2, diffInt.getDenominator());

        // Test multiplication
        Fraction prod = a.multiply(b);
        assertEquals(3, prod.getNumerator());
        assertEquals(8, prod.getDenominator());

        // Test multiplication with int
        Fraction prodInt = a.multiply(2);
        assertEquals(2, prodInt.getNumerator());
        assertEquals(2, prodInt.getDenominator());

        // Test division
        Fraction quot = a.divide(b);
        assertEquals(1, quot.getNumerator());
        assertEquals(6, quot.getDenominator());

        // Test division with int
        Fraction quotInt = a.divide(2);
        assertEquals(1, quotInt.getNumerator());
        assertEquals(2, quotInt.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction p = new Fraction(3, 4);
        assertEquals(75.0, p.percentageValue(), 0.01);
    }

    @Test
    public void testStaticMethods() {
        // Test getReducedFraction
        int numerator = 21;
        int denominator = -14;
        Fraction f = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(3, f.getNumerator());
        assertEquals(-7, f.getDenominator());

        // Test toString
        Fraction g = new Fraction(1, 3);
        assertEquals("1/3", g.toString());
    }
}