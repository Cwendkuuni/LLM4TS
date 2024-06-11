import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = 3.14;
 int expected = 12345; // any valid hash code for comparison
 int actual = MathUtils.hash(value);
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testHashNullDouble() {
 MathUtils.hash(null);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 int expected = 123456789; // any valid hash code for comparison
 int actual = MathUtils.hash(values);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double a = Math.PI;
 double center = 0.0;
 double expected = 3.141592653589793;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testNormalizeAngleWithCenterGreaterThanPi() {
 double a = Math.PI * 2.0;
 double center = 3.141592653589793;
 double expected = a - 6.283185307179586;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeAngleWithInvalidCenter() {
 double a = Math.PI;
 double center = 5.0;
 MathUtils.normalizeAngle(a, center);
 }

 @Test
 public void testReduce() {
 double a = 2.5;
 double period = 1.0;
 double offset = 0.5;
 double expected = 1.25;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testReduceWithNegativePeriod() {
 double a = 2.5;
 double period = -1.0;
 double offset = 0.5;
 double expected = Math.PI - 2.3; // result may depend on the initial value of Math.PI
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 5;
 byte sign = 1;
 byte expected = 5;
 byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test
 public void testCopySignByteNegativeMagnitudePositiveSign() {
 byte magnitude = -1;
 byte sign = 1;
 byte expected = -1;
 byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteMagnitudeEqualsMinus128SignPositive() {
 byte magnitude = Byte.MIN_VALUE;
 byte sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 5;
 short sign = 1;
 short expected = 5;
 short actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test
 public void testCopySignShortNegativeMagnitudePositiveSign() {
 short magnitude = -1;
 short sign = 1;
 short expected = -1;
 short actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortMagnitudeEqualsMinus32768SignPositive() {
 short magnitude = Short.MIN_VALUE;
 short sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignInt() {
 int magnitude = 5;
 int sign = 1;
 int expected = 5;
 int actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test
 public void testCopySignIntNegativeMagnitudePositiveSign() {
 int magnitude = -1;
 int sign = 1;
 int expected = -1;
 int actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntMagnitudeEqualsMinus2147483648SignPositive() {
 int magnitude = Integer.MIN_VALUE;
 int sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignLong() {
 long magnitude = 5;
 long sign = 1;
 long expected = 5;
 long actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test
 public void testCopySignLongNegativeMagnitudePositiveSign() {
 long magnitude = -1;
 long sign = 1;
 long expected = -1;
 long actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongMagnitudeEqualsMinus9223372036854775807SignPositive() {
 long magnitude = Long.MIN_VALUE;
 long sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 3.14;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNaNDouble() {
 double value = Double.NaN;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteInfiniteDouble() {
 double value = Double.POSITIVE_INFINITY;
 MathUtils.checkFinite(value);
 }

 @Test
 public void testCheckFiniteDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNaNDoubleArrayElement() {
 double[] values = {1.2, Double.NaN, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteInfiniteDoubleArrayElement() {
 double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test
 public void testCheckNotNullObject() {
 Object o = new Object();
 MathUtils.checkNotNull(o);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 Object o = null;
 MathUtils.checkNotNull(o);
 }

 @Test
 public void testCheckNotNullLocalizablePatternAndArgs() {
 Object o = new Object();
 MathUtils.checkNotNull(o, "Invalid Object {%s}", "Test");
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObjectAndLocalizablePatternAndArgs() {
 Object o = null;
 MathUtils.checkNotNull(o, "Invalid Object {%s}", "Test");
 }
}