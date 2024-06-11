import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.util.MathUtils;
import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 1234567; // replace with actual expected hashcode
        int actual = MathUtils.hash(value);
        assertEquals("Incorrect hashcode for double", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testHashDoubleWithNegativeValueThrowsException() {
        double value = -1.2;
        MathUtils.hash(value); // no need to assert anything here
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 1234567; // replace with actual expected hashcode
        int actual = MathUtils.hash(values);
        assertEquals("Incorrect hashcode for double array", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expected = 0.7853981633974483; // replace with actual expected value
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Incorrect result of normalizeAngle", expected, actual, 0.00001);
    }

    @Test
    public void testReduce() {
        double a = 3.5;
        double period = 2.0;
        double offset = 1.5;
        double expected = 0.5; // replace with actual expected value
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Incorrect result of reduce", expected, actual, 0.00001);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 5;
        byte sign = -1;
        byte expected = -5; // replace with actual expected value
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result of copySign for byte", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = 127;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign); // no need to assert anything here
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        short magnitude = 32767;
        short sign = -1;
        short expected = -32767; // replace with actual expected value
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result of copySign for short", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = 32768;
        short sign = -1;
        MathUtils.copySign(magnitude, sign); // no need to assert anything here
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        int magnitude = Integer.MAX_VALUE;
        int sign = -1;
        int expected = Integer.MIN_VALUE; // replace with actual expected value
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result of copySign for int", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = -1;
        MathUtils.copySign(magnitude, sign); // no need to assert anything here
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        long magnitude = Long.MAX_VALUE;
        long sign = -1;
        long expected = Long.MIN_VALUE; // replace with actual expected value
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result of copySign for long", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = -1;
        MathUtils.copySign(magnitude, sign); // no need to assert anything here
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // no need to assert anything here
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // no need to assert anything here
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN, 2.3, 3.4};
        MathUtils.checkFinite(values); // no need to assert anything here
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values); // no need to assert anything here
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object o = null;
        MathUtils.checkNotNull(o); // no need to assert anything here
    }

    @Test
    public void testCheckNotNullObjectWithPattern() {
        Object o = new Object();
        String pattern = "Object cannot be null";
        MathUtils.checkNotNull(o, pattern);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectWithPatternAndArgsNull() {
        Object o = null;
        String pattern = "Array element at index %d cannot be null";
        Object[] args = {0};
        MathUtils.checkNotNull(o, pattern, args); // no need to assert anything here
    }
}