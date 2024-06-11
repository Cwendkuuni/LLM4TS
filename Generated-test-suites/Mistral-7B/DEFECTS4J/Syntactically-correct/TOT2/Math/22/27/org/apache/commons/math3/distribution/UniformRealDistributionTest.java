package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {
    private RandomGenerator rng;
    private UniformRealDistribution dist;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new UniformRealDistribution(rng, 0, 1);
    }

    @Test
    public void testConstructorWithDefaultParameters() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0.001);
        assertEquals(1, dist.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithLowerAndUpperBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 2, 5);
        assertEquals(2, dist.getSupportLowerBound(), 0.001);
        assertEquals(5, dist.getSupportUpperBound(), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidLowerAndUpperBounds() {
        new UniformRealDistribution(rng, 5, 2);
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        double result = dist.density(x);
        assertEquals(1.0 / (1.0 - 0.0), result, 0.01);

        x = 0.0;
        result = dist.density(x);
        assertEquals(1.0 / (1.0 - 0.0), result, 0.01);

        x = 1.0;
        result = dist.density(x);
        assertEquals(1.0 / (1.0 - 0.0), result, 0.01);

        x = 2.0;
        result = dist.density(x);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.0;
        double result = dist.cumulativeProbability(x);
        assertEquals(0.0, result, 0.01);

        x = 0.5;
        result = dist.cumulativeProbability(x);
        assertEquals(0.5, result, 0.01);

        x = 1.0;
        result = dist.cumulativeProbability(x);
        assertEquals(1.0, result, 0.01);
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound() && sample < dist.getSupportUpperBound());
        }
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.0625, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, dist.getSupportLowerBound(), 0.001);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, dist.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(dist.isSupportConnected());
    }
}