package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test cases for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     */
    @org.junit.Test
    public void testConstructorValidParameters() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with negative number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -3;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNonPositivePopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive sample size.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorNonPositiveSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = -3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes greater than population size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 12;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size greater than population size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 12;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link HypergeometricDistribution#getLowerDomain(int, int, int)}.
     */
    @org.junit.Test
    public void testGetLowerDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getDomain();
        assertEquals(3, domain[0]);
    }

    /**
     * Test {@link HypergeometricDistribution#getUpperDomain(int, int)}.
     */
    @org.junit.Test
    public void testGetUpperDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getDomain();
        assertEquals(5, domain[1]);
    }

    /**
     * Test {@link HypergeometricDistribution#cumulativeProbability(int)}.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = FastMath.probability(5.0, 10.0, 5.0, 3.0);
        assertEquals(expected, dist.cumulativeProbability(5), 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#probability(int)}.
     */
    @org.junit.Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = FastMath.binomialProbability(3, 10, 5.0 / 10.0);
        assertEquals(expected, dist.probability(3), 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#upperCumulativeProbability(int)}.
     */
    @org.junit.Test
    public void testUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = 1.0 - FastMath.probability(4, 10, 5.0 / 10.0);
        assertEquals(expected, dist.upperCumulativeProbability(4), 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#getNumericalMean()}.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = 3.0 * 5.0 / 10.0;
        assertEquals(expected, dist.getNumericalMean(), 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#getNumericalVariance()}.
     */
    @org.junit.Test
    public void testGetNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = 5.0 * 5.0 * (10.0 - 3.0) * (10.0 - 5.0) / (100.0 * 101.0);
        assertEquals(expected, dist.getNumericalVariance(), 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#getSupportLowerBound()}.
     */
    @org.junit.Test
    public void testGetSupportLowerBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int expected = 3;
        assertEquals(expected, dist.getSupportLowerBound());
    }

    /**
     * Test {@link HypergeometricDistribution#getSupportUpperBound()}.
     */
    @org.junit.Test
    public void testGetSupportUpperBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int expected = 5;
        assertEquals(expected, dist.getSupportUpperBound());
    }

    /**
     * Test {@link HypergeometricDistribution#isSupportConnected()}.
     */
    @org.junit.Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final boolean expected = true;
        assertEquals(expected, dist.isSupportConnected());
    }

    /**
     * Test {@link HypergeometricDistribution#sampleInstance()}.
     */
    @org.junit.Test
    public void testSampleInstance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int expected = 4;
        final int sample = dist.sampleInstance();
        assertEquals(expected, sample);
    }

    /**
     * Test {@link HypergeometricDistribution#sampleInstance(int)} with 0 as seed.
     */
    @org.junit.Test
    public void testSampleInstanceWithSeedZero() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int expected = 4;
        final int sample = dist.sampleInstance(0);
        assertEquals(expected, sample);
    }

    /**
     * Test {@link HypergeometricDistribution#sampleInstance(int)} with non-zero seed.
     */
    @org.junit.Test
    public void testSampleInstanceWithNonZeroSeed() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int seed = 12345;
        final int expected = 3;
        final int sample = dist.sampleInstance(seed);
        assertEquals(expected, sample);
    }
}