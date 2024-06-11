package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Tests for the HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {
    /**
     * Test constructing a hypergeometric distribution with valid parameters.
     */
    @Test
    public void testValidConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        // Check that the distribution is symmetric around the mean
        int mean = (int) (sampleSize * numberOfSuccesses.doubleValue() / populationSize.doubleValue());
        int sumTestValues = 0;
        for (int i = 0; i <= populationSize; i++) {
            sumTestValues += dist.cumulativeProbability(i).intValue();
        }
        assertEquals(100, sumTestValues);
    }

    /**
     * Test constructing a hypergeometric distribution with an invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    /**
     * Test constructing a hypergeometric distribution with an invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    /**
     * Test constructing a hypergeometric distribution with an invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    /**
     * Test constructing a hypergeometric distribution with a population size equal to the number of successes.
     */
    @Test
    public void testPopulationSizeEqualToNumberOfSuccesses() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 1;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        // Check that the distribution is a Dirac delta function
        assertEquals(1.0, dist.probability(numberOfSuccesses), 1e-12);
        assertEquals(0.0, dist.probability(numberOfSuccesses - 1));
        assertEquals(0.0, dist.probability(numberOfSuccesses + 1));
    }

    /**
     * Test constructing a hypergeometric distribution with a population size greater than the number of successes.
     */
    @Test
    public void testPopulationSizeGreaterThanNumberOfSuccesses() {
        int populationSize = 6;
        int numberOfSuccesses = 3;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        // Check properties of the distribution
        int sumProbabilities = 0;
        for (int i = 0; i <= populationSize; i++) {
            sumProbabilities += dist.probability(i).intValue();
        }
        assertEquals(100, sumProbabilities);

        int mean = (int) (sampleSize * numberOfSuccesses.doubleValue() / populationSize.doubleValue());
        assertEquals(mean, dist.getNumericalMean().intValue());
        assertEquals(mean * mean, dist.getNumericalVariance().intValue(), 1e-12);
    }

    /**
     * Test constructing a hypergeometric distribution with a sample size greater than the population size.
     */
    @Test
    public void testSampleSizeGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 3;
        int sampleSize = 6;

        try {
            new HypergeometricDistribution(Well19937c.getDefault(), populationSize, numberOfSuccesses, sampleSize);
            fail("Should have thrown NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    /**
     * Test the distribution's cumulative probability function.
     */
    @Test
    public void testCumulativeProbabilityFunction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int sumProbabilities = 0;
        for (int i = 0; i <= populationSize; i++) {
            // Check that the cumulative probability is monotonically increasing
            assertFalse(dist.cumulativeProbability(i - 1) > dist.cumulativeProbability(i));
            sumProbabilities += dist.probability(i).intValue();
        }

        assertEquals(100, sumProbabilities);

        // Check that the cumulative probability is continuous at the mean
        double mean = (double) sampleSize * numberOfSuccesses / populationSize;
        double meanProbability = dist.cumulativeProbability(mean);
        double leftProbability = dist.probability((int) Math.floor(mean));
        double rightProbability = dist.probability((int) Math.ceil(mean));
        assertEquals(meanProbability, leftProbability + rightProbability, 1e-12);
    }

    /**
     * Test the distribution's probability function.
     */
    @Test
    public void testProbabilityFunction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Check that the probability function is symmetric around the mean
        double mean = (double) sampleSize * numberOfSuccesses / populationSize;
        for (int i = 0; i <= populationSize; i++) {
            double leftProbability = dist.probability(i);
            double rightProbability = dist.probability(populationSize - i);
            assertEquals(leftProbability, rightProbability, 1e-12);
        }

        // Check that the probability of success is increasing with the sample size
        int populationSize2 = 11;
        int numberOfSuccesses2 = 6;
        int sampleSize2 = 6;
        HypergeometricDistribution dist2 = new HypergeometricDistribution(populationSize2, numberOfSuccesses2, sampleSize2);
        for (int i = 0; i <= populationSize2; i++) {
            double leftProbability = dist.probability(i);
            double rightProbability = dist2.probability(i);
            assertTrue(leftProbability <= rightProbability);
        }
    }

    /**
     * Test the distribution's upper cumulative probability function.
     */
    @Test
    public void testUpperCumulativeProbabilityFunction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Check that the upper cumulative probability is equal to 1 - the cumulative probability for the complement
        for (int i = 0; i <= populationSize; i++) {
            double leftProbability = dist.cumulativeProbability(i);
            double rightProbability = 1.0 - dist.upperCumulativeProbability(populationSize - i);
            assertEquals(leftProbability, rightProbability, 1e-12);
        }
    }

    /**
     * Test the distribution's mean.
     */
    @Test
    public void testMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = dist.getNumericalMean();
        double meanTest = (double) sampleSize * numberOfSuccesses / populationSize;
        assertEquals(meanTest, mean, 1e-12);
    }

    /**
     * Test the distribution's variance.
     */
    @Test
    public void testVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double variance = dist.getNumericalVariance();
        double mean = dist.getNumericalMean();
        int populationSize2 = populationSize + 1;
        HypergeometricDistribution dist2 = new HypergeometricDistribution(populationSize2, numberOfSuccesses, sampleSize);
        double varianceTest = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize2 - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(varianceTest, variance, 1e-12);
    }
}