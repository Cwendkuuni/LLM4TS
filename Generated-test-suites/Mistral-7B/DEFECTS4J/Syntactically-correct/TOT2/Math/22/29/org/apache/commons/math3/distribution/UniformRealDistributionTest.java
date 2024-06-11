package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for UniformRealDistribution.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private RandomGenerator random;

    @Before
    public void setUp() {
        random = new Well19937c();
        distribution = new UniformRealDistribution(random, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        Assert.assertEquals(0, urd.getLowerBound(), 0.001);
        Assert.assertEquals(1, urd.getUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithBounds() {
        UniformRealDistribution urd = new UniformRealDistribution(0.5, 1.5);
        Assert.assertEquals(0.5, urd.getLowerBound(), 0.001);
        Assert.assertEquals(1.5, urd.getUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithBoundsAndAccuracy() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1, 0.0001);
        Assert.assertEquals(0, urd.getLowerBound(), 0.0001);
        Assert.assertEquals(1, urd.getUpperBound(), 0.0001);
    }

    @Test
    public void testDensity() {
        double value = 0.5;
        double density = distribution.density(value);
        Assert.assertEquals(1.0 / (1 - 0), density, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        double value = 0.5;
        double cumulativeProbability = distribution.cumulativeProbability(value);
        Assert.assertEquals(0.5, cumulativeProbability, 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        UniformRealDistribution urd = new UniformRealDistribution(1, 0);
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        Assert.assertFalse(sample < 0);
        Assert.assertFalse(sample >= 1);
    }

    @Test
    public void testSampleWithCustomGenerator() {
        RandomGenerator random = new Well19937c();
        UniformRealDistribution urd = new UniformRealDistribution(random, 0, 1);
        double sample = urd.sample();
        Assert.assertFalse(sample < 0);
        Assert.assertFalse(sample >= 1);
    }

    @Test
    public void testMean() {
        double mean = distribution.getNumericalMean();
        Assert.assertEquals(0.5, mean, 0.001);
    }

    @Test
    public void testVariance() {
        double variance = distribution.getNumericalVariance();
        Assert.assertEquals(0.0055555555555556, variance, 0.000001);
    }
}