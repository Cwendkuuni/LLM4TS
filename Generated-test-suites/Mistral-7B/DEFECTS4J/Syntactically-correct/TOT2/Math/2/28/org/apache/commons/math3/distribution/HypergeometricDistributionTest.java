package org.apache.commons.math3.distribution;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

import org.junit.jupiter.api.Test;

/**
 * Test for HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertAll(
            () -> assertEquals(populationSize, hd.getPopulationSize()),
            () -> assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses()),
            () -> assertEquals(sampleSize, hd.getSampleSize()),
            () -> assertEquals(numberOfSuccesses, hd.getSupportLowerBound()),
            () -> assertEquals(Math.min(numberOfSuccesses, sampleSize), hd.getSupportUpperBound())
        );
    }

    /**
     * Test constructor with invalid arguments (negative population size).
     */
    @Test
    public void testInvalidConstructorNegativePopulationSize() {
        int populationSize = -3;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test constructor with invalid arguments (negative number of successes).
     */
    @Test
    public void testInvalidConstructorNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 3;
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test constructor with invalid arguments (negative sample size).
     */
    @Test
    public void testInvalidConstructorNegativeSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -3;
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test constructor with invalid arguments (number of successes larger than population size).
     */
    @Test
    public void testInvalidConstructorNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test constructor with invalid arguments (sample size larger than population size).
     */
    @Test
    public void testInvalidConstructorSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedMean = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        assertEquals(expectedMean, hd.getNumericalMean());
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedVariance = (double) (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (double) (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedVariance, hd.getNumericalVariance());
    }

    /**
     * Test getNumericalVariance method with same sample size and population size.
     */
    @Test
    public void testGetNumericalVarianceSameSampleSizeAndPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedVariance = 0.0;
        assertEquals(expectedVariance, hd.getNumericalVariance());
    }

    /**
     * Test cumulativeProbability method for different values of x.
     */
    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] xValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, populationSize - 1 };
        double[] expectedCPValues = { 0.0, 0.033, 0.056, 0.1, 0.128, 0.143, 0.158, 0.167, 0.169, 0.169, 0.169 };

        for (int i = 0; i < xValues.length; i++) {
            double actualCPValue = hd.cumulativeProbability(xValues[i]);
            assertEquals(expectedCPValues[i], actualCPValue);
        }
    }

    /**
     * Test probability method for different values of x.
     */
    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] xValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, populationSize - 1 };
        double[] expectedProbValues = { 0.0, 0.033, 0.047, 0.065, 0.081, 0.091, 0.099, 0.104, 0.105, 0.102, 0.102 };

        for (int i = 0; i < xValues.length; i++) {
            double actualProbValue = hd.probability(xValues[i]);
            assertEquals(expectedProbValues[i], actualProbValue);
        }
    }

    /**
     * Test upperCumulativeProbability method for different values of x.
     */
    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] xValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, populationSize - 1 };
        double[] expectedUCPValues = { 1.0, 0.367, 0.312, 0.257, 0.206, 0.161, 0.119, 0.084, 0.054, 0.027, 0.0 };

        for (int i = 0; i < xValues.length; i++) {
            double actualUCPValue = hd.upperCumulativeProbability(xValues[i]);
            assertEquals(expectedUCPValues[i], actualUCPValue);
        }
    }
}