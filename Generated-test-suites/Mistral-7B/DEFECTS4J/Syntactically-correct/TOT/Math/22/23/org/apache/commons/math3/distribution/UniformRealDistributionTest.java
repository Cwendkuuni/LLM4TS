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
     * Test construction with valid bounds.
     */
    @Test
    public void testConstruction() {
        final double lower = 0.0;
        final double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        Assert.assertEquals(lower, distribution.getSupportLowerBound(), 0.0);
        Assert.assertEquals(upper, distribution.getSupportUpperBound(), 0.0);
        Assert.assertEquals(0.5 * (lower + upper), distribution.getNumericalMean(), 0.0);
        Assert.assertEquals((upper - lower) * (upper - lower) / 12, distribution.getNumericalVariance(), 0.0);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
        Assert.assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test construction with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstruction() {
        final double lower = 5.0;
        final double upper = 4.0;
        new UniformRealDistribution(lower, upper);
    }

    /**
     * Test sampling from a standard uniform distribution.
     */
    @Test
    public void testSampleStandardUniform() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 10; i++) {
            double sample = distribution.sample(rng);
            Assert.assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    /**
     * Test sampling from a uniform distribution with custom bounds.
     */
    @Test
    public void testSampleCustomUniform() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 10; i++) {
            double sample = distribution.sample(rng);
            Assert.assertTrue(sample >= 0.1 && sample < 0.3);
        }
    }

    /**
     * Test inverse cumulative probability function (quantiles) with valid
     * bounds and default inverse cumulative probability accuracy.
     */
    @Test
    public void testInverseCumulativeProbabilityFunctionValidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            Assert.assertTrue(inverseCumulativeProbability >= 0.0 && inverseCumulativeProbability < 1.0);
            Assert.assertEquals(p, distribution.cumulativeProbability(inverseCumulativeProbability), 0.0001);
        }
    }

    /**
     * Test inverse cumulative probability function (quantiles) with valid
     * bounds and custom inverse cumulative probability accuracy.
     */
    @Test
    public void testInverseCumulativeProbabilityFunctionCustomAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0, 1e-6);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            Assert.assertTrue(inverseCumulativeProbability >= 0.0 && inverseCumulativeProbability < 1.0);
            Assert.assertEquals(p, distribution.cumulativeProbability(inverseCumulativeProbability), 1e-6);
        }
    }

    /**
     * Test inverse cumulative probability function (quantiles) with invalid
     * bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInverseCumulativeProbabilityFunctionInvalidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 0.0);
        distribution.inverseCumulativeProbability(0.5);
    }
}