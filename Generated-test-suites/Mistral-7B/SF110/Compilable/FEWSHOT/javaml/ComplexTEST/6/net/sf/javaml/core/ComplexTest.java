package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComplexTest {

 @Test
 public void testComplexConstruction() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex();

 assertEquals("1.0 + 2.0i", c1.toString());
 assertEquals("3.0 + 4.0i", c2.toString());
 assertEquals("0.0 + 0.0i", c3.toString());
 }

 @Test
 public void testComplexAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 double expected = Math.sqrt(1 * 1 + 2 * 2);

 assertEquals(expected, c1.abs(), 0.001);
 }

 @Test
 public void testComplexPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex expected = new Complex(4.0, 6.0);

 Complex result = new Complex();
 c1.plus(c2);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testComplexMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex expected = new Complex(-2.0, -2.0);

 Complex result = new Complex();
 c1.minus(c2);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testComplexTimes() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex expected = new Complex(-5.0, 10.0);

 Complex result = new Complex();
 c1.times(c2);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testComplexConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex expected = new Complex(1.0, -2.0);

 Complex result = new Complex();
 c1.conjugate();

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testComplexTimesWithDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 double alpha = 3.0;
 Complex expected = new Complex(3.0, 6.0);

 Complex result = new Complex();
 c1.times(alpha);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testComplexStaticMethods() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 Complex product = Complex.multiply(c1, c2);
 Complex i = Complex.I;

 assertEquals(sum.toString(), "(4.0 + 6.0i)");
 assertEquals(product.toString(), "(3.0*1.0 - 4.0*2.0) + (3.0*2.0 + 1.0*4.0)i");
 assertEquals("(0.0 + 1.0i)", i.toString());
 }

 @Test
 public void testComplexNull() {
 Complex c1 = null;
 assertNull(c1);
 }
}