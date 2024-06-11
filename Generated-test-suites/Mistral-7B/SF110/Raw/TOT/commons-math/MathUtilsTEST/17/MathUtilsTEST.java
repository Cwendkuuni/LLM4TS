import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(12345, MathUtils.hash(1.2345));
        assertEquals(-12345, MathUtils.hash(-1.2345));
        assertEquals(Double.hashCode(1.2345), MathUtils.hash(1.2345));
    }

    @Test
    public void testHashDoubleArray() {
        double[] data = {1.23, 2.34, 3.45};
        assertEquals(123456789, MathUtils.hash(data));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.0, MathUtils.normalizeAngle(1.0, 0.0), 0.001);
        assertEquals(1.57, MathUtils.normalizeAngle(3.14, 0.0), 0.001);
        assertEquals(-1.57, MathUtils.normalizeAngle(-3.14, 0.0), 0.001);
        assertEquals(2.09, MathUtils.normalizeAngle(5.57, 3.14), 0.001);
        assertEquals(2.61, MathUtils.normalizeAngle(5.57, 6.28), 0.001);
    }

    @Test
    public void testReduce() {
        assertEquals(-2.3, MathUtils.reduce(4.5, 2.3, 0.0), 0.001);
        assertEquals(0.1, MathUtils.reduce(0.6, 1.5, 1.0), 0.001);
        assertEquals(3.14, MathUtils.reduce(3.14, 6.28, 3.14), 0.001);
        assertThrows(MathArithmeticException.class, () -> MathUtils.reduce(1.0, 0.0, 0.0));
    }

    @Test
    public void testCopySignByte() {
        Assert.assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        Assert.assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) 1));
        Assert.assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) -1));
        Assert.assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) -1));
        Assert.assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte) 1, (byte) -128));
    }

    @Test
    public void testCopySignShort() {
        Assert.assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        Assert.assertEquals((short) -1, MathUtils.copySign((short) -1, (short) 1));
        Assert.assertEquals((short) 1, MathUtils.copySign((short) 1, (short) -1));
        Assert.assertEquals((short) -1, MathUtils.copySign((short) -1, (short) -1));
        Assert.assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short) 1, (short) -32768));
    }

    @Test
    public void testCopySignInt() {
        Assert.assertEquals(1, MathUtils.copySign(1, 1));
        Assert.assertEquals(-1, MathUtils.copySign(-1, 1));
        Assert.assertEquals(-1, MathUtils.copySign(1, -1));
        Assert.assertEquals(-1, MathUtils.copySign(-1, -1));
        Assert.assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-1, Integer.MIN_VALUE));
    }

    @Test
    public void testCopySignLong() {
        Assert.assertEquals(1L, MathUtils.copySign(1L, 1L));
        Assert.assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        Assert.assertEquals(-1L, MathUtils.copySign(1L, -1L));
        Assert.assertEquals(-1L, MathUtils.copySign(-1L, -1L));
        Assert.assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-1L, Long.MIN_VALUE));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.23);
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayThrows() {
        double[] testArray = {Double.NaN, Integer.MAX_VALUE};
        MathUtils.checkFinite(testArray);
    }

    @Test
    public void testCheckNotNullObject() {
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null, "Object cannot be null."));
        MathUtils.checkNotNull(new Object());
    }

    @Test
    public void testCheckNotNullObjects() {
        String str = null;
        MathUtils.checkNotNull(str, "String cannot be null.");
        MathUtils.checkNotNull(new Object(), "Object cannot be null.");
    }
}