package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.getRe(), 0.01);
        assertEquals(2.0, c1.getIm(), 0.01);
        assertEquals(3.0, c2.getRe(), 0.01);
        assertEquals(4.0, c2.getIm(), 0.01);
        assertEquals(0.0, c3.getRe(), 0.01);
        assertEquals(0.0, c3.getIm(), 0.01);
    }

    @Test
    public void testToString() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);
        Complex c4 = new Complex(1.0, 0.0);
        Complex c5 = new Complex(0.0, 0.0);

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("-1.0 + 2.0i", c2.toString());
        assertEquals("0.0 + 3.0i", c3.toString());
        assertEquals("1.0 + 0.0i", c4.toString());
        assertEquals("0.0 + 0.0i", c5.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);
        Complex c4 = new Complex(0.0, 0.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.01);
        assertEquals(Math.sqrt(1.0 * 1.0 + (-1.0) * 1.0), c2.abs(), 0.01);
        assertEquals(Math.sqrt(0.0 * 0.0 + 3.0 * 3.0), c3.abs(), 0.01);
        assertEquals(0.0, c4.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(5.0, 6.0);

        c1.plus(c2);

        assertEquals(1.0 + 3.0, c1.getRe(), 0.01);
        assertEquals(2.0 + 4.0, c1.getIm(), 0.01);

        Complex c4 = Complex.plus(c1, c2);

        assertEquals(1.0 + 3.0, c4.getRe(), 0.01);
        assertEquals(2.0 + 4.0, c4.getIm(), 0.01);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-2.0, -2.0);

        c1.minus(c2);

        assertEquals(1.0 - 3.0, c1.getRe(), 0.01);
        assertEquals(2.0 - 4.0, c1.getIm(), 0.01);

        Complex c4 = Complex.minus(c1, c2);

        assertEquals(1.0 - 3.0, c4.getRe(), 0.01);
        assertEquals(2.0 - 4.0, c4.getIm(), 0.01);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-1.0, 2.0);
        Complex c4 = new Complex(1.0, 1.0);

        c1.times(c2);

        assertEquals(-1.0 * 3.0 + 1.0 * 4.0, c1.getRe(), 0.01);
        assertEquals(1.0 * 3.0 + 1.0 * 4.0, c1.getIm(), 0.01);

        c1.conjugate();

        assertEquals(-1.0 * 3.0 - 1.0 * 4.0, c1.getRe(), 0.01);
        assertEquals(-1.0 * 3.0 + 1.0 * 4.0, c1.getIm(), 0.01);

        Complex c5 = Complex.multiply(c1, c2);
        Complex c6 = Complex.multiply(c1, c3);
        Complex c7 = Complex.multiply(c4, c1);

        assertEquals(-1.0 * 3.0 + 1.0 * 4.0, c5.getRe(), 0.01);
        assertEquals(-1.0 * 3.0 + 1.0 * 4.0, c5.getIm(), 0.01);
        assertEquals(-3.0 * 1.0 - 4.0 * 1.0, c6.getRe(), 0.01);
        assertEquals(-3.0 * 1.0 + 4.0 * 1.0, c6.getIm(), 0.01);
        assertEquals(1.0 * 1.0 * 1.0 + 1.0 * 1.0 * 1.0, c7.getRe(), 0.01);
        assertEquals(1.0 * 1.0 * 1.0, c7.getIm(), 0.01);
    }

    @Test
    public void testTimesDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        double d = 3.0;

        c1.times(d);

        assertEquals(3.0 * 1.0, c1.getRe(), 0.01);
        assertEquals(3.0 * 2.0, c1.getIm(), 0.01);

        Complex c2 = Complex.multiply(c1, d);

        assertEquals(3.0 * 1.0, c2.getRe(), 0.01);
        assertEquals(3.0 * 2.0, c2.getIm(), 0.01);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);

        c1.conjugate();

        assertEquals(-1.0, c1.getIm(), 0.01);
        assertEquals(1.0, c1.getRe(), 0.01);
    }

    @Test
    public void testStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = Complex.I;

        Complex c4 = Complex.plus(c1, c2);
        Complex c5 = Complex.multiply(c1, c3);
        Complex c6 = Complex.multiply(c1, c2);

        assertEquals(-1.0 * 3.0 + 1.0 * 4.0, c4.getRe(), 0.01);
        assertEquals(-1.0 * 3.0 + 1.0 * 4.0, c4.getIm(), 0.01);
        assertEquals(1.0 * 3.0 * 0.0 + 1.0 * 1.0 * 1.0, c5.getRe(), 0.01);
        assertEquals(1.0 * 3.0 * 0.0 + 1.0 * 1.0 * 1.0, c5.getIm(), 0.01);
        assertEquals(-1.0 * 3.0 * 3.0 + 1.0 * 1.0 * 4.0 - 1.0 * 3.0 * 4.0 + 1.0 * 1.0 * 3.0, c6.getRe(), 0.01);
        assertEquals(-1.0 * 3.0 * 3.0 + 1.0 * 1.0 * 4.0 + 1.0 * 3.0 * 4.0 + 1.0 * 1.0 * 3.0, c6.getIm(), 0.01);
    }

    private double getRe(Complex complex) {
        return complex.getRe();
    }

    private double getIm(Complex complex) {
        return complex.getIm();
    }
}