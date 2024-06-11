package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() throws FractionConversionException {
        Fraction f = new Fraction(2.5);
        assertEquals(2, f.getNumerator());
        assertEquals(5, f.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOverflow() throws FractionConversionException {
        new Fraction(Double.LONG_MAX + 1.0);
    }

    @Test
    public void testConstructorWithDoubleEpsilonAndMaxIterations() throws FractionConversionException {
        Fraction f = new Fraction(3.1415926535897932384626433832795, 1.0E-10, 100);
        assertEquals(52821, f.getNumerator());
        assertEquals(15570, f.getDenominator());
    }

    @Test
    public void testConstructorWithDoubleMaxDenominator() throws FractionConversionException {
        Fraction f = new Fraction(Math.PI, 10);
        assertEquals((int) Math.round(Math.PI * 10.0), f.getNumerator());
        assertEquals(10, f.getDenominator());
    }

    @Test
    public void testConstructorWithInt() {
        Fraction f = new Fraction(4);
        assertEquals(4, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorWithIntDenominator() {
        Fraction f = new Fraction(4, 2);
        assertEquals(2, f.getDenominator());
        assertEquals(2, f.getNumerator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testConstructorWithNegativeDenominator() {
        Fraction f = new Fraction(1, -2);
        assertEquals(-1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testConstructorWithNegativeNumeratorAndPositiveDenominator() {
        Fraction f = new Fraction(-3, 2);
        assertEquals(-3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testConstructorWithNegativeNumeratorAndNegativeDenominator() {
        Fraction f = new Fraction(-3, -2);
        assertEquals(3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f.abs();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 1);
        int result = f1.compareTo(f2);
        assertEquals(-1, result);
        
        Fraction f3 = new Fraction(2, 1);
        Fraction f4 = new Fraction(1, 2);
        result = f3.compareTo(f4);
        assertEquals(1, result);
        
        Fraction f5 = new Fraction(1, 2);
        Fraction f6 = new Fraction(1, 2);
        result = f5.compareTo(f6);
        assertEquals(0, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        double result = f.doubleValue();
        assertEquals(0.5, result, 0.0);
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        boolean result = f1.equals(f2);
        assertTrue(result);

        Fraction f3 = new Fraction(1, 3);
        result = f1.equals(f3);
        assertFalse(result);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 2);
        float result = f.floatValue();
        assertEquals(0.5f, result, 0.0f);
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(1, 2);
        int result = f.getDenominator();
        assertEquals(2, result);
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(1, 2);
        int result = f.getNumerator();
        assertEquals(1, result);
    }

    @Test
    public void testHashCode() {
        Fraction f = new Fraction(1, 2);
        int result = f.hashCode();
        int expected = 1527288963; // calculated using 37 * (629 + this.numerator) + this.denominator
        assertEquals(expected, result);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(1, 2);
        int result = f.intValue();
        assertEquals(0, result);
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(1, 2);
        long result = f.longValue();
        assertEquals(0L, result);
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(-1, 2);
        Fraction result = f.negate();
        assertEquals(expected, result);
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 1);
        Fraction result = f.reciprocal();
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 6);
        Fraction result = f1.add(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.add(1);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(2, 1);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(1, 1);
        Fraction result = f1.subtract(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.subtract(1);
        Fraction expected = new Fraction(-1, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddSub() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 3);
        Fraction expected = new Fraction(1, 3);
        Fraction result = f1.addSub(f2, true);
        assertEquals(expected, result);

        Fraction f3 = new Fraction(3, 4);
        Fraction f4 = new Fraction(2, 3);
        expected = new Fraction(-1, 3);
        result = f1.addSub(f4, false);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 8);
        Fraction result = f1.multiply(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.multiply(3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = f1.divide(f2);
        assertEquals(expected, result);
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.divide(3);
        Fraction expected = new Fraction(-1, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        double result = f.percentageValue();
        assertEquals(50.0, result, 0.0);
    }

    @Test
    public void testGetField() {
        Fraction f = new Fraction(1, 2);
        FractionField field = f.getField();
        assertEquals(FractionField.getInstance(), field);
    }

    @Test
    public void testToString() {
        Fraction f = new Fraction(3, 4);
        String result = f.toString();
        assertEquals("3/4", result);
    }

    @Test
    public void testStaticInitialization() {
        assertEquals(2, Fraction.TWO.getNumerator());
        assertEquals(1, Fraction.ONE.getNumerator());
        assertEquals(0, Fraction.ZERO.getNumerator());
        assertEquals(4, Fraction.FOUR_FIFTHS.getNumerator());
        assertEquals(1, Fraction.ONE_FIFTH.getNumerator());
        assertEquals(1, Fraction.ONE_HALF.getNumerator());
        assertEquals(1, Fraction.ONE_QUARTER.getNumerator());
        assertEquals(1, Fraction.ONE_THIRD.getNumerator());
        assertEquals(3, Fraction.THREE_FIFTHS.getNumerator());
        assertEquals(3, Fraction.THREE_QUARTERS.getNumerator());
        assertEquals(2, Fraction.TWO_FIFTHS.getNumerator());
        assertEquals(2, Fraction.TWO_QUARTERS.getNumerator());
        assertEquals(2, Fraction.TWO_THIRDS.getNumerator());
        assertEquals(-1, Fraction.MINUS_ONE.getNumerator());
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNull() {
        new Fraction(1, 2).add(null);
    }

    @Test(expected = FractionConversionException.class)
    public void testAddWithOverflow() throws FractionConversionException {
        Fraction f1 = new Fraction(Long.MAX_VALUE);
        Fraction f2 = new Fraction(1);
        f1.add(f2);
    }

    @Test(expected = FractionConversionException.class)
    public void testMultiplyWithOverflow() throws FractionConversionException {
        Fraction f1 = new Fraction(Long.MAX_VALUE);
        Fraction f2 = new Fraction(1);
        f1.multiply(f2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(0);
        f1.divide(f2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroDenominator() {
        Fraction f1 = new Fraction(0);
        Fraction f2 = new Fraction(1, 2);
        f1.divide(f2);
    }

    @Test(expected = ArithmeticException.class)
    public void testAddWithArithmeticException() {
        Fraction f1 = new Fraction(Integer.MIN_VALUE);
        Fraction f2 = new Fraction(1);
        f1.add(f2);
    }

    @Test(expected = ArithmeticException.class)
    public void testSubtractWithArithmeticException() {
        Fraction f1 = new Fraction(1);
        Fraction f2 = new Fraction(Integer.MIN_VALUE);
        f1.subtract(f2);
    }

    @Test(expected = ArithmeticException.class)
    public void testMultiplyWithArithmeticException() {
        Fraction f1 = new Fraction(Integer.MIN_VALUE);
        Fraction f2 = new Fraction(1);
        f1.multiply(f2);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideWithArithmeticException() {
        Fraction f1 = new Fraction(1);
        Fraction f2 = new Fraction(Integer.MIN_VALUE);
        f1.divide(f2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithIllegalArgumentException() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 0);
        f1.add(f2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyWithIllegalArgumentException() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 0);
        f1.multiply(f2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideWithIllegalArgumentException() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 0);
        f1.divide(f2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPercentageValueWithIllegalArgumentException() {
        Fraction f = new Fraction(-1, 2);
        f.percentageValue();
    }
}