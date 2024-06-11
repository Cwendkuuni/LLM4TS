package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        try {
            Fraction fraction = new Fraction(-1.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        Fraction fraction = new Fraction(0.5);
        assertEquals(Fraction.ONE_HALF, fraction);

        Fraction fraction1 = new Fraction(1.5);
        Fraction fraction2 = new Fraction(3, 2);
        assertEquals(fraction2, fraction1);
    }

    @Test
    public void testConstructorWithDoubleAndEpsilon() {
        Fraction fraction = new Fraction(1.718281828, 0.0000001, 1000);
        assertEquals(Fraction.ONE_HALF, fraction);
    }

    @Test
    public void testConstructorWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(0.5, 10);
        assertEquals(Fraction.ONE_HALF, fraction);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleAndMaxIterations() {
        Fraction fraction = new Fraction(Math.E, 10, 5);
    }

    @Test
    public void testConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(new Fraction(4, 1), fraction);
    }

    @Test
    public void testConstructorWithIntAndInt() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(Fraction.ONE_HALF, fraction);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithNegativeDenominator() {
        new Fraction(1, -1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithOverflow() {
        new Fraction(1 << 31);
    }

    @Test
    public void testEqualsObject() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testNotEqualsObject() {
        Fraction fraction1 = new Fraction(1, 2);
        Object obj = new Object();
        assertFalse(fraction1.equals(obj));
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        int result = fraction1.compareTo(fraction2);
        assertTrue(result < 0);

        Fraction fraction3 = new Fraction(1, 2);
        int result2 = fraction3.compareTo(fraction3);
        assertEquals(0, result2);

        Fraction fraction4 = new Fraction(1, 2);
        int result3 = fraction4.compareTo(fraction2);
        assertTrue(result3 > 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-1, 1);
        assertEquals(new Fraction(1, 1), fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction reciprocal = fraction.reciprocal();
        assertEquals(new Fraction(3, 2), reciprocal);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(new Fraction(3, 6), result);
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(2);
        assertEquals(new Fraction(3, 2), result);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(new Fraction(1, 6), result);
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(2);
        assertEquals(new Fraction(-1, 2), result);
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result;

        result = fraction1.addSub(fraction2, true);
        assertEquals(new Fraction(3, 6), result);

        result = fraction1.addSub(fraction2, false);
        assertEquals(new Fraction(-2, 6), result);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(new Fraction(1, 6), result);
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(3);
        assertEquals(new Fraction(3, 2), result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(new Fraction(3, 2), result);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(3);
        assertEquals(new Fraction(1, 6), result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue(), 0.1);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertEquals(fraction1.hashCode(), fraction2.hashCode());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(FractionField.getInstance(), fraction.getField());
    }
}