package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complex4;
 private Complex complex5;
 private Complex complex6;

 @Test
 public void testComplexConstructorWithDoubleReAndIm() {
 complex1 = new Complex(1.0, 2.0);
 Assert.assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testComplexConstructorWithDefaultValues() {
 complex2 = new Complex();
 Assert.assertEquals("0.0 + 0.0i", complex2.toString());
 }

 @Test
 public void testAbs() {
 complex1 = new Complex(3.0, 4.0);
 double expected = 5.0;
 double actual = complex1.abs();
 Assert.assertEquals(expected, actual, 0.0);
 }

 @Test
 public void testPlus() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = complex1.plus(complex2);
 double expectedReal = 4.0;
 double expectedImag = 6.0;
 double actualReal = complex3.re;
 double actualImag = complex3.im;
 Assert.assertEquals(expectedReal, actualReal, 0.0);
 Assert.assertEquals(expectedImag, actualImag, 0.0);
 }

 @Test
 public void testMinus() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = complex1.minus(complex2);
 double expectedReal = -2.0;
 double expectedImag = -2.0;
 double actualReal = complex3.re;
 double actualImag = complex3.im;
 Assert.assertEquals(expectedReal, actualReal, 0.0);
 Assert.assertEquals(expectedImag, actualImag, 0.0);
 }

 @Test
 public void testTimes() {
 complex1 = new Complex(2.0, 3.0);
 complex2 = new Complex(1.0, 1.0);
 complex3 = complex1.times(complex2);
 double expectedReal = -1.0;
 double expectedImag = 5.0;
 double actualReal = complex3.re;
 double actualImag = complex3.im;
 Assert.assertEquals(expectedReal, actualReal, 0.0);
 Assert.assertEquals(expectedImag, actualImag, 0.0);
 }

 @Test
 public void testTimesWithDouble() {
 complex1 = new Complex(2.0, 3.0);
 complex2 = new Complex(1.0, 1.0);
 complex3 = complex1.times(2.0);
 double expectedReal = 4.0;
 double expectedImag = 6.0;
 double actualReal = complex3.re;
 double actualImag = complex3.im;
 Assert.assertEquals(expectedReal, actualReal, 0.0);
 Assert.assertEquals(expectedImag, actualImag, 0.0);
 }

 @Test
 public void testConjugate() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = complex1.conjugate();
 double expectedReal = 1.0;
 double expectedImag = -2.0;
 double actualReal = complex2.re;
 double actualImag = complex2.im;
 Assert.assertEquals(expectedReal, actualReal, 0.0);
 Assert.assertEquals(expectedImag, actualImag, 0.0);
 }

 @Test
 public void testStaticPlus() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 Complex expectedComplex = new Complex(4.0, 6.0);
 Complex actualComplex = Complex.plus(complex1, complex2);
 Assert.assertEquals(expectedComplex.re, actualComplex.re, 0.0);
 Assert.assertEquals(expectedComplex.im, actualComplex.im, 0.0);
 }

 @Test
 public void testStaticMultiplyWithDouble() {
 complex1 = new Complex(2.0, 3.0);
 double expectedReal = 4.0;
 double expectedImag = 6.0;
 Complex actualComplex = Complex.multiply(complex1, 2.0);
 Assert.assertEquals(expectedReal, actualComplex.re, 0.0);
 Assert.assertEquals(expectedImag, actualComplex.im, 0.0);
 }

 @Test
 public void testStaticMultiply() {
 complex1 = new Complex(1.0, 1.0);
 complex2 = new Complex(2.0, 3.0);
 Complex expectedComplex = new Complex(-1.0, 5.0);
 Complex actualComplex = Complex.multiply(complex1, complex2);
 Assert.assertEquals(expectedComplex.re, actualComplex.re, 0.0);
 Assert.assertEquals(expectedComplex.im, actualComplex.im, 0.0);
 }

 @Test
 public void testStaticI() {
 Complex expectedComplex = new Complex(0.0, 1.0);
 Complex actualComplex = Complex.I;
 Assert.assertEquals(expectedComplex.re, actualComplex.re, 0.0);
 Assert.assertEquals(expectedComplex.im, actualComplex.im, 0.0);
 }
}