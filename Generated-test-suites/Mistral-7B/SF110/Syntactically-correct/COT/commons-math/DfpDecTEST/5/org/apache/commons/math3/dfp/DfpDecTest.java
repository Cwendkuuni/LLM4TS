import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;

public class DfpDecTest {

 private DfpField getField() {
 return new Dfp128Field();
 }

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(getField());
 Assert.assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndByte() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 5);
 Assert.assertEquals(5, dfpDec.intValueExact());
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpDec dfpDec = new DfpDec(getField(), 5);
 Assert.assertEquals(5, dfpDec.intValueExact());
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpDec dfpDec = new DfpDec(getField(), 10L);
 Assert.assertEquals(10, dfpDec.longValueExact());
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 3.14);
 Assert.assertEquals(3.14, dfpDec.doubleValue(), 0.001);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec1 = new DfpDec(getField(), new DfpDec(getField(), 5));
 DfpDec dfpDec2 = new DfpDec(getField(), dfpDec1);
 Assert.assertEquals(dfpDec1, dfpDec2);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec dfpDec = new DfpDec(getField(), "3.1415926536");
 Assert.assertEquals(3.1415926536, dfpDec.doubleValue(), 0.001);
 }

 @Test
 public void testConstructorWithSignAndNans() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 3);
 assertEquals("NaN", dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithFactory() {
 DfpDec dfpDec = new DfpDec(getField());
 DfpDec newInstance = dfpDec.newInstance();
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceWithFactoryAndByte() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 5);
 DfpDec newInstance = dfpDec.newInstance((byte) 5);
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceWithFactoryAndInt() {
 DfpDec dfpDec = new DfpDec(getField(), 5);
 DfpDec newInstance = dfpDec.newInstance(5);
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceWithFactoryAndLong() {
 DfpDec dfpDec = new DfpDec(getField(), 10L);
 DfpDec newInstance = dfpDec.newInstance(10L);
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 3.14);
 DfpDec newInstance = dfpDec.newInstance(3.14);
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec1 = new DfpDec(getField(), new DfpDec(getField(), 5));
 DfpDec newInstance = dfpDec1.newInstance(dfpDec1);
 Assert.assertEquals(dfpDec1, newInstance);
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec dfpDec = new DfpDec(getField(), "3.1415926536");
 DfpDec newInstance = dfpDec.newInstance("3.1415926536");
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceWithSignAndNans() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 3);
 DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 3);
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithDifferentRadixDigits() {
 DfpDec dfpDec1 = new DfpDec(getField(), 5);
 DfpField field = new Dfp16Field();
 DfpDec dfpDec2 = new DfpDec(field, 5);
 DfpDec newInstance = dfpDec1.newInstance(dfpDec2);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(getField(), 314159265358979323846264338327950288419716939937);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(19, decimalDigits);
 }

 @Test
 public void testRoundWithIn() {
 DfpDec dfpDec = new DfpDec(getField(), 3.5);
 int in = DfpField.ROUND_UP;
 int result = dfpDec.round(in);
 assertEquals(4, result);
 }

 @Test
 public void testRoundWithZero() {
 DfpDec dfpDec = new DfpDec(getField(), 3.5);
 int in = 0;
 int result = dfpDec.round(in);
 assertEquals(3, result);
 }

 @Test
 public void testRoundWithNegativeIn() {
 DfpDec dfpDec = new DfpDec(getField(), 3.5);
 int in = -DfpField.ROUND_UP;
 int result = dfpDec.round(in);
 assertEquals(3, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRoundWithInvalidRoundingMode() {
 DfpDec dfpDec = new DfpDec(getField(), 3.5);
 int in = 255;
 int result = dfpDec.round(in);
 }

 @Test
 public void testNextAfterWithSameValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), 3.14159265358979323846264338327950288419716939937);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec1);
 Assert.assertEquals(dfpDec1, nextAfter);
 }

 @Test
 public void testNextAfterWithSmallerValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), 3.14159265358979323846264338327950288419716939937);
 DfpDec dfpDec2 = new DfpDec(getField(), 3.14159265358979323846264338327950288419716939936);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 Assert.assertNotSame(dfpDec1, nextAfter);
 Assert.assertTrue(dfpDec1.compareTo(nextAfter) < 0);
 }

 @Test
 public void testNextAfterWithLargerValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), 3.14159265358979323846264338327950288419716939937);
 DfpDec dfpDec2 = new DfpDec(getField(), 3.14159265358979323846264338327950288419716939938);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 Assert.assertNotSame(dfpDec1, nextAfter);
 Assert.assertTrue(dfpDec1.compareTo(nextAfter) < 0);
 }

 @Test
 public void testNextAfterWithNegativeValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), -3.14159265358979323846264338327950288419716939937);
 DfpDec dfpDec2 = new DfpDec(getField(), -3.14159265358979323846264338327950288419716939936);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 Assert.assertNotSame(dfpDec1, nextAfter);
 Assert.assertTrue(dfpDec1.compareTo(nextAfter) > 0);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDec = new DfpDec(getField(), 0);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 Assert.assertEquals(dfpDec, nextAfter);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.NaN);
 DfpDec dfpDec2 = new DfpDec(getField(), Double.NaN);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 Assert.assertNotSame(dfpDec1, nextAfter);
 Assert.assertEquals(dfpDec1, nextAfter);
 }

 @Test
 public void testNextAfterWithInfinity() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.POSITIVE_INFINITY);
 DfpDec dfpDec2 = new DfpDec(getField(), Double.POSITIVE_INFINITY);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 Assert.assertNotSame(dfpDec1, nextAfter);
 Assert.assertEquals(dfpDec1, nextAfter);
 }
}