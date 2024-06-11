import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Any valid hashcode for testing purposes
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testHashDoubleNegative() {
        double value = -1.2;
        MathUtils.hash(value); // Will throw an IllegalArgumentException
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 123456; // Any valid hashcode for testing purposes
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14;
        double center = 0.0;
        double expected = 0.7853981633974483;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testNormalizeAngleWithLargeAngleAndCenter() {
        double angle = 10.0 * MathUtils.TWO_PI;
        double center = 5.0 * MathUtils.TWO_PI;
        double expected = angle - 11.0 * MathUtils.TWO_PI;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.00001);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNormalizeAngleWithInvalidCenter() {
        double angle = 0.0;
        double center = Double.NaN;
        MathUtils.normalizeAngle(angle, center); // Will throw an IllegalArgumentException
    }

    @Test
    public void testReduce() {
        double a = 1.5;
        double period = 2.0;
        double offset = 0.5;
        double expected = 0.75;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
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
    public void testCopySignByteWithNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        byte expected = -5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteWithOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // Will throw a MathArithmeticException
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 255;
        short sign = 1;
        short expected = 255;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignShortWithNegativeSign() {
        short magnitude = 255;
        short sign = -1;
        short expected = -255;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignShortWithOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign); // Will throw a MathArithmeticException
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 2147483647;
        int sign = 1;
        int expected = 2147483647;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignIntWithNegativeSign() {
        int magnitude = 2147483647;
        int sign = -1;
        int expected = -2147483648;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignIntWithOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign); // Will throw a MathArithmeticException
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 9223372036854775807L;
        long sign = 1;
        long expected = 9223372036854775807L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignLongWithNegativeSign() {
        long magnitude = 9223372036854775807L;
        long sign = -1;
        long expected = -9223372036854775808L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignLongWithOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign); // Will throw a MathArithmeticException
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = 1.2;
        MathUtils.checkFinite(x);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x); // Will throw a NotFiniteNumberException
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x); // Will throw a NotFiniteNumberException
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values); // Will throw a NotFiniteNumberException
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values); // Will throw a NotFiniteNumberException
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object o = null;
        MathUtils.checkNotNull(o); // Will throw a NullArgumentException
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object o = new Object();
        String pattern = "Null argument: %s";
        String[] args = {"o"};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullObjectPatternNull() {
        Object o = null;
        String pattern = "Null argument: %s";
        String[] args = {"o"};
        MathUtils.checkNotNull(o, pattern, args); // Will throw a NullArgumentException
    }
}