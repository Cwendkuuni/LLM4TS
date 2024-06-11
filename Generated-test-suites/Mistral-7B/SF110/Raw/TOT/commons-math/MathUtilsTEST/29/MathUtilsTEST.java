import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class MathUtilsTest {

 @Test
 public void testHashDouble() {
     double value = 3.14;
     int expectedHashCode = 534684553; // this value is just an example, it's not the actual hashcode for the given value
     int actualHashCode = MathUtils.hash(value);
     assertEquals(expectedHashCode, actualHashCode);

     // edge case test with negative double
     double negativeValue = -3.14;
     int negativeHashCode = -1227587919; // this value is just an example, it's not the actual hashcode for the given negative value
     int negativeActualHashCode = MathUtils.hash(negativeValue);
     assertEquals(negativeHashCode, negativeActualHashCode);

     // error case test with NaN and Infinity
     double nan = Double.NaN;
     double infinity = Double.POSITIVE_INFINITY;
     Assertions.assertThrows(NotFiniteNumberException.class, () -> MathUtils.hash(nan));
     Assertions.assertThrows(NotFiniteNumberException.class, () -> MathUtils.hash(infinity));
 }

 @Test
 public void testHashDoubleArray() {
     double[] values = {1.2, 2.3, 3.4};
     int expectedHashCode = 333949328; // this value is just an example, it's not the actual hashcode for the given array
     int actualHashCode = MathUtils.hash(values);
     assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testNormalizeAngle() {
     double a = Math.PI;
     double center = 3.14 * Math.PI;
     double expectedNormalizedAngle = -2.141592653589793;
     double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
     assertEquals(expectedNormalizedAngle, actualNormalizedAngle);
 }

 @Test
 public void testReduce() {
     double a = 1.5 * Math.PI;
     double period = 2.0 * Math.PI;
     double offset = 3.14;
     double expectedReducedValue = 0.141592653589793;
     double actualReducedValue = MathUtils.reduce(a, period, offset);
     assertEquals(expectedReducedValue, actualReducedValue);
 }

 @Test
 public void testCopySignByte() {
     byte magnitude = 5;
     byte sign = -1;
     byte expectedResult = -5;
     byte actualResult;
     try {
         actualResult = MathUtils.copySign(magnitude, sign);
     } catch (MathArithmeticException e) {
         fail("Unexpected MathArithmeticException");
     }
     assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignShort() {
     short magnitude = 32767;
     short sign = -1;
     short expectedResult = -32767;
     short actualResult;
     try {
         actualResult = MathUtils.copySign(magnitude, sign);
     } catch (MathArithmeticException e) {
         fail("Unexpected MathArithmeticException");
     }
     assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignInt() {
     int magnitude = Integer.MAX_VALUE;
     int sign = -1;
     int expectedResult = Integer.MIN_VALUE;
     int actualResult;
     try {
         actualResult = MathUtils.copySign(magnitude, sign);
     } catch (MathArithmeticException e) {
         fail("Unexpected MathArithmeticException");
     }
     assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCopySignLong() {
     long magnitude = Long.MAX_VALUE;
     long sign = -1;
     long expectedResult = Long.MIN_VALUE;
     long actualResult;
     try {
         actualResult = MathUtils.copySign(magnitude, sign);
     } catch (MathArithmeticException e) {
         fail("Unexpected MathArithmeticException");
     }
     assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCheckFiniteDouble() {
     double finiteValue = 3.14;
     MathUtils.checkFinite(finiteValue);
 }

 @Test
 public void testCheckFiniteDoubleArray() {
     double[] finiteValues = {1.2, 2.3, 3.4};
     MathUtils.checkFinite(finiteValues);
 }

 @Test
 public void testCheckNotNullObject() {
     Object obj = new Object();
     MathUtils.checkNotNull(obj);
 }

 @Test
 public void testCheckNotNullObjectPattern() {
     Object obj = null;
     Assertions.assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, "Error message template: %s", "Object is null"));
 }

 @Test
 public void testCheckNotNullObjectNoPattern() {
     Object obj = null;
     Assertions.assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj));
 }
}