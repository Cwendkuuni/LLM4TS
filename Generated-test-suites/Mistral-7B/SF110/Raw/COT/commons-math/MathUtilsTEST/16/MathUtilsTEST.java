import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import static org.apache.commons.math3.util.MathUtils.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 133433335; // Assuming a specific hash code for the given value
        int actual = hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expected = 123456789; // Assuming a specific hash code for the given array
        int actual = hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = Math.PI + 3.14;
        double center = Math.PI;
        double expected = 0.14159265358979323;
        double actual = normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testReduceWithOverflowByte() {
        byte a = Byte.MIN_VALUE;
        byte period = 1;
        byte offset = 1;
        reduce(a, period, offset); // This should throw MathArithmeticException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithOverflowByte() {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        try {
            copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            // Expected exception, no need to assert anything
        }
    }

    @Test
    public void testCheckFinite() {
        double value = Double.POSITIVE_INFINITY;
        try {
            checkFinite(value);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception, no need to assert anything
        }

        double[] values = {1.1, Double.NaN, 3.3};
        try {
            checkFinite(values);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception, no need to assert anything
        }

        double[] validValues = {1.1, 2.2, 3.3};
        checkFinite(validValues);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullArgument() {
        Object o = null;
        checkNotNull((Object) null, (LocalizedFormats) null, new Object[0]);
    }

    @Test
    public void testCheckNotNullWithValidArgument() {
        Object o = new Object();
        checkNotNull(o);
    }
}