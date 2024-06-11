package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComplexTest {

    Complex obj1, obj2, obj3, obj4;

    @Before
    public void setup() {
        obj1 = new Complex(1.0, 2.0);
        obj2 = new Complex(3.0, 4.0);
        obj3 = new Complex(0.0, 1.0);
        obj4 = new Complex();
    }

    @Test
    public void testConstructorWithReAndIm() {
        Complex complex = new Complex(2.0, 3.0);
        assertEquals(2.0, complex.getRe(), 0.0);
        assertEquals(3.0, complex.getIm(), 0.0);
    }

    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.getRe(), 0.0);
        assertEquals(0.0, complex.getIm(), 0.0);
    }

    @Test
    public void testToString() {
        assertEquals("1.0 + 2.0i", obj1.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), obj1.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        obj1.plus(obj2);
        Complex sum = new Complex(obj1.getRe(), obj1.getIm());
        Complex expectedSum = new Complex(4.0, 6.0);
        assertEquals(expectedSum.getRe(), sum.getRe(), 0.0);
        assertEquals(expectedSum.getIm(), sum.getIm(), 0.0);
    }

    @Test
    public void testMinus() {
        obj1.minus(obj2);
        Complex difference = new Complex(obj1.getRe(), obj1.getIm());
        Complex expectedDifference = new Complex(-2.0, -2.0);
        assertEquals(expectedDifference.getRe(), difference.getRe(), 0.0);
        assertEquals(expectedDifference.getIm(), difference.getIm(), 0.0);
    }

    @Test
    public void testTimes() {
        obj1.times(obj2);
        Complex product = new Complex(obj1.getRe(), obj1.getIm());
        Complex expectedProduct = new Complex(10.0, 12.0);
        assertEquals(expectedProduct.getRe(), product.getRe(), 0.0);
        assertEquals(expectedProduct.getIm(), product.getIm(), 0.0);
    }

    @Test
    public void testTimesWithScalar() {
        obj1.times(2.0);
        Complex product = new Complex(obj1.getRe(), obj1.getIm());
        Complex expectedProduct = new Complex(2.0, 4.0);
        assertEquals(expectedProduct.getRe(), product.getRe(), 0.0);
        assertEquals(expectedProduct.getIm(), product.getIm(), 0.0);
    }

    @Test
    public void testConjugate() {
        obj1.conjugate();
        Complex conjugate = new Complex(obj1.getRe(), obj1.getIm());
        Complex expectedConjugate = new Complex(1.0, -2.0);
        assertEquals(expectedConjugate.getRe(), conjugate.getRe(), 0.0);
        assertEquals(expectedConjugate.getIm(), conjugate.getIm(), 0.0);
    }

    @Test
    public void testPlusStatic() {
        Complex sum = Complex.plus(obj1, obj2);
        Complex expectedSum = new Complex(4.0, 6.0);
        assertEquals(expectedSum.getRe(), sum.getRe(), 0.0);
        assertEquals(expectedSum.getIm(), sum.getIm(), 0.0);
    }

    @Test
    public void testMultiplyStaticWithScalar() {
        Complex product = Complex.multiply(obj1, 2.0);
        Complex expectedProduct = new Complex(2.0, 4.0);
        assertEquals(expectedProduct.getRe(), product.getRe(), 0.0);
        assertEquals(expectedProduct.getIm(), product.getIm(), 0.0);
    }

    @Test
    public void testMultiplyStatic() {
        Complex product = Complex.multiply(obj1, obj2);
        Complex expectedProduct = new Complex(10.0, 12.0);
        assertEquals(expectedProduct.getRe(), product.getRe(), 0.0);
        assertEquals(expectedProduct.getIm(), product.getIm(), 0.0);
    }

    private double getRe(Complex complex) {
        return complex.getRe();
    }

    private double getIm(Complex complex) {
        return complex.getIm();
    }
}