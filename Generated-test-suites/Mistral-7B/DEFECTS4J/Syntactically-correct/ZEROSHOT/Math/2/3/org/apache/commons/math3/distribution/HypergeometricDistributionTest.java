import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(4, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        new HypergeometricDistribution(0, 4, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroSampleSize() {
        new HypergeometricDistribution(10, 4, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 4, 11);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        double[] expectedProbs = {0.0, 0.00389318323861, 0.0190225323862, 0.0373493619231, 0.0537110740741, 0.0671660112682, 0.0780305977253, 0.0867435873068, 0.0936234568819, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
        for (int i = 0; i < expectedProbs.length; i++) {
            double resultProb = dist.cumulativeProbability(i);
            assertEquals(expectedProbs[i], resultProb, 1e-12);
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        double[] expectedProbs = {0.0, 0.00389318323861, 0.0190225323862, 0.0373493619231, 0.0537110740741, 0.0671660112682, 0.0780305977253, 0.0867435873068, 0.0936234568819, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
        for (int i = 0; i < expectedProbs.length; i++) {
            double resultProb = dist.probability(i);
            assertEquals(expectedProbs[i], resultProb, 1e-12);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        double[] expectedProbs = {1.0, 0.996106816772, 0.987873183529, 0.976215054217, 0.961322325263, 0.943421127864, 0.922753228905, 0.900413723875, 0.876493109964, 0.851110484653, 0.824495040111, 0.796788539667, 0.768092759649, 0.738502798217, 0.708138309301, 0.676997223392, 0.645156958144, 0.612749241469, 0.579816393806, 0.546488398931};
        for (int i = 0; i < expectedProbs.length; i++) {
            double resultProb = dist.upperCumulativeProbability(i);
            assertEquals(expectedProbs[i], resultProb, 1e-12);
        }
    }

    @Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 100);
        assertEquals(50.0, dist.getNumericalMean(), 1e-12);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 100);
        assertEquals(24.5, dist.getNumericalVariance(), 1e-12);
    }

    @Test
    public void testHypergeometricDistributionSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();
        int expectedLowerBound = FastMath.max(0, 5 + 4 - 10);
        int expectedUpperBound = FastMath.min(4, 5);
        assertEquals(expectedLowerBound, lowerBound);
        assertEquals(expectedUpperBound, upperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        assertTrue(dist.isSupportConnected());
    }
}