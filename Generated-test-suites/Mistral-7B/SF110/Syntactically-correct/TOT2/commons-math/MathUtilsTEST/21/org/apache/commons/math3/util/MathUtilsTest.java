import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double input = 3.14;
        int expected = 1234567; // Any valid hashcode
        int result = MathUtils.hash(input);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDouble() {
        double input = null;
        MathUtils.hash(input);
    }

    @Test
    public void testHashDoubleArray() {
        double[] input = {1.2, 2.3, 3.4};
        int expected = 123456789; // Any valid hashcode
        int result = MathUtils.hash(input);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDoubleArray() {
        double[] input = null;
        MathUtils.hash(input);
    }

    // Test other methods and their test cases here...

    @Test
    public void testNormalizeAngleEdgeCases() {
        double input = -Math.PI;
        double expected = Math.PI;
        double result = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, result, 0.001);

        input = 6.28;
        expected = 0;
        result = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, result, 0.001);

        input = 6.281;
        expected = 0.001;
        result = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, result, 0.001);

        input = Math.PI;
        expected = 0;
        result = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleInvalidCenter() {
        double input = Math.PI;
        double center = Integer.MIN_VALUE;
        MathUtils.normalizeAngle(input, center);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceNegativePeriod() {
        double input = 2.0;
        double period = -1.0;
        double offset = 1.0;
        MathUtils.reduce(input, period, offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceZeroPeriod() {
        double input = 2.0;
        double period = 0.0;
        double offset = 1.0;
        MathUtils.reduce(input, period, offset);
    }

    @Test
    public void testCopySignPositiveMagnitudeSameSign() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignNegativeMagnitudePositiveSign() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteValidInputs() {
        MathUtils.checkFinite(1.2);
        double[] inputs = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(inputs);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNInput() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteInput() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckFiniteNullInput() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNArrayElement() {
        double[] inputs = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(inputs);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteArrayElements() {
        double[] inputs = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(inputs);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckFiniteNullArrayInput() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckNotNullValidInput() {
        MathUtils.checkNotNull(new Object(), null, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullInputWithPattern() {
        MathUtils.checkNotNull(null, "Null input", new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullInputNoPattern() {
        MathUtils.checkNotNull(null);
    }
}