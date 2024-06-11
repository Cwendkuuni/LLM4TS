package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import java.util.Random;

/**
 * Test class for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     *
     * @throws NotPositiveException if population size is less than or equal to zero.
     * @throws NotStrictlyPositiveException if number of successes or population size is not strictly positive.
     * @throws NumberIsTooLargeException if number of successes or sample size is larger than population size.
     */
    @org.junit.Test
    public void testValidConstruction()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test construction with invalid population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid sample size.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (number of successes larger than population size).
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testInvalidNumberOfSuccessesLargerThanPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (sample size larger than population size).
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSizeLargerThanPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link #cumulativeProbability(int)}.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);

        for (int i = 0; i <= populationSize; i++) {
            double p = distribution.cumulativeProbability(i);
            int expectedCumProb = 0;
            if (i >= numberOfSuccesses) {
                expectedCumProb = 1;
            }
            if (i >= sampleSize) {
                expectedCumProb += FastMath.comb(populationSize - numberOfSuccesses, i - sampleSize);
            }
            assertTrue(FastMath.abs(p - expectedCumProb) < 1e-11);
        }
    }

    /**
     * Test {@link #probability(int)}.
     */
    @org.junit.Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);

        for (int i = 0; i <= populationSize; i++) {
            double p = distribution.probability(i);
            double expectedProb = 0.0;
            if (i == numberOfSuccesses) {
                expectedProb = FastMath.comb(populationSize, numberOfSuccesses) / FastMath.pow(populationSize, sampleSize);
            }
            assertTrue(FastMath.abs(p - expectedProb) < 1e-11);
        }
    }

    /**
     * Test {@link #upperCumulativeProbability(int)}.
     */
    @org.junit.Test
    public void testUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);

        for (int i = 0; i <= populationSize; i++) {
            double p = distribution.upperCumulativeProbability(i);
            int expectedUpperCumProb = 0;
            if (i < numberOfSuccesses) {
                expectedUpperCumProb = 0.0;
            } else if (i > populationSize) {
                expectedUpperCumProb = 1.0;
            } else {
                expectedUpperCumProb = FastMath.comb(populationSize - numberOfSuccesses, i - numberOfSuccesses);
                expectedUpperCumProb /= FastMath.pow(populationSize - numberOfSuccesses, sampleSize);
            }
            assertEquals(expectedUpperCumProb, p, 1e-11);
        }
    }

    /**
     * Test {@link #getNumericalMean()}.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(sampleSize * numberOfSuccesses / populationSize, distribution.getNumericalMean(), 1e-11);
    }

    /**
     * Test {@link #getNumericalVariance()}.
     */
    @org.junit.Test
    public void testGetNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(populationSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) /
                (FastMath.pow(populationSize, 2) * (populationSize - 1)), distribution.getNumericalVariance(), 1e-11);
    }

    /**
     * Test {@link #getSupportLowerBound()}.
     */
    @org.junit.Test
    public void testGetSupportLowerBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(FastMath.max(0, sampleSize + numberOfSuccesses - populationSize), distribution.getSupportLowerBound());
    }

    /**
     * Test {@link #getSupportUpperBound()}.
     */
    @org.junit.Test
    public void testGetSupportUpperBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(FastMath.min(numberOfSuccesses, sampleSize), distribution.getSupportUpperBound());
    }

    /**
     * Test {@link #isSupportConnected()}.
     */
    @org.junit.Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test {@link #sample()} with default random.
     */
    @org.junit.Test
    public void testSampleDefaultRandom() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        final int[] sample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = distribution.sample();
        }

        final int[] expectedSample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            expectedSample[i] = FastMath.round(FastMath.binomialProbability(numberOfSuccesses, populationSize, 1.0) * populationSize);
        }

        for (int i = 0; i < sampleSize; i++) {
            assertEquals(expectedSample[i], sample[i]);
        }
    }

    /**
     * Test {@link #sample()} with custom random.
     */
    @org.junit.Test
    public void testSampleCustomRandom() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(new Random(), populationSize, numberOfSuccesses,
                sampleSize);
        final int[] sample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = distribution.sample();
        }

        final int[] expectedSample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            expectedSample[i] = FastMath.round(FastMath.binomialProbability(numberOfSuccesses, populationSize, 1.0) * populationSize);
        }

        for (int i = 0; i < sampleSize; i++) {
            assertEquals(expectedSample[i], sample[i]);
        }
    }
}