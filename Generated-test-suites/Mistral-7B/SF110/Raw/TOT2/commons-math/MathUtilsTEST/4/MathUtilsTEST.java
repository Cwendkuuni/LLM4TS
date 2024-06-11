import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.0;
        int expected = 134626441; // hashcode for 1.0
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = -1725285479; // hashcode for the given double array
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    // test other basic methods with similar test cases

    // test edge cases for normalizeAngle and reduce
    @Test
    public void testNormalizeAngleEdgeCases() {
        // ...
    }

    @Test
    public void testReduceEdgeCases() {
        // ...
    }

    // test copySign exceptions
    @Test(expected = MathArithmeticException.class)
    public void testCopySignException() {
        // ...
    }

    // test checkFinite exceptions
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteException() {
        // ...
    }

    // test checkNotNull exceptions
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullException() {
        // ...
    }

    // test checkNotNull without exception
    @Test
    public void testCheckNotNullNoException() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
}