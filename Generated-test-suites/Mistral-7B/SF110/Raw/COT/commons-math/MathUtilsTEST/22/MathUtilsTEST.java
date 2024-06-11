import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(123456789, MathUtils.hash(1.23456789));
    }

    @Test(expected = NullPointerException.class)
    public void testHashDoubleNull() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        long expected = 1234567890L;
        assertEquals(expected, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expected = -3.141592653589793; // -pi
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.00001);

        a = 6.283185307179586; // 2 * pi
        expected = 0.0;
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.00001);

        a = 2.5 * MathUtils.TWO_PI; // 5 * pi
        expected = Math.PI * 2;
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.00001);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAngleNull() {
        MathUtils.normalizeAngle(null, 0.0);
    }

    @Test
    public void testReduce() {
        double a = 1.5 * MathUtils.TWO_PI;
        double period = MathUtils.TWO_PI / 2;
        double offset = MathUtils.PI;
        double expected = -Math.PI; // -pi
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.00001);

        a = 1.5 * MathUtils.TWO_PI + Math.PI;
        expected = 0.0; // 0
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.00001);

        a = 1.5 * MathUtils.TWO_PI + 2 * Math.PI;
        expected = Math.PI; // pi
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.00001);
    }

    @Test(expected = NullPointerException.class)
    public void testReduceNull() {
        MathUtils.reduce(null, 0.0, 0.0);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));

        magnitude = -128;
        sign = 1;
        expected = -128;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = -5;
        sign = -1;
        expected = 5;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = NullPointerException.class)
    public void testCopySignByteNull() {
        MathUtils.copySign((byte)0, null);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        short expected = 32767;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));

        magnitude = -32768;
        sign = 1;
        expected = -32768;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = -32768;
        sign = -1;
        expected = 32768;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = NullPointerException.class)
    public void testCopySignShortNull() {
        MathUtils.copySign((short)0, null);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = Integer.MIN_VALUE;
        sign = -1;
        expected = -Integer.MIN_VALUE;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = NullPointerException.class)
    public void testCopySignIntNull() {
        MathUtils.copySign(0, null);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        long expected = Long.MAX_VALUE;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));

        magnitude = Long.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = Long.MIN_VALUE;
        sign = -1;
        expected = -Long.MIN_VALUE;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = NullPointerException.class)
    public void testCopySignLongNull() {
        MathUtils.copySign(0L, null);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(value);
            fail();
        } catch (NotFiniteNumberException e) {
            // Expected
        }

        value = Double.NaN;
        try {
            MathUtils.checkFinite(value);
            fail();
        } catch (NotFiniteNumberException e) {
            // Expected
        }

        value = 0.0;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckFiniteDoubleNull() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(values);
            fail();
        } catch (NotFiniteNumberException e) {
            // Expected
        }

        values = new double[] {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        String obj = "test";
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullPattern() {
        Object obj = new Object();
        try {
            MathUtils.checkNotNull(obj, "Expected object to be not null", new Object[0]);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }

        obj = new Object();
        MathUtils.checkNotNull(obj, "Expected object to be not null", "Index: 0");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPatternNull() {
        MathUtils.checkNotNull(null, "Expected object to be not null", "Index: 0");
    }

    @Test
    public void testCheckNotNullPatternNullArguments() {
        MathUtils.checkNotNull(null, null, new Object[0]);
    }
}