package net.sf.javaml.core;

 import org.junit.Test;
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNotSame;

 public class ComplexTest {

 @Test
 public void testComplexConstruction() {
 Complex c1 = new Complex(3.0, 4.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex();

 assertEquals("3.0 + 4.0i", c1.toString());
 assertEquals("3.0 + 0.0i", c3.toString());

 assertEquals(c1, c2); // should be equal since they have the same values
 }

 @Test
 public void testAbs() {
 Complex c1 = new Complex(3.0, 4.0);
 double expected = 5.0;

 assertEquals(expected, c1.abs(), 0.0);
 }

 @Test
 public void testPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex expected = new Complex(4.0, 6.0);

 c1.plus(c2);

 assertEquals(expected.toString(), c1.toString());
 }

 @Test
 public void testMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex expected = new Complex(-2.0, -2.0);

 c1.minus(c2);

 assertEquals(expected.toString(), c1.toString());
 }

 @Test
 public void testTimesReal() {
 Complex c1 = new Complex(3.0, 4.0);
 double scalar = 2.0;
 Complex expected = new Complex(6.0, 8.0);

 c1.times(scalar);

 assertEquals(expected.toString(), c1.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex c1 = new Complex(1.0, 1.0);
 Complex c2 = new Complex(1.0, 1.0);
 Complex expected = new Complex(1.0, 2.0);

 c1.times(c2);

 assertEquals(expected.toString(), c1.toString());
 }

 @Test
 public void testConjugate() {
 Complex c1 = new Complex(1.0, 1.0);
 Complex expected = new Complex(1.0, -1.0);

 c1.conjugate();

 assertEquals(expected.toString(), c1.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex expected = new Complex(4.0, 6.0);

 Complex result = Complex.plus(c1, c2);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyReal() {
 Complex c1 = new Complex(1.0, 1.0);
 double scalar = 2.0;
 Complex expected = new Complex(2.0, 2.0);

 Complex result = Complex.multiply(c1, scalar);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyComplex() {
 Complex c1 = new Complex(1.0, 1.0);
 Complex c2 = new Complex(1.0, 1.0);
 Complex expected = new Complex(-1.0, 1.0);

 Complex result = Complex.multiply(c1, c2);

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticI() {
 Complex expected = new Complex(0.0, 1.0);
 Complex result = Complex.I;

 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testEquals() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(1.0, 3.0);
 Complex c3 = new Complex(1.0, 2.0);

 assertNotSame(c1, c2); // different objects
 assertEquals(c1, c3); // same values
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNull() {
 Complex c1 = new Complex(1.0, 1.0);
 Complex c2 = null;

 c1.times(c2);
 }
 }