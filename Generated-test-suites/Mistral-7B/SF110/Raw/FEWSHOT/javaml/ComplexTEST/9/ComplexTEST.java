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
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(5.0), c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = new Complex();
        sum.plus(c1);
        sum.plus(c2);
        assertEquals("4.0 + 6.0i", sum.toString());
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex diff = new Complex();
        diff.minus(c2);
        diff.minus(c1);
        assertEquals("0.0 - 2.0i", diff.toString());
    }

    @Test
    public void testTimesReal() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();
        result.times(c);
        result.times(3.0);
        assertEquals("3.0 + 6.0i", result.toString());
    }

    @Test
    public void testTimesComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex product = new Complex();
        product.times(c1);
        product.times(c2);
        assertEquals("-2.0 + 11.0i", product.toString());
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex conjugate = new Complex();
        conjugate.conjugate();
        assertEquals("1.0 - 2.0i", conjugate.toString());
    }

    @Test
    public void testPlusStatic() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(c1, c2);
        assertEquals("4.0 + 6.0i", sum.toString());
    }

    @Test
    public void testMultiplyRealStatic() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();
        result.times(c);
        result.times(3.0);
        Complex multiplied = Complex.multiply(c, 3.0);
        assertEquals("3.0 + 6.0i", result.toString());
        assertEquals("3.0 + 6.0i", multiplied.toString());
    }

    @Test
    public void testMultiplyComplexStatic() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex product = new Complex();
        product.times(c1);
        product.times(c2);
        Complex multiplied = Complex.multiply(c1, c2);
        assertEquals("-2.0 + 11.0i", product.toString());
        assertEquals("-2.0 + 11.0i", multiplied.toString());
    }
}