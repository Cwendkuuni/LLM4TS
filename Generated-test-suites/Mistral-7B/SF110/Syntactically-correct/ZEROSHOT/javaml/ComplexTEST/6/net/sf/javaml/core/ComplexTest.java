package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complex4;
 private Complex complex5;
 private Complex complexI;

 @Test
 public void testConstructorWithReAndIm() {
 complex1 = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", complex1.toString());
 }

 @Test
 public void testConstructorWithReAndImZero() {
 complex2 = new Complex();
 assertEquals("0.0 + 0.0i", complex2.toString());
 }

 @Test
 public void testAbs() {
 complex1 = new Complex(3.0, 4.0);
 assertEquals(5.0, complex1.abs(), 0.001);
 complex2 = new Complex(-1.0, -1.0);
 assertEquals(Math.sqrt(2.0), complex2.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex3.toString());
 complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex1.toString());
 }

 @Test
 public void testMinus() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = complex1.minus(complex2);
 assertEquals("-2.0 - 2.0i", complex3.toString());
 complex1.minus(complex2);
 assertEquals("-2.0 - 2.0i", complex1.toString());
 }

 @Test
 public void testTimes() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = complex1.times(complex2);
 assertEquals("3.0 - 10.0i", complex3.toString());
 complex1.times(complex2);
 assertEquals("3.0 - 10.0i", complex1.toString());
 }

 @Test
 public void testConjugate() {
 complex1 = new Complex(1.0, 2.0);
 complex1.conjugate();
 assertEquals("1.0 - 2.0i", complex1.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex complexA = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 Complex complexC = Complex.plus(complexA, complexB);
 assertEquals("4.0 + 6.0i", complexC.toString());
 }

 @Test
 public void testStaticMultiplyWithDouble() {
 Complex complexA = new Complex(1.0, 2.0);
 Complex complexB = Complex.multiply(complexA, 3.0);
 assertEquals("3.0 + 6.0i", complexB.toString());
 }

 @Test
 public void testStaticMultiply() {
 Complex complexA = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 Complex complexC = Complex.multiply(complexA, complexB);
 assertEquals("3.0 - 10.0i", complexC.toString());
 }

 @Test
 public void testStaticI() {
 complexI = Complex.I;
 assertEquals("0.0 + 1.0i", complexI.toString());
 }

 @Test
 public void testToString() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex(-1.0, -2.0);
 assertEquals("1.0 + 2.0i", complex1.toString());
 assertEquals("3.0 + 4.0i", complex2.toString());
 assertEquals("-1.0 - 2.0i", complex3.toString());
 }

 @Test
 public void testTimesWithReAndIm() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = complex1.times(complex2);
 complex1.times(complex2);
 assertEquals("3.0 - 10.0i", complex3.toString());
 }
}