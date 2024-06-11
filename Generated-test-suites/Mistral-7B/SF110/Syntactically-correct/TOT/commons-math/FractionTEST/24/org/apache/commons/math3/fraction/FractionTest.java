package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructionFromDouble() {
        // Test positive values
        Fraction fr = new Fraction(1.5);
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
        assertEquals(0.75, fr.doubleValue(), 0.001);

        // Test negative values
        fr = new Fraction(-1.5);
        assertEquals(-1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
        assertEquals(-0.75, fr.doubleValue(), 0.001);

        // Test edge value (0)
        try {
            new Fraction(-0.0);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // Expected
        }

        // Test edge value (infinity)
        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // Expected
        }

        // Test NaN
        try {
            new Fraction(Double.NaN);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructionFromInt() {
        // Test positive values
        Fraction fr = new Fraction(1, 2);
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
        assertEquals(0.5, fr.doubleValue(), 0.001);

        // Test negative values
        fr = new Fraction(-1, 2);
        assertEquals(-1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
        assertEquals(-0.5, fr.doubleValue(), 0.001);

        // Test zero value
        fr = new Fraction(0, 1);
        assertEquals(0, fr.getNumerator());
        assertEquals(1, fr.getDenominator());
        assertEquals(0.0, fr.doubleValue(), 0.001);

        // Test negative denominator
        try {
            new Fraction(1, -2);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test zero denominator
        try {
            new Fraction(1, 0);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Expected
        }

        // Test dividing by maximum integer value
        try {
            new Fraction(1, Integer.MAX_VALUE);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructionFromDoubleWithEpsilonAndMaxIterations() {
        // Test positive value with default epsilon and maxIterations
        Fraction fr = new Fraction(Math.sqrt(2));
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
        assertEquals(1.4142135625, fr.doubleValue(), 0.001);

        // Test positive value with smaller epsilon and maxIterations
        Fraction fr2 = new Fraction(Math.sqrt(2), 1.0E-6, 1000);
        assertEquals(538246070, fr2.getNumerator());
        assertEquals(239504838, fr2.getDenominator());
        assertEquals(1.4142137611, fr2.doubleValue(), 1.0E-7);

        // Test negative value
        try {
            new Fraction(-Math.sqrt(2), 1.0E-6, 1000);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // Expected
        }

        // Test value out of range
        try {
            new Fraction(Double.MAX_VALUE, 1.0E-6, 1000);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test maxIterations reached
        try {
            new Fraction(0.5, 1.0E-22, 1);
            fail("FractionConversionException expected");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionMethods() {
        Fraction fr = new Fraction(1, 2);

        assertEquals(1, fr.compareTo(new Fraction(1, 2)));
        assertEquals(0, fr.compareTo(new Fraction(1, 2
))) ;
        assertEquals(-1, fr.compareTo(new Fraction(2, 1)));

        assertEquals(1.5, fr.doubleValue(), 0.01);
        assertEquals(1, fr.getDenominator());
        assertEquals(1, fr.getNumerator());
        assertEquals(1, fr.intValue());
        assertEquals(-1, fr.compareTo(Fraction.ZERO));

        Fraction fr2 = fr.negate();
        assertEquals(-1, fr2.getNumerator());
        assertEquals(1, fr2.getDenominator());
        assertEquals(-1.5, fr2.doubleValue(), 0.01);
        assertEquals(-1, fr2.compareTo(fr));

        Fraction fr3 = fr.reciprocal();
        assertEquals(2, fr3.getNumerator());
        assertEquals(1, fr3.getDenominator());
        assertEquals(2.0, fr3.doubleValue(), 0.01);
        assertEquals(0, fr3.compareTo(fr.multiply(fr3)));

        Fraction fr4 = fr.add(new Fraction(1, 2));
        assertEquals(2, fr4.getNumerator());
        assertEquals(1, fr4.getDenominator());
        assertEquals(1.5, fr4.doubleValue(), 0.01);
        assertEquals(1, fr4.compareTo(new Fraction(3, 2)));

        Fraction fr5 = fr.subtract(new Fraction(1, 2));
        assertEquals(0, fr5.getNumerator());
        assertEquals(1, fr5.getDenominator());
        assertEquals(0.0, fr5.doubleValue(), 0.01);
        assertEquals(0, fr5.compareTo(Fraction.ZERO));

        Fraction fr6 = fr.add(new Fraction(2, 3));
        assertEquals(3, fr6.getNumerator());
        assertEquals(1, fr6.getDenominator());
        assertEquals(1.5, fr6.doubleValue(), 0.01);
        assertEquals(1, fr6.compareTo(new Fraction(7, 4)));

        Fraction fr7 = fr.multiply(new Fraction(2, 3));
        assertEquals(2, fr7.getNumerator());
        assertEquals(3, fr7.getDenominator());
        assertEquals(2.0 / 3.0, fr7.doubleValue(), 0.01);
        assertEquals(0, fr7.compareTo(new Fraction(2, 3 * 2)));

        Fraction fr8 = fr.divide(new Fraction(2, 3));
        assertEquals(-1, fr8.getNumerator());
        assertEquals(3, fr8.getDenominator());
        assertEquals(-1.0 / 2.0, fr8.doubleValue(), 0.01);
        assertEquals(0, fr8.compareTo(fr.reciprocal().multiply(new Fraction(2, 3))));

        Fraction fr9 = fr.abs();
        assertEquals(1, fr9.getNumerator());
        assertEquals(1, fr9.getDenominator());
        assertEquals(1.0, fr9.doubleValue(), 0.01);
        assertEquals(0, fr9.compareTo(fr));

        Fraction fr10 = new Fraction(-1, 1);
        Fraction fr11 = fr.add(fr10);
        assertEquals(0, fr11.getNumerator());
        assertEquals(1, fr11.getDenominator());
        assertEquals(0.0, fr11.doubleValue(), 0.01);
        assertEquals(0, fr11.compareTo(Fraction.ZERO));

        Fraction fr12 = fr.subtract(fr10);
        assertEquals(2, fr12.getNumerator());
        assertEquals(1, fr12.getDenominator());
        assertEquals(1.5, fr12.doubleValue(), 0.01);
        assertEquals(1, fr12.compareTo(fr));
    }

    @Test
    public void testFractionStaticMethods() {
        Fraction fr = Fraction.getReducedFraction(6, 8);
        assertEquals(3, fr.getNumerator());
        assertEquals(4, fr.getDenominator());
        assertEquals(3.0 / 4.0, fr.doubleValue(), 0.01);

        try {
            Fraction.getReducedFraction(0, 1);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            Fraction.getReducedFraction(Integer.MIN_VALUE, 1);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Fraction.getReducedFraction(1, Integer.MIN_VALUE);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Fraction.getReducedFraction(-1, 1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Fraction fr2 = Fraction.valueOf("1/3");
        assertEquals(1, fr2.getNumerator());
        assertEquals(3, fr2.getDenominator());
        assertEquals(1.0 / 3.0, fr2.doubleValue(), 0.01);

        try {
            Fraction.valueOf("0/-1");
            fail("FractionFormatException expected");
        } catch (FractionFormatException e) {
            // Expected
        }

        try {
            Fraction.valueOf("1/0");
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Fraction.valueOf("10/0");
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Fraction.valueOf("1/Inf");
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Fraction.valueOf("Inf/1");
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}