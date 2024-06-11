import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 Double value = new Double(1.2);
 int expectedHash = 12345; // Any expected hash value would be valid here
 int actualHash = MathUtils.hash(value.doubleValue());
 assertEquals("Incorrect hash for double value", expectedHash, actualHash);
 }

 @Test
 public void testHashDoubleArray() {
 double[] value = {1.2, 2.3, 3.4};
 int expectedHash = 123456789; // Any expected hash value would be valid here
 int actualHash = MathUtils.hash(value);
 assertEquals("Incorrect hash for double array", expectedHash, actualHash);
 }

 @Test
 public void testNormalizeAngle() {
 double angle = 7.5;
 double center = 0.0;
 double expectedResult = 2.5;
 double actualResult = MathUtils.normalizeAngle(angle, center);
 assertEquals("Incorrect normalize angle result", expectedResult, actualResult, 0.001);
 }

 @Test
 public void testNormalizeAngleWithNegativeCenter() {
 double angle = 7.5;
 double center = -3.14;
 double expectedResult = 10.62;
 double actualResult = MathUtils.normalizeAngle(angle, center);
 assertEquals("Incorrect normalize angle result with negative center", expectedResult, actualResult, 0.001);
 }

 @Test
 public void testNormalizeAngleWithSameCenter() {
 double angle = 0.0;
 double center = 0.0;
 double expectedResult = 0.0;
 double actualResult = MathUtils.normalizeAngle(angle, center);
 assertEquals("Incorrect normalize angle result with same center", expectedResult, actualResult, 0.001);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteWithNaN() {
 MathUtils.checkFinite(Double.NaN);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteWithInfinite() {
 MathUtils.checkFinite(Double.POSITIVE_INFINITY);
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 5;
 byte sign = -1;
 byte expectedResult = -5;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 byte actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals("Incorrect copy sign result for byte", expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteWithOverflow() {
 byte magnitude = 127;
 byte sign = 1;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 500;
 short sign = -1;
 short expectedResult = -500;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 short actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals("Incorrect copy sign result for short", expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortWithOverflow() {
 short magnitude = 32767;
 short sign = 1;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignInt() {
 int magnitude = 50000;
 int sign = -1;
 int expectedResult = -50000;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 int actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals("Incorrect copy sign result for int", expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntWithOverflow() {
 int magnitude = Integer.MIN_VALUE;
 int sign = 1;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignLong() {
 long magnitude = 50000L;
 long sign = -1L;
 long expectedResult = -50000L;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 long actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals("Incorrect copy sign result for long", expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongWithOverflow() {
 long magnitude = Long.MIN_VALUE;
 long sign = 1L;
 MathUtils.checkNotNull(magnitude);
 MathUtils.checkNotNull(sign);
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCheckNotNull() {
 Object obj = new Object();
 MathUtils.checkNotNull(obj);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 MathUtils.checkNotNull(null);
 }

 @Test
 public void testCheckNotNullLocalizablePattern() {
 Object obj = new Object();
 String pattern = "Null object provided";
 Object[] args = new Object[0];
 MathUtils.checkNotNull(obj, pattern, args);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObjectWithPattern() {
 MathUtils.checkNotNull(null, "Null object provided", new Object[0]);
 }
}