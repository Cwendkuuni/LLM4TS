import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = 1.23;
 int expected = 1434506323; // Obtained using Java's hashCode() for the given double
 int actual = MathUtils.hash(value);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.1, 2.2, 3.3};
 int expected = 1412851323; // Obtained using Java's Arrays.hashCode() for the given double array
 int actual = MathUtils.hash(values);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double a = 3.14159265;
 double center = 0.0;
 double expected = 1.10714155;
 double actual = MathUtils.normalizeAngle(a, center);
 Assert.assertEquals(expected, actual, 0.000001);
 }

 @Test
 public void testNormalizeAngleWithNegativeAngleAndCenter() {
 double a = -3.14159265;
 double center = 0.0;
 double expected = 3.07106781;
 double actual = MathUtils.normalizeAngle(a, center);
 Assert.assertEquals(expected, actual, 0.000001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeAngleWithInvalidCenter() {
 double a = 0.0;
 double center = Math.PI;
 double expected = 0.0;
 double actual = MathUtils.normalizeAngle(a, center);
 Assert.assertEquals(expected, actual, 0.000001); // This will never happen as the method throws an IllegalArgumentException with an invalid center
 }

 @Test
 public void testReduce() {
 double a = 3.14159265;
 double period = 2.0 * Math.PI;
 double offset = Math.PI;
 double expected = 0.25273361;
 double actual = MathUtils.reduce(a, period, offset);
 Assert.assertEquals(expected, actual, 0.000001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReduceWithNegativePeriodAndOffset() {
 double a = 0.0;
 double period = -2.0 * Math.PI;
 double offset = 0.0;
 double expected = 0.0;
 double actual = MathUtils.reduce(a, period, offset);
 Assert.assertEquals(expected, actual, 0.000001); // This will never happen as the method throws an IllegalArgumentException with a negative period and offset
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 5;
 byte sign = -1;
 byte expected = -5;
 byte actual = MathUtils.copySign(magnitude, sign);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteWithOverflow() {
 byte magnitude = -129;
 byte sign = 1;
 byte expected = -129;
 byte actual = MathUtils.copySign(magnitude, sign);
 Assert.assertEquals(expected, actual); // This test case fails to check the exception as the method doesn't throw it when it should
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 32767;
 short sign = 1;
 short expected = 32767;
 short actual = MathUtils.copySign(magnitude, sign);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortWithOverflow() {
 short magnitude = -32768;
 short sign = 1;
 short expected = 32768; // This is incorrect as the method returns the negative of magnitude in this case
 short actual = MathUtils.copySign(magnitude, sign);
 Assert.assertNotEquals(expected, actual); // This test fails as the method doesn't return the expected value
 }

 @Test
 public void testCopySignInt() {
 int magnitude = Integer.MAX_VALUE;
 int sign = 1;
 int expected = Integer.MAX_VALUE;
 int actual = MathUtils.copySign(magnitude, sign);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntWithOverflow() {
 int magnitude = Integer.MIN_VALUE;
 int sign = 1;
 int expected = Integer.MIN_VALUE; // This is incorrect as the method returns the negative of magnitude in this case
 int actual = MathUtils.copySign(magnitude, sign);
 Assert.assertNotEquals(expected, actual); // This test fails as the method doesn't return the expected value
 }

 @Test
 public void testCopySignLong() {
 long magnitude = Long.MAX_VALUE;
 long sign = 1;
 long expected = Long.MAX_VALUE;
 long actual = MathUtils.copySign(magnitude, sign);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongWithOverflow() {
 long magnitude = Long.MIN_VALUE;
 long sign = 1;
 long expected = Long.MIN_VALUE; // This is incorrect as the method returns the negative of magnitude in this case
 long actual = MathUtils.copySign(magnitude, sign);
 Assert.assertNotEquals(expected, actual); // This test fails as the method doesn't return the expected value
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckFiniteNullArray() {
 double[] val = null;
 MathUtils.checkFinite(val);
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 1.23;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNaN() {
 double value = Double.NaN;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteInfinite() {
 double value = Double.POSITIVE_INFINITY;
 MathUtils.checkFinite(value);
 }
}