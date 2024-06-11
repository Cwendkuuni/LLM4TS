package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
   @Test
   public void testComplexConstructor() {
      Complex c = new Complex(1.0, 2.0);
      assertEquals("1.0 + 2.0i", c.toString());
   }

   @Test
   public void testComplexAbs() {
      Complex c = new Complex(3.0, 4.0);
      assertEquals(5.0, c.abs(), 0.01);
   }

   @Test
   public void testComplexPlus() {
      Complex a = new Complex(1.0, 2.0);
      Complex b = new Complex(3.0, 4.0);
      Complex sum = new Complex();
      a.plus(b);
      sum.re = 4.0;
      sum.im = 6.0;
      assertEquals(sum, a);
   }

   // Add tests for other methods similarly
}