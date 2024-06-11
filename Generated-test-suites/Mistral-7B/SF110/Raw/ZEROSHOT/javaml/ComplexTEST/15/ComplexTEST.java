package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 @Test
 public void testComplexConstruction() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex();

 assertEquals("3.0 + 4.0i", c1.toString());
 assertEquals("1.0 + 2.0i", c2.toString());
 assertEquals("0.0 + 0.0i", c3.toString());
 }

 @Test
 public void testAbs() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 1.0);

 assertEquals(5.0, c1.abs(), 0.001);
 assertEquals(1.4142135625, c2.abs(), 0.00000001);
 }

 @Test
 public void testPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = new Complex();

 c1.plus(c2);
 sum.re = 4.0;
 sum.im = 6.0;

 assertEquals("sum = 4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex diff = new Complex();

 c1.minus(c2);
 diff.re = -2.0;
 diff.im = -2.0;

 assertEquals("diff = -2.0 - 2.0i", diff.toString());
 }

 @Test
 public void testTimes() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = new Complex();

 c1.times(c2);
 product.re = 3.0 * 1.0 - 2.0 * 4.0;
 product.im = 3.0 * 2.0 + 1.0 * 4.0;

 assertEquals("product = -2.0 + 11.0i", product.toString());
 }

 @Test
 public void testConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex conj = new Complex();

 c1.conjugate();
 conj.re = 1.0;
 conj.im = -2.0;

 assertEquals("conjugate = 1.0 - 2.0i", conj.toString());
 }

 @Test
 public void testStaticMethods() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 1.0);
 Complex sum = Complex.plus(c1, c2);
 Complex prod = Complex.multiply(c1, c2);
 Complex i = Complex.I;

 assertEquals("sum = 4.0 + 6.0i", sum.toString());
 assertEquals("product = -2.0 + 11.0i", prod.toString());
 assertEquals("i = 0.0 + 1.0i", i.toString());
 }

 @Test
 public void testEquals() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex(3.0, 3.0);
 Complex c4 = new Complex(3.0, 4.1);

 assertEquals(c1, c2);
 assertNotSame(c1, c3);
 assertNotSame(c1, c4);
 }

 @Test
 public void testToString() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 1.0);
 Complex i = Complex.I;

 assertEquals("3.0 + 4.0i", c1.toString());
 assertEquals("1.0 + 1.0i", c2.toString());
 assertEquals("0.0 + 1.0i", i.toString());
 }
}