package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complex4;
 private Complex complex5;
 private Complex complex6;
 private Complex complex7;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex(5.0, 6.0);
 complex4 = new Complex(1.0, 0.0);
 complex5 = new Complex(0.0, 1.0);
 complex6 = new Complex();
 complex7 = Complex.I;
 }

 @Test
 public void testConstructor() {
 Complex complex8 = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", complex8.toString());
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), complex1.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex complex9 = new Complex(1.0, 1.0);
 complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex1.toString());
 complex1.plus(complex3);
 assertEquals("10.0 + 12.0i", complex1.toString());
 complex1.plus(complex4);
 assertEquals("11.0 + 2.0i", complex1.toString());
 complex1.plus(complex5);
 assertEquals("12.0 + 3.0i", complex1.toString());
 complex1.plus(complex6);
 assertEquals("12.0 + 3.0i", complex1.toString());
 complex1.plus(complex7);
 assertEquals("12.0 + 3.0i + 0.0i + 1.0i = 13.0 + 3.0i", (complex1.plus(complex7)).toString());
 }

 @Test
 public void testMinus() {
 Complex complex10 = new Complex(1.0, -2.0);
 complex1.minus(complex2);
 assertEquals("-2.0 + 2.0i", complex1.toString());
 complex1.minus(complex3);
 assertEquals("-6.0 + 1.0i", complex1.toString());
 complex1.minus(complex4);
 assertEquals("0.0 + 2.0i", complex1.toString());
 complex1.minus(complex5);
 assertEquals("1.0 - 1.0i", complex1.toString());
 complex1.minus(complex6);
 assertEquals("1.0 - 1.0i", complex1.toString());
 complex1.minus(complex7);
 assertEquals("1.0 - 1.0i - 0.0i - 1.0i = 0.0 + 0.0i", complex1.minus(complex7).toString());
 }

 @Test
 public void testTimes() {
 Complex complex11 = new Complex(2.0, 3.0);
 complex1.times(complex11);
 assertEquals("-3.0 + 6.0i", complex1.toString());
 complex1.times(5.0);
 assertEquals("5.0 + 10.0i", complex1.toString());
 complex11.conjugate();
 complex1.times(complex11);
 assertEquals("35.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testTimesDouble() {
 Complex complex12 = new Complex(2.0, 3.0);
 complex1.times(2.0);
 assertEquals("2.0 + 4.0i", complex1.toString());
 complex1.times(-2.0);
 assertEquals("-2.0 + -4.0i = -2.0 - 4.0i", complex1.toString());
 complex1.times(Complex.I);
 assertEquals("2.0i", complex1.toString());
 }

 @Test
 public void testConjugate() {
 Complex complex13 = new Complex(1.0, -1.0);
 complex1.conjugate();
 assertEquals("1.0 + 2.0i", complex1.toString());
 complex13.conjugate();
 assertEquals("1.0 - 1.0i", complex13.toString());
 complex1.conjugate().conjugate();
 assertEquals("1.0 + 2.0i", complex1.conjugate().toString());
 }

 @Test
 public void testStaticMethodsPlus() {
 Complex complex14 = new Complex(1.0, 1.0);
 Complex complex15 = new Complex(2.0, 3.0);
 assertEquals("3.0 + 4.0i", Complex.plus(complex1, complex12).toString());
 assertEquals("3.0 + 4.0i", Complex.plus(complex12, complex1).toString());
 assertEquals("1.0 + 1.0i + 2.0 + 3.0i = 3.0 + 4.0i", Complex.plus(complex1, complex12).toString());
 }

 @Test
 public void testStaticMethodsMultiply() {
 Complex complex16 = new Complex(1.0, 1.0);
 Complex complex17 = new Complex(1.0, 1.0);
 assertEquals("-2.0 + 2.0i", Complex.multiply(complex1, complex15).toString());
 assertEquals("-2.0 + 2.0i", Complex.multiply(complex15, complex1).toString());
 assertEquals("-3.0 + 3.0i", Complex.multiply(complex1, complex17).toString());
 assertEquals("-3.0 + 3.0i", Complex.multiply(complex17, complex1).toString());
 }

 @Test
 public void testStaticMethodsI() {
 assertEquals("0.0 + 1.0i", complex7.toString());
 }
}