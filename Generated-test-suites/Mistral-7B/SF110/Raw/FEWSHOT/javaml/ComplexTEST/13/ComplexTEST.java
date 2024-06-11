package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.getRe(), 0.0);
        assertEquals(2.0, c1.getIm(), 0.0);
        assertEquals(3.0, c2.getRe(), 0.0);
        assertEquals(4.0, c2.getIm(), 0.0);
        assertEquals(0.0, c3.getRe(), 0.0);
        assertEquals(0.0, c3.getIm(), 0.0);

        Complex c4 = new Complex(5.5, 6.6);
        assertEquals(5.5, c4.getRe(), 0.0);
        assertEquals(6.6, c4.getIm(), 0.0);
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

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.0);
        assertEquals(Math.sqrt(3.0 * 3.0 + 4.0 * 4.0), c2.abs(), 0.0);
        assertEquals(0.0, c3.abs(), 0.0);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(5.0, 6.0);

        Complex sum = new Complex();
        c1.plus(c2);
        assertEquals(c3.getRe(), sum.getRe(), 0.0);
        assertEquals(c3.getIm(), sum.getIm(), 0.0);

        sum = Complex.plus(c1, c2);
        assertEquals(c3.getRe(), sum.getRe(), 0.0);
        assertEquals(c3.getIm(), sum.getIm(), 0.0);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex diff = new Complex();
        c1.minus(c2);

        Complex subtract = Complex.minus(c1, c2);

        Complex negC2 = new Complex(c2.getRe(), -c2.getIm());
        Complex add = Complex.plus(c1, negC2);

        assertEquals(diff.getRe(), add.getRe(), 0.0);
        assertEquals(diff.getIm(), add.getIm(), 0.0);
        assertEquals(c1.getRe() - c2.getRe(), diff.getRe(), 0.0);
        assertEquals(c1.getIm() - c2.getIm(), diff.getIm(), 0.0);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(3.0, 4.0);
        Complex c4 = new Complex(1.0, 2.0);
        Complex c5 = new Complex(0.0, 0.0);

        Complex product = new Complex();
        c1.times(c2);

        Complex multiply = Complex.multiply(c1, c2);

        double real = c1.getRe() * c2.getRe() - c1.getIm() * c2.getIm();
        double imag = c1.getRe() * c2.getIm() + c1.getIm() * c2.getRe();

        Complex expectedProduct = new Complex(real, imag);

        assertEquals(expectedProduct.getRe(), product.getRe(), 0.0);
        assertEquals(expectedProduct.getIm(), product.getIm(), 0.0);

        Complex expectedMultiply = Complex.multiply(c1, c2);

        assertEquals(expectedMultiply.getRe(), multiply.getRe(), 0.0);
        assertEquals(expectedMultiply.getIm(), multiply.getIm(), 0.0);

        Complex c6 = new Complex(5.0, 6.0);
        Complex c7 = new Complex(2.0, 3.0);

        Complex result = new Complex();
        c1.times(c6);
        c1.times(c7);

        Complex expectedResult = Complex.multiply(c1, Complex.multiply(c6, c7));

        assertEquals(expectedResult.getRe(), result.getRe(), 0.0);
        assertEquals(expectedResult.getIm(), result.getIm(), 0.0);

        Complex identity = Complex.I;
        Complex multiplyByI = new Complex();
        c1.times(identity);

        assertEquals(c1.getRe(), multiplyByI.getRe(), 0.0);
        assertEquals(c1.getIm() + c1.getRe(), multiplyByI.getIm(), 0.0);
    }

    @Test
    public void testTimesDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex result = new Complex();
        c1.times(2.5);

        Complex expectedResult = Complex.multiply(c1, new Complex(2.5, 0.0));

        assertEquals(expectedResult.getRe(), result.getRe(), 0.0);
        assertEquals(expectedResult.getIm(), result.getIm(), 0.0);

        Complex c3 = new Complex(5.0, 6.0);

        result = new Complex();
        c3.times(c1);

        Complex expectedMultipleResult = Complex.multiply(c1, c3);

        assertEquals(expectedMultipleResult.getRe(), result.getRe(), 0.0);
        assertEquals(expectedMultipleResult.getIm(), result.getIm(), 0.0);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex conjugate = new Complex();
        c1.conjugate();

        Complex expectedConjugate = new Complex(1.0, -2.0);

        assertEquals(expectedConjugate.getRe(), conjugate.getRe(), 0.0);
        assertEquals(expectedConjugate.getIm(), conjugate.getIm(), 0.0);
    }

    @Test
    public void testStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex sum = Complex.plus(c1, c2);

        Complex product = Complex.multiply(c1, c2);

        Complex identity = Complex.I;

        Complex expectedSum = new Complex(4.0, 6.0);
        Complex expectedProduct = new Complex(-1.0, 10.0);

        assertEquals(expectedSum.getRe(), sum.getRe(), 0.0);
        assertEquals(expectedSum.getIm(), sum.getIm(), 0.0);

        assertEquals(expectedProduct.getRe(), product.getRe(), 0.0);
        assertEquals(expectedProduct.getIm(), product.getIm(), 0.0);

        Complex result = new Complex();
        Complex c3 = new Complex(5.0, 0.0);
        Complex c4 = new Complex(0.0, 5.0);

        result = Complex.multiply(c3, c4);

        Complex expectedResult = Complex.multiply(new Complex(0.0, 5.0), new Complex(5.0, 0.0));

        assertEquals(expectedResult.getRe(), result.getRe(), 0.0);
        assertEquals(expectedResult.getIm(), result.getIm(), 0.0);

        Complex c5 = new Complex(1.0, 2.0);
        Complex c6 = new Complex(3.0, 4.0);

        result = Complex.multiply(c5, c6);

        Complex expectedMultiplyResult = Complex.multiply(new Complex(1.0, 2.0), new Complex(3.0, 4.0));

        assertEquals(expectedMultiplyResult.getRe(), result.getRe(), 0.0);
        assertEquals(expectedMultiplyResult.getIm(), result.getIm(), 0.0);
    }

    @Test
    public void testStaticMethodsWithIdentity() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex result = new Complex();
        Complex identity = Complex.I;

        result = Complex.multiply(c1, identity);

        Complex expectedResult = new Complex(1.0, 2.0);

        assertEquals(expectedResult.getRe(), result.getRe(), 0.0);
        assertEquals(expectedResult.getIm(), result.getIm(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testStaticMethodsWithNullComplex() {
        Complex result = Complex.multiply(null, new Complex(3.0, 4.0));
    }

    @Test(expected = NullPointerException.class)
    public void testStaticMethodsWithNullDouble() {
        Complex result = Complex.multiply(new Complex(3.0, 4.0), null);
    }
}