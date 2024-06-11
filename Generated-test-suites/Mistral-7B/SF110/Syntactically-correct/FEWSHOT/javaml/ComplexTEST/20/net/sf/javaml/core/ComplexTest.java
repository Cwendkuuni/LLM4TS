package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
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
    public void testToString() {
        Complex c1 = new Complex(1.0, 2.0);

        assertEquals("1.0 + 2.0i", c1.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = new Complex();

        c1.plus(c2);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex diff = new Complex();

        c1.minus(c2);

        assertEquals(-2.0, diff.re, 0.0);
        assertEquals(-2.0, diff.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex product = new Complex();

        c1.times(c2);

        final double real = 1.0 * 3.0 - 2.0 * 4.0;
        final double imag = 1.0 * 4.0 + 2.0 * 3.0;

        assertEquals(real, product.re, 0.0);
        assertEquals(imag, product.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex conj = new Complex();

        c1.conjugate();

        assertEquals(1.0, conj.re, 0.0);
        assertEquals(-2.0, conj.im, 0.0);
    }

    @Test
    public void testStaticPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(c1, c2);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);
    }

    @Test
    public void testStaticMultiplyDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(c1, 3.0);

        assertEquals(3.0 * 1.0, result.re, 0.0);
        assertEquals(3.0 * 2.0, result.im, 0.0);
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(c1, c2);

        final double real = 1.0 * 3.0 - 2.0 * 4.0;
        final double imag = 1.0 * 4.0 + 2.0 * 3.0;

        assertEquals(real, result.re, 0.0);
        assertEquals(imag, result.im, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullComplex() {
        Complex c1 = null;
        Complex c2 = new Complex(1.0, 2.0);
        c1.plus(c2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        double d = null;
        c1.times(d);
    }

    @Test(expected = NullPointerException.class)
    public void testNullResult() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = null;
        c1.times(c2);
        c1.plus(result);
    }

    @Test
    public void testStaticI() {
        Complex i = Complex.I;
        Complex c = new Complex(1.0, 0.0);
        Complex sum = Complex.plus(c, i);

        assertEquals(1.0, sum.re, 0.0);
        assertEquals(1.0, sum.im, 0.0);
    }
}