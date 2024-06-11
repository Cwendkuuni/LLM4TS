package net.sf.javaml.core;

import org.junit.Test;
import org.junit.Assert.*;

public class ComplexTest {

 private Complex c1;
 private Complex c2;
 private Complex c3;
 private Complex c4;
 private Complex I = Complex.I;

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex();
 c4 = new Complex(0.0, 5.0);
 }

 @Test
 public void testComplexConstructor() {
 Complex obj = new Complex(2.0, 3.0);
 Assert.assertEquals(2.0, obj.getRe(), 0.0);
 Assert.assertEquals(3.0, obj.getIm(), 0.0);
 }

 @Test
 public void testComplexNoArgumentConstructor() {
 Complex obj = new Complex();
 Assert.assertEquals(0.0, obj.getRe(), 0.0);
 Assert.assertEquals(0.0, obj.getIm(), 0.0);
 }

 @Test
 public void testToString() {
 Assert.assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testAbs() {
 Assert.assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusWithNullComplex() {
 c1.plus(null);
 }

 @Test
 public void testPlus() {
 Complex result = new Complex();
 Complex.plus(c1, c2);
 result.re = 4.0;
 result.im = 6.0;
 Assert.assertEquals(result.re, c1.getRe() + c2.getRe());
 Assert.assertEquals(result.im, c1.getIm() + c2.getIm());
 c1.plus(c2);
 Assert.assertEquals(result.re, c1.getRe());
 Assert.assertEquals(result.im, c1.getIm());
 }

 @Test
 public void testMinus() {
 Complex result = new Complex();
 Complex.minus(c1, c2);
 result.re = -1.0;
 result.im = -2.0;
 Assert.assertEquals(result.re, c1.getRe() - c2.getRe());
 Assert.assertEquals(result.im, c1.getIm() - c2.getIm());
 c1.minus(c2);
 Assert.assertEquals(result.re, c1.getRe());
 Assert.assertEquals(result.im, c1.getIm());
 }

 @Test
 public void testTimesWithComplex() {
 Complex result = new Complex();
 Complex.times(c1, c2);
 result.re = 11.0;
 result.im = 12.0;
 Assert.assertEquals(result.re, (c1.getRe() * c2.getRe() - c1.getIm() * c2.getIm()));
 Assert.assertEquals(result.im, (c1.getRe() * c2.getIm() + c1.getIm() * c2.getRe()));
 c1.times(c2);
 Assert.assertEquals(result.re, c1.getRe());
 Assert.assertEquals(result.im, c1.getIm());
 }

 @Test
 public void testTimesWithDouble() {
 Complex result = new Complex();
 Complex.times(c1, 3.0);
 result.re = 3.0 * 1.0;
 result.im = 3.0 * 2.0;
 Assert.assertEquals(result.re, c1.getRe() * 3.0);
 Assert.assertEquals(result.im, c1.getIm() * 3.0);
 c1.times(3.0);
 Assert.assertEquals(result.re, c1.getRe());
 Assert.assertEquals(result.im, c1.getIm());
 }

 @Test
 public void testConjugate() {
 Complex result = new Complex();
 result.im = -c1.getIm();
 Assert.assertEquals(result.re, c1.getRe());
 Assert.assertEquals(result.im, -c1.getIm());
 c1.conjugate();
 Assert.assertEquals(result.re, c1.getRe());
 Assert.assertEquals(result.im, c1.getIm());
 }

 @Test
 public void testPlusStatic() {
 Complex result = new Complex();
 result.re = 4.0;
 result.im = 6.0;
 Assert.assertEquals(result.re, Complex.plus(c1, c2).getRe());
 Assert.assertEquals(result.im, Complex.plus(c1, c2).getIm());
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex result = new Complex();
 result.re = 11.0;
 result.im = 12.0;
 Complex.multiply(c1, c2);
 result.re *= 3.0;
 result.im *= 3.0;
 Assert.assertEquals(result.re, Complex.multiply(c1, c2, 3.0).getRe());
 Assert.assertEquals(result.im, Complex.multiply(c1, c2, 3.0).getIm());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticWithNullComplex() {
 Complex.multiply(c1, null);
 }

 private double getRe(Complex complex) {
 return complex.getRe();
 }

 private double getIm(Complex complex) {
 return complex.getIm();
 }

}