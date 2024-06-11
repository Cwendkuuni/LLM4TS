import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int result = MathUtils.hash(value);
        long expected = Double.doubleToLongBits(value);
        assertEquals("Hash code for double should match its bit representation", expected, result);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.4, 3.5};
        int result = MathUtils.hash(values);
        long expected = Arrays.deepHashCode(values);
        assertEquals("Hash code for double array should match the hash code of its elements", expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double input = 1.5 * MathUtils.TWO_PI;
        double expected = 1.5 * MathUtils.TWO_PI - MathUtils.TWO_PI;
        double result = MathUtils.normalizeAngle(input, 0);
        assertEquals("Normalization of an angle with a center at zero should correct the result", expected, result);

        input = 1.5 * MathUtils.TWO_PI + MathUtils.PI;
        expected = 1.5 * MathUtils.TWO_PI - MathUtils.TWO_PI + MathUtils.TWO_PI;
        result = MathUtils.normalizeAngle(input, MathUtils.PI);
        assertEquals("Normalization of an angle with a center different than zero should correctly shift the result", expected, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }
}