package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructor_double() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertFalse(Precision.isClose(fraction.doubleValue(), 1.5, 0.00001));

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

        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(1.0E-308);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructor_double_epsilon_maxIterations() {
        Fraction fraction = new Fraction(Math.PI, 0.00001, 100);
        assertEquals(3, fraction.getNumerator());
        assertEquals(-22, fraction.getDenominator());
        assertFalse(Precision.isClose(fraction.doubleValue(), Math.PI, 0.00001));
    }

    @Test
    public void testFractionConstructor_double_maxDenominator() {
        Fraction fraction = new Fraction(Math.PI, 100);
        assertEquals(355, fraction.getNumerator());
        assertEquals(113, fraction.getDenominator());
        assertFalse(Precision.isClose(fraction.doubleValue(), Math.PI, 0.00001));
    }

    @Test
    public void testFractionConstructor_int() {
        Fraction fraction = new Fraction(3);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructor_int_int() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructor_int_zeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testFractionMethods() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("3/4", fraction.toString());
        assertEquals(1.5, fraction.doubleValue(), 0.0001);
        assertEquals(-1, fraction.compareTo(new Fraction(1, 3)));
        assertEquals(1, fraction.compareTo(new Fraction(3, 4)));
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        assertEquals(-3, fraction.negate().getNumerator());
        assertEquals(4, fraction.negate().getDenominator());
        assertEquals(1.25, new Fraction(1, 4).reciprocal().doubleValue(), 0.0001);

        Fraction sum = new Fraction(1, 2).add(new Fraction(1, 3));
        assertEquals("5/6", sum.toString());
        assertEquals(8.333333333333334, sum.doubleValue(), 0.00000000000001);

        Fraction prod = new Fraction(1, 2).multiply(new Fraction(1, 3));
        assertEquals("1/6", prod.toString());
        assertEquals(0.16666666666666664, prod.doubleValue(), 0.00000000000001);

        Fraction quot = new Fraction(2, 3).divide(new Fraction(1, 2));
        assertEquals("3/2", quot.toString());
        assertEquals(1.5, quot.doubleValue(), 0.0001);

        Fraction result = new Fraction(1, 2).add(new Fraction(1, 3).negate());
        assertEquals("2/3", result.toString());
        assertEquals(0.6666666666666666, result.doubleValue(), 0.00000000000001);

        Fraction result2 = new Fraction(1, 2).multiply(new Fraction(1, 3).add(new Fraction(1, 4)));
        assertEquals("1/3 + 1/4 = 5/12", result2.toString());
        assertEquals(0.41666666666666664, result2.doubleValue(), 0.00000000000001);

        Fraction result3 = new Fraction(1, 2).multiply(new Fraction(1, 3).subtract(new Fraction(1, 4)));
        assertEquals("1/3 - 1/4 = 1/12", result3.toString());
        assertEquals(0.08333333333333333, result3.doubleValue(), 0.00000000000001);
    }

    @Test
    public void testFractionConstructor_double_gcd() {
        Fraction fraction = new Fraction(1.5, 2.1);
        assertEquals(1, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructor_BigInteger() {
        Fraction fraction = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2));
        assertEquals("1/2", fraction.toString());
        assertEquals(0.5, fraction.doubleValue(), 0.0);
    }

    @Test
    public void testFractionMethods_BigInteger() {
        Fraction fraction = new Fraction(BigInteger.valueOf(3), BigInteger.valueOf(4));
        assertEquals("3/4", fraction.toString());
        assertEquals(0.75, fraction.doubleValue(), 0.0);

        Fraction sum = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2)).add(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(3)));
        assertEquals("7/6", sum.toString());
        assertEquals(1.1666666666666667, sum.doubleValue(), 0.0);

        Fraction prod = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2)).multiply(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(3)));
        assertEquals("1/6", prod.toString());
        assertEquals(0.16666666666666667, prod.doubleValue(), 0.0);

        Fraction quot = new Fraction(BigInteger.valueOf(2), BigInteger.valueOf(3)).divide(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2)));
        assertEquals("2", quot.toString());
        assertEquals(2.0, quot.doubleValue(), 0.0);

        Fraction result = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2)).add(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(3)).negate());
        assertEquals("3/2", result.toString());
        assertEquals(1.5, result.doubleValue(), 0.0);

        Fraction result2 = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2)).multiply(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(3)).add(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(4))));
        assertEquals("1/3 + 1/4 = 5/12", result2.toString());
        assertEquals(0.41666666666666667, result2.doubleValue(), 0.0);

        Fraction result3 = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2)).multiply(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(3)).subtract(new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(4))));
        assertEquals("1/3 - 1/4 = 1/12", result3.toString());
        assertEquals(0.08333333333333333, result3.doubleValue(), 0.0);
    }

    @Test
    public void testFractionMethods_BigInteger_gcd() {
        Fraction fraction = new Fraction(BigInteger.valueOf(3), BigInteger.valueOf(6));
        assertEquals("3/6 = 1/2", fraction.toString());
        assertEquals(0.5, fraction.doubleValue(), 0.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_double_overflow() {
        new Fraction(1.1E308);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_double_smallDenominator() {
        try {
            new Fraction(0.5, -1);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_double_maxIterations() {
        try {
            new Fraction(Math.E, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_double_largeDenominator() {
        try {
            new Fraction(1.0, Integer.MAX_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethods_addSub_NullFraction() {
        Fraction fraction = new Fraction(1, 2);
        try {
            fraction.add(null);
            fail("Expected NullArgumentException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            fraction.subtract(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethods_addSub_IncompatibleFractions() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(4, 3);

        try {
            fraction1.add(fraction2);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            fraction1.subtract(fraction2);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethods_multiply_IncompatibleFractions() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(4, 3);

        try {
            fraction1.multiply(fraction2);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethods_divide_DivisionByZero() {
        Fraction fraction = new Fraction(1, 2);
        try {
            fraction.divide(new Fraction(0, 1));
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethods_divide_IncompatibleFractions() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(4, 3);

        try {
            fraction1.divide(fraction2);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test(expected = ArithmeticException.class)
    public void testFractionMethods_divide_IntDivisionByZero() {
        Fraction fraction = new Fraction(1, 2);
        try {
            fraction.divide(new Fraction(0, 1));
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}