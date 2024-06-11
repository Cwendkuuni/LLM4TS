package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Tests for the {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        final double mean = 5.0;
        final double standardDeviation = 3.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(standardDeviation, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructor() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test {@link #density(double)} method.
     */
    @Test
    public void testDensity() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.398942280401433, dist.density(0.0), 1e-6);
        assertEquals(0.398942280401433, dist.density(1.0), 1e-6);
        assertEquals(0.398942280401433, dist.density(-1.0), 1e-6);
    }

    /**
     * Test {@link #cumulativeProbability(double)} method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 1e-6);
        assertEquals(0.841344746068547, dist.cumulativeProbability(1.0), 1e-6);
        assertEquals(1.0, dist.cumulativeProbability(10.0), 1e-6);
        assertEquals(0.0, dist.cumulativeProbability(-1.0), 1e-6);
    }

    /**
     * Test {@link #inverseCumulativeProbability(double)} method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, NotStrictlyPositiveException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.2821333, dist.inverseCumulativeProbability(0.15865525), 1e-6);
        assertEquals(-0.0455937, dist.inverseCumulativeProbability(0.5), 1e-6);
        assertEquals(1.2821333, dist.inverseCumulativeProbability(0.841344746), 1e-6);
    }

    /**
     * Test {@link #sample()} method.
     */
    @Test
    public void testSample() throws MathException, NotStrictlyPositiveException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final double sample = dist.sample();
        assertTrue(dist.isDense());
        assertTrue(dist.isSupportBounded());
        assertEquals(0.0, dist.getMin(), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, dist.getMax(), 1e-6);
        assertTrue(Math.abs(sample - dist.density(sample)) < 1e-6);
    }
}