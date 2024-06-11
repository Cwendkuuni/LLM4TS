import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.util.MathUtils.TWO_PI;
import static org.apache.commons.math3.util.FastMath.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 12345; // just an example for a valid hash code
        int actual = MathUtils.hash(value);
        Assert.assertEquals("Unexpected hash code for double value: " + value, expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null); // should throw a NullPointerException
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.78};
        int expected = 123456789; // just an example for a valid hash code
        int actual = MathUtils.hash(values);
        Assert.assertEquals("Unexpected hash code for double array: " + Arrays.toString(values), expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null); // should throw a NullPointerException
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI + 2 * TWO_PI;
        double expected = -Math.PI;
        double actual = MathUtils.normalizeAngle(angle, 0);
        Assert.assertEquals("Unexpected normalized angle: " + angle, expected, actual, 1e-12);
    }

    @Test
    public void testReduce() {
        double a = Math.PI / 2;
        double period = TWO_PI;
        double offset = Math.PI / 4;
        double expected = 0;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals("Unexpected reduced value: " + a, expected, actual, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        byte magnitude = 5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw an IllegalArgumentException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign); // should throw a MathArithmeticException
    }

    @Test
    public void testCopySignBytePositiveMagnitudePositiveSign() {
        byte magnitude = 5;
        byte sign = 1;
        byte actual = MathUtils.copySign(magnitude, sign);
        byte expected = magnitude;
        Assert.assertEquals("Unexpected result for copySign(5, 1): " + actual, expected, actual);
    }

    @Test
    public void testCheckFiniteDouble() {
        double finite = 1.23;
        MathUtils.checkFinite(finite);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double naN = Double.NaN;
        MathUtils.checkFinite(naN); // should throw a NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double infinite = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(infinite); // should throw a NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.23, Double.NaN, 3.45};
        MathUtils.checkFinite(values); // should throw a NotFiniteNumberException
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils obj = new MathUtils();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils obj = null;
        MathUtils.checkNotNull(obj); // should throw a NullArgumentException
    }

    @Test
    public void testCheckNotNullArray() {
        double[] values = new double[3];
        MathUtils.checkNotNull(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullArrayNull() {
        double[] values = null;
        MathUtils.checkNotNull(values); // should throw a NullArgumentException
    }
}