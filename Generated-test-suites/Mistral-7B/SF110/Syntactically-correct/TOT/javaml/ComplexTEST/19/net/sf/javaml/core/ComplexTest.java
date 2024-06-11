import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 @Test
 public void testComplexConstructor() {
 Complex c1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testComplexDefaultConstructor() {
 Complex c1 = new Complex();
 assertEquals("0.0 + 0.0i", c1.toString());
 }

 @Test
 public void testComplexAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 assertEquals(Math.sqrt(5.0), c1.abs(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplexAbsWithNaN() {
 Complex c1 = new Complex(Double.NaN, 0.0);
 c1.abs();
 }

 @Test
 public void testComplexPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = new Complex();
 c1.plus(c2);
 assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testComplexMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex difference = new Complex();
 c1.minus(c2);
 assertEquals("-2.0 - 2.0i", difference.toString());
 }

 @Test
 public void testComplexTimes() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = new Complex();
 c1.times(c2);
 assertEquals("3.0 + 10.0i", product.toString());
 }

 @Test
 public void testComplexConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex conjugate = new Complex();
 c1.conjugate();
 assertEquals("1.0 - 2.0i", conjugate.toString());
 }

 @Test
 public void testComplexStaticMethods() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 Complex product = Complex.multiply(c1, c2);
 Complex scalarProduct = Complex.multiply(c1, 2.0);
 assertEquals("4.0 + 6.0i", sum.toString());
 assertEquals("3.0 + 10.0i", product.toString());
 assertEquals("2.0 + 2.0i", scalarProduct.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplexTimesWithNaN() {
 Complex c1 = new Complex(Double.NaN, 0.0);
 Complex c2 = new Complex(0.0, Double.NaN);
 Complex product = new Complex();
 c1.times(c2);
 }
}