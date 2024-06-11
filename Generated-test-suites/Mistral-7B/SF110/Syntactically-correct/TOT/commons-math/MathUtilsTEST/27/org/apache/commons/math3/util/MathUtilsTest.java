import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 Double value = new Double(123.45);
 int expectedHashCode = value.hashCode();
 int actualHashCode = MathUtils.hash(value.doubleValue());
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test(expected = MathArithmeticException.class)
 public void testHashDoubleNegative() {
 double value = -123.45;
 MathUtils.hash(value);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.1, 2.2, 3.3};
 int expectedHashCode = Arrays.hashCode(values);
 int actualHashCode = MathUtils.hash(values);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testNormalizeAngle() {
 double a = 3.141592653589793; // pi
 double center = MathUtils.TWO_PI / 2; // pi/2
 double expectedResult = 1.5707963267948966; // pi/2
 double actualResult = MathUtils.normalizeAngle(a, center);
 assertEquals(expectedResult, actualResult, 0.000001);
 }

 @Test
 public void testNormalizeAngleWrongCentre() {
 double a = 3.141592653589793; // pi
 double center = 10.0;
 double expectedResult = a - MathUtils.TWO_PI; // -pi
 double actualResult = MathUtils.normalizeAngle(a, center);
 assertEquals(expectedResult, actualResult, 0.000001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeAngleNullAngle() {
 MathUtils.normalizeAngle(null, MathUtils.TWO_PI);
 }

 @Test
 public void testReduce() {
 double a = 1.5 * MathUtils.TWO_PI; // 3 pi
 double period = MathUtils.TWO_PI / 2; // pi/2
 double offset = MathUtils.PI; // pi
 double expectedResult = a - period * Math.floor((a - offset) / period) - offset; // 1.5 * pi
 double actualResult = MathUtils.reduce(a, period, offset);
 assertEquals(expectedResult, actualResult, 0.000001);
 }

 @Test
 public void testReduceNegativePeriod() {
 double a = 1.5 * MathUtils.TWO_PI; // 3 pi
 double period = -MathUtils.TWO_PI / 2; // -pi/2
 double offset = MathUtils.PI; // pi
 double expectedResult = a + period * Math.floor((a - offset) / period) + offset; // 4.5 * pi
 double actualResult = MathUtils.reduce(a, period, offset);
 assertEquals(expectedResult, actualResult, 0.000001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReduceNullInput() {
 MathUtils.reduce(null, MathUtils.TWO_PI, MathUtils.PI);
 }

 @Test
 public void testCopySignByte() {
 byte sign = 1;
 byte magnitude = 50;
 byte expectedResult = 50;
 byte actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignByteNegativeSign() {
 byte sign = -1;
 byte magnitude = 50;
 byte expectedResult = -50;
 byte actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteOverflow() {
 byte sign = 1;
 byte magnitude = Byte.MIN_VALUE;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignShort() {
 short sign = 1;
 short magnitude = 32767;
 short expectedResult = 32767;
 short actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignShortNegativeSign() {
 short sign = -1;
 short magnitude = 32767;
 short expectedResult = -32767;
 short actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortOverflow() {
 short sign = 1;
 short magnitude = Short.MIN_VALUE;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignInt() {
 int sign = 1;
 int magnitude = Integer.MAX_VALUE;
 int expectedResult = Integer.MAX_VALUE;
 int actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignIntNegativeSign() {
 int sign = -1;
 int magnitude = Integer.MAX_VALUE;
 int expectedResult = Integer.MIN_VALUE;
 int actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntOverflow() {
 int sign = 1;
 int magnitude = Integer.MIN_VALUE;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignLong() {
 long sign = 1;
 long magnitude = Long.MAX_VALUE;
 long expectedResult = Long.MAX_VALUE;
 long actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignLongNegativeSign() {
 long sign = -1;
 long magnitude = Long.MAX_VALUE;
 long expectedResult = Long.MIN_VALUE;
 long actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongOverflow() {
 long sign = 1;
 long magnitude = Long.MIN_VALUE;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 10.0;
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

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteTwoDimsArray() {
 double[] values = {1.0, Double.NaN};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteOneDimsArray() {
 double[] values = {Double.POSITIVE_INFINITY};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 MathUtils.checkNotNull(null);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullStringPattern() {
 MathUtils.checkNotNull(null, "Found null for required '{0}'", "name");
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullArray() {
 double[] values = null;
 MathUtils.checkNotNull(values);
 }
}