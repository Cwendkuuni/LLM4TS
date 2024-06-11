import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareWithValidInput() {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {2, 3, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double chiSquare = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);

        Assert.assertEquals(1.0, pValue, 0.01);
        Assert.assertEquals(6.119, chiSquare, 0.1);
    }

    @Test
    public void testChiSquareDataSetComparisonWithValidInput() {
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);

        Assert.assertEquals(1.0, pValue, 0.01);
        Assert.assertEquals(6.119, chiSquare, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputLength() {
        long[] observed = {1, 2};
        long[] expected = {2, 3, 4};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputLength2() {
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeExpectedValues() {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {-2, 3, 2, -3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeObservedValues() {
        long[] observed = {1, 2, 3, -4};
        long[] expected = {2, 3, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithZeroExpectedValues() {
        long[] observed = {1, 2, 3, 4};
        long[] expected = {0, 0, 0, 0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        try {
            test.chiSquare(expected, observed);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }
}