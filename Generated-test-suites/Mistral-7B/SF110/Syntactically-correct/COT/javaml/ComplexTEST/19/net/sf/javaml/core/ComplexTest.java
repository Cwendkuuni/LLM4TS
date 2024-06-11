import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import net.sf.javaml.core.Complex;
import org.junit.Test;

public class ComplexTest {

 // Test constructor with valid arguments
 @Test
 public void testComplexConstruction() {
 Complex c1 = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", c1.toString());
 }

 // Test constructor with default arguments
 @Test
 public void testComplexDefaultConstruction() {
 Complex c1 = new Complex();
 assertEquals("0.0 + 0.0i", c1.toString());
 }

 // Test abs method
 @Test
 public void testAbs() {
 Complex c1 = new Complex(3.0, 4.0);
 assertEquals(5.0, c1.abs(), 0.001);
 }

 // Test plus method
 @Test
 public void testPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 c1.plus(c2);
 assertEquals("4.0 + 6.0i", c1.toString());
 }

 // Test minus method
 @Test
 public void testMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 c1.minus(c2);
 assertEquals("-2.0 - 2.0i", c1.toString());
 }

 // Test times method with Complex
 @Test
 public void testTimesComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(2.0, 3.0);
 c1.times(c2);
 assertEquals("-2.0 + 6.0i", c1.toString());
 }

 // Test times method with double
 @Test
 public void testTimesDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 c1.times(3.0);
 assertEquals("3.0 + 6.0i", c1.toString());
 }

 // Test conjugate method
 @Test
 public void testConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 c1.conjugate();
 assertEquals("1.0 - 2.0i", c1.toString());
 }

 // Test static plus method
 @Test
 public void testStaticPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 assertEquals("4.0 + 6.0i", sum.toString());
 }

 // Test static multiply method with double
 @Test
 public void testStaticMultiplyDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex product = Complex.multiply(c1, 3.0);
 assertEquals("3.0 + 6.0i", product.toString());
 }

 // Test static multiply method with Complex
 @Test
 public void testStaticMultiplyComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = Complex.multiply(c1, c2);
 assertEquals("-2.0 + 6.0i", product.toString());
 }

 // Test null argument in plus method
 @Test(expected = NullPointerException.class)
 public void testPlusNullArgument() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = null;
 c1.plus(c2);
 }

 // Test null argument in minus method
 @Test(expected = NullPointerException.class)
 public void testMinusNullArgument() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = null;
 c1.minus(c2);
 }

 // Test null argument in times method with Complex
 @Test(expected = NullPointerException.class)
 public void testTimesComplexNullArgument() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = null;
 c1.times(c2);
 }

 // Test null argument in times method with double
 @Test(expected = NullPointerException.class)
 public void testTimesDoubleNullArgument() {
 Complex c1 = new Complex(1.0, 2.0);
 double d = null;
 c1.times(d);
 }
}