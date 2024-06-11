package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

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
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);
        Complex c4 = new Complex();

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("-1.0 + 2.0i", c2.toString());
        assertEquals("0.0 + 3.0i", c3.toString());
        assertEquals("0.0 + 0.0i", c4.toString());
    }

    @Test
    public void testComplexAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);
        Complex c4 = new Complex();

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
        assertEquals(Math.sqrt(1.0 * 1.0 + (-2.0) * 2.0), c2.abs(), 0.001);
        assertNotEquals(3.0, c3.abs(), 0.001);
        assertEquals(0.0, c4.abs(), 0.001);
    }

    @Test
    public void testComplexPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = Complex.plus(c1, c2);

        assertEquals(4.0, c3.re, 0.0);
        assertEquals(6.0, c3.im, 0.0);
    }

    @Test
    public void testComplexMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = c1.minus(c2);

        assertEquals(-2.0, c3.re, 0.0);
        assertEquals(-6.0, c3.im, 0.0);
    }

    @Test
    public void testComplexTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = c1.times(c2);

        double real = 1.0 * 3.0 - 2.0 * 4.0;
        double imag = 1.0 * 4.0 + 2.0 * 3.0;

        assertEquals(real, c3.re, 0.0);
        assertEquals(imag, c3.im, 0.0);
    }

    @Test
    public void testComplexConjugate() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = c1.conjugate();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(1.0, c2.re, 0.0);
        assertEquals(-2.0, c2.im, 0.0);
    }

    @Test
    public void testComplexStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = Complex.plus(c1, c2);
        Complex c4 = Complex.multiply(c1, 3.0);
        Complex c5 = Complex.multiply(c1, c2);

        assertEquals(4.0, c3.re, 0.0);
        assertEquals(6.0, c3.im, 0.0);
        assertEquals(3.0 * 1.0, c4.re, 0.0);
        assertEquals(3.0 * 2.0, c4.im, 0.0);
        assertEquals(1.0 * 3.0 * 3.0 - 2.0 * 4.0 * 3.0, c5.re, 0.001);
        assertEquals(1.0 * 3.0 * 4.0 + 2.0 * 3.0 * 3.0, c5.im, 0.001);
    }

    @Test
    public void testComplexWithNull() {
        Complex c1 = new Complex(1.0, 2.0);
        assertNull(c1.plus(null));
        assertNull(c1.minus(null));
        assertNull(c1.times(null));
    }
}