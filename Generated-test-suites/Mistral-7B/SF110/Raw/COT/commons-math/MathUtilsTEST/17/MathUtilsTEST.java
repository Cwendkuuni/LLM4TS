import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI * 2.5;
        double expected = Math.PI * 1.5;
        double center = Math.PI;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidAngle() {
        double angle = Double.POSITIVE_INFINITY;
        double center = Math.PI;
        MathUtils.normalizeAngle(angle, center); // This should throw an exception
    }

    @Test
    public void testReduce() {
        double a = 3.14;
        double period = 2.0;
        double offset = 0.5;
        double expected = 1.14;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCopySignByte() {
        byte sign = 1;
        byte magnitude = 5;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignByteWithNegativeMagnitudeAndPositiveSign() {
        byte sign = 1;
        byte magnitude = -5;
        byte expected = -5;
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            actual = (byte) -magnitude; // This is the correct value when an exception is thrown
        }
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithNegativeMagnitudeAndNegativeSign() {
        byte sign = -1;
        byte magnitude = -5;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignShort() {
        short sign = 1;
        short magnitude = 5;
        short expected = 5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignShortWithNegativeMagnitudeAndPositiveSign() {
        short sign = 1;
        short magnitude = -5;
        short expected = -5;
        try {
            short actual = MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            actual = (short) -magnitude; // This is the correct value when an exception is thrown
        }
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortWithNegativeMagnitudeAndNegativeSign() {
        short sign = -1;
        short magnitude = -5;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignInt() {
        int sign = 1;
        int magnitude = 5;
        int expected = 5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignIntWithNegativeMagnitudeAndPositiveSign() {
        int sign = 1;
        int magnitude = -5;
        int expected = -5;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            actual = -magnitude; // This is the correct value when an exception is thrown
        }
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntWithNegativeMagnitudeAndNegativeSign() {
        int sign = -1;
        int magnitude = -5;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignLong() {
        long sign = 1;
        long magnitude = 5;
        long expected = 5;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignLongWithNegativeMagnitudeAndPositiveSign() {
        long sign = 1;
        long magnitude = -5;
        long expected = -5;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            actual = -magnitude; // This is the correct value when an exception is thrown
        }
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongWithNegativeMagnitudeAndNegativeSign() {
        long sign = -1;
        long magnitude = -5;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // This should throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinity() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // This should throw an exception
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values); // This should throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinity() {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values); // This should throw an exception
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj); // This should throw an exception
    }

    @Test
    public void testCheckNotNullPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, "Expected a non-null object", new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPatternNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj, "Expected a non-null object", new Object[0]); // This should throw an exception
    }
}