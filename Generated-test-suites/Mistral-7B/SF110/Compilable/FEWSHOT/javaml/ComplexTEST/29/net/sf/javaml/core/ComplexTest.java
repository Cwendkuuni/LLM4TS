package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
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
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();
        Complex c3 = new Complex(3.0, 4.0);

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("0.0 + 0.0i", c2.toString());
        assertEquals("3.0 + 4.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-1.0, -2.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
        assertEquals(Math.sqrt(3.0 * 3.0 + 4.0 * 4.0), c2.abs(), 0.001);
        assertEquals(Math.sqrt(1.0 * 1.0 + (-2.0) * (-2.0)), c3.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.plus(b);

        assertEquals(4.0, a.re, 0.0);
        assertEquals(6.0, a.im, 0.0);

        c = new Complex();
        Complex d = new Complex(5.0, 6.0);

        c.plus(d);

        assertEquals(10.0, c.re, 0.0);
        assertEquals(12.0, c.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.minus(b);

        assertEquals(-2.0, a.re, 0.0);
        assertEquals(-2.0, a.im, 0.0);

        c = new Complex(5.0, 6.0);
        Complex d = new Complex(3.0, 4.0);

        c.minus(d);

        assertEquals(2.0, c.re, 0.0);
        assertEquals(-2.0, c.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.times(b);

        assertEquals(-2.0 * 3.0 + 1.0 * 4.0, a.re, 0.0);
        assertEquals(1.0 * 4.0 + 2.0 * 3.0, a.im, 0.0);

        c = new Complex(5.0, 6.0);
        double d = 2.0;

        c.times(d);

        assertEquals(10.0 * 2.0, c.re, 0.0);
        assertEquals(6.0 * 2.0, c.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);

        c.conjugate();

        assertEquals(1.0, c.re, 0.0);
        assertEquals(-2.0, c.im, 0.0);
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.I;

        Complex d = Complex.plus(a, b);

        assertEquals(4.0 + 1.0, d.re, 0.0);
        assertEquals(6.0 + 2.0, d.im, 0.0);

        Complex e = Complex.multiply(a, b.re);

        assertEquals(1.0 * 3.0, e.re, 0.0);
        assertEquals(2.0 * 3.0, e.im, 0.0);

        Complex f = Complex.multiply(a, b);

        assertEquals(-2.0 * 3.0 + 1.0 * 4.0, f.re, 0.0);
        assertEquals(1.0 * 4.0 + 2.0 * 3.0, f.im, 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testStaticMethodsWithNull() {
        Complex a = null;
        Complex b = new Complex(1.0, 2.0);

        Complex c = Complex.plus(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticMethodsWithImproperInput() {
        Complex a = new Complex(1.0, 2.0);
        double b = -1.0;

        Complex c = Complex.multiply(a, b);
    }
}