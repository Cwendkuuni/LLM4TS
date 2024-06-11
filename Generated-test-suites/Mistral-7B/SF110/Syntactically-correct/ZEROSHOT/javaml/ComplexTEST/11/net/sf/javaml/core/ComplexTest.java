package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testAbs() {
 Complex c = new Complex(3.0, 4.0);
 double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
 assertEquals(expected, c.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex sum = new Complex();

 a.plus(b);

 assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testMinus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex diff = new Complex();

 a.minus(b);

 assertEquals("-2.0 - 2.0i", diff.toString());
 }

 @Test
 public void testTimesReal() {
 Complex c = new Complex(2.0, 3.0);
 double factor = 5.0;

 c.times(factor);

 assertEquals("10.0 + 15.0i", c.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex result = new Complex();

 a.times(b);

 assertEquals("-5.0 + 11.0i", result.toString());
 }

 @Test
 public void testConjugate() {
 Complex c = new Complex(1.0, 2.0);
 Complex conjugate = new Complex();

 c.conjugate();

 assertEquals("1.0 - 2.0i", conjugate.toString());
 }

 @Test
 public void testPlusStatic() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(a, b);

 assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testMultiplyStaticReal() {
 Complex c = new Complex(2.0, 3.0);
 double factor = 5.0;
 Complex product = Complex.multiply(c, factor);

 assertEquals("10.0 + 15.0i", product.toString());
 }

 @Test
 public void testMultiplyStaticComplex() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex product = Complex.multiply(a, b);

 assertEquals("-5.0 + 11.0i", product.toString());
 }
}