package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.BigInteger;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructor_double() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        try {
            new Fraction(-1.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(2.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructor_double_epsilon_maxIterations() {
        Fraction fraction = new Fraction(1.71828);
        assertEquals(2, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());

        Fraction fraction1 = new Fraction(1.71828, 1.0E-10, 1000);
        assertEquals(2, fraction1.getDenominator());
        assertEquals(1, fraction1.getNumerator());

        try {
            new Fraction(Math.E, 1.0E-15, 5000);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructor_double_maxDenominator() {
        Fraction fraction = new Fraction(1.23456789);
        assertEquals(12, fraction.getDenominator());
        assertEquals(5, fraction.getNumerator());

        try {
            new Fraction(1.23456789, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructor_int() {
        Fraction fraction = new Fraction(3);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        Fraction fraction1 = new Fraction(3, 2);
        assertEquals(3, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());

        try {
            new Fraction(-3);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(Integer.MIN_VALUE);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(Integer.MAX_VALUE);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructor_int_int() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction fraction1 = new Fraction(3, 4);
        assertEquals(3, fraction1.getNumerator());
        assertEquals(4, fraction1.getDenominator());

        Fraction fraction2 = new Fraction(-1, 2);
        assertEquals(-1, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());

        Fraction fraction3 = new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, fraction3.getNumerator());
        assertEquals(Integer.MAX_VALUE, fraction3.getDenominator());

        Fraction fraction4 = new Fraction(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, fraction4.getNumerator());
        assertEquals(Integer.MIN_VALUE, fraction4.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-1, 2);
        Fraction expected = new Fraction(1, 2);
        assertEquals(expected, fraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 2);
        Fraction fraction3 = new Fraction(3, 2);

        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction3);

        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(-1, result3);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction2));
        assertTrue(fraction2.equals(fraction1));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction3.equals(fraction1));
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
        Fraction fraction2 = new Fraction(1, 3);

        int result1 = fraction1.hashCode();
        int result2 = fraction2.hashCode();

        assertNotEquals(result1, result2);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0, fraction.intValue());

        Fraction fraction1 = new Fraction(1);
        assertEquals(1, fraction1.intValue());

        Fraction fraction2 = new Fraction(3, 2);
        assertEquals(1, fraction2.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0L, fraction.longValue());

        Fraction fraction1 = new Fraction(1);
        assertEquals(1L, fraction1.longValue());

        Fraction fraction2 = new Fraction(3, 2);
        assertEquals(1L, fraction2.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(-1, 2);
        assertEquals(expected, fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, fraction.reciprocal());
    }

    @Test(expected = NullArgumentException.class)
    public void testAdd_NullFraction() {
        Fraction fraction = new Fraction(1, 2);
        Fraction nullFraction = null;
        Fraction result = fraction.add(nullFraction);
    }

    @Test
    public void testAdd_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.add(fraction2);
        Fraction expected = new Fraction(5, 6);

        assertEquals(expected, result);
    }

    @Test
    public void testAdd_Int() {
        Fraction fraction = new Fraction(1, 2);
        int i = 3;

        Fraction result = fraction.add(i);
        Fraction expected = new Fraction(5, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testSubtract_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.subtract(fraction2);
        Fraction expected = new Fraction(-1, 6);

        assertEquals(expected, result);
    }

    @Test
    public void testSubtract_Int() {
        Fraction fraction = new Fraction(1, 2);
        int i = 3;

        Fraction result = fraction.subtract(i);
        Fraction expected = new Fraction(-3, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testAddSub_Fraction_isAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.addSub(fraction2, true);
        Fraction expected = new Fraction(5, 6);

        assertEquals(expected, result);
    }

    @Test
    public void testAddSub_Fraction_isSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.addSub(fraction2, false);
        Fraction expected = new Fraction(-1, 6);

        assertEquals(expected, result);
    }

    @Test
    public void testMultiply_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.multiply(fraction2);
        Fraction expected = new Fraction(1, 3);

        assertEquals(expected, result);
    }

    @Test
    public void testMultiply_Int() {
        Fraction fraction = new Fraction(1, 2);
        int i = 3;

        Fraction result = fraction.multiply(i);
        Fraction expected = new Fraction(3, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testDivide_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.divide(fraction2);
        Fraction expected = new Fraction(3, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testDivide_Int() {
        Fraction fraction = new Fraction(1, 2);
        int i = 3;

        Fraction result = fraction.divide(i);
        Fraction expected = new Fraction(1, 6);

        assertEquals(expected, result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        double percentage = fraction.percentageValue();

        assertEquals(50.0, percentage, 0.01);
    }

    @Test(expected = NullArgumentException.class)
    public void testGetField_Null() {
        Fraction fraction = new Fraction(1, 2);
        FractionField nullField = null;
        fraction.getField();
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField field = fraction.getField();

        assertTrue(field instanceof FractionField);
    }
}