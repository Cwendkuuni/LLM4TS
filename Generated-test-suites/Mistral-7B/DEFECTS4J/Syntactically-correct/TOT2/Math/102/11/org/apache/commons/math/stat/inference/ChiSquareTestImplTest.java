import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare_BasicUsage() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 2, 3, 3};
        double chiSquare = chiSquareTest.chiSquare(expected, observed);
        Assert.assertEquals(7.918969505913135, chiSquare, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_InvalidInput_NullOrEmptyObservedOrExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = null;
        long[] expected = {};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_InvalidInput_DifferentLengthsObservedAndExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 3, 3};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_InvalidInput_NegativeObservedOrExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {-1, 2, 3};
        long[] expected = {2, 3, 2, 3, 3};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest_BasicUsage() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 2, 3, 3};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        Assert.assertEquals(0.03365483944316631, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_InvalidInput_NullOrEmptyObservedOrExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = null;
        long[] expected = {};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_InvalidInput_DifferentLengthsObservedAndExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 2, 3, 3};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_InvalidInput_NegativeObservedOrExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {-1, 2, 3};
        long[] expected = {2, 3, 2, 3, 3};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTest_SignificantAt5PercentLevel() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 2, 3, 3};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        Assert.assertTrue(result);
    }

    @Test
    public void testChiSquareTest_NotSignificantAt5PercentLevel() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {2, 2, 2, 3, 3};
        long[] expected = {2, 3, 2, 3, 3};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        Assert.assertFalse(result);
    }
}