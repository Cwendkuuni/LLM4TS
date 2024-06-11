import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

 @Test
 public void testHashDouble() {
 double value = Math.PI;
 int expected = new Double(value).hashCode();
 int actual = MathUtils.hash(value);
 assertEquals(expected, actual);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testHashDoubleNaN() {
 double value = Double.NaN;
 MathUtils.hash(value);
 }

 @Test(expected = NullArgumentException.class)
 public void testHashNull() {
 MathUtils.hash(null);
 }

 @Test
 public void testHashDoubleArray() {
 double[] value = {1.0, 2.0, 3.0};
 int expected = Arrays.hashCode(value);
 int actual = MathUtils.hash(value);
 assertEquals(expected, actual);
 }

 @Test(expected = NullArgumentException.class)
 public void testHashNullArray() {
 MathUtils.hash(null);
 }

 @Test
 public void testNormalizeAngle() {
 double a = Math.PI * 1.5;
 double center = Math.PI;
 double expected = Math.PI * -0.5;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testNormalizeAngleZero() {
 double a = 0.0;
 double center = Math.PI;
 double expected = 0.0;
 double actual = MathUtils.normalizeAngle(a, center);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testReduce() {
 double a = Math.PI * 1.5;
 double period = 2.0 * Math.PI;
 double offset = 0.0;
 double expected = Math.PI * 0.25;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testReduceNegativePeriod() {
 double a = Math.PI * 1.5;
 double period = -2.0 * Math.PI;
 double offset = 0.0;
 double expected = Math.PI * 5.25;
 double actual = MathUtils.reduce(a, period, offset);
 assertEquals(expected, actual, 0.0001);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleNaN() {
 MathUtils.checkFinite(Double.NaN);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckFiniteNull() {
 MathUtils checkFinite = MathUtils::checkFinite;
 checkFinite(null);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFiniteDoubleInfinite() {
 MathUtils.checkFinite(Double.POSITIVE_INFINITY);
 }

 @Test
 public void testCheckFiniteDoubleArray() {
 double[] values = {1.0, 2.0, 3.0};
 MathUtils.checkFinite(values);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckFiniteNullArray() {
 MathUtils checkFinite = MathUtils::checkFinite;
 checkFinite(null);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullObject() {
 MathUtils.checkNotNull(null, "An object cannot be null");
 }

 @Test
 public void testCheckNotNullNonNullObject() {
 Object o = new Object();
 MathUtils.checkNotNull(o);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNullNullPattern() {
 MathUtils.checkNotNull(null, "%s is null", "pattern");
 }

 @Test
 public void testCheckNotNullNonNullPattern() {
 Object o = new Object();
 MathUtils.checkNotNull(o, "%s is not null", "o");
 }
}