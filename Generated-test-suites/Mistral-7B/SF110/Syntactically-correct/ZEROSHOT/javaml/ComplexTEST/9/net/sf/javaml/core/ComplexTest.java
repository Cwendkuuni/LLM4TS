package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 Complex obj1, obj2, obj3, obj4, obj5;
 final double EPS = 0.001;

 @Test
 public void testConstructor() {
 obj1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", obj1.toString());
 }

 @Test
 public void testDefaultConstructor() {
 obj1 = new Complex();
 assertEquals("0.0 + 0.0i", obj1.toString());
 }

 @Test
 public void testAbs() {
 obj1 = new Complex(1.0, 2.0);
 assertEquals(Math.sqrt(5.0), obj1.abs(), EPS);
 }

 @Test
 public void testPlus() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj1.plus(obj2);
 assertEquals("4.0 + 6.0i", obj1.toString());
 }

 @Test
 public void testMinus() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj1.minus(obj2);
 assertEquals("-2.0 - 2.0i", obj1.toString());
 }

 @Test
 public void testTimesReal() {
 obj1 = new Complex(1.0, 2.0);
 obj1.times(3.0);
 assertEquals("3.0 + 6.0i", obj1.toString());
 }

 @Test
 public void testTimesComplex() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj1.times(obj2);
 assertEquals("-2.0 + 10.0i", obj1.toString());
 }

 @Test
 public void testConjugate() {
 obj1 = new Complex(1.0, 2.0);
 obj1.conjugate();
 assertEquals("1.0 - 2.0i", obj1.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testStaticTimesReal() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex mul = Complex.multiply(c1, 3.0);
 assertEquals("3.0 + 6.0i", mul.toString());
 }

 @Test
 public void testStaticTimesComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex prod = Complex.multiply(c1, c2);
 assertEquals("-2.0 + 10.0i", prod.toString());
 }

 @Test
 public void testStaticI() {
 Complex i = Complex.I;
 assertEquals("0.0 + 1.0i", i.toString());
 }
}