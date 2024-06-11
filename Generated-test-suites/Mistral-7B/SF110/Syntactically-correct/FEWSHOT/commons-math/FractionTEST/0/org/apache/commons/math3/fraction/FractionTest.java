package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor_double() {
        Fraction fraction = new Fraction(1.23);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_overflow() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructor_double_epsilon() {
        Fraction fraction = new Fraction(1.23, 0.001, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_epsilon_maxIterations() {
        new Fraction(1.23, 0.001, Integer.MIN_VALUE, 100);
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals(4, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(2, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_minDenominator() {
        new Fraction(2, Integer.MIN_VALUE);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_minNumAndDenominator() {
        new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction result = fraction.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testAbs_intMinValue() {
        Fraction fraction = new Fraction(Integer.MIN_VALUE, 1);
        Fraction result = fraction.abs();
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(1, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        int comparisonResult2 = fraction2.compareTo(fraction1);
        int comparisonResult3 = fraction1.compareTo(fraction1);

        assertEquals(-1, comparisonResult1);
        assertEquals(1, comparisonResult2);
        assertEquals(0, comparisonResult3);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(0.6666666666666666, fraction.doubleValue(), 0.000000000000001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        Object obj = new Object();

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction1.equals(obj));
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetDenominator_zero() {
        Fraction fraction = new Fraction(1, 0);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction fraction3 = new Fraction(1, 2);

        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction2.hashCode();
        int hashCode3 = fraction3.hashCode();

        assertEquals(hashCode1, hashCode3); // equal fractions should have the same hash code
        assertNotEquals(hashCode1, hashCode2); // different fractions should have different hash codes
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegate_intMinValue() {
        Fraction fraction = new Fraction(Integer.MIN_VALUE, 1);
        Fraction result = fraction.negate();
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testAdd_FractionNull() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(null);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(2, 1);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testSubtract_int() {
        Fraction fraction = new Fraction(2, 1);
        Fraction result = fraction.subtract(1);
        assertEquals(1, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testSubtract_FractionNull() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(null);
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(7, result.getNumerator());
        assertEquals(12, result.getDenominator());

        Fraction result2 = fraction1.addSub(fraction2, false);
        assertEquals(-1, result2.getNumerator());
        assertEquals(1, result2.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(4, 5);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(8, result.getNumerator());
        assertEquals(15, result.getDenominator());
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(2, 1);
        Fraction result = fraction.multiply(3);
        assertEquals(6, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testMultiply_FractionZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(Fraction.ZERO);
    }

    @Test(expected = FractionConversionException.class)
    public void testMultiply_intZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(0);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(2, 1);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(4, 3);
        Fraction result = fraction.divide(2);
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testDivide_FractionZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(Fraction.ZERO);
    }

    @Test(expected = FractionConversionException.class)
    public void testDivide_intZero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(0);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 0.001);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator1 = 15;
        int denominator1 = 20;
        Fraction result = Fraction.getReducedFraction(numerator1, denominator1);
        assertEquals(3, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFraction_intMinDenominator() {
        Fraction.getReducedFraction(0, Integer.MIN_VALUE);
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFraction_minNumDenominator() {
        Fraction.getReducedFraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }

    @Test
    public void testToString_denominatorOne() {
        Fraction fraction = new Fraction(3, 1);
        assertEquals("3", fraction.toString());
    }

    @Test
    public void testToString_negativeNumerator() {
        Fraction fraction = new Fraction(-1, 2);
        assertEquals("-1/2", fraction.toString());
    }
}