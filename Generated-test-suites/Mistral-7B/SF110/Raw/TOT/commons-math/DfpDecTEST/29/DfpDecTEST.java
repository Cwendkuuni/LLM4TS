import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

 private static final DfpField FIELD = new Dfp128Field();

 @Test
 public void testConstructorWithInt() {
 DfpDec dfpDec = new DfpDec(FIELD, 42);
 Assert.assertEquals(42.0, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithDouble() {
 DfpDec dfpDec = new DfpDec(FIELD, 3.14);
 Assert.assertEquals(3.14, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec dfpDec = new DfpDec(FIELD, "123.4567");
 Assert.assertEquals(123.4567, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec1 = new DfpDec(FIELD, new DfpDec(FIELD, 123.45));
 DfpDec dfpDec2 = new DfpDec(FIELD, dfpDec1);
 Assert.assertEquals(dfpDec1, dfpDec2);
 }

 @Test
 public void testNewInstanceMethod() {
 DfpDec dfpDec = new DfpDec(FIELD, 123.45);
 DfpDec newInstance = dfpDec.newInstance();
 Assert.assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testRoundMethod() {
 DfpDec dfpDec = new DfpDec(FIELD, 123.4567);
 int result = dfpDec.round(Math.RoundingMode.HALF_UP.value());
 Assert.assertEquals(123.457, dfpDec.doubleValueExact(), 0.0);
 Assert.assertEquals(123.457, dfpDec.round(Math.RoundingMode.HALF_UP.value()).doubleValueExact(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRoundMethodInvalidRoundingMode() {
 DfpDec dfpDec = new DfpDec(FIELD, 123.45);
 dfpDec.round(-1);
 }

 @Test
 public void testNextAfterMethod() {
 DfpDec dfpDec1 = new DfpDec(FIELD, 0.12345678901234567890123456789);
 DfpDec dfpDec2 = new DfpDec(FIELD, 0.12345678901234567890123456789).nextAfter(dfpDec1);
 Assert.assertNotEquals(dfpDec1, dfpDec2);
 Assert.assertTrue(dfpDec2.compareTo(dfpDec1) > 0);
 }

 @Test
 public void testNextAfterMethodZero() {
 DfpDec zero = new DfpDec(FIELD, 0.0);
 DfpDec nextAfter = zero.nextAfter(zero);
 Assert.assertEquals(zero, nextAfter);
 }

 @Test
 public void testNextAfterMethodNaN() {
 DfpDec dfpDec1 = new DfpDec(FIELD, Double.NaN);
 DfpDec dfpDec2 = new DfpDec(FIELD, Double.NaN).nextAfter(dfpDec1);
 Assert.assertEquals(dfpDec1, dfpDec2);
 }

 @Test
 public void testNextAfterMethodWithSameValue() {
 DfpDec dfpDec1 = new DfpDec(FIELD, 123.45);
 DfpDec dfpDec2 = new DfpDec(FIELD, 123.45).nextAfter(dfpDec1);
 Assert.assertEquals(dfpDec1, dfpDec2);
 }
}