package net.sf.javaml.core;

import static org.junit.Assert.*;

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
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("3.0 + 4.0i", c2.toString());
        assertEquals("0.0 + 0.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(0.0, 0.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
        assertEquals(Math.sqrt(3.0 * 3.0 + 4.0 * 4.0), c2.abs(), 0.001);
        assertEquals(0.0, c3.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(2.0, 5.0);

        Complex sum = new Complex();
        c1.plus(c2);
        sum.re = 4.0;
        sum.im = 6.0;
        assertEquals(sum.re, c1.re, 0.0);
        assertEquals(sum.im, c1.im, 0.0);

        c1.plus(c3);
        sum.re = 6.0;
        sum.im = 11.0;
        assertEquals(sum.re, c1.re, 0.0);
        assertEquals(sum.im, c1.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-1.0, -2.0);

        Complex diff = new Complex();
        c1.minus(c2);
        diff.re = -2.0;
        diff.im = -6.0;
        assertEquals(diff.re, c1.re, 0.0);
        assertEquals(diff.im, c1.im, 0.0);

        c1.minus(c3);
        diff.re = 2.0;
        diff.im = 4.0;
        assertEquals(diff.re, c1.re, 0.0);
        assertEquals(diff.im, c1.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-1.0, 2.0);
        double factor = 2.0;

        Complex product = new Complex();
        c1.times(c2);
        product.re = -5.0;
        product.im = 10.0;
        assertEquals(product.re, c1.re * c2.re - c1.im * c2.im, 0.001);
        assertEquals(product.im, c1.re * c2.im + c1.im * c2.re, 0.001);

        c1.times(factor);
        product.re = 2.0;
        product.im = 4.0;
        assertEquals(product.re, c1.re * factor, 0.001);
        assertEquals(product.im, c1.im * factor, 0.001);

        c1.conjugate();
        Complex conj = new Complex(c1.re, -c1.im);
        c1.times(conj);
        product.re = c1.re * conj.re + c1.im * conj.im;
        product.im = 0.0;
        assertEquals(product.re, c1.re * c1.re + c1.im * c1.im, 0.001);
    }

    @Test
    public void testStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(c1, c2);

        assertEquals(sum.re, 4.0, 0.0);
        assertEquals(sum.im, 6.0, 0.0);

        Complex product = Complex.multiply(c1, c2);
        assertEquals(product.re, -5.0, 0.001);
        assertEquals(product.im, 10.0, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new Complex("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();
        c1.times(c2);
    }
}