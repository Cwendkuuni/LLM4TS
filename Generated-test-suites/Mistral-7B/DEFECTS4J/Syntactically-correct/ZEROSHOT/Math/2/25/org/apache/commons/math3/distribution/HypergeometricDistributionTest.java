package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        // Test with valid parameters
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        // Test with negative number of successes
        try {
            new HypergeometricDistribution(populationSize, -1, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test with zero or negative population size
        try {
            new HypergeometricDistribution(0, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(-1, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with number of successes greater than population size
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses + 1, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        // Test with sample size greater than population size
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize + 1);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        // Test with random generator
        Well19937c rng = new Well19937c();
        dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test cumulative probability.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCumulativeProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.cumulativeProbability(x);
            double expectedProb = innerCumulativeProbability(populationSize, numberOfSuccesses, sampleSize, x);
            assertEquals("Error in cumulativeProbability for x = " + x, expectedProb, prob, 1e-12);
        }
    }

    /**
     * Test probability.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double prob = dist.probability(x);
            double expectedProb = innerProbability(populationSize, numberOfSuccesses, sampleSize, x);
            assertEquals("Error in probability for x = " + x, expectedProb, prob, 1e-12);
        }
    }

    /**
     * Test upperCumulativeProbability.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testUpperCumulativeProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.upperCumulativeProbability(x);
            double expectedProb = 1.0 - dist.cumulativeProbability(x);
            assertEquals("Error in upperCumulativeProbability for x = " + x, expectedProb, prob, 1e-12);
        }
    }

    /**
     * Test getNumericalMean.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testGetNumericalMean() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        double mean = dist.getNumericalMean();
        double expectedMean = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        assertEquals("Error in getNumericalMean", expectedMean, mean, 1e-12);
    }

    /**
     * Test getNumericalVariance.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testGetNumericalVariance() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        double variance = dist.getNumericalVariance();
        double expectedVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize)
                * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals("Error in getNumericalVariance", expectedVariance, variance, 1e-12);
    }

    private double innerCumulativeProbability(int n, int m, int k, int x) {
        double p = (double) k / (double) n;
        double q = (double) (n - k) / (double) n;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(k - x, n - m, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(k, n, p, q);
        return Math.exp(p1 + p2 - p3);
    }

    private double innerProbability(int n, int m, int k, int x) {
        double p = (double) k / (double) n;
        double q = (double) (n - k) / (double) n;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(k - x, n - m, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(k, n, p, q);
        return Math.exp(p1 + p2 - p3) - Math.exp(p1 + p2 - p3 + 1);
    }
}