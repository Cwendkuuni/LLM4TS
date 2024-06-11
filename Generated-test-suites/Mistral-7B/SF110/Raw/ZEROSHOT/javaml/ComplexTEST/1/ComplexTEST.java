package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 Complex obj1, obj2, obj3, obj4;
 final double EPS = 0.0001;

 @Before
 public void setUp() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(1.0, 0.0);
 obj4 = new Complex(0.0, 1.0);
 }

 @Test
 public void testConstructor() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex();
 assertEquals("(1.0 + 2.0i)", c1.toString());
 assertEquals("(0.0 + 0.0i)", c2.toString());
 }

 @Test
 public void testAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 double result = c1.abs();
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), result, EPS);
 }

 @Test
 public void testPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = new Complex();
 c1.plus(c2);
 sum.re = 4.0;
 sum.im = 6.0;
 assertEquals(sum.toString(), c1.toString());
 }

 @Test
 public void testMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex diff = new Complex();
 c1.minus(c2);
 diff.re = -2.0;
 diff.im = -2.0;
 assertEquals(diff.toString(), c1.toString());
 }

 @Test
 public void testTimesReal() {
 Complex c1 = new Complex(1.0, 2.0);
 double factor = 3.0;
 c1.times(factor);
 assertEquals("(3.0 + 6.0i)", c1.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = new Complex();
 c1.times(c2);
 double real = 1.0 * 3.0 - 2.0 * 4.0;
 double imag = 1.0 * 4.0 + 2.0 * 3.0;
 product.re = real;
 product.im = imag;
 assertEquals(product.toString(), c1.toString());
 }

 @Test
 public void testConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex conj = new Complex();
 c1.conjugate();
 conj.im = -2.0;
 assertEquals(conj.toString(), c1.toString() + "*");
 }

 @Test
 public void testStaticPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 assertEquals(sum.toString(), "(4.0 + 6.0i)");
 }

 @Test
 public void testStaticMultiplyReal() {
 Complex c1 = new Complex(1.0, 2.0);
 double factor = 3.0;
 Complex product = Complex.multiply(c1, factor);
 assertEquals("(3.0 + 6.0i)", product.toString());
 }

 @Test
 public void testStaticMultiplyComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = Complex.multiply(c1, c2);
 double real = 1.0 * 3.0 - 2.0 * 4.0;
 double imag = 1.0 * 4.0 + 2.0 * 3.0;
 product.re = real;
 product.im = imag;
 assertEquals(product.toString(), "(1.0-2.0i)*(3.0+4.0i)=" + product.toString());
 }

 @Test
 public void testI() {
 Complex c1 = Complex.I;
 assertEquals("(0.0 + 1.0i)", c1.toString());
 }
}