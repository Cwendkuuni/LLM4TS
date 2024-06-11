
package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.MathException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testHypergeometricDistributionValidConstructor() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        int populationSize = -5;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testCumulativeProbabilityValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = FastMath.max(0.0, 6.0 / 10.0);
        double result = distribution.cumulativeProbability(6);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testGetDomainValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] expected = { 3, 6 };
        int[] result = distribution.getDomain();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProbabilityValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = FastMath.exp(SaddlePointExpansion.logBinomialProbability(2, 3, 5.0 / 10.0, 5.0 / 10.0));
        double result = distribution.probability(2);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testUpperCumulativeProbabilityValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = FastMath.max(0.0, 5.0 / 10.0);
        double result = distribution.upperCumulativeProbability(5);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testGetNumericalMeanValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 3.0;
        double result = distribution.getNumericalMean();
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testGetNumericalVarianceValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.0598262558965355;
        double result = distribution.getNumericalVariance();
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testIsSupportConnectedValidCase() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(distribution.isSupportConnected());
    }
}

