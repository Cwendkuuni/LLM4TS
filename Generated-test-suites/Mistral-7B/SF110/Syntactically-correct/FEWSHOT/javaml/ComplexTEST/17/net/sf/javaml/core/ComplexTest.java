package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.getRe(), 0.0);
        assertEquals(2.0, c1.getIm(), 0.0);
        assertEquals(3.0, c2.getRe(), 0.0);
        assertEquals(4.0, c2.getIm(), 0.0);
        assertEquals(0.0, c3.getRe(), 0.0);
        assertEquals(0.0, c3.getIm(), 0.0);
    }

    @Test
    public void testComplexToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testComplexAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(5.0), c.abs(), 0.0);
    }

    @Test
    public void testComplexPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.plus(a, b);

        assertEquals(4.0, c.getRe(), 0.0);
        assertEquals(6.0, c.getIm(), 0.0);
    }

    @Test
    public void testComplexMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(1.0, -2.0);
        Complex d = Complex.minus(a, b);

        assertEquals(-2.0, d.getRe(), 0.0);
        assertEquals(-6.0, d.getIm(), 0.0);
    }

    @Test
    public void testComplexTimesReal() {
        Complex a = new Complex(1.0, 2.0);
        double d = 3.0;
        Complex c = Complex.multiply(a, d);

        assertEquals(3.0 * 1.0, c.getRe(), 0.0);
        assertEquals(3.0 * 2.0, c.getIm(), 0.0);
    }

    @Test
    public void testComplexTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.multiply(a, b);

        double real = 1.0 * 3.0 - 2.0 * 4.0;
        double imag = 1.0 * 4.0 + 2.0 * 3.0;

        assertEquals(real, c.getRe(), 0.0);
        assertEquals(imag, c.getIm(), 0.0);
    }

    @Test
    public void testComplexTimesSelf() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = new Complex();
        d.times(c);

        double real = 1.0 * 1.0 - 2.0 * 2.0;
        double imag = 1.0 * 2.0 + 2.0 * 1.0;

        assertEquals(real, d.getRe(), 0.0);
        assertEquals(imag, d.getIm(), 0.0);
    }

    @Test
    public void testComplexConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = new Complex();
        d.conjugate();

        assertEquals(1.0, d.getRe(), 0.0);
        assertEquals(-2.0, d.getIm(), 0.0);
    }

    @Test
    public void testComplexStaticConstants() {
        Complex zero = Complex.ZERO;
        Complex one = Complex.I;

        assertEquals(0.0, zero.getRe(), 0.0);
        assertEquals(0.0, zero.getIm(), 0.0);

        assertEquals(0.0, one.getRe(), 0.0);
        assertEquals(1.0, one.getIm(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComplexInvalidConstruction() {
        new Complex(0.0, null);
    }

    @Test
    public void testGetters() {
        Complex c = new Complex(1.0, 2.0);

        assertEquals(1.0, c.getRe(), 0.0);
        assertEquals(2.0, c.getIm(), 0.0);
    }

    private Complex getComplex(double re, double im) {
        return new Complex(re, im);
    }
}