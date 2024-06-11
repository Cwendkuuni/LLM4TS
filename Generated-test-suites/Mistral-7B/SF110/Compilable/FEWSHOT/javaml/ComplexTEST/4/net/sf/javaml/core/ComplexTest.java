package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("3.0 + 4.0i", c2.toString());
        assertEquals("0.0 + 0.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        double result = c1.abs();

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), result, 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = new Complex();

        c1.plus(c2);
        result = c1;

        assertEquals("4.0 + 6.0i", result.toString());
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = new Complex();

        c1.minus(c2);
        result = c1;

        assertEquals("-2.0 - 2.0i", result.toString());
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = new Complex();

        c1.times(c2);
        result = c1;

        double real = 1.0 * 3.0 - 2.0 * 4.0;
        double imag = 1.0 * 4.0 + 2.0 * 3.0;

        assertEquals(real, result.re, 0.001);
        assertEquals(imag, result.im, 0.001);
    }

    @Test
    public void testTimesWithDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        double alpha = 3.0;
        Complex result = new Complex();

        c1.times(alpha);
        result = c1;

        assertEquals("3.0 + 6.0i", result.toString());
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex result = new Complex();

        c1.conjugate();
        result = c1;

        assertEquals("1.0 - 2.0i", result.toString());
    }

    @Test
    public void testStaticPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = Complex.plus(c1, c2);

        assertEquals("4.0 + 6.0i", result.toString());
    }

    @Test
    public void testStaticMultiplyWithDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        double alpha = 3.0;
        Complex result = Complex.multiply(c1, alpha);

        assertEquals("3.0 + 6.0i", result.toString());
    }

    @Test
    public void testStaticMultiply() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(c1, c2);

        double real = 1.0 * 3.0 - 2.0 * 4.0;
        double imag = 1.0 * 4.0 + 2.0 * 3.0;

        assertEquals(real, result.re, 0.001);
        assertEquals(imag, result.im, 0.001);
    }
}