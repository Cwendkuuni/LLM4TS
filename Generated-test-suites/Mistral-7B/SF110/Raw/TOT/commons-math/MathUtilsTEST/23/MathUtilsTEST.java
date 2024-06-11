import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = 1.2;
 int expected = 12345; // any valid hashcode value for testing purposes
 int actual = MathUtils.hash(value);
 assertEquals("Hashcode of doublevalue should be equal to given value.", expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testHashNullDouble() {
 MathUtils.hash(null);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 int expected = 123456; // any valid hashcode value for testing purposes
 int actual = MathUtils.hash(values);
 assertEquals("Hashcode of double array should be equal to given value.", expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double a = 3.14;
 double center = 0.0;
 double expected = 1.11;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals("Normalize angle should return correct value.", expected, actual, 0.001);
 }

 @Test
 public void testNormalizeAngleWithNegativeCenter() {
 double a = 3.14;
 double center = -3.14;
 double expected = -2.01;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals("Normalize angle should return correct value.", expected, actual, 0.001);
 }

 @Test
 public void testReduce() {
 double a = 1.5;
 double period = 2.0;
 double offset = 1.0;
 double expected = -0.5;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals("Reduce method should return correct value.", expected, actual, 0.001);
 }

 @Test
 public void testCopySignByte() {
 byte magnitude = 5;
 byte sign = -1;
 byte expected = -5;
 try {
 byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign method for byte should return correct value.", expected, actual);
 } catch (MathArithmeticException e) {
 fail("copySign method for byte should not throw exception for given input.");
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteWithOverflow() {
 byte magnitude = -129;
 byte sign = 1;
 try {
 byte actual = MathUtils.copySign(magnitude, sign);
 fail("copySign method for byte should throw exception for given input.");
 } catch (MathArithmeticException e) {
 //expected
 }
 }

 @Test
 public void testCopySignShort() {
 short magnitude = 32767;
 short sign = 1;
 short expected = 32767;
 try {
 short actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign method for short should return correct value.", expected, actual);
 } catch (MathArithmeticException e) {
 fail("copySign method for short should not throw exception for given input.");
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortWithOverflow() {
 short magnitude = -32768;
 short sign = 1;
 try {
 short actual = MathUtils.copySign(magnitude, sign);
 fail("copySign method for short should throw exception for given input.");
 } catch (MathArithmeticException e) {
 //expected
 }
 }

 @Test
 public void testCopySignInt() {
 int magnitude = -2147483648;
 int sign = 1;
 int expected = 2147483648;
 try {
 int actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign method for int should return correct value.", expected, actual);
 } catch (MathArithmeticException e) {
 fail("copySign method for int should not throw exception for given input.");
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntWithOverflow() {
 int magnitude = 2147483647;
 int sign = 1;
 try {
 int actual = MathUtils.copySign(magnitude, sign);
 fail("copySign method for int should throw exception for given input.");
 } catch (MathArithmeticException e) {
 //expected
 }
 }

 @Test
 public void testCopySignLong() {
 long magnitude = -9223372036854775808L;
 long sign = 1;
 long expected = 9223372036854775808L;
 try {
 long actual = MathUtils.copySign(magnitude, sign);
 assertEquals("copySign method for long should return correct value.", expected, actual);
 } catch (MathArithmeticException e) {
 fail("copySign method for long should not throw exception for given input.");
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongWithOverflow() {
 long magnitude = 9223372036854775807L;
 long sign = 1;
 try {
 long actual = MathUtils.copySign(magnitude, sign);
 fail("copySign method for long should throw exception for given input.");
 } catch (MathArithmeticException e) {
 //expected
 }
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 1.2;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 double value = Double.NaN;
 try {
 MathUtils.checkFinite(value);
 fail("checkFinite method for double should throw exception for NaN value.");
 } catch (NotFiniteNumberException e) {
 //expected
 }
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleInfinite() {
 double value = Double.POSITIVE_INFINITY;
 try {
 MathUtils.checkFinite(value);
 fail("checkFinite method for double should throw exception for infinite value.");
 } catch (NotFiniteNumberException e) {
 //expected
 }
 }

 @Test
 public void testCheckFiniteDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayNaN() {
 double[] values = {Double.NaN, 2.3, 3.4};
 try {
 MathUtils.checkFinite(values);
 fail("checkFinite method for double array should throw exception for NaN value.");
 } catch (NotFiniteNumberException e) {
 //expected
 }
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleArrayInfinite() {
 double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
 try {
 MathUtils.checkFinite(values);
 fail("checkFinite method for double array should throw exception for infinite value.");
 } catch (NotFiniteNumberException e) {
 //expected
 }
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 MathUtils.checkNotNull(null, "Null Object passed for check.", new Object[0]);
 }

 @Test
 public void testCheckNotNullNonNullObject() {
 Object obj = new Object();
 MathUtils.checkNotNull(obj);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullPattern() {
 MathUtils.checkNotNull(null, "%s", new Object[0]);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullArguments() {
 MathUtils.checkNotNull(null, "%s", new Object[]{"Argument1", "Argument2"});
 }

 @Test
 public void testCheckNotNullValidObject() {
 Object obj = new Object();
 MathUtils.checkNotNull(obj, "Valid Object.");
 }
}