import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Category(org.apache.commons.math3.distribution.HypergeometricDistributionTest.class)
public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativePopulationSize() {
        new HypergeometricDistribution(-3, 2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(3, -2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() {
        new HypergeometricDistribution(3, 2, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(3, 4, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(3, 2, 4);
    }

    @Test
    public void testConstructorValidParameters() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertNotNull(dist);
        assertEquals(5, dist.getPopulationSize());
        assertEquals(3, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testGetCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.cumulativeProbability(x);
            int expectedProbability = 0;
            int successes = x;
            int populationSize = domain[1];
            int sampleSize = domain[1];

            for (int i = 0; i < successes; i++) {
                expectedProbability += SaddlePointExpansion.logBinomialProbability(i + 1, populationSize, 1.0 / (populationSize + 1.0), 1.0 - 1.0 / (populationSize + 1.0));
            }

            for (int i = successes + 1; i <= sampleSize; i++) {
                expectedProbability += SaddlePointExpansion.logBinomialProbability(sampleSize - i + 1, populationSize - successes, 1.0 / (populationSize + 1.0), 1.0 - 1.0 / (populationSize + 1.0));
            }

            expectedProbability = Math.exp(expectedProbability);
            assertEquals(expectedProbability, prob, 0.001);
        }
    }

    @Test
    public void testGetProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.probability(x);
            double expectedProbability = 0;
            int successes = x;
            int populationSize = domain[1];
            int sampleSize = domain[1];

            for (int i = 0; i < successes; i++) {
                expectedProbability += SaddlePointExpansion.logBinomialProbability(i + 1, populationSize, 1.0 / (populationSize + 1.0), 1.0 - 1.0 / (populationSize + 1.0));
            }

            for (int i = successes + 1; i <= sampleSize; i++) {
                expectedProbability += SaddlePointExpansion.logBinomialProbability(sampleSize - i + 1, populationSize - successes, 1.0 / (populationSize + 1.0), 1.0 - 1.0 / (populationSize + 1.0));
            }

            expectedProbability = Math.exp(expectedProbability);
            assertEquals(expectedProbability, prob, 0.001);
        }
    }

    @Test
    public void testGetUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.upperCumulativeProbability(x);
            int expectedProbability = 1.0;
            int successes = x;
            int populationSize = domain[1];
            int sampleSize = domain[1];

            for (int i = successes + 1; i <= sampleSize; i++) {
                expectedProbability += SaddlePointExpansion.logBinomialProbability(sampleSize - i + 1, populationSize - successes, 1.0 / (populationSize + 1.0), 1.0 - 1.0 / (populationSize + 1.0));
            }

            expectedProbability = Math.exp(expectedProbability);
            assertEquals(1.0 - expectedProbability, prob, 0.001);
        }
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(3, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(5, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(0.2222222222222222, dist.getNumericalVariance(), 0.000001);
    }
}