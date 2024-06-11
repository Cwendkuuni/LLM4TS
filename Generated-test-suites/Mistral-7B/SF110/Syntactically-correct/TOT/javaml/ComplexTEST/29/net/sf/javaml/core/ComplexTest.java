import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 @Test
 public void testComplexConstructor() {
 Complex c1 = new Complex(2.0, 3.0);
 Complex c2 = new Complex(1.0, 4.5);
 Complex c3 = new Complex();

 assertEquals("2.0 + 3.0i", c1.toString());
 assertEquals("1.0 + 4.5i", c2.toString());
 assertEquals("0.0 + 0.0i", c3.toString());
 }

 @Test
 public void testComplexAbs() {
 Complex c1 = new Complex(1.0, 2.0);
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);

 assertEquals(expected, c1.abs(), 0.001);
 }

 @Test
 public void testComplexPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = new Complex();

 c1.plus(c2);
 sum.re = 4.0;
 sum.im = 6.0;

 assertEquals("sum = 4.0 + 6.0i", sum.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplexPlusWithNULL() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = null;
 Complex sum = new Complex();

 c1.plus(c2);
 }

 @Test
 public void testComplexMinus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex diff = new Complex();

 c1.minus(c2);
 diff.re = -2.0;
 diff.im = -2.0;

 assertEquals("diff = -2.0 - 2.0i", diff.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplexMinusWithNULL() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = null;
 Complex diff = new Complex();

 c1.minus(c2);
 }

 @Test
 public void testComplexTimes() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = new Complex();

 c1.times(c2);
 product.re = 3.0 * 1.0 - 2.0 * 4.0;
 product.im = 3.0 * 2.0 + 1.0 * 4.0;

 assertEquals("product = -3.0 + 10.0i", product.toString());
 }

 @Test
 public void testComplexTimesWithDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 double factor = 3.0;
 Complex product = new Complex();

 c1.times(factor);
 product.re = 3.0 * 1.0;
 product.im = 3.0 * 2.0;

 assertEquals("product = 3.0 + 6.0i", product.toString());
 }

 @Test
 public void testComplexConjugate() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex conjugate = new Complex();

 c1.conjugate();
 conjugate.im = -2.0;

 assertEquals("conjugate = 1.0 - 2.0i", conjugate.toString());
 }

 @Test
 public void testComplexStaticPlus() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);

 assertEquals("sum = 4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testComplexStaticMultiplyWithDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 double factor = 3.0;
 Complex product = Complex.multiply(c1, factor);

 assertEquals("product = 3.0 + 6.0i", product.toString());
 }

 @Test
 public void testComplexStaticMultiply() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = Complex.multiply(c1, c2);

 assertEquals("-3.0 + 10.0i", product.toString());
 }
}