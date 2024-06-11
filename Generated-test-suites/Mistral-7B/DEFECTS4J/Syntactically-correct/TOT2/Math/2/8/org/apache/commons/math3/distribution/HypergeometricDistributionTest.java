package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testConstructValid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Check distribution properties
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
        assertEquals(numberOfSuccesses, distribution.getNumericalMean());
        // Note: numericalVariance cannot be checked directly as it is calculated lazily

        // Check distribution behavior
        for (int i = 0; i <= populationSize; i++) {
            double probability = distribution.probability(i);
            int cumulativeProbability = distribution.cumulativeProbability(i);
            int expectedCumulativeProbability = distribution.cumulativeProbability(i - 1);
            assertEquals(cumulativeProbability, expectedCumulativeProbability + probability, 1e-13);
        }

        // Test getDomain method
        int[] domain = distribution.getDomain();
        assertEquals(domain[0], distribution.getSupportLowerBound());
        assertEquals(domain[1], distribution.getSupportUpperBound());

        // Test isSupportConnected method
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test constructor with negative population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructNegativePopulationSize() {
        int populationSize = -1;
        int numberOfSuccesses = 5;
        int sampleSize = 10;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with negative number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with negative sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructNegativeSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -1;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with numberOfSuccesses > populationSize.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sampleSize > populationSize.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test probability method with valid parameters.
     */
    @Test
    public void testProbabilityValid() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 8;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 3;
        double expectedProbability = distribution.probability(x);
        double actualProbability = 0;

        for (int i = 0; i < sampleSize; i++) {
            if (distribution.cumulativeProbability(i) >= 0.5 && distribution.cumulativeProbability(i + 1) < 0.5) {
                actualProbability = i + 1 - distribution.cumulativeProbability(i);
                break;
            }
        }

        assertEquals(expectedProbability, actualProbability, 1e-13);
    }

    /**
     * Test probability method with invalid parameters (lower bound).
     */
    @Test
    public void testProbabilityLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedProbability = 0;
        double actualProbability = 0;

        int x = distribution.getSupportLowerBound();

        actualProbability = distribution.probability(x);

        assertEquals(expectedProbability, actualProbability, 1e-13);
    }

    /**
     * Test probability method with invalid parameters (upper bound).
     */
    @Test
    public void testProbabilityUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedProbability = 0;
        double actualProbability = 0;

        int x = populationSize;

        actualProbability = distribution.probability(x);

        assertEquals(expectedProbability, actualProbability, 1e-13);
    }

    /**
     * Test cumulativeProbability method with valid parameters.
     */
    @Test
    public void testCumulativeProbabilityValid() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 8;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 3;
        double expectedCumulativeProbability = distribution.cumulativeProbability(x);

        double actualCumulativeProbability = 0;

        for (int i = 0; i <= populationSize; i++) {
            double probability = distribution.probability(i);
            actualCumulativeProbability += probability;
            if (i == x) {
                break;
            }
        }

        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-13);
    }

    /**
     * Test cumulativeProbability method with invalid parameters (lower bound).
     */
    @Test
    public void testCumulativeProbabilityLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedCumulativeProbability = 0;
        double actualCumulativeProbability = 0;

        int x = distribution.getSupportLowerBound();

        actualCumulativeProbability = distribution.cumulativeProbability(x);

        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-13);
    }

    /**
     * Test cumulativeProbability method with invalid parameters (upper bound).
     */
    @Test
    public void testCumulativeProbabilityUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedCumulativeProbability = 1;
        double actualCumulativeProbability = 0;

        int x = populationSize;

        actualCumulativeProbability = distribution.cumulativeProbability(x);

        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-13);
    }

    /**
     * Test numericalVariance method with valid parameters.
     */
    @Test
    public void testNumericalVarianceValid() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 8;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedVariance = distribution.getNumericalVariance();
        double actualVariance = 0;

        int numberOfTrials = 10000;
        double sumOfSquaredDifferences = 0;

        for (int i = 0; i < numberOfTrials; i++) {
            double randomValue = distribution.Sample();
            sumOfSquaredDifferences += Math.pow(randomValue - distribution.getNumericalMean(), 2);
        }

        actualVariance = sumOfSquaredDifferences / numberOfTrials;

        assertEquals(expectedVariance, actualVariance, 1e-13);
    }

    /**
     * Test numericalVariance method with invalid parameters (lower bound).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNumericalVarianceLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        distribution.getNumericalVariance();
        int x = distribution.getSupportLowerBound();

        distribution.getNumericalVariance();
    }
}