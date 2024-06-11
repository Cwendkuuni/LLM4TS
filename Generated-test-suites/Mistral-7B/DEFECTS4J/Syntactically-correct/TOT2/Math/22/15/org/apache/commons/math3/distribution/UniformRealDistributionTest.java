package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
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
        Assert.assertEquals(0, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(1, dist.getSupportUpperBound(), 0);
        Assert.assertEquals(0.5, dist.getNumericalMean(), 0);
        Assert.assertEquals(0.25, dist.getNumericalVariance(), 0);
    }

    /**
     * Test constructor with custom bounds.
     * @throws NumberIsTooLargeException if bounds are incorrect.
     */
    @Test
    public void testCustomConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3);
        Assert.assertEquals(1, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(3, dist.getSupportUpperBound(), 0);
        Assert.assertEquals(2, dist.getNumericalMean(), 0);
        Assert.assertEquals(0.75, dist.getNumericalVariance(), 0);
    }

    /**
     * Test constructor with custom bounds and random number generator.
     * @throws NumberIsTooLargeException if bounds are incorrect.
     */
    @Test
    public void testCustomConstructorWithRng() throws NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1, 3);
        Assert.assertEquals(1, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(3, dist.getSupportUpperBound(), 0);
        Assert.assertEquals(2, dist.getNumericalMean(), 0);
        Assert.assertEquals(0.75, dist.getNumericalVariance(), 0);
    }

    /**
     * Test method {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double expected = 1 / (1 - 0);
            double actual = dist.density(x);
            Assert.assertEquals(expected, actual, 1e-6);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double expected = x;
            double actual = dist.cumulativeProbability(x);
            Assert.assertEquals(expected, actual, 1e-6);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            Assert.assertTrue(sample >= 0 && sample < 1);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double mean = dist.getNumericalMean();
        Assert.assertEquals(0.5, mean, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double variance = dist.getNumericalVariance();
        Assert.assertEquals(0.25, variance, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double lowerBound = dist.getSupportLowerBound();
        Assert.assertEquals(0, lowerBound, 0);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double upperBound = dist.getSupportUpperBound();
        Assert.assertEquals(1, upperBound, 0);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportLowerBoundInclusive()}.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean inclusive = dist.isSupportLowerBoundInclusive();
        Assert.assertTrue(inclusive);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportUpperBoundInclusive()}.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean inclusive = dist.isSupportUpperBoundInclusive();
        Assert.assertFalse(inclusive);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean connected = dist.isSupportConnected();
        Assert.assertTrue(connected);
    }
}