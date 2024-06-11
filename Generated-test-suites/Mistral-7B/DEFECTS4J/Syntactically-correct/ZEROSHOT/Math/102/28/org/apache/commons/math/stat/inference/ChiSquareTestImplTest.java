import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        this.chiSquareTestImpl = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquareMethodWithLongArray() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {2, 3, 2, 3};
        double chiSquare = chiSquareTestImpl.chiSquare(expected, observed);
        Assert.assertEquals(11.1764705882353, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareMethodWithDoubleArray() throws IllegalArgumentException {
        double[] expected = {0.3, 0.4, 0.2, 0.1};
        double[] observed = {0.35, 0.45, 0.25, 0.1};
        double chiSquare = chiSquareTestImpl.chiSquare(observed, expected);
        Assert.assertEquals(0.23986268742385823, chiSquare, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidLengthArrays() throws IllegalArgumentException {
        long[] observed = {1, 2};
        long[] expected = {2, 3, 4};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithLongArray() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {2, 3, 2, 3};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        Assert.assertEquals(0.11764705882353, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidLengthArrays() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2};
        long[] expected = {2, 3, 4};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithAlphaAndLongArray() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {2, 3, 2, 3};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareMethodWithLongArrayOfTwoWayTable() throws IllegalArgumentException {
        long[][] counts = {
                {1, 2, 3},
                {2, 3, 1},
                {3, 1, 2}
        };
        double chiSquare = chiSquareTestImpl.chiSquare(counts);
        Assert.assertEquals(7.810755996613315, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithLongArrayOfTwoWayTableAndAlpha() throws IllegalArgumentException, MathException {
        long[][] counts = {
                {1, 2, 3},
                {2, 3, 1},
                {3, 1, 2}
        };
        boolean result = chiSquareTestImpl.chiSquareTest(counts, 0.05);
        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareTestMethodWithLongArrayOfTwoDataSets() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 1, 4};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.04556348415495533, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithLongArrayOfTwoDataSetsAndAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 1, 4};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        Assert.assertFalse(result);
    }
}