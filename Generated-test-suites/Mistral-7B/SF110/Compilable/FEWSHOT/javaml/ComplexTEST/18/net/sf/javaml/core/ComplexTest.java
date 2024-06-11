package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c1.toString());

        Complex c2 = new Complex();
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
        Complex sum = new Complex();
        sum.plus(a);
        sum.plus(b);
        Complex expectedSum = new Complex(4.0, 6.0);
        assertEquals(expectedSum.toString(), sum.toString());
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex difference = new Complex();
        difference.minus(b);
        difference.minus(a);
        Complex expectedDifference = new Complex(-2.0, -2.0);
        assertEquals(expectedDifference.toString(), difference.toString());
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = new Complex();
        product.times(a);
        product.times(b);
        Complex expectedProduct = new Complex(3.0 - 4.0 * 2.0, 1.0 * 3.0 + 2.0 * 4.0);
        assertEquals(expectedProduct.toString(), product.toString());
    }

    @Test
    public void testTimesWithDouble() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();
        result.times(c);
        result.times(2.5);
        Complex expectedResult = new Complex(2.5 * 1.0, 2.5 * 2.0);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex conjugate = new Complex();
        conjugate.conjugate();
        Complex expectedConjugate = new Complex(1.0, -2.0);
        assertEquals(expectedConjugate.toString(), conjugate.toString());
    }

    @Test
    public void testStaticPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(a, b);
        Complex expectedSum = new Complex(4.0, 6.0);
        assertEquals(expectedSum.toString(), sum.toString());
    }

    @Test
    public void testStaticMultiplyWithDouble() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(c, 2.5);
        Complex expectedResult = new Complex(2.5 * 1.0, 2.5 * 2.0);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testStaticMultiply() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = Complex.multiply(a, b);
        Complex expectedProduct = new Complex(3.0 - 4.0 * 2.0, 1.0 * 3.0 + 2.0 * 4.0);
        assertEquals(expectedProduct.toString(), product.toString());
    }

    @Test
    public void testI() {
        Complex I = Complex.I;
        assertEquals("0.0 + 1.0i", I.toString());
    }
}