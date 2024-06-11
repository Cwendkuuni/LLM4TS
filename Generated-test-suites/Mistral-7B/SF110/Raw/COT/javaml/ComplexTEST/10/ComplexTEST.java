package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex complex1 = new Complex(1.0, 2.0);
 private Complex complex2 = new Complex(3.0, 4.0);
 private Complex complex3 = new Complex(0.0, 0.0);
 private Complex complexI = Complex.I;

 @Test
 public void testComplexConstructorWithReAndIm() {
 Complex complex = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", complex.toString());
 }

 @Test
 public void testComplexConstructorWithoutReAndIm() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 @Test
 public void testAbsMethod() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), complex1.abs(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusMethodWithNullComplexB() {
 complex1.plus(null);
 }

 @Test
 public void testPlusMethod() {
 Complex result = new Complex();
 complex1.plus(complex2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result.re, complex1.re + complex2.re);
 assertEquals(result.im, complex1.im + complex2.im);
 }

 @Test
 public void testMinusMethod() {
 Complex result = new Complex();
 complex1.minus(complex2);
 result.re = -1.0;
 result.im = -4.0;
 assertEquals(result.re, complex1.re - complex2.re);
 assertEquals(result.im, complex1.im - complex2.im);
 }

 @Test
 public void testTimesMethodWithComplexB() {
 Complex result = new Complex();
 complex1.times(complex2);
 result.re = -1.0 * complex2.re * complex1.re + complex2.im * complex1.im;
 result.im = complex2.re * complex1.im + complex2.im * complex1.re;
 assertEquals(result.re, complex1.re * complex2.re - complex1.im * complex2.im);
 assertEquals(result.im, complex1.re * complex2.im + complex1.im * complex2.re);
 }

 @Test
 public void testTimesMethodWithDoubleAlpha() {
 Complex result = new Complex();
 complex1.times(2.0);
 result.re = complex1.re * 2.0;
 result.im = complex1.im * 2.0;
 assertEquals(result.re, complex1.re * 2.0);
 assertEquals(result.im, complex1.im * 2.0);
 }

 @Test
 public void testConjugateMethod() {
 Complex result = new Complex();
 complex1.conjugate();
 result.im = -complex1.im;
 assertEquals(result.toString(), complex1.toString().replace("i", "-i"));
 }

 @Test
 public void testPlusStaticMethod() {
 Complex result = new Complex();
 Complex sum = Complex.plus(complex1, complex2);
 result.re = complex1.re + complex2.re;
 result.im = complex1.im + complex2.im;
 assertEquals(result.re, complexSum.re);
 assertEquals(result.im, complexSum.im);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAAndDoubleD() {
 Complex result = new Complex();
 Complex product = Complex.multiply(complex1, 2.0);
 result.re = complex1.re * 2.0;
 result.im = complex1.im * 2.0;
 assertEquals(result.re, product.re);
 assertEquals(result.im, product.im);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAAndComplexB() {
 Complex result = new Complex();
 Complex product = Complex.multiply(complex1, complex2);
 result.re = complex1.re * complex2.re - complex1.im * complex2.im;
 result.im = complex1.re * complex2.im + complex1.im * complex2.re;
 assertEquals(result.re, product.re);
 assertEquals(result.im, product.im);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticMethodWithComplexAAndNullComplexB() {
 Complex product = Complex.multiply(complex1, null);
 fail("Illegal argument exception expected");
 }
}