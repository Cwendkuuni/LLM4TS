package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -5, 2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, -2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionWithZeroOrNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 2, 0);
        new HypergeometricDistribution(new Well19937c(), 5, 2, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 6, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 2, 6);
    }

    @Test
    public void testHypergeometricDistributionWithValidArguments() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        assertEquals(3, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.cumulativeProbability(x);
            double q = dist.probability(x);
            assertEquals(p + q, 1.0, 1e-10);
        }
    }

    @Test
    public void testProbability() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.probability(x);
            assertEquals(p * 100, 31.0, 0.1); // Approximately 31% chance of success
        }
    }

    @Test
    public void testGetDomain() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int[] domain = dist.getDomain();
        assertEquals(6, domain.length);
        assertEquals(3, domain[0]);
        assertEquals(5, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        int lowerDomain = dist.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(3, lowerDomain);
    }

    @Test
    public void testGetUpperDomain() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        int upperDomain = dist.getUpperDomain(populationSize, sampleSize);
        assertEquals(5, upperDomain);
    }

    @Test
    public void testNumericalMean() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        double numericalMean = dist.getNumericalMean();
        assertEquals(3.5, numericalMean, 1e-10);
    }

    @Test
    public void testNumericalVariance() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(1.5873015873015873, numericalVariance, 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int lowerBound = dist.getSupportLowerBound();
        assertEquals(-2, lowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int upperBound = dist.getSupportUpperBound();
        assertEquals(5, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testCalculateNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        double numericalVariance = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize).calculateNumericalVariance();
        assertEquals(1.5873015873015873, numericalVariance, 1e-10);
    }
}