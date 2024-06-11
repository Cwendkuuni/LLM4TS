package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.SaddlePointExpansion;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionWithNegativePopulationSize() {
        new HypergeometricDistribution(-5, 3, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithZeroOrNonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(5, 0, 10);
        new HypergeometricDistribution(5, -3, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithZeroOrNonPositiveSampleSize() {
        new HypergeometricDistribution(5, 3, 0);
        new HypergeometricDistribution(5, 3, -10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(5, 6, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(5, 3, 11);
    }

    @Test
    public void testHypergeometricDistributionCreation() {
        RandomGenerator rng = new MersenneTwister();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 5, 3, 10);
        assertNotNull(dist);
        assertEquals(5, dist.getPopulationSize());
        assertEquals(3, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        RandomGenerator rng = new MersenneTwister();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 5, 3, 10);
        int[] testCases = new int[] { 0, 3, 5, 6, 10 };
        for (int x : testCases) {
            double p = dist.probability(x);
            assertTrue(p >= 0 && p <= 1);
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        RandomGenerator rng = new MersenneTwister();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 5, 3, 10);
        int[] testCases = new int[] { 0, 2, 3, 6, 10 };
        double[] expected = new double[] { 0.0, 0.125, 0.25, 0.625, 1.0 };
        for (int i = 0; i < testCases.length; i++) {
            double actual = dist.cumulativeProbability(testCases[i]);
            assertEquals(expected[i], actual, 0.00001);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        RandomGenerator rng = new MersenneTwister();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 5, 3, 10);
        int[] testCases = new int[] { 1, 3, 4, 9, 10 };
        double[] expected = new double[] { 0.125, 0.25, 0.375, 0.875, 1.0 };
        for (int i = 0; i < testCases.length; i++) {
            double actual = dist.upperCumulativeProbability(testCases[i]);
            assertEquals(expected[i], actual, 0.00001);
        }
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 10);
        assertEquals(3.0, dist.getNumericalMean(), 0.00001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 10);
        double variance = dist.getNumericalVariance();
        assertEquals(0.27832, variance, 0.000001);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 10);
        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();
        assertEquals(13, lowerBound);
        assertEquals(3, upperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 10);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testHypergeometricDistributionProbabilityWithSaddlePointExpansion() {
        int populationSize = 5;
        int numberOfSuccesses = 3;
        int sampleSize = 10;
        double p = sampleSize / (double) populationSize;
        double q = (populationSize - sampleSize) / (double) populationSize;
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
        int x = 3;
        double logPX = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, q);
        double logPM1X = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, q);
        double logPN = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
        double expected = Math.exp(logPX + logPM1X - logPN);
        double actual = dist.probability(x);
        assertEquals(expected, actual, 0.00001);
    }
}