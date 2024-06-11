package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testHypergeometricDistributionConstruction() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid population size.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionPopulationSizeZero() throws NotStrictlyPositiveException {
        new HypergeometricDistribution(0, 5, 3);
    }

    /**
     * Test construction with invalid number of successes.
     * @throws NotPositiveException
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNumberOfSuccessesNegative() throws NotPositiveException {
        new HypergeometricDistribution(10, -1, 3);
    }

    /**
     * Test construction with invalid sample size.
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() throws NotPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Test construction with invalid sample size smaller than number of successes.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionSampleSizeSmallerThanNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException {
        new HypergeometricDistribution(10, 5, 2);
    }

    /**
     * Test construction with random generator.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testHypergeometricDistributionRandomGenerator() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        final RandomGenerator rng = new Well19937c();
        new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] testCases = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int testCase : testCases) {
            double cumProb = distribution.cumulativeProbability(testCase);
            double prob = distribution.probability(testCase);
            double upperTailProb = distribution.upperCumulativeProbability(testCase);
            double expectedCumProb = cumProb + prob;
            double expectedUpperTailProb = 1 - expectedCumProb;

            Assert.assertEquals("Test case: " + testCase, expectedCumProb, cumProb, 1e-14);
            Assert.assertEquals("Test case: " + testCase, expectedUpperTailProb, upperTailProb, 1e-14);
        }
    }

    /**
     * Test probability.
     */
    @Test
    public void testHypergeometricDistributionProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] testCases = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int testCase : testCases) {
            double probability = distribution.probability(testCase);
            double expectedProbability = FastMath.binomialProbability(sampleSize, testCase, populationSize, numberOfSuccesses);

            Assert.assertEquals("Test case: " + testCase, expectedProbability, probability, 1e-14);
        }
    }

    /**
     * Test upper cumulative probability.
     */
    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] testCases = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int testCase : testCases) {
            double upperTailProb = distribution.upperCumulativeProbability(testCase);
            double expectedUpperTailProb = 1 - FastMath.binomialProbability(sampleSize, testCase, populationSize, numberOfSuccesses);

            Assert.assertEquals("Test case: " + testCase, expectedUpperTailProb, upperTailProb, 1e-14);
        }
    }

    /**
     * Test numerical mean.
     */
    @Test
    public void testHypergeometricDistributionNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = distribution.getNumericalMean();
        double expectedNumericalMean = (double) sampleSize * numberOfSuccesses / (double) populationSize;

        Assert.assertEquals("Numerical mean test", expectedNumericalMean, numericalMean, 1e-14);
    }

    /**
     * Test numerical variance.
     */
    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance =
            (numberOfSuccesses * sampleSize * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) /
            (populationSize * populationSize * (populationSize - 1));

        Assert.assertEquals("Numerical variance test", expectedNumericalVariance, numericalVariance, 1e-14);
    }
}