package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructionWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithDoubleOutOfRange() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructionWithDoubleAndEpsilon() {
        Fraction fraction = new Fraction(1.718281828, 0.000001, 1000);
        assertEquals(2261533127, fraction.getNumerator());
        assertEquals(1000000, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructionWithDoubleAndMaxDenominator() {
        new Fraction(1.5, 10);
    }

    @Test
    public void testConstructionWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructionWithIntAndDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithZeroDenominator() {
        new Fraction(4, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeDenominator() {
        new Fraction(4, -3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithNegativeAndZeroDenominator() {
        new Fraction(-4, 0);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 6);
        Fraction result = fraction.abs();
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(4, 3);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        int hashCode = fraction.hashCode();
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals(hashCode, fraction2.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3 / 4, fraction.intValue(), 0);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3L / 4L, fraction.longValue(), 0L);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(5, 6);
        Fraction result = fraction.negate();
        assertEquals(-5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.reciprocal();
        assertEquals(4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(5, 6);
        Fraction result = fraction1.add(fraction2);
        assertEquals(13, result.getNumerator());
        assertEquals(12, result.getDenominator());
    }

    @Test
    public void testAddWithInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.add(2);
        assertEquals(7, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(5, 6);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(-2, result.getNumerator());
        assertEquals(12, result.getDenominator());
    }

    @Test
    public void testSubtractWithInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.subtract(2);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(5, 6);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(13, result.getNumerator());
        assertEquals(12, result.getDenominator());

        result = fraction1.addSub(fraction2, false);
        assertEquals(-2, result.getNumerator());
        assertEquals(12, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(5, 6);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(15, result.getNumerator());
        assertEquals(12, result.getDenominator());
    }

    @Test
    public void testMultiplyWithInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.multiply(5);
        assertEquals(15, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(5, 6);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(7, result.getNumerator());
        assertEquals(12, result.getDenominator());
    }

    @Test
    public void testDivideWithInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.divide(5);
        assertEquals(-3, result.getNumerator());
        assertEquals(20, result.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 21;
        int denominator = 10;
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(7, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("3/4", fraction.toString());
    }

    @Test
    public void testCompareToZero() {
        Fraction fraction = new Fraction(3, 4);
        int comparisonResult = fraction.compareTo(Fraction.ZERO);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareToNegativeFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(-3, 4);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testEqualsWithNull() {
        Fraction fraction = new Fraction(3, 4);
        assertFalse(fraction.equals(null));
    }

    @Test
    public void testAddWithNull() {
        Fraction fraction = new Fraction(3, 4);
        try {
            Fraction result = fraction.add(null);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubtractWithNull() {
        Fraction fraction = new Fraction(3, 4);
        try {
            Fraction result = fraction.subtract(null);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiplyWithNull() {
        Fraction fraction = new Fraction(3, 4);
        try {
            Fraction result = fraction.multiply(null);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDivideWithNull() {
        Fraction fraction = new Fraction(3, 4);
        try {
            Fraction result = fraction.divide(null);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddWithZero() {
        Fraction fraction = new Fraction(3, 4);
        Fraction zero = Fraction.ZERO;
        Fraction result = fraction.add(zero);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testSubtractWithZero() {
        Fraction fraction = new Fraction(3, 4);
        Fraction zero = Fraction.ZERO;
        Fraction result = fraction.subtract(zero);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testMultiplyWithZero() {
        Fraction fraction = new Fraction(3, 4);
        Fraction zero = Fraction.ZERO;
        Fraction result = fraction.multiply(zero);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testDivideWithZero() {
        Fraction fraction = new Fraction(3, 4);
        Fraction zero = Fraction.ZERO;
        try {
            Fraction result = fraction.divide(zero);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testAddWithMaxDenominator() {
        Fraction fraction = new Fraction(Double.MAX_VALUE);
        Fraction zero = Fraction.ZERO;
        Fraction result = fraction.add(zero);
        assertEquals(Double.MAX_VALUE, result.doubleValue());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testMultiplyWithMaxDenominator() {
        Fraction fraction1 = new Fraction(Double.MAX_VALUE);
        Fraction fraction2 = Fraction.ONE;
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(Double.MAX_VALUE, result.doubleValue());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testMultiplyWithMaxDenominatorAndMaxIterations() {
        Fraction fraction1 = new Fraction(Double.MAX_VALUE);
        Fraction fraction2 = new Fraction(Double.MAX_VALUE);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(Double.MAX_VALUE * Double.MAX_VALUE, result.doubleValue(), Precision.EPSILON);
        assertEquals(1, result.getDenominator());
    }
}