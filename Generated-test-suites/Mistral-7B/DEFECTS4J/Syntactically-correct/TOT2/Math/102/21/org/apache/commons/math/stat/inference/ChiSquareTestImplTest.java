import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareWithPositiveExpectedAndObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};

        double chiSquareStat = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);

        assertEquals(3.0, chiSquareStat, 0.001);
        // Assuming an acceptable p-value for this example, e.g., 0.05
        assertTrue(pValue > 0.05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidParameters() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {};
        long[] observed = {};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidParametersLengthMismatch() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {1.0, 2.0};
        long[] observed = {3.0};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeExpectedOrObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {-1.0, 2.0};
        long[] observed = {1.0};
        test.chiSquare(expected, observed);
    }
}