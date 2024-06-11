package org.junit.tests; // replace with your package name

import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.FractionConversionException;

public class FractionTest {

    

    @org.junit.Test
    public void testConstructor_double() {
        try {
            Fraction fraction = new Fraction(0.5);
            assertEquals(0.5, fraction.doubleValue(), 0.001);
        } catch (FractionConversionException e) {
            fail("Unexpected FractionConversionException");
        }

        try {
            Fraction fraction = new Fraction(-1.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testConstructor_double_epsilon_maxIterations() {
        try {
            Fraction fraction = new Fraction(1.5, 0.1, 10);
            assertEquals(1.5, fraction.doubleValue(), 0.1);
        } catch (FractionConversionException e) {
            fail("Unexpected FractionConversionException");
        }

        try {
            Fraction fraction = new Fraction(0.1, 20, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            Fraction fraction = new Fraction(0.5, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testConstructor_double_maxDenominator() {
        try {
            Fraction fraction = new Fraction(1.5, 100);
            assertEquals(1.5, fraction.doubleValue(), 0.001);
        } catch (FractionConversionException e) {
            fail("Unexpected FractionConversionException");
        }

        try {
            Fraction fraction = new Fraction(1.5, 10000);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.intValue());
    }

    @org.junit.Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(4, 5);
        assertEquals(2, fraction.doubleValue());
    }

    @org.junit.Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, fraction.abs());
    }

    @org.junit.Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 2);
        int result1 = fraction1.compareTo(fraction1);
        int result2 = fraction1.compareTo(fraction2);
        int result3 = fraction2.compareTo(fraction1);

        assertEquals(0, result1);
        assertEquals(-1, result2);
        assertEquals(1, result3);
    }

    @org.junit.Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(0.6666666666666666, fraction.doubleValue(), 0.000000000000001);
    }

    @org.junit.Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @org.junit.Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @org.junit.Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @org.junit.Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        int result1 = fraction1.hashCode();
        int result2 = fraction1.hashCode();
        int result3 = fraction1.hashCode();

        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
    }

    @org.junit.Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.intValue());
    }

    @org.junit.Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.longValue());
    }

    @org.junit.Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 2);
        Fraction expected = new Fraction(-3, 2);
        assertEquals(expected, fraction.negate());
    }

    @org.junit.Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, fraction.reciprocal());
    }

    @org.junit.Test
    public void testAdd_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 6);
        Fraction result = fraction1.add(fraction2);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(8, 3);
        Fraction result = fraction.add(new Fraction(2, 1));

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSubtract_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        Fraction result = fraction1.subtract(fraction2);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSubtract_int() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(1, 3);
        Fraction result = fraction.subtract(new Fraction(1, 2));

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testAddSub_Fraction_isAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 6);
        Fraction result = fraction1.addSub(fraction2, true);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testAddSub_Fraction_isSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(-1, 6);
        Fraction result = fraction1.addSub(fraction2, false);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testMultiply_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        Fraction result = fraction1.multiply(fraction2);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(6, 3);
        Fraction result = fraction.multiply(new Fraction(1, 1));

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDivide_Fraction() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction1.divide(fraction2);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(1, 1);
        Fraction result = fraction.divide(new Fraction(2, 1));

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(50.0, fraction.percentageValue(), 0.01);
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testAdd_nullFraction() {
        Fraction fraction = new Fraction(1, 2);
        fraction.add(null);
        fail("Expected NullArgumentException");
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testConstructor_double_outOfRange() {
        try {
            new Fraction(Double.MAX_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(-Double.MAX_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testDivide_zeroDivision() {
        Fraction fraction = new Fraction(1, 2);
        fraction.divide(new Fraction(0, 1));
        fail("Expected ArithmeticException");
    }

    
}