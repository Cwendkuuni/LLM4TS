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

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("3.0 + 4.0i", c2.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
        assertEquals(Math.sqrt(3.0 * 3.0 + 4.0 * 4.0), c2.abs(), 0.001);
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
        Complex prod = new Complex();

        c1.times(c2);

        assertEquals(-5.0, prod.re, 0.0);
        assertEquals(11.0, prod.im, 0.0);
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
    public void testStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(c1, c2);
        Complex prod = Complex.multiply(c1, c2);
        Complex constant = Complex.multiply(c1, 2.0);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);

        assertEquals(-5.0, prod.re, 0.0);
        assertEquals(11.0, prod.im, 0.0);

        assertEquals(2.0 * 1.0, constant.re, 0.0);
        assertEquals(2.0 * 2.0, constant.im, 0.0);
    }

    @Test
    public void testStaticConstant() {
        Complex i = Complex.I;
        Complex c = new Complex(1.0, 1.0);

        assertEquals(0.0, i.re, 0.0);
        assertEquals(1.0, i.im, 0.0);

        Complex product = i.times(c);

        assertEquals(1.0, product.re, 0.0);
        assertEquals(1.0, product.im, 0.0);
    }
}