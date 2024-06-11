package net.sf.javaml.core;

import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.*;import static net.sf.javaml.core.Complex.*;

public class ComplexTest {

 @Test
 public void testConstructorWithReAndIm() {
 Complex c = new Complex(2.0, 3.0);
 Assert.assertEquals("2.0 + 3.0i", c.toString());
 }

 @Test
 public void testConstructorWithDefaultValues() {
 Complex c = new Complex();
 Assert.assertEquals("0.0 + 0.0i", c.toString());
 }

 @Test
 public void testAbs() {
 Complex c = new Complex(3.0, 4.0);
 Assert.assertEquals(5.0, c.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 a.plus(b);
 Assert.assertEquals("4.0 + 6.0i", a.toString());
 }

 @Test
 public void testMinus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 a.minus(b);
 Assert.assertEquals("-2.0 - 2.0i", a.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 a.times(b);
 Assert.assertEquals("7.0 + 10.0i", a.toString());
 }

 @Test
 public void testTimesDouble() {
 Complex a = new Complex(1.0, 2.0);
 a.times(2.0);
 Assert.assertEquals("2.0 + 4.0i", a.toString());
 }

 @Test
 public void testConjugate() {
 Complex a = new Complex(1.0, 2.0);
 a.conjugate();
 Assert.assertEquals("1.0 - 2.0i", a.toString());
 }

 @Test
 public void testPlusStatic() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex sum = plus(a, b);
 Assert.assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testMultiplyStaticWithDouble() {
 Complex a = new Complex(1.0, 2.0);
 Complex result = multiply(a, 2.0);
 Assert.assertEquals("2.0 + 4.0i", result.toString());
 }

 @Test
 public void testMultiplyStaticWithComplex() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex result = multiply(a, b);
 Assert.assertEquals("7.0 + 10.0i", result.toString());
 }

 @Test
 public void testStaticI() {
 Complex expected = new Complex(0.0, 1.0);
 Complex actual = I;
 Assert.assertEquals(expected, actual);
 }
}