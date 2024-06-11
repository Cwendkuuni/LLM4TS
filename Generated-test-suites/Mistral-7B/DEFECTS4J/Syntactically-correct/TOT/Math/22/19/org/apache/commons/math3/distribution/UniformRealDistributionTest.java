package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() throws NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test method for {@link UniformRealDistribution#UniformRealDistribution()}.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(1, dist.getSupportUpperBound(), 0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test method for {@link UniformRealDistribution#UniformRealDistribution(double, double)}.
     */
    @Test
    public void testConstructorsWithLowerAndUpperBounds() {
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        Assert.assertEquals(lower, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(upper, dist.getSupportUpperBound(), 0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test method for {@link UniformRealDistribution#UniformRealDistribution(double, double, double)}.
     */
    @Test
    public void testConstructorWithLowerBoundsUpperBoundsAndSolverAbsoluteAccuracy() {
        double lower = 2.5;
        double upper = 4.5;
        double inverseCumAccuracy = 1e-5;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper, inverseCumAccuracy);
        Assert.assertEquals(lower, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(upper, dist.getSupportUpperBound(), 0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
        Assert.assertEquals(inverseCumAccuracy, dist.getSolverAbsoluteAccuracy(), 0);
    }

    /**
     * Test method for {@link UniformRealDistribution#UniformRealDistribution(RandomGenerator, double, double)}.
     */
    @Test
    public void testConstructorWithRandomGeneratorLowerBoundAndUpperBound() {
        RandomGenerator rng = new Well19937c();
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution dist = new UniformRealDistribution(rng, lower, upper);
        Assert.assertEquals(lower, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(upper, dist.getSupportUpperBound(), 0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test method for {@link UniformRealDistribution#UniformRealDistribution(RandomGenerator, double, double, double)}.
     */
    @Test
    public void testConstructorWithRandomGeneratorLowerBoundsUpperBoundsAndSolverAbsoluteAccuracy() {
        RandomGenerator rng = new Well19937c();
        double lower = 2.5;
        double upper = 4.5;
        double inverseCumAccuracy = 1e-5;
        UniformRealDistribution dist = new UniformRealDistribution(rng, lower, upper, inverseCumAccuracy);
        Assert.assertEquals(lower, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(upper, dist.getSupportUpperBound(), 0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
        Assert.assertEquals(inverseCumAccuracy, dist.getSolverAbsoluteAccuracy(), 0);
    }

    /**
     * Test method for {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        double testPoint = 0.5;
        double expectedDensity = 1.0 / (upper - lower);
        double actualDensity = distribution.density(testPoint);
        Assert.assertEquals(expectedDensity, actualDensity, 0);
    }

    /**
     * Test method for {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        double testPoint = 0.25;
        double expectedCumProb = 0.25;
        double actualCumProb = distribution.cumulativeProbability(testPoint);
        Assert.assertEquals(expectedCumProb, actualCumProb, 0);
    }

    /**
     * Test method for {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityWithOutOfRangeInput() {
        double testPoint = 5.0;
        distribution.cumulativeProbability(testPoint);
    }

    /**
     * Test method for {@link UniformRealDistribution#getNumericalMean()}.
     */
    @Test
    public void testGetNumericalMean() {
        double expectedMean = 0.5 * (lower + upper);
        double actualMean = distribution.getNumericalMean();
        Assert.assertEquals(expectedMean, actualMean, 0);
    }

    /**
     * Test method for {@link UniformRealDistribution#getNumericalVariance()}.
     */
    @Test
    public void testGetNumericalVariance() {
        double expectedVariance = Math.pow(upper - lower, 2) / 12;
        double actualVariance = distribution.getNumericalVariance();
        Assert.assertEquals(expectedVariance, actualVariance, 0);
    }

    /**
     * Test method for {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        double sample = distribution.sample();
        Assert.assertTrue(sample >= lower && sample < upper);
    }
}