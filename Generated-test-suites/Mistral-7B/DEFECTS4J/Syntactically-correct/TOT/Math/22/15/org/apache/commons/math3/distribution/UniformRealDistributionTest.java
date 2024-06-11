package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(0.5, dist.sample(), 0.0);
    }

    /**
     * Test constructor with non-default bounds.
     */
    @Test
    public void testCustomConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 4.0);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(4.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(2.5, dist.getNumericalMean(), 0.0);
        assertEquals(8.25, dist.getNumericalVariance(), 0.0);
        assertEquals(2.75, dist.sample(), 0.0);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBounds() throws NumberIsTooLargeException {
        new UniformRealDistribution(3.0, 2.0);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.0);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.0);
        assertEquals(0.25, dist.inverseCumulativeProbability(0.25), 1e-9);
        assertEquals(0.75, dist.inverseCumulativeProbability(0.75), 1e-9);

        try {
            dist.inverseCumulativeProbability(1.1);
        } catch (AssertionError e) {
            // expected
        }
    }

    /**
     * Test random number generation.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Well19937c rng = new Well19937c();
        for (int i = 0; i < 1000; i++) {
            assertTrue(dist.isSupportLowerBoundInclusive());
            assertTrue(dist.isSupportUpperBoundExclusive());
            assertEquals(dist.sample(), dist.sample(rng), 0.0);
        }
    }

    /**
     * Test density.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 1e-7);
        }
        for (double x = 1.01; x < Double.POSITIVE_INFINITY; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 1e-7);
        }
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            assertEquals(x, cdf, 1e-7);
        }
    }

    /**
     * Test is support connected.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportConnected());
    }
}