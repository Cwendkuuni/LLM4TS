package net.sf.javaml.core;

import static org.junit.Assert.*;

public class ComplexTest {

 @Test
 public void testComplexConstructor() {
 Complex c = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c.toString());
 }

 @Test
 public void testAbsMethod() {
 Complex c = new Complex(1.0, 2.0);
 double result = c.abs();
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), result, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplexConstructorInvalidInput() {
 new Complex("Hello", 2.0);
 }

 @Test
 public void testToStringMethod() {
 Complex c = new Complex(0.0, 0.0);
 assertEquals("0.0 + 0.0i", c.toString());
 }

 @Test
 public void testPlusMethod() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex result = new Complex();
 a.plus(b);
 assertEquals(4.0, result.re, 0.001);
 assertEquals(6.0, result.im, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusMethodInvalidInput() {
 Complex a = new Complex(1.0, 2.0);
 a.plus(null);
 }

 @Test
 public void testMinusMethod() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(-1.0, -2.0);
 Complex result = new Complex();
 a.minus(b);
 assertEquals(2.0, result.re, 0.001);
 assertEquals(-4.0, result.im, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusMethodInvalidInput() {
 Complex a = new Complex(1.0, 2.0);
 a.minus(null);
 }

 @Test
 public void testTimesMethod() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex result = new Complex();
 a.times(b);
 double real = 3.0 * 1.0 - 4.0 * 2.0;
 double imag = 3.0 * 2.0 + 1.0 * 4.0;
 assertEquals(real, result.re, 0.001);
 assertEquals(imag, result.im, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodInvalidInput() {
 Complex a = new Complex(1.0, 2.0);
 a.times(null);
 }

 @Test
 public void testConjugateMethod() {
 Complex a = new Complex(1.0, 2.0);
 Complex result = new Complex();
 a.conjugate();
 assertEquals(-2.0, result.im, 0.001);
 }

 @Test
 public void testPlusStaticMethodWithComplexNumbers() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex result = Complex.plus(a, b);
 double real = 4.0;
 double imag = 6.0;
 assertEquals(real, result.re, 0.001);
 assertEquals(imag, result.im, 0.001);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexNumbers() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(a, b);
 double real = 3.0 * 1.0 - 4.0 * 2.0;
 double imag = 3.0 * 2.0 + 1.0 * 4.0;
 assertEquals(real, result.re, 0.001);
 assertEquals(imag, result.im, 0.001);
 }

 @Test
 public void testComplexNumberWithLargeRealAndImaginaryParts() {
 Complex a = new Complex(Double.MAX_VALUE, Double.MAX_VALUE);
 Complex b = new Complex(Double.MAX_VALUE, Double.MIN_VALUE);
 Complex c = new Complex();
 Complex.times(a, b);
 assertEquals(Double.MAX_VALUE * Double.MAX_VALUE, c.re, 0.001);
 assertEquals(Double.MAX_VALUE * Double.MIN_VALUE, c.im, 0.001);
 }

 @Test
 public void testComplexNumberWithZeroRealAndImaginaryParts() {
 Complex a = new Complex(0.0, 0.0);
 Complex b = new Complex(0.0, 0.0);
 Complex c = new Complex();
 Complex.times(a, b);
 assertEquals(0.0, c.re, 0.001);
 assertEquals(0.0, c.im, 0.001);
 }

}