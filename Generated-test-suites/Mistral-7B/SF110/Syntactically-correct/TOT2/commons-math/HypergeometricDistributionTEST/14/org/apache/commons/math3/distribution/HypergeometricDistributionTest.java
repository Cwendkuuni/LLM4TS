
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    // Test cases for the constructor method
    // Test cases for the cumulativeProbability method
    // Test cases for the getDomain method
    // Test cases for the probability method
    // Test cases for the other methods

    @Test
    public void testGetNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalMean = dist.getNumericalMean();
        double expectedNumericalMean = (double) sampleSize * numberOfSuccesses / populationSize;
        assertEquals(expectedNumericalMean, numericalMean, 1e-10);
    }

    @Test
    public void testGetNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalVariance = dist.getNumericalVariance();
        double expectedNumericalVariance = sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedNumericalVariance, numericalVariance, 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int supportLowerBound = dist.getSupportLowerBound();
        int expectedSupportLowerBound = populationSize + numberOfSuccesses - sampleSize;
        assertEquals(expectedSupportLowerBound, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int supportUpperBound = dist.getSupportUpperBound();
        int expectedSupportUpperBound = Math.min(numberOfSuccesses, sampleSize);
        assertEquals(expectedSupportUpperBound, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}