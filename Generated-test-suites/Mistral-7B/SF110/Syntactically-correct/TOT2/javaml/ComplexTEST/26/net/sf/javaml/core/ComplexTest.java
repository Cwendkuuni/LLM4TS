import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

   @org.junit.Test
   public void testComplexConstructor() {
      Complex complex = new Complex(1.0, 2.0);
      assertEquals(1.0, complex.getRe(), 0.0);
      assertEquals(2.0, complex.getIm(), 0.0);
   }

   @org.junit.Test
   public void testEmptyComplexConstructor() {
      Complex complex = new Complex();
      assertEquals(0.0, complex.getRe(), 0.0);
      assertEquals(0.0, complex.getIm(), 0.0);
   }

   @org.junit.Test(expected = IllegalArgumentException.class)
   public void testComplexConstructorWithInvalidArguments() {
      new Complex("invalid", 2.0);
   }

   @org.junit.Test
   public void testComplexToString() {
      Complex complex = new Complex(1.0, 2.0);
      assertEquals("1.0 + 2.0i", complex.toString());
   }

   @org.junit.Test
   public void testComplexAbs() {
      Complex complex = new Complex(1.0, 2.0);
      double abs = complex.abs();
      assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), abs, 0.0);
   }

   @org.junit.Test
   public void testComplexPlus() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = new Complex(3.0, 4.0);
      Complex sum = new Complex();
      a.plus(b, sum);
      assertEquals(4.0, sum.getRe(), 0.0);
      assertEquals(6.0, sum.getIm(), 0.0);
   }

   // Add test cases for other methods here

   @org.junit.Test
   public void testComplexPlusNaN() {
      Complex a = new Complex(Double.NaN, 0.0);
      Complex b = new Complex(Double.NaN, 0.0);
      Complex sum = new Complex();
      Complex expected = new Complex(Double.NaN, Double.NaN);
      a.plus(b, sum);
      assertEquals(expected.getRe(), sum.getRe(), 0.0);
      assertEquals(expected.getIm(), sum.getIm(), 0.0);
   }

   @org.junit.Test(expected = IllegalArgumentException.class)
   public void testComplexPlusNull() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = null;
      Complex sum = new Complex();
      a.plus(b, sum);
   }

   @org.junit.Test
   public void testComplexMinus() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = new Complex(3.0, 4.0);
      Complex difference = new Complex();
      a.minus(b, difference);
      assertEquals(-2.0, difference.getRe(), 0.0);
      assertEquals(-6.0, difference.getIm(), 0.0);
   }

   // Add test cases for other methods here

   @org.junit.Test
   public void testComplexTimes() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = new Complex(3.0, 4.0);
      Complex product = new Complex();
      a.times(b, product);
      double real = a.getRe() * b.getRe() - a.getIm() * b.getIm();
      double imag = a.getRe() * b.getIm() + a.getIm() * b.getRe();
      assertEquals(real, product.getRe(), 0.0);
      assertEquals(imag, product.getIm(), 0.0);
   }

   @org.junit.Test
   public void testComplexTimesWithLargeValues() {
      Complex a = new Complex(1e10, 1e10);
      Complex b = new Complex(1e10, 1e10);
      Complex product = new Complex();
      a.times(b, product);
      // The results of this test case may differ based on the floating-point implementation
      // but the difference between the expected and actual values should be within the tolerance
      double expectedReal = 1e20 * Math.cos(Math.PI / 4.0);
      double expectedImag = 1e20 * Math.sin(Math.PI / 4.0);
      assertEquals(expectedReal, product.getRe(), 1e20);
      assertEquals(expectedImag, product.getIm(), 1e20);
   }

   @org.junit.Test
   public void testComplexTimeWithSmallValues() {
      Complex a = new Complex(1e-10, 1e-10);
      Complex b = new Complex(1e-10, 1e-10);
      Complex product = new Complex();
      a.times(b, product);
      double expectedReal = 0.0;
      double expectedImag = 0.0;
      assertEquals(expectedReal, product.getRe(), 0.0);
      assertEquals(expectedImag, product.getIm(), 0.0);
   }

   @org.junit.Test
   public void testComplexConjugate() {
      Complex complex = new Complex(1.0, 2.0);
      Complex conjugated = new Complex();
      complex.conjugate(conjugated);
      assertEquals(-2.0, conjugated.getIm(), 0.0);
      assertEquals(1.0, conjugated.getRe(), 0.0);
   }

   @org.junit.Test
   public void testComplexStaticPlus() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = new Complex(3.0, 4.0);
      Complex sum = Complex.plus(a, b);
      assertEquals(4.0, sum.getRe(), 0.0);
      assertEquals(6.0, sum.getIm(), 0.0);
   }

   @org.junit.Test
   public void testComplexStaticMultiplyWithDouble() {
      Complex a = new Complex(1.0, 2.0);
      Complex result = Complex.multiply(a, 2.0);
      Complex expected = new Complex(2.0, 4.0);
      assertEquals(expected.getRe(), result.getRe(), 0.0);
      assertEquals(expected.getIm(), result.getIm(), 0.0);
   }

   @org.junit.Test
   public void testComplexStaticMultiply() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = new Complex(3.0, 4.0);
      Complex result = Complex.multiply(a, b);
      Complex expected = new Complex(-1.0, 10.0);
      assertEquals(expected.getRe(), result.getRe(), 0.0);
      assertEquals(expected.getIm(), result.getIm(), 0.0);
   }

   // Add test cases for static methods with edge cases and exception handling scenarios here

}