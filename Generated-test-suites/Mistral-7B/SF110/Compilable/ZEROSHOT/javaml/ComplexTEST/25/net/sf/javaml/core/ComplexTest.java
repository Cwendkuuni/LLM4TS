package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 @Test
 public void testComplexConstruction() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex();
 Complex c4 = new Complex(0.0, 1.0); // I

 assertEquals("1.0 + 2.0i", c1.toString());
 assertEquals("3.0 + 4.0i", c2.toString());
 assertEquals("0.0 + 0.0i", c3.toString());
 assertEquals("0.0 + 1.0i", c4.toString());
 }

 @Test
 public void testComplexAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(-1.0, -2.0);
 Complex c3 = new Complex(0.0, 0.0);

 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
 assertEquals(Math.sqrt(1.0 * 1.0 + (-2.0) * (-2.0)), c2.abs(), 0.001);
 assertEquals(0.0, c3.abs(), 0.001);
 }

 @Test
 public void testComplexPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex(5.0, 6.0);
 Complex c4 = new Complex();

 c1.plus(c2);
 assertEquals("4.0 + 6.0i", c1.toString());

 c3.plus(c1);
 assertEquals("9.0 + 10.0i", c3.toString());

 c1.plus(c4);
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testComplexMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex();
 Complex c4 = new Complex(0.0, 1.0); // I

 c1.minus(c2);
 assertEquals("-2.0 - 2.0i", c1.toString());

 c3.minus(c1);
 assertEquals("-1.0 - (-1.0)i", c3.toString());

 c1.minus(c4);
 assertEquals("0.0 + 1.0i", c1.toString());
 }

 @Test
 public void testComplexTimes() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex(1.0, 1.0);
 Complex c4 = new Complex(-1.0, -1.0);

 c1.times(c2);
 assertEquals("-3.0 - 5.0i", c1.toString());

 c1.times(c3);
 assertEquals("1.0 + 1.0i", c1.toString());

 c1.times(c4);
 assertEquals("-1.0 - (-1.0)i", c1.toString());
 }

 @Test
 public void testComplexTimesDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(1.0, 1.0);

 c1.times(2.0);
 assertEquals("2.0 + 2.0i", c1.toString());

 c1.times(-2.0);
 assertEquals("-2.0 - 2.0i", c1.toString());
 }

 @Test
 public void testComplexConjugate() {
 Complex c1 = new Complex(1.0, 2.0);

 c1.conjugate();
 assertEquals("1.0 - 2.0i", c1.toString());
 }

 @Test
 public void testComplexStaticMethods() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);

 Complex sum = Complex.plus(c1, c2);
 assertEquals("4.0 + 6.0i", sum.toString());

 Complex prod = Complex.multiply(c1, c2);
 assertEquals("-3.0 - 5.0i", prod.toString());

 Complex prodWithI = Complex.multiply(c1, Complex.I);
 assertEquals("1.0 + 2.0i", prodWithI.toString());
 }

 @Test
 public void testComplexImmutability() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = c1;
 c2.re = 3.0;
 c2.im = 4.0;

 assertEquals("1.0 + 2.0i", c1.toString());
 assertNotSame(c1, c2);
 }
}