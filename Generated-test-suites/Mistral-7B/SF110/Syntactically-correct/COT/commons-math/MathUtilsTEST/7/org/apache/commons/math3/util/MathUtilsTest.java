import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    // hash(double) test
    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHashCode = 123; // a random hash code for testing
        int actualHashCode = MathUtils.hash(value);
        assertEquals("hash(double) should return expected hash code", expectedHashCode, actualHashCode);
    }

    @Test(expected = NullPointerException.class)
    public void testHashDoubleNull() {
        double value = null;
        MathUtils.hash(value); // should throw NullPointerException
    }

    // hash(double[]) test
    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHashCode = 7654321; // a random hash code for testing
        int actualHashCode = MathUtils.hash(values);
        assertEquals("hash(double[]) should return expected hash code", expectedHashCode, actualHashCode);
    }

    @Test(expected = NullPointerException.class)
    public void testHashDoubleArrayNull() {
        double[] values = null;
        MathUtils.hash(values); // should throw NullPointerException
    }

    // normalizeAngle(double, double) test
    @Test
    public void testNormalizeAngle() {
        double angle = 6.5;
        double center = 3.141592653589793;
        double expectedNormalizedAngle = 2.465925886529859; // a random normalized angle for testing
        double actualNormalizedAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals("normalizeAngle(double, double) should return expected normalized angle", expectedNormalizedAngle, actualNormalizedAngle, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleNegativeCenter() {
        double angle = 3.141592653589793;
        double center = -3.141592653589793;
        MathUtils.normalizeAngle(angle, center); // should throw IllegalArgumentException
    }

    // reduce(double, double, double) test
    @Test
    public void testReduce() {
        double a = 4.5;
        double period = 2.0;
        double offset = 1.0;
        double expectedReducedValue = 1.5; // a random reduced value for testing
        double actualReducedValue = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(double, double, double) should return expected reduced value", expectedReducedValue, actualReducedValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceNegativePeriod() {
        double a = 4.5;
        double period = -2.0;
        double offset = 1.0;
        MathUtils.reduce(a, period, offset); // should throw IllegalArgumentException
    }

    // copySign(byte, byte) test
    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return expected result", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflowPositive() {
        byte magnitude = 127;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflowNegative() {
        byte magnitude = -128;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    // copySign(short, short) test
    @Test
    public void testCopySignShort() {
        short magnitude = 5;
        short sign = 1;
        short expectedResult = 5;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(short, short) should return expected result", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflowPositive() {
        short magnitude = 32767;
        short sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflowNegative() {
        short magnitude = -32768;
        short sign = -1;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    // copySign(int, int) test
    @Test
    public void testCopySignInt() {
        int magnitude = 5;
        int sign = 1;
        int expectedResult = 5;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(int, int) should return expected result", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflowPositive() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflowNegative() {
        int magnitude = Integer.MIN_VALUE;
        int sign = -1;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    // copySign(long, long) test
    @Test
    public void testCopySignLong() {
        long magnitude = 5L;
        long sign = 1L;
        long expectedResult = 5L;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(long, long) should return expected result", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflowPositive() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflowNegative() {
        long magnitude = Long.MIN_VALUE;
        long sign = -1L;
        MathUtils.copySign(magnitude, sign); // should throw MathArithmeticException
    }

    // checkFinite(double) test
    @Test
    public void testCheckFiniteDouble() {
        double value = 1.23;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // should throw NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // should throw NotFiniteNumberException
    }

    // checkFinite(double[]) test
    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.1, Double.NaN, 3.3};
        MathUtils.checkFinite(values); // should throw NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.1, Double.POSITIVE_INFINITY, 3.3};
        MathUtils.checkFinite(values); // should throw NotFiniteNumberException
    }

    // checkNotNull(Object, Localizable, Object...) test
    @Test
    public void testCheckNotNullObjects() {
        Object obj1 = new Object();
        Localizable pattern = new LocalizedFormats.NullArgumentException();
        Object[] args = new Object[0];
        MathUtils.checkNotNull(obj1, pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        Localizable pattern = new LocalizedFormats.NullArgumentException();
        Object[] args = new Object[0];
        MathUtils.checkNotNull(obj, pattern, args); // should throw NullArgumentException
    }

    // checkNotNull(Object) test
    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull2() {
        Object obj = null;
        MathUtils.checkNotNull(obj); // should throw NullArgumentException
    }
}