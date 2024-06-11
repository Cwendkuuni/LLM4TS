package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ComplexTest {

 private Complex c1;
 private Complex c2;
 private Complex c3;
 private Complex c4;
 private Complex c5;

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex(0.0, 5.0);
 c4 = new Complex(-1.0, 0.0);
 c5 = Complex.I;
 }

 @Test
 public void testConstructorWithDouble() {
 Complex obj = new Complex(3.0);
 assertEquals("3.0 + 0.0i", obj.toString());
 }

 @Test
 public void testConstructorWithDoubleAndDouble() {
 Complex obj = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", obj.toString());
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testAbs() {
 Complex obj = new Complex(3.0, 4.0);
 assertEquals("5.0", Double.toString(obj.abs()));
 }

 @Test
 public void testPlus() {
 c1.plus(c2);
 assertEquals("4.0 + 6.0i", c1.toString());
 }

 @Test
 public void testMinus() {
 c1.minus(c2);
 assertEquals("-2.0 - 2.0i", c1.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex obj = new Complex(1.0, 2.0);
 Complex b = new Complex(2.0, 3.0);
 obj.times(b);
 assertEquals("-1.0 + 6.0i", obj.toString());
 }

 @Test
 public void testTimesDouble() {
 Complex obj = new Complex(3.0, 4.0);
 obj.times(2.0);
 assertEquals("6.0 + 8.0i", obj.toString());
 }

 @Test
 public void testConjugate() {
 c1.conjugate();
 assertEquals("1.0 - 2.0i", c1.toString());
 }

 @Test
 public void testPlusStatic() {
 Complex sum = Complex.plus(c1, c2);
 assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testTimesStatic() {
 Complex product = Complex.multiply(c1, c2);
 assertEquals("-5.0 + 12.0i", product.toString());
 }

 @Test
 public void testStaticI() {
 Complex obj = Complex.I;
 assertEquals("0.0 + 1.0i", obj.toString());
 }

 @Test
 public void testEquals() {
 Complex obj = new Complex(1.0, 2.0);
 Complex obj2 = new Complex(1.0, 2.0);
 Complex obj3 = new Complex(1.0, 3.0);
 assertEquals(obj, obj2);
 assertNotEquals(obj, obj3);
 }

 @Test
 public void testEqualsComplex() {
 Complex obj = new Complex(1.0, 2.0);
 Complex obj2 = new Complex(1.0, 2.0);
 Complex obj3 = new Complex(1.0, 2.0);
 assertEquals(obj, obj2);
 assertEquals(obj, obj3);
 }

 @Test
 public void testEqualsDouble() {
 Complex obj = new Complex(1.0, 2.0);
 double d = 2.0;
 assertNotEquals(obj, d);
 }
}