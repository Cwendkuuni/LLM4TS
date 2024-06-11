package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testHypergeometricDistributionValidConstruction() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        new HypergeometricDistribution(-5, 5, 5);
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(5, -5, 5);
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        new HypergeometricDistribution(5, 5, -5);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(5, 6, 5);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(5, 5, 6);
    }

    @org.junit.Test
    public void testHypergeometricDistributionProbabilityValidInput() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.2, dist.probability(2), 0.01);
        assertEquals(0.2, dist.probability(5), 0.01);
        assertEquals(0.5, dist.probability(5), 0.01);
        assertEquals(0.8, dist.probability(8), 0.01);
    }

    @org.junit.Test
    public void testHypergeometricDistributionProbabilityOutOfBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.0, dist.probability(0), 0.01);
        assertEquals(0.0, dist.probability(12), 0.01);
    }

    @org.junit.Test
    public void testHypergeometricDistributionCumulativeProbabilityValidInput() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.01);
        assertEquals(0.2, dist.cumulativeProbability(2), 0.01);
        assertEquals(0.4, dist.cumulativeProbability(4), 0.01);
        assertEquals(0.6, dist.cumulativeProbability(5), 0.01);
        assertEquals(0.8, dist.cumulativeProbability(8), 0.01);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.01);
    }

    @org.junit.Test
    public void testHypergeometricDistributionCumulativeProbabilityOutOfBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.01);
        assertEquals(0.0, dist.cumulativeProbability(11), 0.01);
    }

    @org.junit.Test
    public void testHypergeometricDistributionGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(10, domain[1]);
    }

    @org.junit.Test
    public void testHypergeometricDistributionGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5.0, dist.getNumericalMean(), 0.01);
    }

    @org.junit.Test
    public void testHypergeometricDistributionGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(2.43, dist.getNumericalVariance(), 0.01);
    }

    @org.junit.Test
    public void testHypergeometricDistributionGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(-2, dist.getSupportLowerBound());
    }

    @org.junit.Test
    public void testHypergeometricDistributionGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @org.junit.Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertTrue(dist.isSupportConnected());
    }

    @org.junit.Test
    public void testHypergeometricDistributionRandomNumberGeneration() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        for (int i = 0; i < 1000; i++) {
            int x = dist.nextInt();
            int[] domain = dist.getDomain();
            assertTrue(x >= domain[0] && x <= domain[1]);
        }
    }
}