import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareBasic() {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {2, 3, 1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double chiSquare = chiSquareTest.chiSquare(expected, observed);
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        Assert.assertEquals(1.0, pValue, 0.001); // pValue is expected to be close to 1.0 for this test case
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareIllegalArgument_MismatchedLengths() {
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareIllegalArgument_NegativeExpected() {
        long[] expected = {-1, 2, 3};
        long[] observed = {1, 2, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareIllegalArgument_NegativeObserved() {
        long[] observed = {-1, 2, 3};
        long[] expected = {1, 2, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquare_LongArray_DataSetComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double chiSquare = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.0, pValue, 0.001); // pValue is expected to be close to 0.0 for this test case
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparison_MismatchedLengths() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparison_BothZero() {
        long[] observed1 = {0, 0};
        long[] observed2 = {0, 0};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }
}