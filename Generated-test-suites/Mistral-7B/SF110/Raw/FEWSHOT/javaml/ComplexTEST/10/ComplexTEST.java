package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(3.0, c2.re, 0.0);
        assertEquals(4.0, c2.im, 0.0);
        assertEquals(0.0, c3.re, 0.0);
        assertEquals(0.0, c3.im, 0.0);

        Complex c4 = new Complex("1+2i");
        Complex c5 = new Complex("3+4i");

        assertEquals(1.0, c4.re, 0.0);
        assertEquals(2.0, c4.im, 0.0);
        assertEquals(3.0, c5.re, 0.0);
        assertEquals(4.0, c5.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex c1 = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c1.toString());

        Complex c2 = new Complex(0.0, 1.0);
        assertEquals("0.0 + 1.0i", c2.toString());

        Complex c3 = new Complex(0.0, 0.0);
        assertEquals("0.0 + 0.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(5.0), c1.abs(), 0.001);

        Complex c2 = new Complex(-1.0, 2.0);
        assertEquals(Math.sqrt(5.0), c2.abs(), 0.001);

        Complex c3 = new Complex(0.0, 0.0);
        assertEquals(0.0, c3.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex sum = new Complex();
        c1.plus(c2);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);

        Complex expectedSum = new Complex(4.0, 6.0);
        Complex expectedSum2 = Complex.plus(c1, c2);

        assertEquals(expectedSum.re, sum.re, 0.0);
        assertEquals(expectedSum.im, sum.im, 0.0);
        assertEquals(expectedSum2.re, sum.re, 0.0);
        assertEquals(expectedSum2.im, sum.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex difference = new Complex();
        c1.minus(c2);

        assertEquals(-2.0, difference.re, 0.0);
        assertEquals(-6.0, difference.im, 0.0);

        Complex expectedDifference = new Complex(-2.0, -6.0);
        Complex expectedDifference2 = Complex.minus(c1, c2);

        assertEquals(expectedDifference.re, difference.re, 0.0);
        assertEquals(expectedDifference.im, difference.im, 0.0);
        assertEquals(expectedDifference2.re, difference.re, 0.0);
        assertEquals(expectedDifference2.im, difference.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex product = new Complex();
        c1.times(c2);

        assertEquals(3.0 * 1.0 - 2.0 * 4.0, product.re, 0.0);
        assertEquals(3.0 * 2.0 + 1.0 * 4.0, product.im, 0.0);

        Complex expectedProduct = new Complex(1.0 - 4.0, 6.0);
        Complex expectedProduct2 = Complex.multiply(c1, c2);

        assertEquals(expectedProduct.re, product.re, 0.0);
        assertEquals(expectedProduct.im, product.im, 0.0);
        assertEquals(expectedProduct2.re, product.re, 0.0);
        assertEquals(expectedProduct2.im, product.im, 0.0);
    }

    @Test
    public void testTimesDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        double factor = 3.0;

        Complex result = new Complex();
        c1.times(factor);

        assertEquals(3.0 * 1.0, result.re, 0.0);
        assertEquals(3.0 * 2.0, result.im, 0.0);

        Complex expectedResult = new Complex(3.0, 6.0);
        Complex expectedResult2 = Complex.multiply(c1, factor);

        assertEquals(expectedResult.re, result.re, 0.0);
        assertEquals(expectedResult.im, result.im, 0.0);
        assertEquals(expectedResult2.re, result.re, 0.0);
        assertEquals(expectedResult2.im, result.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex conjugate = new Complex();
        c1.conjugate();

        assertEquals(1.0, conjugate.re, 0.0);
        assertEquals(-2.0, conjugate.im, 0.0);

        Complex expectedConjugate = new Complex(1.0, -2.0);
        Complex expectedConjugate2 = Complex.I.times(c1);

        assertEquals(expectedConjugate.re, conjugate.re, 0.0);
        assertEquals(expectedConjugate.im, conjugate.im, 0.0);
        assertEquals(expectedConjugate2.re, conjugate.re, 0.0);
        assertEquals(expectedConjugate2.im, conjugate.im, 0.0);
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
    public void testStaticMultiplyComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex product = Complex.multiply(c1, c2);

        assertEquals(3.0 * 1.0 - 2.0 * 4.0, product.re, 0.0);
        assertEquals(3.0 * 2.0 + 1.0 * 4.0, product.im, 0.0);
    }

    @Test
    public void testStaticMultiplyComplexDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        double factor = 3.0;

        Complex result = Complex.multiply(c1, factor);

        assertEquals(3.0 * 1.0, result.re, 0.0);
        assertEquals(3.0 * 2.0, result.im, 0.0);
    }
}