package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 private Complex obj1;
 private Complex obj2;
 private Complex obj3;
 private Complex obj4;
 private Complex obj5;
 private Complex constantI;

 @Before
 public void setup() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(5.0, 6.0);
 obj4 = new Complex(1.0, 1.0);
 obj5 = new Complex(-1.0, 1.0);
 constantI = Complex.I;
 }

 @Test
 public void testConstructor() {
 Complex obj = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", obj.toString());
 }

 @Test
 public void testDefaultConstructor() {
 Complex obj = new Complex();
 assertEquals("0.0 + 0.0i", obj.toString());
 }

 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, obj1.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex result = obj1;
 result.plus(obj2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testMinus() {
 Complex result = obj1;
 result.minus(obj2);
 Complex expected = new Complex(-2.0, -2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testTimesReal() {
 Complex result = obj1;
 result.times(3.0);
 Complex expected = new Complex(3.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex result = obj1;
 result.times(obj2);
 Complex expected = new Complex(-5.0, 2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testConjugate() {
 Complex result = obj1;
 result.conjugate();
 Complex expected = new Complex(1.0, -2.0);
 assertNotSame(obj1, result);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex result = Complex.plus(obj1, obj2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyReal() {
 Complex result = Complex.multiply(obj1, 3.0);
 Complex expected = new Complex(3.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyComplex() {
 Complex result = Complex.multiply(obj1, obj2);
 Complex expected = new Complex(-5.0, 2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticI() {
 Complex expected = new Complex(0.0, 1.0);
 assertEquals(expected.toString(), Complex.I.toString());
 }
}