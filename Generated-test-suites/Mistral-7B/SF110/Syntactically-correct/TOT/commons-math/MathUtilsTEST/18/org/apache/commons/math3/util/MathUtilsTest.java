import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 Double value = new Double(1.2);
 int expected = value.hashCode();
 int actual = MathUtils.hash(value.doubleValue());
 assertEquals("Hash code for double should be equal", expected, actual);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.1, 2.2, 3.3};
 int expected = Arrays.hashCode(values);
 int actual = MathUtils.hash(values);
 assertEquals("Hash code for double array should be equal", expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double a = Math.PI * 2.5;
 double center = Math.PI;
 double expected = 1.131415926535937;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals("Normalize angle should return expected value", expected, actual, 0.000001);
 }

 @Test(expected = MathArithmeticException.class)
 public void testReduceWithNegativePeriod() {
 double a = 0.5;
 double period = -1.0;
 double offset = 0.0;
 MathUtils.reduce(a, period, offset);
 fail("Reduce method should throw MathArithmeticException when period is negative");
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 5;
 byte sign = 1;
 byte expected = 5;
 byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals("Copy sign for byte should return expected value", expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteWithOverflow() {
 byte magnitude = 127;
 byte sign = 1;
 MathUtils.copySign(magnitude, sign);
 fail("Copy sign for byte should throw MathArithmeticException when magnitude is maximum negative value and sign is positive");
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 32767;
 short sign = 1;
 short expected = 32767;
 short actual = MathUtils.copySign(magnitude, sign);
 assertEquals("Copy sign for short should return expected value", expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortWithOverflow() {
 short magnitude = 32768;
 short sign = 1;
 MathUtils.copySign(magnitude, sign);
 fail("Copy sign for short should throw MathArithmeticException when magnitude is maximum negative value and sign is positive");
 }

 @Test
 public void testCopySignInt() {
 int magnitude = Integer.MAX_VALUE;
 int sign = 1;
 int expected = Integer.MAX_VALUE;
 int actual = MathUtils.copySign(magnitude, sign);
 assertEquals("Copy sign for int should return expected value", expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntWithOverflow() {
 int magnitude = Integer.MIN_VALUE;
 int sign = 1;
 MathUtils.copySign(magnitude, sign);
 fail("Copy sign for int should throw MathArithmeticException when magnitude is minimum value and sign is positive");
 }

 @Test
 public void testCopySignLong() {
 long magnitude = Long.MAX_VALUE;
 long sign = 1;
 long expected = Long.MAX_VALUE;
 long actual = MathUtils.copySign(magnitude, sign);
 assertEquals("Copy sign for long should return expected value", expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongWithOverflow() {
 long magnitude = Long.MIN_VALUE;
 long sign = 1;
 MathUtils.copySign(magnitude, sign);
 fail("Copy sign for long should throw MathArithmeticException when magnitude is minimum value and sign is positive");
 }

 @Test
 public void testCheckFiniteDouble() {
 double x = 1.2;
 MathUtils.checkFinite(x);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 double x = Double.NaN;
 MathUtils.checkFinite(x);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleInfinite() {
 double x = Double.POSITIVE_INFINITY;
 MathUtils.checkFinite(x);
 }

 @Test
 public void testCheckFiniteDoubleArray() {
 double[] values = {1.1, 2.2, 3.3};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayNaNElement() {
 double[] values = {1.1, 2.2, Double.NaN};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayInfiniteElement() {
 double[] values = {1.1, 2.2, Double.POSITIVE_INFINITY};
 MathUtils.checkFinite(values);
 }

 @Test
 public void testCheckNotNullObject() {
 Object obj = new Object();
 MathUtils.checkNotNull(obj, null);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullObjectNull() {
 Object obj = null;
 MathUtils.checkNotNull(obj, null);
 }

 @Test
 public void testCheckNotNullPrimative() {
 int i = 5;
 MathUtils.checkNotNull(i);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullPrimativeNull() {
 int i = 0;
 MathUtils.checkNotNull(i);
 }
}