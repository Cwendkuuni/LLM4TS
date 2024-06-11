import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = 1.2;
 int expectedHash = 151677638; // obtained using HashCodeUtil.hashCodes(new Object[]{value})
 int actualHash = MathUtils.hash(value);
 assertEquals(expectedHash, actualHash);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNegativeInfinite() {
 MathUtils.checkFinite(-Double.POSITIVE_INFINITY);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteNaN() {
 MathUtils.checkFinite(Double.NaN);
 }

 @Test
 public void testHashDoubleArray() {
 double[] values = {1.2, 2.3, 3.4};
 int expectedHash = 1121067351; // obtained using HashCodeUtil.hashCodes(new Object[]{values})
 int actualHash = MathUtils.hash(values);
 assertEquals(expectedHash, actualHash);
 }

 @Test(expected = IllegALArgumentException.class)
 public void testNormalizeAngleInvalidCenter() {
 MathUtils.normalizeAngle(Math.PI, 0.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReduceInvalidPeriod() {
 MathUtils.reduce(-1.0, 0.1, 0.0);
 }

 @Test
 public void testCopySignBytePositiveSign() {
 byte magnitude = 5;
 byte sign = 1;
 byte expectedResult = 5;
 byte actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignByteNegativeSign() {
 byte magnitude = -5;
 byte sign = -1;
 byte expectedResult = -5;
 byte actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignByteOverflow() {
 byte magnitude = 127;
 byte sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignShortPositiveSign() {
 short magnitude = 5;
 short sign = 1;
 short expectedResult = 5;
 short actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignShortNegativeSign() {
 short magnitude = -5;
 short sign = -1;
 short expectedResult = -5;
 short actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignShortOverflow() {
 short magnitude = 32767;
 short sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignIntPositiveSign() {
 int magnitude = 5;
 int sign = 1;
 int expectedResult = 5;
 int actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignIntNegativeSign() {
 int magnitude = -5;
 int sign = -1;
 int expectedResult = -5;
 int actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignIntOverflow() {
 int magnitude = Integer.MIN_VALUE;
 int sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test
 public void testCopySignLongPositiveSign() {
 long magnitude = 5;
 long sign = 1;
 long expectedResult = 5;
 long actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignLongNegativeSign() {
 long magnitude = -5;
 long sign = -1;
 long expectedResult = -5;
 long actualResult = MathUtils.copySign(magnitude, sign);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCopySignLongOverflow() {
 long magnitude = Long.MIN_VALUE;
 long sign = -1;
 MathUtils.copySign(magnitude, sign);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 MathUtils.checkNotNull(null, "Null object not allowed.");
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullArrayNullElement() {
 double[] values = new double[1];
 values[0] = null;
 MathUtils.checkFinite(values);
 }

 @Test
 public void testCheckNotNullObject() {
 Object obj = new Object();
 MathUtils.checkNotNull(obj);
 }

 @Test
 public void testCheckNotNullArray() {
 double[] values = new double[]{1.2, 2.3, 3.4};
 MathUtils.checkNotNull(values);
 }
}