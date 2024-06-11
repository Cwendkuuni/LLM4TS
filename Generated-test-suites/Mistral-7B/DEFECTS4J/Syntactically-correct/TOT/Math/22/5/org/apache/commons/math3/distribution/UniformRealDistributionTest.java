package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Unit tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test construction of standard uniform distribution.
     */
    @Test
    public void testStandardUniform() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 1e-14);
        assertEquals(1, dist.getSupportUpperBound(), 1e-14);
        assertEquals(0.5, dist.getNumericalMean(), 1e-14);
        assertEquals(0.25, dist.getNumericalVariance(), 1e-14);
    }

    /**
     * Test construction of uniform distribution with given bounds.
     */
    @Test
    public void testConstructWithBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.2);
        assertEquals(0.1, dist.getSupportLowerBound(), 1e-14);
        assertEquals(0.2, dist.getSupportUpperBound(), 1e-14);
        assertEquals(0.15, dist.getNumericalMean(), 1e-14);
        assertEquals(0.000125, dist.getNumericalVariance(), 1e-14);
    }

    /**
     * Test construction of uniform distribution with given bounds and inverse
     * cumulative probability accuracy.
     */
    @Test
    public void testConstructWithBoundsAndAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.2, 1e-6);
        assertEquals(0.1, dist.getSupportLowerBound(), 1e-14);
        assertEquals(0.2, dist.getSupportUpperBound(), 1e-14);
        assertEquals(0.15, dist.getNumericalMean(), 1e-14);
        assertEquals(0.000125, dist.getNumericalVariance(), 1e-14);

        // Test inverse cumulative distribution
        double quantiles[] = {0.25, 0.5, 0.75};
        for (double quantile : quantiles) {
            double x = dist.inverseCumulativeProbability(quantile);
            double expected = 0.1 + (0.2 - 0.1) * quantile;
            assertEquals(expected, x, 1e-6);
        }
    }

    /**
     * Test construction of uniform distribution with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithInvalidBounds() {
        new UniformRealDistribution(1.0, 0.5);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Well19937c rng = new Well19937c();
        dist.setSeed(rng.nextLong());
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound());
            assertTrue(sample < dist.getSupportUpperBound());
        }
    }
}