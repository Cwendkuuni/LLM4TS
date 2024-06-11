package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex obj1;
 private Complex obj2;
 private Complex obj3;
 private Complex obj4;
 private Complex I;

 @Before
 public void setUp() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(5.0, 6.0);
 obj4 = new Complex();
 I = Complex.I;
 }

 @Test
 public void testConstructor() {
 Complex c1 = new Complex(2.5, 3.5);
 Complex c2 = new Complex();
 assertEquals("2.5 + 3.5i", c1.toString());
 assertEquals("0.0 + 0.0i", c2.toString());
 }

 @Test
 public void testAbs() {
 assertEquals("5.0", String.format("%.2f", obj1.abs()));
 assertEquals("0.0", obj2.abs().toString());
 }

 @Test
 public void testPlus() {
 Complex result = new Complex();
 obj1.plus(obj2);
 result.re = obj1.re + obj2.re;
 result.im = obj1.im + obj2.im;
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.toString(), obj1.toString());
 }

 @Test
 public void testMinus() {
 Complex result = new Complex();
 obj1.minus(obj2);
 result.re = obj1.re - obj2.re;
 result.im = obj1.im - obj2.im;
 Complex expected = new Complex(-1.0, -2.0);
 assertEquals(expected.toString(), obj1.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex result = new Complex();
 obj1.times(obj2);
 final double real = obj1.re * obj2.re - obj1.im * obj2.im;
 final double imag = obj1.re * obj2.im + obj1.im * obj2.re;
 result.re = real;
 result.im = imag;
 Complex expected = new Complex(-3.0, 10.0);
 assertEquals(expected.toString(), obj1.toString());
 }

 @Test
 public void testTimesDouble() {
 Complex result = new Complex();
 obj1.times(2.5);
 result.re *= 2.5;
 result.im *= 2.5;
 Complex expected = new Complex(2.5, 5.0);
 assertEquals(expected.toString(), obj1.toString());
 }

 @Test
 public void testConjugate() {
 Complex result = new Complex();
 obj1.conjugate();
 result.im = -obj1.im;
 Complex expected = new Complex(1.0, -2.0);
 assertEquals(expected.toString(), obj1.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex result = Complex.plus(obj1, obj2);
 final double real = obj1.re + obj2.re;
 final double imag = obj1.im + obj2.im;
 result.re = real;
 result.im = imag;
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyDoubleComplex() {
 Complex result = Complex.multiply(obj1, 2.5);
 result.re *= 2.5;
 result.im *= 2.5;
 Complex expected = new Complex(2.5, 5.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyComplexComplex() {
 Complex result = Complex.multiply(obj1, obj2);
 final double real = obj1.re * obj2.re - obj1.im * obj2.im;
 final double imag = obj1.re * obj2.im + obj1.im * obj2.re;
 result.re = real;
 result.im = imag;
 Complex expected = new Complex(-3.0, 10.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticI() {
 Complex expected = Complex.I;
 assertEquals("0.0 + 1.0i", expected.toString());
 }
}