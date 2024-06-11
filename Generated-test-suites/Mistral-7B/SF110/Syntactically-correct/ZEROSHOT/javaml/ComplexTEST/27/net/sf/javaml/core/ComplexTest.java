package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 @Test
 public void testConstructorWithReAndIm() {
 Complex c1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testConstructorWithOnlyRe() {
 Complex c1 = new Complex(3.0);
 assertEquals("3.0 + 0.0i", c1.toString());
 }

 @Test
 public void testAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 double result = c1.abs();
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), result, 0.001);
 }

 @Test
 public void testPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = new Complex();
 c1.plus(c2);
 sum.re = 4.0;
 sum.im = 6.0;
 assertEquals(sum.toString(), c1.toString() + " + " + c2.toString());
 }

 @Test
 public void testMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex diff = new Complex();
 c1.minus(c2);
 diff.re = -2.0;
 diff.im = -2.0;
 assertEquals(diff.toString(), c1.toString() + " - " + c2.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = new Complex();
 c1.times(c2);
 double real = 3.0 * 1.0 - 2.0 * 4.0;
 double imag = 3.0 * 2.0 + 1.0 * 4.0;
 product.re = real;
 product.im = imag;
 assertEquals(product.toString(), c1.toString() + " * " + c2.toString());
 }

 @Test
 public void testTimesDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex result = new Complex();
 c1.times(2.0);
 result.re = 2.0 * 1.0;
 result.im = 2.0 * 2.0;
 assertEquals(result.toString(), c1.toString() + " * 2.0");
 }

 @Test
 public void testConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex conjugate = new Complex();
 c1.conjugate();
 conjugate.im = -2.0;
 assertEquals(conjugate.toString(), "(" + c1.toString() + ").conjugate()");
 }

 @Test
 public void testPlusStatic() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 assertEquals(sum.toString(), "(" + c1.toString() + ") + (" + c2.toString() + ")");
 }

 @Test
 public void testMultiplyStatic() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = Complex.multiply(c1, c2);
 assertEquals(product.toString(), "(" + c1.toString() + ") * (" + c2.toString() + ")");
 }

 @Test
 public void testI() {
 Complex i = Complex.I;
 assertEquals("0.0 + 1.0i", i.toString());
 }

 @Test
 public void testEquals() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex(1.0, 3.0);
 Complex c4 = new Complex(1.0, 2.0);
 assertEquals(c1, c4);
 assertNotSame(c1, c2);
 assertNotSame(c1, c3);
 }
}