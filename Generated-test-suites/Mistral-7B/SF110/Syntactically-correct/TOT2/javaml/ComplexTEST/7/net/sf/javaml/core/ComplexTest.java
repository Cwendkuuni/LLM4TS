import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;
import org.junit.Test;
import org.junit.Before;
import org.junit.ExpectedException;

public class ComplexTest {

 @Test
 public void testComplexConstructor() {
     Complex c1 = new Complex(1.0, 2.0);
     Complex c2 = new Complex();
     assertEquals("1.0 + 2.0i", c1.toString());
     assertEquals("0.0 + 0.0i", c2.toString());
 }

 @Test
 public void testAbs() {
     Complex c = new Complex(3.0, 4.0);
     assertEquals(5.0, c.abs(), 0.0);
 }

 @Test
 public void testPlus() {
     Complex c1 = new Complex(1.0, 2.0);
     Complex c2 = new Complex(3.0, 4.0);
     Complex sum = new Complex();
     c1.plus(c2);
     sum.re = 4.0;
     sum.im = 6.0;
     assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testPlusWithNullComplex() {
     Complex c1 = new Complex(1.0, 2.0);
     Complex nullComplex = null;
     c1.plus(nullComplex);
 }

 @Test
 public void testPlusWithZeroComplex() {
     Complex c1 = new Complex(1.0, 2.0);
     Complex zeroComplex = new Complex();
     Complex sum = new Complex();
     c1.plus(zeroComplex);
     sum.re = 1.0;
     sum.im = 2.0;
     assertEquals("1.0 + 2.0i", sum.toString());
 }

 @Test
 public void testMinus() {
     Complex c1 = new Complex(1.0, 2.0);
     Complex c2 = new Complex(3.0, 4.0);
     Complex diff = new Complex();
     c1.minus(c2);
     diff.re = -2.0;
     diff.im = -2.0;
     assertEquals("-2.0 - 2.0i", diff.toString());
 }

 // Test other methods and edge cases here...

}