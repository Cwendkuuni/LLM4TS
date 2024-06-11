package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

 @Test
 public void testComplexConstruction() {
 Complex c1 = new Complex(3.0, 4.0);
 Assert.assertEquals("3.0 + 4.0i", c1.toString());
 Complex c2 = new Complex();
 Assert.assertEquals("0.0 + 0.0i", c2.toString());
 }

 @Test
 public void testAbs() {
 Complex c = new Complex(3.0, 4.0);
 Assert.assertEquals(5.0, c.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(2.0, 3.0);
 Complex c = new Complex();
 a.plus(b);
 Assert.assertEquals("3.0 + 5.0i", c.toString());
 c.re = 0.0;
 c.im = 0.0;
 Complex.plus(a, b).plus(c).re = 0.0;
 Complex.plus(a, b).im = 0.0;
 Assert.assertEquals("3.0 + 5.0i", Complex.plus(a, b).toString());
 }

 @Test
 public void testMinus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(2.0, 3.0);
 Complex c = new Complex();
 a.minus(b);
 Assert.assertEquals("-1.0 - 1.0i", c.toString());
 c.re = 0.0;
 c.im = 0.0;
 Complex.minus(a, b).re = 0.0;
 Complex.minus(a, b).im = 0.0;
 Assert.assertEquals("-1.0 - 1.0i", Complex.minus(a, b).toString());
 }

 @Test
 public void testTimes() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(2.0, 3.0);
 Complex c = new Complex();
 a.times(b);
 Assert.assertEquals("-(2.0*1.0) + (3.0*1.0)i", c.toString());
 c.re = -2.0;
 c.im = 3.0;
 Complex.times(a, b).re = c.re;
 Complex.times(a, b).im = c.im;
 Assert.assertEquals("-(2.0*1.0) + (3.0*1.0)i", Complex.times(a, b).toString());
 }

 @Test
 public void testConjugate() {
 Complex c = new Complex(3.0, 4.0);
 Complex conj = new Complex();
 c.conjugate();
 Assert.assertEquals("3.0 - 4.0i", c.toString());
 conj.im = -c.im;
 Assert.assertEquals("3.0 - 4.0i", conj.toString());
 }

 @Test
 public void testStaticMethods() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(2.0, 3.0);
 Complex c = new Complex();
 Complex d = Complex.plus(Complex.plus(a, b), c);
 Assert.assertEquals("3.0 + 5.0i", d.toString());
 Complex e = Complex.multiply(Complex.multiply(a, b), Complex.I);
 Assert.assertEquals("(1.0*2.0+2.0*2.0i)*(0.0+1.0i) = 2.0i", e.toString());
 }

}