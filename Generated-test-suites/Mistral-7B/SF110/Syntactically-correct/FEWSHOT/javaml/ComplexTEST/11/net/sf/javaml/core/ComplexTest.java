package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructors() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();
        Complex c3 = new Complex(3.0, 4.0);

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(0.0, c2.re, 0.0);
        assertEquals(0.0, c2.im, 0.0);
        assertEquals(3.0, c3.re, 0.0);
        assertEquals(4.0, c3.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex sum = new Complex();

        a.plus(b);
        assertEquals(sum.re, a.re + b.re, 0.0);
        assertEquals(sum.im, a.im + b.im, 0.0);

        sum = Complex.plus(a, b);
        assertEquals(sum.re, a.re + b.re, 0.0);
        assertEquals(sum.im, a.im + b.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex diff = new Complex();

        a.minus(b);
        assertEquals(diff.re, a.re - b.re, 0.0);
        assertEquals(diff.im, a.im - b.im, 0.0);

        diff = Complex.minus(a, b);
        assertEquals(diff.re, a.re - b.re, 0.0);
        assertEquals(diff.im, a.im - b.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = new Complex();

        a.times(b);
        assertEquals(product.re, a.re * b.re - a.im * b.im, 0.0);
        assertEquals(product.im, a.re * b.im + a.im * b.re, 0.0);

        product = Complex.multiply(a, b);
        assertEquals(product.re, a.re * b.re - a.im * b.im, 0.0);
        assertEquals(product.im, a.re * b.im + a.im * b.re, 0.0);
    }

    @Test
    public void testTimesDouble() {
        Complex c = new Complex(1.0, 2.0);

        c.times(2.0);
        assertEquals(2.0 * c.re, c.re, 0.0);
        assertEquals(2.0 * c.im, c.im, 0.0);

        Complex d = Complex.multiply(c, 2.0);
        assertEquals(2.0 * c.re, d.re, 0.0);
        assertEquals(2.0 * c.im, d.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex conj = new Complex();

        c.conjugate();
        assertEquals(-c.im, conj.im, 0.0);
        assertEquals(c.re, conj.re, 0.0);

        conj = Complex.conjugate(c);
        assertEquals(-c.im, conj.im, 0.0);
        assertEquals(c.re, conj.re, 0.0);
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        Complex sum = Complex.plus(a, b);
        Complex product = Complex.multiply(a, b);

        Complex c = new Complex(1.0, -2.0);
        Complex d = Complex.conjugate(c);

        assertEquals(sum.re, a.re + b.re, 0.0);
        assertEquals(sum.im, a.im + b.im, 0.0);

        assertEquals(product.re, a.re * b.re - a.im * b.im, 0.0);
        assertEquals(product.im, a.re * b.im + a.im * b.re, 0.0);

        assertEquals(-c.im, d.im, 0.0);
        assertEquals(c.re, d.re, 0.0);
    }

    @Test
    public void testStaticMethodsWithNull() {
        assertNull(Complex.plus(null, null));
        assertNull(Complex.multiply(null, null));
    }

    @Test
    public void testStaticMethodsWithSingleArgument() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = Complex.multiply(c, 2.0);

        assertEquals(2.0 * c.re, d.re, 0.0);
        assertEquals(2.0 * c.im, d.im, 0.0);

        Complex e = new Complex();
        e.conjugate();

        assertEquals(c.re, e.re, 0.0);
        assertEquals(-c.im, e.im, 0.0);
    }
}