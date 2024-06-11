package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
     * Test constructor with valid arguments.
     *
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test
    public void testValidConstruction() throws NotPositiveException,
            NotStrictlyPositiveException, NumberIsTooLargeException {
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
     * Test constructor with invalid arguments (population size <= 0).
     *
     * @throws NotStrictlyPositiveException
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments (number of successes <= 0).
     *
     * @throws NotPositiveException
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments (sample size <= 0).
     *
     * @throws NotPositiveException
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments (number of successes > population size).
     *
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testInvalidNumberOfSuccessesGreaterThanPopulationSize()
            throws NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments (sample size > population size).
     *
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSizeGreaterThanPopulationSize()
            throws NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link #getNumberOfSuccesses()}.
     */
    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, 5);
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
    }

    /**
     * Test {@link #getPopulationSize()}.
     */
    @org.junit.Test
    public void testGetPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
    }

    /**
     * Test {@link #getSampleSize()}.
     */
    @org.junit.Test
    public void testGetSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(sampleSize, distribution.getSampleSize());
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

        int[] domain = distribution.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            assertEquals(i, distribution.cumulativeProbability(i), 0.0);
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

        int[] domain = distribution.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            assertEquals(distribution.probability(i), distribution.cumulativeProbability(i + 1), 0.0);
        }
    }

    /**
     * Test {@link #getDomain()}.
     */
    @org.junit.Test
    public void testGetDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        int[] domain = distribution.getDomain();
        assertEquals(2, domain.length);
        assertEquals(1, domain[0]);
        assertEquals(4, domain[1]);
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
        assertEquals(1, distribution.getSupportLowerBound());
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
        assertEquals(4, distribution.getSupportUpperBound());
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
     * Test {@link #getNumericalMean()}.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(3.5, distribution.getNumericalMean(), 0.0);
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
        assertEquals(0.29166666666666666, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test {@link #getNumericalVariance()}.
     */
    @org.junit.Test
    public void testGetNumericalVarianceWithDifferentSampleSizes() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize1 = 5;
        final int sampleSize2 = 10;
        final HypergeometricDistribution distribution1 = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize1);
        final HypergeometricDistribution distribution2 = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize2);
        assertEquals(0.29166666666666666, distribution1.getNumericalVariance(), 0.0);
        assertEquals(0.30952380952380954, distribution2.getNumericalVariance(), 0.0);
    }

    /**
     * Test {@link #getNumericalMean()} and {@link #getNumericalVariance()}.
     *
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test
    public void testMeanAndVariance() throws NotPositiveException,
            NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(3.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.29166666666666666, distribution.getNumericalVariance(), 0.0);
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

        int[] domain = distribution.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            assertEquals(1.0 - distribution.cumulativeProbability(i),
                    distribution.upperCumulativeProbability(i), 0.0);
        }
    }
}