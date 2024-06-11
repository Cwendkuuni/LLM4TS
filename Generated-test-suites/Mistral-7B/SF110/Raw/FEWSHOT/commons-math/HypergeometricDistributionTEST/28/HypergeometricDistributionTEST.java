package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 3, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, 3);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);

        int[] domain = dist.getDomain();

        assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 1e-12);
        assertEquals(0.06666666666666667, dist.cumulativeProbability(domain[0]), 1e-12);
        assertEquals(0.13333333333333332, dist.cumulativeProbability(domain[0] + 1), 1e-12);

        assertEquals(0.5, dist.cumulativeProbability(domain[1] - 1), 1e-12);
        assertEquals(0.5, dist.cumulativeProbability(domain[1]), 1e-12);
        assertEquals(0.5, dist.cumulativeProbability(domain[1] + 1), 1e-12);

        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 1e-12);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);

        for (int i = 0; i <= 15; i++) {
            double prob = dist.probability(i);
            assertEquals(dist.cumulativeProbability(i + 1) - dist.cumulativeProbability(i), prob, 1e-12);
        }
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(3, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(15, dist.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(6.0, dist.getNumericalMean(), 1e-12);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(1.5874010526315788, dist.getNumericalVariance(), 1e-12);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(-3, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertEquals(12, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertTrue(dist.isSupportConnected());
    }
}