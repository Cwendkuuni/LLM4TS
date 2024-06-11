package org.apache.commons.math3.distribution;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with standard uniform distribution.
     */
    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with specified lower and upper bounds.
     */
    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.255, dist.getNumericalMean(), 0.001);
        assertEquals(0.00375, dist.getNumericalVariance(), 0.0001);
    }

    /**
     * Test constructor with specified lower bound, upper bound, and inverse cumulative probability accuracy.
     */
    @Test
    public void testUniformRealDistributionCustomConstructorWithAccuracy() {
        try {
            UniformRealDistribution dist = new UniformRealDistribution(-0.1, 0.4, 0.0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4, 1e-6);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.255, dist.getNumericalMean(), 0.001);
        assertEquals(0.00375, dist.getNumericalVariance(), 0.0001);
    }

    /**
     * Test constructor with specified random number generator, lower bound, upper bound, and inverse cumulative probability accuracy.
     */
    @Test
    public void testUniformRealDistributionCustomConstructorWithRNG() {
        UniformRealDistribution dist = new UniformRealDistribution(
                new Well19937c(), 0.1, 0.4, 1e-6);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.255, dist.getNumericalMean(), 0.001);
        assertEquals(0.00375, dist.getNumericalVariance(), 0.0001);
    }

    /**
     * Test method {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getSupportLowerBound(); x <= dist.getSupportUpperBound(); x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (dist.getSupportUpperBound() - dist.getSupportLowerBound()), density, 0.001);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getSupportLowerBound(); x <= dist.getSupportUpperBound(); x += 0.01) {
            double p = dist.cumulativeProbability(x);
            assertEquals(x, dist.inverseCumulativeProbability(p), 0.001);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            assertEquals(p, dist.cumulativeProbability(x), 0.001);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10000; i++) {
            double x = dist.sample();
            assertTrue(x >= dist.getSupportLowerBound());
            assertTrue(x < dist.getSupportUpperBound());
        }
    }

    /**
     * Test method {@link UniformRealDistribution#sample(int, int)}.
     */
    @Test
    public void testSampleMultiple() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double[] samples = new double[1000];
        random.nextInts(samples, 0, samples.length);
        for (int i = 0; i < samples.length; i++) {
            assertEquals(dist.getSupportLowerBound(), samples[i], 0.001);
            assertTrue(samples[i] < dist.getSupportUpperBound());
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportLowerBoundInclusive()}.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportUpperBoundInclusive()}.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
    }
}