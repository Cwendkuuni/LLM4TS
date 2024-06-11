package net.sf.javaml.core;
import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 @Test
 public void testComplexConstructor() {
 Complex c1 = new Complex(1.0, 2.0);
 Assert.assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testComplexAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 double result = c1.abs();
 Assert.assertEquals(Math.sqrt(5.0), result, 0.01);
 }

 @Test
 public void testComplexPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = new Complex();
 c1.plus(c2);
 Assert.assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testComplexMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex difference = new Complex();
 c1.minus(c2);
 Assert.assertEquals("-2.0 - 2.0i", difference.toString());
 }

 @Test
 public void testComplexTimesReal() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex result = new Complex();
 c1.times(2.0);
 Assert.assertEquals("2.0 + 4.0i", result.toString());
 }

 @Test
 public void testComplexTimesComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = new Complex();
 c1.times(c2);
 Assert.assertEquals("-5.0 + 10.0i", product.toString());
 }

 @Test
 public void testComplexConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex conjugate = new Complex();
 c1.conjugate();
 Assert.assertEquals("1.0 - 2.0i", conjugate.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplexTimesWithZeroDivision() {
 Complex c1 = new Complex(1.0, 0.0);
 Complex c2 = new Complex(0.0, 1.0);
 Complex result = new Complex();
 c1.times(c2);
 }

 @Test
 public void testComplexStaticMethods() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 Complex product = Complex.multiply(c1, c2);
 Assert.assertEquals("4.0 + 6.0i", sum.toString());
 Assert.assertEquals("-5.0 + 10.0i", product.toString());
 }
}