package net.sf.javaml.core;

import static org.junit.Assert.*;

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
    public void testToString() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);
        Complex c4 = new Complex(0.0, 0.0);

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("-1.0 + 2.0i", c2.toString());
        assertEquals("0.0 + 3.0i", c3.toString());
        assertEquals("0.0 + 0.0i", c4.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, -2.0);
        Complex c3 = new Complex(0.0, 0.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.0);
        assertEquals(Math.sqrt(1.0 * 1.0 + (-2.0) * (-2.0)), c2.abs(), 0.0);
        assertEquals(0.0, c3.abs(), 0.0);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(1.0, 2.0);

        a.plus(b);

        assertEquals(4.0, a.re, 0.0);
        assertEquals(6.0, a.im, 0.0);

        Complex d = new Complex();
        Complex e = new Complex(1.0, 2.0);
        Complex f = new Complex(3.0, 4.0);

        d.plus(e);
        e.plus(f);

        Complex g = Complex.plus(d, e);

        assertEquals(4.0, g.re, 0.0);
        assertEquals(6.0, g.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(1.0, 2.0);

        a.minus(b);

        assertEquals(-2.0, a.re, 0.0);
        assertEquals(-6.0, a.im, 0.0);

        Complex d = new Complex();
        Complex e = new Complex(1.0, 2.0);
        Complex f = new Complex(3.0, 4.0);

        d.minus(e);
        e.minus(f);

        Complex g = Complex.minus(d, e);

        assertEquals(-2.0, g.re, 0.0);
        assertEquals(-6.0, g.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(-1.0, 2.0);
        double d = 2.0;

        a.times(b);

        assertEquals(-3.0 * 1.0 - 4.0 * 2.0, a.re, 0.0);
        assertEquals(-3.0 * 2.0 + 4.0 * 1.0, a.im, 0.0);

        a.times(d);

        assertEquals(2.0 * (-3.0 * 1.0 - 4.0 * 2.0), a.re, 0.0);
        assertEquals(2.0 * (-3.0 * 2.0 + 4.0 * 1.0), a.im, 0.0);

        Complex dComplex = new Complex(2.0, 0.0);
        a.times(dComplex);

        assertEquals(-6.0 * 1.0 - 8.0 * 2.0, a.re, 0.0);
        assertEquals(-6.0 * 2.0 + 8.0 * 1.0, a.im, 0.0);

        a = new Complex(1.0, 2.0);
        b = new Complex(3.0, 4.0);

        Complex result = Complex.multiply(a, b);

        assertEquals(-3.0 * 1.0 - 4.0 * 2.0, result.re, 0.0);
        assertEquals(-3.0 * 2.0 + 4.0 * 1.0, result.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(1.0, 2.0);

        a.conjugate();

        assertEquals(1.0, a.re, 0.0);
        assertEquals(-2.0, a.im, 0.0);
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        Complex sum = Complex.plus(a, b);
        Complex product = Complex.multiply(a, b);

        Complex aTimesB = new Complex();
        aTimesB.times(b);
        Complex bTimesA = new Complex();
        bTimesA.times(a);

        Complex sumByHand = new Complex();
        sumByHand.plus(a);
        sumByHand.plus(b);

        Complex productByHand = new Complex();
        productByHand.times(a);
        productByHand.times(b);

        assertEquals(sum.re, sumByHand.re, 0.0);
        assertEquals(sum.im, sumByHand.im, 0.0);
        assertEquals(product.re, productByHand.re, 0.0);
        assertEquals(product.im, productByHand.im, 0.0);
    }

    @Test
    public void testI() {
        Complex zero = new Complex();
        Complex i = Complex.I;

        Complex result = zero.times(i);

        assertEquals(0.0, result.re, 0.0);
        assertEquals(1.0, result.im, 0.0);

        result = i.times(zero);

        assertEquals(1.0, result.re, 0.0);
        assertEquals(0.0, result.im, 0.0);
    }
}