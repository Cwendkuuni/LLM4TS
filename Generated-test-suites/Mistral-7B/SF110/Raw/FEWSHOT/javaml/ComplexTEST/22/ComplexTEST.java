package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c1 = new Complex(3.0, 2.0);
        Complex c2 = new Complex(1.0, 1.0);
        Complex c3 = new Complex();

        assertEquals("3.0 + 2.0i", c1.toString());
        assertEquals("1.0 + 1.0i", c2.toString());
        assertEquals("0.0 + 0.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 2.0);
        double expected = Math.sqrt(3 * 3 + 2 * 2);

        assertEquals(expected, c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.plus(b);

        assertEquals("4.0 + 6.0i", a.toString());
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 2.0);
        Complex b = new Complex(1.0, 1.0);
        Complex c = new Complex();

        a.minus(b);

        assertEquals("2.0 - 1.0i", a.toString());
        assertEquals("1.0 + 1.0i", c.toString());
    }

    @Test
    public void testTimesReal() {
        Complex a = new Complex(2.0, 3.0);
        double factor = 3.5;

        a.times(factor);

        assertEquals("7.0 + 10.5i", a.toString());
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(2.0, 3.0);
        Complex c = new Complex();

        a.times(b);

        assertEquals("-1.0 + 5.0i", c.toString());
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(3.0, 2.0);

        a.conjugate();

        assertEquals("3.0 - 2.0i", a.toString());
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.plus(a, b);

        assertEquals("4.0 + 6.0i", c.toString());
    }

    @Test
    public void testMultiplyStaticReal() {
        Complex a = new Complex(2.0, 3.0);
        double factor = 3.5;
        Complex c = Complex.multiply(a, factor);

        assertEquals("7.0 + 10.5i", c.toString());
    }

    @Test
    public void testMultiplyStaticComplex() {
        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(2.0, 3.0);
        Complex c = Complex.multiply(a, b);

        assertEquals("-1.0 + 5.0i", c.toString());
    }

    @Test
    public void testStaticI() {
        Complex i = Complex.I;
        Complex a = new Complex(1.0, 0.0);

        assertEquals("1.0 + 1.0i", Complex.plus(a, i).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivide() {
        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(0.0, 0.0);

        a.divide(b); // This should throw an IllegalArgumentException
    }
}