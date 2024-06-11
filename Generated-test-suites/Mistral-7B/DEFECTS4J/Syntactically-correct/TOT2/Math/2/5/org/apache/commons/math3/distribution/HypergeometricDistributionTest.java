package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;

/**
 * Test cases for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     */
    @org.junit.Test
    public void testValidConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with invalid population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        int populationSize = 5;
        int numberOfSuccesses = -1;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sample size.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes larger than population size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size larger than population size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link #getNumberOfSuccesses()} method.
     */
    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
    }

    /**
     * Test {@link #getPopulationSize()} method.
     */
    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
    }

    /**
     * Test {@link #getSampleSize()} method.
     */
    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test {@link #cumulativeProbability(int)} method with valid argument.
     */
    @org.junit.Test
    public void testCumulativeProbabilityValidArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 2;
        double expected = (sampleSize + numberOfSuccesses - populationSize + 1) * 1.0 / (double) (populationSize + 1);
        double actual = dist.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-14);
    }

    /**
     * Test {@link #cumulativeProbability(int)} method with lower boundary.
     */
    @org.junit.Test
    public void testCumulativeProbabilityLowerBoundary() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBoundary = 0;
        double expected = 0.0;
        double actual = dist.cumulativeProbability(lowerBoundary);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link #cumulativeProbability(int)} method with upper boundary.
     */
    @org.junit.Test
    public void testCumulativeProbabilityUpperBoundary() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBoundary = 7;
        double expected = 1.0;
        double actual = dist.cumulativeProbability(upperBoundary);
        assertEquals(expected, actual, 1e-14);
    }

    /**
     * Test {@link #probability(int)} method with valid argument.
     */
    @org.junit.Test
    public void testProbabilityValidArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 2;
        double expected = FastMath.binomialProbability(sampleSize, populationSize, numberOfSuccesses, x) / FastMath.binomialProbability(populationSize, numberOfSuccesses, x + 1);
        double actual = dist.probability(x);
        assertEquals(expected, actual, 1e-14);
    }

    /**
     * Test {@link #probability(int)} method with lower boundary.
     */
    @org.junit.Test
    public void testProbabilityLowerBoundary() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBoundary = 0;
        double expected = 0.0;
        double actual = dist.probability(lowerBoundary);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link #probability(int)} method with upper boundary.
     */
    @org.junit.Test
    public void testProbabilityUpperBoundary() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBoundary = 7;
        double expected = 0.0;
        double actual = dist.probability(upperBoundary);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link #upperCumulativeProbability(int)} method with valid argument.
     */
    @org.junit.Test
    public void testUpperCumulativeProbabilityValidArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 2;
        double expected = FastMath.binomialProbability(sampleSize, populationSize, numberOfSuccesses, x) / FastMath.binomialProbability(populationSize, numberOfSuccesses, x + 1);
        double actual = dist.upperCumulativeProbability(x);
        assertEquals(1.0 - expected, actual, 1e-14);
    }

    /**
     * Test {@link #upperCumulativeProbability(int)} method with lower boundary.
     */
    @org.junit.Test
    public void testUpperCumulativeProbabilityLowerBoundary() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBoundary = 0;
        double expected = 1.0;
        double actual = dist.upperCumulativeProbability(lowerBoundary);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link #upperCumulativeProbability(int)} method with upper boundary.
     */
    @org.junit.Test
    public void testUpperCumulativeProbabilityUpperBoundary() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBoundary = 7;
        double expected = 0.0;
        double actual = dist.upperCumulativeProbability(upperBoundary);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link #getNumericalMean()} method.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 1e-14);
    }

    /**
     * Test {@link #getNumericalVariance()} method with valid parameters.
     */
    @org.junit.Test
    public void testGetNumericalVarianceValidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (double) (populationSize * populationSize * (populationSize - 1));
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 1e-14);
    }

    /**
     * Test {@link #getNumericalVariance()} method with invalid parameters.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetNumericalVarianceInvalidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.setNumericalVarianceIsCalculated(false);
        dist.getNumericalVariance();
    }
}