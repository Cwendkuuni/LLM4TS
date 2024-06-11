package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link HypergeometricDistribution}.
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
    public void testHypergeometricConstruction() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals("Incorrect population size", populationSize, dist.getPopulationSize());
        assertEquals("Incorrect number of successes", numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals("Incorrect sample size", sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with invalid arguments - population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments - number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricInvalidNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments - sample size.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments -population size, number of successes > population size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricInvalidNumberOfSuccessesLargerThanPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid arguments -sample size > population size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricInvalidSampleSizeLargerThanPopulationSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link #cumulativeProbability(int)} method.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int lowerBound = dist.getSupportLowerBound();
        final int upperBound = dist.getSupportUpperBound();
        for (int i = lowerBound; i <= upperBound; i++) {
            final double prob = dist.cumulativeProbability(i);
            final double p = (double) sampleSize / (double) populationSize;
            final double q = (double) (populationSize - sampleSize) / (double) populationSize;
            final double expected = SaddlePointExpansion.logBinomialProbability(i, numberOfSuccesses, p, q);
            final double tolerance = 1e-12;
            assertTrue("Incorrect cumulative probability for value " + i + ": expected = " + Precision.round(expected, tolerance) + ", actual = " + prob, Precision.isClose(prob, expected, tolerance));
        }
    }

    /**
     * Test {@link #probability(int)} method.
     */
    @org.junit.Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int lowerBound = dist.getSupportLowerBound();
        final int upperBound = dist.getSupportUpperBound();
        for (int i = lowerBound; i <= upperBound; i++) {
            final double prob = dist.probability(i);
            final double p = (double) sampleSize / (double) populationSize;
            final double q = (double) (populationSize - sampleSize) / (double) populationSize;
            final double expected = SaddlePointExpansion.logBinomialProbability(i, numberOfSuccesses, p, q);
            final double tolerance = 1e-12;
            assertTrue("Incorrect probability for value " + i + ": expected = " + Precision.round(expected, tolerance) + ", actual = " + prob, Precision.isClose(prob, expected, tolerance));
        }
    }

    /**
     * Test {@link #upperCumulativeProbability(int)} method.
     */
    @org.junit.Test
    public void testUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int lowerBound = dist.getSupportLowerBound();
        final int upperBound = dist.getSupportUpperBound();
        for (int i = lowerBound + 1; i <= upperBound; i++) {
            final double prob = dist.upperCumulativeProbability(i);
            final double p = (double) sampleSize / (double) populationSize;
            final double q = (double) (populationSize - sampleSize) / (double) populationSize;
            final double expected = SaddlePointExpansion.logBinomialProbability(i, numberOfSuccesses, p, q);
            final double tolerance = 1e-12;
            assertTrue("Incorrect upper cumulative probability for value " + i + ": expected = " + Precision.round(expected, tolerance) + ", actual = " + prob, Precision.isClose(prob, 1.0 - expected, tolerance));
        }
    }

    /**
     * Test {@link #getNumericalMean()} method.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        assertEquals("Incorrect mean: expected = " + expected, expected, dist.getNumericalMean(), 1e-12);
    }

    /**
     * Test {@link #getNumericalVariance()} method.
     */
    @org.junit.Test
    public void testGetNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals("Incorrect numerical variance: expected = " + expected, expected, dist.getNumericalVariance(), 1e-12);
    }

    /**
     * Test {@link #getNumericalVariance()} method with maximum values (as per JUnit 4.12 requirement).
     */
    @org.junit.Test
    public void testGetNumericalVarianceMaxValues() {
        final int populationSize = Integer.MAX_VALUE;
        final int numberOfSuccesses = Integer.MAX_VALUE;
        final int sampleSize = Integer.MAX_VALUE;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        // The formula for the numerical variance is not valid for these values
        // as it would result in an overflow, hence the test will fail
        final double expected = Double.NaN;
        assertEquals("Incorrect numerical variance: expected = " + expected, expected, dist.getNumericalVariance(), 1e-12);
    }

    /**
     * Test {@link #getSupportLowerBound()} method.
     */
    @org.junit.Test
    public void testGetSupportLowerBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int expected = FastMath.max(0, numberOfSuccesses + sampleSize - populationSize);
        assertEquals("Incorrect support lower bound: expected = " + expected, expected, dist.getSupportLowerBound());
    }

    /**
     * Test {@link #getSupportUpperBound()} method.
     */
    @org.junit.Test
    public void testGetSupportUpperBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int expected = FastMath.min(numberOfSuccesses, sampleSize);
        assertEquals("Incorrect support upper bound: expected = " + expected, expected, dist.getSupportUpperBound());
    }

    /**
     * Test {@link #isSupportConnected()} method.
     */
    @org.junit.Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue("Support is not connected", dist.isSupportConnected());
    }
}