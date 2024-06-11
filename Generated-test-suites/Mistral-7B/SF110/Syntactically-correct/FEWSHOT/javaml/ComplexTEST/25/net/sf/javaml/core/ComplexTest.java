package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();
        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("0.0 + 0.0i", c2.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.plus(b);
        assertEquals("4.0 + 6.0i", a.toString());
        assertEquals("1.0 + 2.0i", c.toString());

        c = Complex.plus(a, b);
        assertEquals("4.0 + 6.0i", c.toString());
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.minus(b);
        assertEquals("-2.0 - 2.0i", a.toString());
        assertEquals("1.0 + 2.0i", c.toString());

        c = Complex.plus(a, Complex.multiply(b, -1.0));
        assertEquals("-2.0 - 2.0i", a.toString());
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(2.0, 3.0);
        Complex c = new Complex();

        a.times(b);
        assertEquals("-1.0 + 7.0i", a.toString());
        assertEquals("1.0 + 2.0i", c.toString());

        c = Complex.multiply(a, b);
        assertEquals("-1.0 + 7.0i", a.toString());
    }

    @Test
    public void testTimesWithDouble() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();

        c.times(2.0);
        result = Complex.multiply(c, 2.0);
        assertEquals("2.0 + 4.0i", c.toString());
        assertEquals("2.0 + 4.0i", result.toString());
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();

        c.conjugate();
        assertEquals("1.0 - 2.0i", c.toString());
        result = Complex.conjugate(c);
        assertEquals("1.0 - 2.0i", c.toString());
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        c = Complex.plus(a, b);
        assertEquals("4.0 + 6.0i", c.toString());

        c = Complex.multiply(a, b);
        assertEquals("-1.0 + 7.0i", c.toString());

        c = Complex.multiply(a, 2.0);
        assertEquals("2.0 + 4.0i", c.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComplexConstruction() {
        new Complex("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComplexConstructionWithDouble() {
        new Complex(Double.NaN, 2.0);
    }

    @Test
    public void testI() {
        Complex c = Complex.I;
        assertEquals("0.0 + 1.0i", c.toString());
    }

    @Test
    public void testZeroComplex() {
        Complex zero = new Complex(0.0, 0.0);
        Complex result = new Complex();

        result = Complex.plus(zero, zero);
        assertEquals("0.0 + 0.0i", result.toString());

        assertEquals("0.0 + 0.0i", zero.toString());
    }
}