import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complexI;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex();
 complexI = Complex.I;
 }

 @Test
 public void testComplexConstructor() {
 Complex c = new Complex(1.0, 2.0);
 assertEquals(1.0, c.getRe(), 0.01);
 assertEquals(2.0, c.getIm(), 0.01);
 }

 @Test
 public void testComplexNoArgumentConstructor() {
 Complex c = new Complex();
 assertEquals(0.0, c.getRe(), 0.01);
 assertEquals(0.0, c.getIm(), 0.01);
 }

 @Test
 public void testToString() {
 Complex c = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c.toString());
 }

 @Test
 public void testAbs() {
 Complex c = new Complex(3.0, 4.0);
 double result = c.abs();
 assertEquals(5.0, result, 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsWithNaN() {
 Complex c = new Complex(Double.NaN, 0.0);
 c.abs();
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testPlus() {
 Complex result = complex1.plus(complex2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testMinus() {
 Complex result = complex1.minus(complex2);
 Complex expected = new Complex(-2.0, -2.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testTimes() {
 Complex result = complex1.times(complex2);
 Complex expected = new Complex(-5.0, 2.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testTimesWithDouble() {
 Complex result = complex1.times(2.0);
 Complex expected = new Complex(2.0, 4.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testConjugate() {
 Complex result = complex1.conjugate();
 Complex expected = new Complex(1.0, -2.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testPlusStatic() {
 Complex result = Complex.plus(complex1, complex2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex result = Complex.multiply(complex1, 2.0);
 Complex expected = new Complex(2.0, 4.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex result = Complex.multiply(complex1, complex2);
 Complex expected = new Complex(-5.0, 2.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testStaticI() {
 Complex result = Complex.I;
 Complex expected = new Complex(0.0, 1.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testStaticZero() {
 Complex result = Complex.ZERO;
 Complex expected = new Complex(0.0, 0.0);
 assertEquals(expected.getRe(), result.getRe(), 0.01);
 assertEquals(expected.getIm(), result.getIm(), 0.01);
 }

 @Test
 public void testEquals() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex(1.0, 3.0);
 assertTrue(c1.equals(c1));
 assertTrue(c1.equals(c2));
 assertFalse(c1.equals(c3));
 }

 @Test
 public void testHashCode() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(1.0, 2.0);
 Complex c3 = new Complex(1.0, 3.0);
 assertEquals(c1.hashCode(), c1.hashCode());
 assertEquals(c1.hashCode(), c2.hashCode());
 assertNotEquals(c1.hashCode(), c3.hashCode());
 }

}