package net.sf.javaml.core;
import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.javaml.core.Complex;

public class ComplexTest {

    @Test
    public void testComplexDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re);
        assertEquals(0.0, complex.im);
    }

    @Test
    public void testComplexConstructorWithValues() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(3.0, complex.re);
        assertEquals(4.0, complex.im);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = new Complex();
        a.plus(b);
        assertEquals(4.0, result.re);
        assertEquals(6.0, result.im);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = new Complex();
        a.minus(b);
        assertEquals(-2.0, result.re);
        assertEquals(-2.0, result.im);
    }

    @Test
    public void testTimesWithComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = new Complex();
        a.times(b);
        assertEquals(-5.0, result.re);
        assertEquals(10.0, result.im);
    }

    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.0);
        assertEquals(6.0, complex.re);
        assertEquals(8.0, complex.im);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        complex.conjugate();
        assertEquals(1.0, complex.re);
        assertEquals(-2.0, complex.im);
    }

    @Test
    public void testStaticPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(a, b);
        assertEquals(4.0, sum.re);
        assertEquals(6.0, sum.im);
    }

    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals(6.0, result.re);
        assertEquals(8.0, result.im);
    }

    @Test
    public void testStaticMultiplyWithComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = Complex.multiply(a, b);
        assertEquals(-5.0, product.re);
        assertEquals(10.0, product.im);
    }

    // Add edge cases and exception handling test cases here

}