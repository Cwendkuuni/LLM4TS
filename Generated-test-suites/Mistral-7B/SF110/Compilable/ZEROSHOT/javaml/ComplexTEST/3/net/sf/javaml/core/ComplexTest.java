package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
 public void testAbsMethod() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex();

 assertEquals(5.0, c1.abs(), 0.01);
 assertEquals(1.4142135625, c2.abs(), 0.01);
 assertEquals(0.0, c3.abs(), 0.01);
 }

 @Test
 public void testPlusMethod() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex();

 c1.plus(c2);
 Complex sum = new Complex(4.0, 6.0);

 assertEquals(sum.toString(), c1.toString());

 c3.plus(c1);
 Complex expected = new Complex(4.0, 10.0);

 assertEquals(expected.toString(), c3.toString());
 }

 @Test
 public void testMinusMethod() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex();

 c1.minus(c2);
 Complex diff = new Complex(2.0, -2.0);

 assertEquals(diff.toString(), c1.toString());

 c3.minus(c1);
 Complex expected = new Complex(-3.0, -4.0);

 assertEquals(expected.toString(), c3.toString());
 }

 @Test
 public void testTimesMethod() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(2.0, 5.0);
 Complex c3 = new Complex();

 c1.times(c2);
 Complex product = new Complex(-5.0, 12.0);

 assertEquals(product.toString(), c1.toString());

 c3.times(2.0);
 Complex expected = new Complex(0.0, 0.0);
 expected.re = 0.0;
 expected.im = 0.0;

 assertEquals(expected.toString(), c3.toString());
 }

 @Test
 public void testConjugateMethod() {
 Complex c1 = new Complex(3.0, 4.0);

 Complex expected = new Complex(3.0, -4.0);

 c1.conjugate();

 assertEquals(expected.toString(), c1.toString());
 }

 @Test
 public void testStaticMethods() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex();

 Complex sum = Complex.plus(c1, c2);
 Complex product = Complex.multiply(c1, c2);

 Complex expectedSum = new Complex(4.0, 6.0);
 Complex expectedProduct = new Complex(-5.0, 12.0);

 assertEquals(expectedSum.toString(), sum.toString());
 assertEquals(expectedProduct.toString(), product.toString());

 Complex multipleOfC1 = Complex.multiply(c1, 2.0);
 Complex multipleOfC2 = Complex.multiply(c2, c1);

 Complex expectedMultipleOfC1 = new Complex(6.0, 8.0);
 Complex expectedMultipleOfC2 = new Complex(-11.0, 12.0);

 assertEquals(expectedMultipleOfC1.toString(), multipleOfC1.toString());
 assertEquals(expectedMultipleOfC2.toString(), multipleOfC2.toString());
 }

 @Test
 public void testToStringMethod() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex();

 assertEquals("3.0 + 4.0i", c1.toString());
 assertEquals("1.0 + 2.0i", c2.toString());
 assertEquals("0.0 + 0.0i", c3.toString());
 }

 @Test
 public void testIConstant() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex i = Complex.I;
 Complex c2 = Complex.multiply(c1, i);

 Complex expected = new Complex(3.0, -4.0);

 assertEquals(expected.toString(), c2.toString());
 }
}