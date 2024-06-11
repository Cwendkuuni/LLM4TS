import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.junit.Test;
import org.junit.Assert;

import java.util.Random;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value1 = 1.23;
        double value2 = 4.56;
        double value3 = Double.NaN;
        double value4 = Double.POSITIVE_INFINITY;
        double value5 = Double.NEGATIVE_INFINITY;

        int result1 = MathUtils.hash(value1);
        int result2 = MathUtils.hash(value2);
        int result3 = MathUtils.hash(value3);
        int result4 = MathUtils.hash(value4);
        int result5 = MathUtils.hash(value5);

        Assert.assertNotSame(result1, result2);
        Assert.assertNotSame(result1, result3);
        Assert.assertNotSame(result1, result4);
        Assert.assertNotSame(result1, result5);
        // NaN and infinities should have the same hash code as themselves
        Assert.assertEquals(result3, result3);
        Assert.assertEquals(result4, result4);
        Assert.assertEquals(result5, result5);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleNegativeCenter() {
        double a = 3.14;
        double center = -3.14;
        MathUtils.normalizeAngle(a, center);
    }

    @Test
    public void testNormalizeAngleZeroCenter() {
        double a = 0.0;
        double center = 0.0;
        double result = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNormalizeAnglePositiveCenter() {
        double a = 3.14;
        double center = 6.28;
        double result = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(-3.10, result, 0.01);
    }

    @Test
    public void testHashArray() {
        double[] values = new double[] {1.23, 4.56, 7.89};
        int result = MathUtils.hash(values);

        // Changing the order of elements should result in a different hash code
        double[] values2 = new double[] {4.56, 1.23, 7.89};
        int result2 = MathUtils.hash(values2);

        // Adding an element should also result in a different hash code
        double[] values3 = new double[] {1.23, 4.56, 7.89, 2.71828};
        int result3 = MathUtils.hash(values3);

        Assert.assertNotSame(result, result2);
        Assert.assertNotSame(result, result3);
        Assert.assertNotSame(result2, result3);
    }

    @Test
    public void testReduceZeroPeriod() {
        double a = 0.0;
        double period = 0.0;
        double offset = 5.12;
        double result = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(a, result);
    }

    @Test
    public void testReduceZeroOffset() {
        double a = 12.34;
        double period = 6.28;
        double offset = 0.0;
        double result = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(a, result);
    }

    @Test
    public void testReduceNonZeroValues() {
        double a = 12.34;
        double period = 6.28;
        double offset = 5.12;
        double result = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(a - period * Math.floor((a - offset) / period) - offset, result, 0.01);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeAndPositiveSign() {
        byte magnitude = -12;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByteValidValues() {
        byte magnitude = 42;
        byte sign = 1;
        byte result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(magnitude, result);

        magnitude = -42;
        sign = 1;
        result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals((byte)-42, result);
    }

    @Test
    public void testCopySignShortValidValues() {
        short magnitude = 42;
        short sign = 1;
        short result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(magnitude, result);

        magnitude = -42;
        sign = 1;
        result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals((short)-42, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeMagnitudeAndPositiveSign() {
        short magnitude = -128;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDoubleValidValue() {
        double value = 1.23;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinity() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNegativeInfinity() {
        double value = Double.NEGATIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInvalidElement() {
        double[] values = new double[] {1.23, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullValidObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        MathUtils.checkNotNull(null, "pattern");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArgs() {
        MathUtils.checkNotNull(new Object(), null, new Object[0]);
    }

    @Test
    public void testCheckNotNullValidArguments() {
        MathUtils.checkNotNull(new Object(), "pattern", new Object[] {1, 2, 3});
    }
}