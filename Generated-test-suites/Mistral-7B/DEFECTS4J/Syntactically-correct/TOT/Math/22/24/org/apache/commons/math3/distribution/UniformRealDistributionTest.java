package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    private static final double DELTA = 1e-9;

    private UniformRealDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        try {
            distribution = new UniformRealDistribution(rng, 0, 1);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), DELTA);
        assertEquals(1, distribution.getSupportUpperBound(), DELTA);
        assertEquals(0.5, distribution.getNumericalMean(), DELTA);
        assertEquals(0.25, distribution.getNumericalVariance(), DELTA);
    }

    /**
     * Test constructor with specified bounds.
     */
    @Test
    public void testSpecifiedBoundsConstructor() {
        double lower = 1.5;
        double upper = 2.5;
        try {
            distribution = new UniformRealDistribution(rng, lower, upper);
        } catch (NumberIsTooLargeException e) {
            assertTrue(false);
        }
        assertEquals(lower, distribution.getSupportLowerBound(), DELTA);
        assertEquals(upper, distribution.getSupportUpperBound(), DELTA);
        assertEquals((lower + upper) / 2, distribution.getNumericalMean(), DELTA);
        assertEquals((upper - lower) * (upper - lower) / 12, distribution.getNumericalVariance(), DELTA);
    }

    /**
     * Test constructor with specified bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testSpecifiedBoundsAndInverseCumulativeProbabilityAccuracy() {
        double inverseCumulativeProbabilityAccuracy = 1e-6;
        distribution = new UniformRealDistribution(rng, 0, 1, inverseCumulativeProbabilityAccuracy);
        assertEquals(0, distribution.getSupportLowerBound(), DELTA);
        assertEquals(1, distribution.getSupportUpperBound(), DELTA);
        assertEquals(0.5, distribution.getNumericalMean(), DELTA);
        assertEquals(0.25, distribution.getNumericalVariance(), DELTA);
    }

    /**
     * Test method {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        for (double x = 0; x < 2; x += 0.01) {
            double density = distribution.density(x);
            if (x >= 1) {
                assertEquals(0, density, DELTA);
            } else {
                assertEquals(1 / (2 - 0), density, DELTA);
            }
        }
    }

    /**
     * Test method {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        for (double x = 0; x <= 2; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            if (x <= 0) {
                assertEquals(0, cumProb, DELTA);
            } else if (x >= 1) {
                assertEquals(1, cumProb, DELTA);
            } else {
                assertEquals(x / (2 - 0), cumProb, DELTA);
            }
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalMean()}.
     */
    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), DELTA);
    }

    /**
     * Test method {@link Unif
     * ormRealDistribution#getNumericalVariance()}.
     */
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.25, distribution.getNumericalVariance(), DELTA);
    }

    /**
     * Test method {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0 && sample < 1);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound(), DELTA);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1, distribution.getSupportUpperBound(), DELTA);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportLowerBoundInclusive()}.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportUpperBoundInclusive()}.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}