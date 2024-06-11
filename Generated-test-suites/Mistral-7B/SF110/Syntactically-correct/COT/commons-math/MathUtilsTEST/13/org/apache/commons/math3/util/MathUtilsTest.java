import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Replace this with the expected hashcode for the given value
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 1234567; // Replace this with the expected hashcode for the given array
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.14;
        double center = 0.0;
        double expected = -3.14;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // This method doesn't throw an exception, but the test case below will
    }

    @Test
    public void testCopySignByteValid() {
        byte magnitude = 1;
        byte sign = 1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    // Test other copySign methods with similar test cases

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaNElement() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Object cannot be null");
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}