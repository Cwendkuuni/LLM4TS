import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.commons.math3.util.MathConstants.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = Double.doubleToRawLongBits(value);
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
        double input = PI + 0.1;
        double expected = input - 2 * PI;
        double actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, actual, 1e-12);

        input = -PI / 2;
        expected = PI / 2;
        actual = MathUtils.normalizeAngle(input, PI / 2);
        assertEquals(expected, actual, 1e-12);

        input = 0;
        expected = 0;
        actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleNaN() {
        double input = Double.NaN;
        MathUtils.normalizeAngle(input, 0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleInfinite() {
        double input = Double.POSITIVE_INFINITY;
        MathUtils.normalizeAngle(input, 0);
    }

    @Test
    public void testReduce() {
        double input = 3.14;
        double expected = 1.14;
        double period = 2.14;
        double offset = 0.14;
        double actual = MathUtils.reduce(input, period, offset);
        assertEquals(expected, actual, 1e-12);

        input = 6.28;
        expected = 0.28;
        actual = MathUtils.reduce(input, 6.28, 0);
        assertEquals(expected, actual, 1e-12);

        input = 0;
        expected = 0;
        actual = MathUtils.reduce(input, 2 * PI, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceNaN() {
        double input = Double.NaN;
        double period = 2.14;
        double offset = 0.14;
        MathUtils.reduce(input, period, offset);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceInfinite() {
        double input = Double.POSITIVE_INFINITY;
        double period = 2.14;
        double offset = 0.14;
        MathUtils.reduce(input, period, offset);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -127;
        sign = 1;
        expected = -127;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 5;
        sign = -1;
        expected = -5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        short magnitude = 5000;
        short sign = 1;
        short expected = 5000;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32767;
        sign = 1;
        expected = -32767;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 5000;
        sign = -1;
        expected = -5000;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        int magnitude = 50000;
        int sign = 1;
        int expected = 50000;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -2147483647;
        sign = 1;
        expected = -2147483647;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 50000;
        sign = -1;
        expected = -50000;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = -2147483648;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        long magnitude = 500000000L;
        long sign = 1;
        long expected = 500000000L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -9223372036854775807L;
        sign = 1;
        expected = -9223372036854775807L;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 500000000L;
        sign = -1;
        expected = -500000000L;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = -9223372036854775808L;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() throws NotFiniteNumberException {
        double value = 1.23;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() throws NotFiniteNumberException {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() throws NotFiniteNumberException {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test
    public void testCheckFiniteDoubleArray() throws NotFiniteNumberException {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() throws NotFiniteNumberException {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() throws NotFiniteNumberException {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values);
    }
}