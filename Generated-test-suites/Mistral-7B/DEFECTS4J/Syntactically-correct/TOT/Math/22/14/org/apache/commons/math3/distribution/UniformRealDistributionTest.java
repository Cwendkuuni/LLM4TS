package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private RandomGenerator random;

    @Before
    public void setUp() {
        random = new Well19937c();
        distribution = new UniformRealDistribution(random, 0, 1);
    }

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    /**
     * Test constructor with lower and upper bounds.
     */
    @Test
    public void testConstructor() {
        double lower = 2;
        double upper = 4;
        UniformRealDistribution distribution = new UniformRealDistribution(random, lower, upper);
        Assert.assertEquals(lower, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(upper, distribution.getSupportUpperBound(), 0);
    }

    /**
     * Test constructor with lower, upper bounds and inverseCumulativeProbabilityAccuracy.
     */
    @Test
    public void testConstructorWithAccuracy() {
        double lower = 2;
        double upper = 4;
        double inverseCumulativeProbabilityAccuracy = 1e-5;
        UniformRealDistribution distribution = new UniformRealDistribution(random, lower, upper, inverseCumulativeProbabilityAccuracy);
        Assert.assertEquals(lower, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(upper, distribution.getSupportUpperBound(), 0);
        Assert.assertEquals(inverseCumulativeProbabilityAccuracy, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    /**
     * Test method {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        double x = 0.5;
        double expected = 2 / (upper - lower);
        double actual = distribution.density(x);
        Assert.assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        // Test at boundary
        double x = lower;
        double expected = 0;
        double actual = distribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, 1e-6);

        x = upper;
        expected = 1;
        actual = distribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, 1e-6);

        // Test in between
        x = 0.5;
        expected = 0.5;
        actual = distribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= lower && sample < upper);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        double expected = 0.5 * (lower + upper);
        double actual = distribution.getNumericalMean();
        Assert.assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        double ul = upper - lower;
        double expected = ul * ul / 12;
        double actual = distribution.getNumericalVariance();
        Assert.assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testSupportLowerBound() {
        Assert.assertEquals(lower, distribution.getSupportLowerBound(), 0);
    }

    /**
     * Test method {@link Unif
     * ormRealDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testSupportUpperBound() {
        Assert.assertEquals(upper, distribution.getSupportUpperBound(), 0);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportLowerBoundInclusive()}.
     */
    @Test
    public void testSupportLowerBoundInclusive() {
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportUpperBoundInclusive()}.
     */
    @Test
    public void testSupportUpperBoundInclusive() {
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportConnected()}.
     */
    @Test
    public void testSupportConnected() {
        Assert.assertTrue(distribution.isSupportConnected());
    }
}