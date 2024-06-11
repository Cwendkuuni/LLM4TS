import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = 1.2;
 int expected = 1313455383; // hashcode of Double.valueOf(1.2)
 int actual = MathUtils.hash(value);
 assertEquals(expected, actual);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testHashNaN() {
 double value = Double.NaN;
 MathUtils.hash(value); // this line should throw an exception
 }

 @Test
 public void testHashArrayDouble() {
 double[] values = {1.2, 2.3, 3.4};
 int expected = 101840884; // hashcode of {1.2, 2.3, 3.4} using Arrays.hashCode
 int actual = MathUtils.hash(values);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAngle() {
 double a = Math.PI + 2 * Math.PI;
 double center = Math.PI;
 double expected = 0.0;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 1e-12);
 }

 @Test
 public void testNormalizeAngleNegative() {
 double a = Math.PI - 2 * Math.PI;
 double center = Math.PI;
 double expected = -2 * Math.PI;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 1e-12);
 }

 @Test
 public void testReduce() {
 double a = 1.5;
 double period = 2.0;
 double offset = 0.5;
 double expected = 0.75;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 1e-12);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteNegativeSignPositive() {
 byte magnitude = 5;
 byte sign = 1;
 MathUtils.copySign(magnitude, sign); // this line should throw an exception
 }

 @Test
 public void testCopySignBytePositive() {
 byte magnitude = 5;
 byte sign = 1;
 byte expected = 5;
 byte actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteNegativeMagnitudePositive() {
 byte magnitude = 5;
 byte sign = -1;
 MathUtils.copySign(magnitude, sign); // this line should throw an exception
 }

 @Test
 public void testCopySignShortPositive() {
 short magnitude = 5;
 short sign = 1;
 short expected = 5;
 short actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortNegativeMagnitudePositive() {
 short magnitude = 5;
 short sign = -1;
 MathUtils.copySign(magnitude, sign); // this line should throw an exception
 }

 @Test
 public void testCopySignIntPositive() {
 int magnitude = 5;
 int sign = 1;
 int expected = 5;
 int actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntNegativeMagnitudePositive() {
 int magnitude = 5;
 int sign = -1;
 MathUtils.copySign(magnitude, sign); // this line should throw an exception
 }

 @Test
 public void testCopySignLongPositive() {
 long magnitude = 5;
 long sign = 1;
 long expected = 5;
 long actual = MathUtils.copySign(magnitude, sign);
 assertEquals(expected, actual);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongNegativeMagnitudePositive() {
 long magnitude = 5;
 long sign = -1;
 MathUtils.copySign(magnitude, sign); // this line should throw an exception
 }

 @Test
 public void testCheckFiniteDouble() {
 double value = 1.2;
 MathUtils.checkFinite(value);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNaN() {
 double value = Double.NaN;
 MathUtils.checkFinite(value); // this line should throw an exception
 }

 @Test
 public void testCheckFiniteArrayDouble() {
 double[] values = {1.2, 2.3, 3.4};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteArrayNaN() {
 double[] values = {1.2, Double.NaN, 3.4};
 MathUtils.checkFinite(values); // this line should throw an exception
 }

 @Test
 public void testCheckNotNullObject() {
 Object o = new Object();
 MathUtils.checkNotNull(o);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 Object o = null;
 MathUtils.checkNotNull(o); // this line should throw an exception
 }

 @Test
 public void testCheckNotNullArray() {
 Object[] o = new Object[3];
 MathUtils.checkNotNull(o);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullArray() {
 Object[] o = null;
 MathUtils.checkNotNull(o); // this line should throw an exception
 }
}