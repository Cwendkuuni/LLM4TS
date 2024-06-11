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
 Complex c = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", c.toString());
 }

 @Test
 public void testComplexNoArgumentConstructor() {
 Complex c = new Complex();
 assertEquals("0.0 + 0.0i", c.toString());
 }

 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, complex1.abs(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsWhenInstanceIsNull() {
 Complex nullComplex = null;
 assertNull(nullComplex); // for consistency, as the test should fail anyway
 try {
 nullComplex.abs();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testPlusMethod() {
 Complex expected = new Complex(4.0, 6.0);
 complex1.plus(complex2);
 assertEquals(expected.re, complex1.re);
 assertEquals(expected.im, complex1.im);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusWhenInstanceIsNull() {
 Complex a = new Complex();
 Complex b = null;
 try {
 a.plus(b);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testMinusMethod() {
 Complex expected = new Complex(0.0, -6.0);
 complex1.minus(complex2);
 assertEquals(expected.re, complex1.re);
 assertEquals(expected.im, complex1.im);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWhenInstanceIsNull() {
 Complex a = new Complex();
 Complex b = complex2;
 try {
 a.minus(b);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTimesMethod() {
 Complex expected = new Complex(-5.0, 10.0);
 complex1.times(complex2);
 assertEquals(expected.re, complex1.re);
 assertEquals(expected.im, complex1.im);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWhenInstanceIsNull() {
 Complex a = new Complex();
 Complex b = complex2;
 try {
 a.times(b);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTimesDouble() {
 Complex expected = new Complex(3.0, 4.0 * 2.0);
 complex1.times(2.0);
 assertEquals(expected.re, complex1.re);
 assertEquals(expected.im, complex1.im);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWhenComplexInstanceIsNull() {
 Complex a = new Complex();
 Complex b = null;
 try {
 a.times(b);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testConjugateMethod() {
 Complex expected = new Complex(1.0, -2.0);
 complex1.conjugate();
 assertEquals(expected.re, complex1.re);
 assertEquals(expected.im, complex1.im);
 }

 @Test
 public void testPlusStaticMethod() {
 Complex expected = new Complex(4.0, 6.0);
 Complex result = Complex.plus(complex1, complex2);
 assertEquals(expected.re, result.re);
 assertEquals(expected.im, result.im);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex expected = new Complex(3.0, 2.0 * 4.0);
 Complex result = Complex.multiply(complex1, 2.0);
 assertEquals(expected.re, result.re);
 assertEquals(expected.im, result.im);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex expected = new Complex(-5.0, 10.0);
 Complex result = Complex.multiply(complex1, complex2);
 assertEquals(expected.re, result.re);
 assertEquals(expected.im, result.im);
 }
}