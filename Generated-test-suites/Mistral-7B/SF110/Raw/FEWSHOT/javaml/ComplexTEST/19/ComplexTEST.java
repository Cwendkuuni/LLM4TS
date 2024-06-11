package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(3.0, c2.re, 0.0);
        assertEquals(4.0, c2.im, 0.0);
        assertEquals(0.0, c3.re, 0.0);
        assertEquals(0.0, c3.im, 0.0);
    }

    @Test
    public void testComplexToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testComplexAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c.abs(), 0.001);
    }

    @Test
    public void testComplexPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.plus(a, b);

        assertEquals(4.0, c.re, 0.0);
        assertEquals(6.0, c.im, 0.0);
    }

    @Test
    public void testComplexMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(1.0, -2.0);

        a.minus(b);

        assertEquals(0.0, a.re, 0.0);
        assertEquals(-1.0, a.im, 0.0);
    }

    @Test
    public void testComplexTimesReal() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = Complex.multiply(c, 3.0);

        assertEquals(3.0 * 1.0, d.re, 0.0);
        assertEquals(3.0 * 2.0, d.im, 0.0);
    }

    @Test
    public void testComplexTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.multiply(a, b);

        assertEquals(-1.0 * 4.0 + 1.0 * 3.0 * 2.0 + 2.0 * 3.0 * 1.0, c.re, 0.0);
        assertEquals(1.0 * 4.0 + 1.0 * 3.0 * 2.0 + 2.0 * 3.0 * 1.0, c.im, 0.0);
    }

    @Test
    public void testComplexConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = new Complex(1.0, -2.0);

        c.conjugate();

        assertEquals(d.re, c.re, 0.0);
        assertEquals(d.im, -c.im, 0.0);
    }

    @Test
    public void testComplexStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        Complex c = Complex.plus(a, b);
        Complex d = Complex.multiply(a, 3.0);
        Complex e = Complex.multiply(a, b);

        assertEquals(c.re, 4.0, 0.0);
        assertEquals(c.im, 6.0, 0.0);
        assertEquals(d.re, 3.0 * 1.0, 0.0);
        assertEquals(d.im, 3.0 * 2.0, 0.0);
        assertEquals(e.re, -1.0 * 4.0 + 1.0 * 3.0 * 2.0 + 2.0 * 3.0 * 1.0, 0.0);
        assertEquals(e.im, 1.0 * 4.0 + 1.0 * 3.0 * 2.0 + 2.0 * 3.0 * 1.0, 0.0);
    }

    @Test
    public void testComplexStaticConstant() {
        Complex i = Complex.I;
        Complex j = new Complex(0.0, 1.0);

        assertEquals(i.im, j.im, 0.0);
        assertEquals(i.re, 0.0, 0.0);
    }
}