import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MathUtilsTest {

    private double value;
    private int expectedHash;

    public MathUtilsTest(double value, int expectedHash) {
        this.value = value;
        this.expectedHash = expectedHash;
    }

    @Parameterized.Parameters(name = "{index}: hash({0} = {1})")
    public static Collection<Object[]> dataForHashDouble() {
        return Arrays.asList(new Object[][]{
                {0.0, 0},
                {1.23, 136160931},
                {-3.14, -2047444619},
                {Double.MAX_VALUE, -2147483648},
                {Double.MIN_VALUE, 4221016351},
                {Double.NaN, 460318262},
                {Double.POSITIVE_INFINITY, 460318262},
                {Double.NEGATIVE_INFINITY, 460318262}
        });
    }

    @Test
    public void testHashDouble() {
        int actualHash = MathUtils.hash(value);
        Assert.assertEquals(expectedHash, actualHash);
    }

    // Test cases for other methods can be added here in a similar manner, covering various use cases, edge cases, and error scenarios.

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNullArray() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] values = new double[]{1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = new double[]{Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "An object cannot be null.");
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}