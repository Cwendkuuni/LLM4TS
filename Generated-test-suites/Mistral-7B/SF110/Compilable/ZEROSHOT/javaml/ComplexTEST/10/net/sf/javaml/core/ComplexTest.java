package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 @Test
 public void testComplexCreation() {
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
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = new Complex();
 c1.plus(c2);

 assertEquals("4.0 + 6.0i", result.toString());
 }

 @Test
 public void testMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = new Complex();
 c1.minus(c2);

 assertEquals("-2.0 - 2.0i", result.toString());
 }

 @Test
 public void testTimesReal() {
 Complex c = new Complex(3.0, 4.0);
 double alpha = 2.0;
 Complex result = new Complex();
 c.times(alpha);

 assertEquals("6.0 + 8.0i", result.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = new Complex();
 c1.times(c2);

 assertEquals("-5.0 + 10.0i", result.toString());
 }

 @Test
 public void testConjugate() {
 Complex c = new Complex(1.0, 2.0);
 Complex result = new Complex();
 c.conjugate();

 assertEquals("1.0 - 2.0i", result.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = Complex.plus(c1, c2);

 assertEquals("4.0 + 6.0i", result.toString());
 }

 @Test
 public void testStaticMultiplyReal() {
 Complex c = new Complex(3.0, 4.0);
 double alpha = 2.0;
 Complex result = Complex.multiply(c, alpha);

 assertEquals("6.0 + 8.0i", result.toString());
 }

 @Test
 public void testStaticMultiplyComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(c1, c2);

 assertEquals("-5.0 + 10.0i", result.toString());
 }

 @Test
 public void testI() {
 Complex expected = new Complex(0.0, 1.0);
 Complex result = Complex.I;

 assertNotSame(expected, result);
 assertEquals("0.0 + 1.0i", result.toString());
 }
}