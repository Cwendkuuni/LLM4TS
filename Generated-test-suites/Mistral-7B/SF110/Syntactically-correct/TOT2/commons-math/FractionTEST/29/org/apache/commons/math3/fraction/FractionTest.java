
package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class FractionTest {

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithLargeValueThrowsException() {
        Fraction fraction = new Fraction(1.0E10);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithNegativeValueThrowsException() {
        Fraction fraction = new Fraction(-1.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithZeroDenominatorThrowsException() {
        Fraction fraction = new Fraction(0.5, 0);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithLargeDenominatorThrowsException() {
        Fraction fraction = new Fraction(1.0, 1.0E10);
    }

    @Test
    public void testFractionConstructorWithValidValue() {
        Fraction fraction = new Fraction(1.25);
        assertEquals(new Fraction(1, 4), fraction);
    }

    @Test
    public void testGetNumeratorAndDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testAddition() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(2, 3);
        Fraction expected = new Fraction(5, 3);
        Fraction result = a.add(b);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivisionByZeroThrowsException() {
        Fraction a = new Fraction(1, 2);
        Fraction b = Fraction.ZERO;
        a.divide(b);
    }

    @Test(expected = FractionConversionException.class)
    public void testConversionFromStringWithInvalidFormatThrowsException() {
        Fraction fraction = new Fraction("xyz/5");
    }

    @Test
    public void testConversionFromStringWithValidFormat() {
        Fraction fraction = new Fraction("3/4");
        assertEquals(new Fraction(3, 4), fraction);
    }

    @Test
    public void testComparable() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 1);
        int result = a.compareTo(b);
        assertEquals(-1, result);
    }

    @Test
    public void testNegate() {
        Fraction a = new Fraction(1, 1);
        Fraction expected = new Fraction(-1, 1);
        Fraction result = a.negate();
        assertEquals(expected, result);
    }

    @Test
    public void testReciprocal() {
        Fraction a = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 1);
        Fraction result = a.reciprocal();
        assertEquals(expected, result);
    }

    @Test
    public void testAddSub() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);
        Fraction expected = new Fraction(7, 8);
        Fraction result = a.addSub(b, true);
        assertEquals(expected, result);

        expected = new Fraction(-1, 2);
        result = a.addSub(b, false);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = a.multiply(b);
        assertEquals(expected, result);
    }

    @Test
    public void testReducedFraction() {
        Fraction fraction = new Fraction(3, 6);
        Fraction expected = new Fraction(1, 2);
        Fraction result = Fraction.getReducedFraction(fraction.getNumerator(), fraction.getDenominator());
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        String expected = "1/2";
        String result = fraction.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testFieldOperations() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = a.add(b);
        Fraction d = a.subtract(b);
        Fraction e = a.multiply(b);
        Fraction f = a.divide(b);
        assertEquals(c, new Fraction(2, 3));
        assertEquals(d, new Fraction(1, 3));
        assertEquals(e, new Fraction(3, 6));
        assertEquals(f, new Fraction(3, 2));
    }
}