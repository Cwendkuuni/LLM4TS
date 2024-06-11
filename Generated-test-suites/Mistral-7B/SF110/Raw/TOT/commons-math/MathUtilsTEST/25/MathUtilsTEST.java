import org.junit.Test;
import org.junit.Assert.*; // for Assert.assertEquals, Assert.assertThrows, etc.
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import java.util.Random;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = Math.PI;
        int expected = 106110989; // hashcode for Math.PI
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = 123456789; // arbitrary integer for testing
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = Math.PI * 3.0; // 3π
        double center = Math.PI;
        double expected = Math.PI; // the same angle
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001); // allow for small floating-point error
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidAngle() {
        double a = Double.POSITIVE_INFINITY;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center); // this will throw an exception
    }

    @Test
    public void testReduce() {
        double a = 5.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = 0.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopySignByteNegativeSignPositiveMagnitude() {
        byte sign = 1;
        byte magnitude = 1;
        MathUtils.copySign(magnitude, sign); // this will throw an exception
    }

    @Test
    public void testCopySignByteValidInputs() {
        byte sign = 1;
        byte magnitude = 1;
        byte expected = 1;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteMagnitudeNegative128() {
        byte sign = 1;
        byte magnitude = -128;
        MathUtils.copySign(magnitude, sign); // this will throw an exception
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 3.141592653589793;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // this will throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinity() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // this will throw an exception
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaNElement() {
        double[] values = {1.0, 2.0, Double.NaN};
        MathUtils.checkFinite(values); // this will throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfiniteElement() {
        double[] values = {1.0, 2.0, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values); // this will throw an exception
    }

    @Test
    public void testCheckNotNullObjectNull() {
        Object o = null;
        String pattern = "null object";
        MathUtils.checkNotNull(o); // this will throw an exception
    }

    @Test
    public void testCheckNotNullObjectNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNullPattern() {
        Object o = null;
        String pattern = "NULL object{0}";
        MathUtils.checkNotNull(o, pattern); // this will throw an exception
    }

    @Test
    public void testCheckNotNullObjectNotNullPattern() {
        Object o = new Object();
        String pattern = "NULL object{0}";
        MathUtils.checkNotNull(o, pattern);
    }
}