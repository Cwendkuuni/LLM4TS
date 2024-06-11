package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
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
        Assert.assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        Assert.assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.2);
        Assert.assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
        Assert.assertEquals(0.2, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(1.0, 1.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.0, dist.density(0.0), 1e-9);
        Assert.assertEquals(0.01, dist.density(0.1), 1e-9);
        Assert.assertEquals(1.0 / (1.0 - 0.0), dist.density(0.9), 1e-9);
        Assert.assertEquals(1.0 / (1.0 - 0.0), dist.density(1.0), 1e-9);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.0, dist.cumulativeProbability(0.0), 1e-9);
        Assert.assertEquals(0.5, dist.cumulativeProbability(0.5), 1e-9);
        Assert.assertEquals(1.0, dist.cumulativeProbability(1.0), 1e-9);
    }

    /**
     * Test inverse cumulative probability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 1e-9);
        Assert.assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 1e-9);
        Assert.assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 1e-9);
        Assert.assertEquals(0.25, dist.inverseCumulativeProbability(0.25), 1e-9);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double sample = dist.sample();
        Assert.assertTrue(sample >= 0.0 && sample < 1.0);
    }

    /**
     * Test sample method with custom generator.
     */
    @Test
    public void testSampleWithCustomGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.0, 1.0);
        double sample = dist.sample();
        Assert.assertTrue(sample >= 0.0 && sample < 1.0);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.5, dist.getNumericalMean(), 1e-9);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.0625, dist.getNumericalVariance(), 1e-9);
    }
}