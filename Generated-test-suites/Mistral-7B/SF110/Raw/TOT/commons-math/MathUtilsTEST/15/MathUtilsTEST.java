import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = 3.14;
 int expectedHash = 12345; // arbitrary expected hash value
 int actualHash = MathUtils.hash(value);
 assertEquals(expectedHash, actualHash);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testHashDoubleInfinite() {
 double value = Double.POSITIVE_INFINITY;
 MathUtils.hash(value);
 }

 @Test(expected = NullArgumentException.class)
 public void testHashNullObject() {
 MathUtils.hash(null);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 int expectedHash = 1234567; // arbitrary expected hash value
 int actualHash = MathUtils.hash(values);
 assertEquals(expectedHash, actualHash);
 }

 @Test
 public void testNormalizeAngle() {
 double angle = Math.PI + 3.14;
 double center = 0.0;
 double expectedResult = -2.14;
 double actualResult = MathUtils.normalizeAngle(angle, center);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 @Test
 public void testNormalizeAngleSameCenter() {
 double angle = Math.PI + 3.14;
 double center = Math.PI;
 double expectedResult = 1.24;
 double actualResult = MathUtils.normalizeAngle(angle, center);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testNormalizeAngleNaN() {
 double angle = Double.NaN;
 double center = 0.0;
 MathUtils.normalizeAngle(angle, center);
 }

 @Test(expected = NullArgumentException.class)
 public void testNormalizeAngleNullCenter() {
 double angle = 0.0;
 MathUtils.normalizeAngle(angle, null);
 }

 @Test
 public void testReduce() {
 double a = 10.5;
 double period = 6.28;
 double offset = 3.14;
 double expectedResult = 1.14;
 double actualResult = MathUtils.reduce(a, period, offset);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 @Test
 public void testReduceZeroPeriod() {
 double a = 1.0;
 double period = 0.0;
 double offset = 0.0;
 double expectedResult = a;
 double actualResult = MathUtils.reduce(a, period, offset);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testReduceNaNInput() {
 double a = Double.NaN;
 double period = 6.28;
 double offset = 3.14;
 MathUtils.reduce(a, period, offset);
 }

 @Test(expected = NullArgumentException.class)
 public void testReduceNullInput() {
 double a = null;
 double period = 6.28;
 double offset = 3.14;
 MathUtils.reduce(a, period, offset);
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 50;
 byte sign = -1;
 byte expectedResult = 50;
 byte actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteOverflow() {
 byte magnitude = -129;
 byte sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = NullArgumentException.class)
 public void testCopySignByteNullMagnitude() {
 byte sign = 1;
 MathUtils.copySign(null, sign);
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 5000;
 short sign = -1;
 short expectedResult = 5000;
 short actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortOverflow() {
 short magnitude = -32769;
 short sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = NullArgumentException.class)
 public void testCopySignShortNullMagnitude() {
 short sign = 1;
 MathUtils.copySign(null, sign);
 }

 @Test
 public void testCopySignInt() {
 int magnitude = 50000;
 int sign = -1;
 int expectedResult = 50000;
 int actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntOverflow() {
 int magnitude = -2147483649;
 int sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = NullArgumentException.class)
 public void testCopySignIntNullMagnitude() {
 int sign = 1;
 MathUtils.copySign(null, sign);
 }

 @Test
 public void testCopySignLong() {
 long magnitude = 50000000L;
 long sign = -1;
 long expectedResult = 50000000L;
 long actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongOverflow() {
 long magnitude = -9223372036854775809L;
 long sign = 1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = NullArgumentException.class)
 public void testCopySignLongNullMagnitude() {
 long sign = 1;
 MathUtils.copySign(null, sign);
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 3.14;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 MathUtils.checkFinite(Double.NaN);
 }

 @Test
 public void testCheckFiniteDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayNaN() {
 double[] values = {Double.NaN};
 MathUtils.checkFinite(values);
 }

 @Test
 public void testCheckNotNullObject() {
 Object obj = new Object();
 MathUtils.checkNotNull(obj);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 Object obj = null;
 MathUtils.checkNotNull(obj);
 }

 @Test
 public void testCheckNotNullObjectArray() {
 Object[] objArray = new Object[3];
 MathUtils.checkNotNull(objArray);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObjectArray() {
 Object[] objArray = null;
 MathUtils.checkNotNull(objArray);
 }
}