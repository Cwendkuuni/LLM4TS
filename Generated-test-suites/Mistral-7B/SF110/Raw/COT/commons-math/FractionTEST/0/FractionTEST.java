package org.junit.tests; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

public class FractionTest {

    // Basic tests for constructors

    @Test
    public void testFractionConstructionWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructionWithDoubleAndEpsilon() {
        Fraction fraction = new Fraction(1.5, 0.001, 100);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructionWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(1.5, 10);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructionWithIntAndInt() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    // Test for constructor with FractionConversionException

    @Test
    public void testFractionConstructionWithDoubleOutOfRange() {
        assertThrows(FractionConversionException.class, () -> new Fraction(Double.MAX_VALUE));
    }

    // Test equals method

    @Test
    public void testEqualsObjectSameFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction otherFraction = new Fraction(1, 2);
        assertTrue(fraction.equals(otherFraction));
    }

    @Test
    public void testEqualsObjectDifferentFraction() {
        Fraction fraction = new Fraction(1, 2);
        Object otherObject = new Object();
        assertFalse(fraction.equals(otherObject));
    }

    // Test getNumerator and getDenominator methods

    @Test
    public void testGetNumeratorAndGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    // Test compareTo method

    @Test
    public void testCompareToSameFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction otherFraction = new Fraction(1, 2);
        assertEquals(0, fraction.compareTo(otherFraction));
    }

    @Test
    public void testCompareToSmallerFraction() {
        Fraction fraction = new Fraction(1, 3);
        Fraction otherFraction = new Fraction(1, 2);
        assertEquals(-1, fraction.compareTo(otherFraction));
    }

    @Test
    public void testCompareToBiggerFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction otherFraction = new Fraction(1, 3);
        assertEquals(1, fraction.compareTo(otherFraction));
    }

    // Test doubleValue method

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    // Test abs method

    @Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, fraction.abs());
    }

    // Test compareTo method with null argument

    @Test(expected = NullArgumentException.class)
    public void testCompareToNullArgument() {
        new Fraction(1, 2).compareTo(null);
    }

    // Test hashCode method

    @Test
    public void testHashCodeMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertEquals(fraction1.hashCode(), fraction2.hashCode());
    }

    // Test intValue method

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1, fraction.intValue());
    }

    // Test longValue method

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1L, fraction.longValue());
    }

    // Test negate method

    @Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, fraction.negate());
    }

    // Test reciprocal method

    @Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(4, 3);
        assertEquals(expected, fraction.reciprocal());
    }

    // Test add method with Fraction argument

    @Test
    public void testAddMethodWithFractionArgument() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(5, 6);
        assertEquals(expected, fraction1.add(fraction2));
    }

    @Test
    public void testAddMethodWithIntArgument() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(15, 12);
        assertEquals(expected, fraction.add(new Fraction(2, 1)));
    }

    // Test subtract method with Fraction argument

    @Test
    public void testSubtractMethodWithFractionArgument() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 3);
        assertEquals(expected, fraction1.subtract(fraction2));
    }

    @Test
    public void testSubtractMethodWithIntArgument() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(1, 12);
        assertEquals(expected, fraction.subtract(new Fraction(2, 1)));
    }

    // Test addSub method with null argument

    @Test(expected = NullArgumentException.class)
    public void testAddSubMethodWithNullArgument() {
        new Fraction(1, 2).addSub(null, true);
    }

    // Test addSub method with incorrect boolean argument

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubMethodWithIncorrectBooleanArgument() {
        new Fraction(1, 2).addSub(new Fraction(1, 2), false);
    }

    // Test multiply method with Fraction argument

    @Test
    public void testMultiplyMethodWithFractionArgument() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, fraction1.multiply(fraction2));
    }

    @Test
    public void testMultiplyMethodWithIntArgument() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(9, 16);
        assertEquals(expected, fraction.multiply(new Fraction(3, 4)));
    }

    // Test divide method with Fraction argument

    @Test(expected = MathArithmeticException.class)
    public void testDivideMethodWithZeroDivision() {
        new Fraction(1, 2).divide(new Fraction(0, 1));
    }

    @Test
    public void testDivideMethodWithFractionArgument() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, fraction1.divide(fraction2));
    }

    @Test
    public void testDivideMethodWithIntArgument() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(1, 2);
        assertEquals(expected, fraction.divide(new Fraction(3, 4)));
    }

    // Test percentageValue method

    @Test
    public void testPercentageValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 0.01);
    }

    // Test getField method

    @Test
    public void testGetFieldMethod() {
        assertNotNull(Fraction.getField());
    }

    // Test static initializer

    @Test
    public void testStaticInitializer() {
        Fraction one = Fraction.ONE;
        assertEquals(1, one.getNumerator());
        assertEquals(1, one.getDenominator());
    }

    // Test edge cases and exception handling

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithOutOfRangeDouble() {
        new Fraction(Double.MAX_VALUE * 2);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithNegativeDouble() {
        new Fraction(-1.5);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithOutOfRangeIntNumerator() {
        new Fraction(Integer.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithNegativeIntNumerator() {
        new Fraction(-1);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction fraction = new Fraction(1, 2);
        assertThrows(MathArithmeticException.class, () -> fraction.divide(new Fraction(0, 1)));
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideByNull() {
        Fraction fraction = new Fraction(1, 2);
        assertThrows(NullArgumentException.class, () -> fraction.divide(null));
    }
}