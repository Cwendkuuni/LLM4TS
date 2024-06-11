package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest extends AbstractTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testValidConstruction() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals("Wrong population size.", populationSize, dist.getPopulationSize());
        assertEquals("Wrong number of successes.", numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals("Wrong sample size.", sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid parameters.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstruction1() {
        final int populationSize = 0;
        final int numberOfSuccesses = -1;
        final int sampleSize = 2;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction2() {
        final int populationSize = 1;
        final int numberOfSuccesses = 0;
        final int sampleSize = 2;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstruction3() {
        final int populationSize = 5;
        final int numberOfSuccesses = 6;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstruction4() {
        final int populationSize = 5;
        final int numberOfSuccesses = 5;
        final int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test the distribution properties.
     */
    @Test
    public void testProperties() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test mean
        assertEquals("Wrong mean.", 5.0, dist.getNumericalMean(), 1e-12);

        // Test variance
        assertEquals("Wrong variance.", 2.58266225, dist.getNumericalVariance(), 1e-12);

        // Test support bounds
        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();
        assertEquals("Wrong support lower bound.", 2, lowerBound);
        assertEquals("Wrong support upper bound.", 5, upperBound);

        // Test support connectedness
        assertTrue("Support is not connected.", dist.isSupportConnected());

        // Test calculation of probabilities
        for (int i = 0; i <= 5; i++) {
            double p = dist.probability(i);
            assertEquals("Wrong probability for x = " + i + ".",
                         SaddlePointExpansion.logBinomialProbability(i, numberOfSuccesses,
                                                                   sampleSize / (double) populationSize,
                                                                   (populationSize - sampleSize) / (double) populationSize),
                         p, 1e-12);
        }

        // Test cumulative probabilities
        for (int i = 0; i <= 5; i++) {
            double cdf = dist.cumulativeProbability(i);
            assertEquals("Wrong cumulative probability for x = " + i + ".",
                         innerCumulativeProbability(i, sampleSize, populationSize, numberOfSuccesses),
                         cdf, 1e-12);
        }
    }

    /**
     * Test calculation of inner cumulative probabilities.
     *
     * @param x Test value.
     * @param sampleSize Sample size.
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes.
     * @return inner cumulative probability for the given parameters and x.
     */
    private double innerCumulativeProbability(int x, int sampleSize, int populationSize, int numberOfSuccesses) {
        double p = sampleSize / (double) populationSize;
        double q = (populationSize - sampleSize) / (double) populationSize;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
        return FastMath.exp(p1 + p2 - p3);
    }
}