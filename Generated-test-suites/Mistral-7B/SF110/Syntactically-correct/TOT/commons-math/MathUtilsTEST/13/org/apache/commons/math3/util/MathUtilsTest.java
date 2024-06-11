import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 Double value = new Double(1.2);
 int expected = 1234567; // Any valid hash code can be used for testing purposes
 int actual = MathUtils.hash(value.doubleValue());
 assertEquals(expected, actual);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testHashNaN() {
 MathUtils.hash(Double.NaN);
 }

 @Test(expected = NullArgumentException.class)
 public void testHashNull() {
 MathUtils.hash(null);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 int expected = 123456789; // Any valid hash code can be used for testing purposes
 int actual = MathUtils.hash(values);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double a = 3.141592653589793;
 double center = 0.0;
 double expected = 0.7853981633974483;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.0000001); // Allow for small rounding errors
 }

 @Test
 public void testNormalizeAngleWithCenterGreaterThanPI() {
 double a = 3.141592653589793;
 double center = MathUtils.TWO_PI;
 double expected = -2.4453562353027554;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.0000001); // Allow for small rounding errors
 }

 @Test
 public void testNormalizeAngleWithCenterLessThanZero() {
 double a = 3.141592653589793;
 double center = -MathUtils.TWO_PI;
 double expected = -5.785398163397448;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.0000001); // Allow for small rounding errors
 }

 @Test(expected = NullArgumentException.class)
 public void testNormalizeAngleWithNullArray() {
 MathUtils.normalizeAngle(null, 0.0);
 }

 @Test
 public void testReduce() {
 double a = 3.141592653589793;
 double period = 2.0 * MathUtils.TWO_PI;
 double offset = 0.0;
 double expected = 1.141592653589793;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.0000001); // Allow for small rounding errors
 }

 @Test
 public void testReduceWithNegativePeriod() {
 double a = 3.141592653589793;
 double period = -2.0 * MathUtils.TWO_PI;
 double offset = 0.0;
 double expected = MathUtils.TWO_PI - 1.141592653589793;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.0000001); // Allow for small rounding errors
 }

 @Test
 public void testReduceWithOffsetGreaterThanPeriod() {
 double a = 3.141592653589793;
 double period = 2.0 * MathUtils.TWO_PI;
 double offset = period + 0.1;
 double expected = a - period - offset;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.0000001); // Allow for small rounding errors
 }

 @Test(expected = NullArgumentException.class)
 public void testReduceWithNullArray() {
 MathUtils.reduce(null, 0.0, 0.0);
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 5;
 byte sign = -1;
 byte expected = -5;
 byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteOverflow() {
 byte magnitude = -129;
 byte sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 32767;
 short sign = -1;
 short expected = -32767;
 short actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortOverflow() {
 short magnitude = -32769;
 short sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignInt() {
 int magnitude = Integer.MAX_VALUE;
 int sign = -1;
 int expected = -Integer.MAX_VALUE;
 int actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntOverflow() {
 int magnitude = Integer.MIN_VALUE;
 int sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignLong() {
 long magnitude = Long.MAX_VALUE;
 long sign = -1;
 long expected = -Long.MAX_VALUE;
 long actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongOverflow() {
 long magnitude = Long.MIN_VALUE;
 long sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 1.2;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNaN() {
 MathUtils.checkFinite(Double.NaN);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteInfinite() {
 MathUtils.checkFinite(Double.POSITIVE_INFINITY);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckFiniteNull() {
 MathUtils.checkFinite(null);
 }

 @Test
 public void testCheckFiniteArray() {
 double[] values = {1.2, 2.3, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteArrayNaN() {
 double[] values = {1.2, Double.NaN, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteArrayInfinite() {
 double[] values = {1.2, 2.3, Double.POSITIVE_INFINITY};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckFiniteArrayNull() {
 MathUtils.checkFinite(null);
 }
}