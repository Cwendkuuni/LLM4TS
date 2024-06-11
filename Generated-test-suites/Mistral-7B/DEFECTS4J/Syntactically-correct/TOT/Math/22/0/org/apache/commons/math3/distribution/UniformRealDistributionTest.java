package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() throws NumberIsTooLargeException {
        rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test {@link UniformRealDistribution#UniformRealDistribution()} constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0, dist.getNumericalMean(), 0.0001);
        Assert.assertEquals(0, dist.getNumericalVariance(), 0.0001);
        Assert.assertEquals(0, dist.getSupportLowerBound(), 0.0, 0.0001);
        Assert.assertEquals(1, dist.getSupportUpperBound(), 0.0, 0.0001);
    }

    /**
     * Test {@link UniformRealDistribution#UniformRealDistribution(double, double)}
     * constructor with valid bounds.
     */
    @Test
    public void testConstructorWithValidBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1, 3);
        Assert.assertEquals(2, dist.getNumericalMean(), 0.0001);
        Assert.assertEquals(0.25, dist.getNumericalVariance(), 0.0001);
        Assert.assertEquals(1, dist.getSupportLowerBound(), 0.0, 0.0001);
        Assert.assertEquals(3, dist.getSupportUpperBound(), 0.0, 0.0001);
    }

    /**
     * Test {@link UniformRealDistribution#UniformRealDistribution(double, double, double)}
     * constructor with valid bounds and accuracy.
     */
    @Test
    public void testConstructorWithValidBoundsAndAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1, 3, 1e-12);
        Assert.assertEquals(2, dist.getNumericalMean(), 0.0000001);
        Assert.assertEquals(0.25, dist.getNumericalVariance(), 0.0000001);
        Assert.assertEquals(1, dist.getSupportLowerBound(), 0.0, 0.0000001);
        Assert.assertEquals(3, dist.getSupportUpperBound(), 0.0, 0.0000001);
    }

    /**
     * Test {@link UniformRealDistribution#UniformRealDistribution(double, double, double)}
     * constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(rng, 3, 1);
    }

    /**
     * Test {@link UniformRealDistribution#density(double)} method.
     */
    @Test
    public void testDensity() {
        double x = distribution.sample();
        double density = distribution.density(x);
        Assert.assertEquals(1 / (1 - 0), density, 0.0001);
    }

    /**
     * Test {@link UniformRealDistribution#cumulativeProbability(double)} method.
     */
    @Test
    public void testCumulativeProbability() {
        double x = distribution.sample();
        double p = distribution.cumulativeProbability(x);
        Assert.assertEquals(x, p, 0.0001);
    }

    /**
     * Test {@link UniformRealDistribution#sample()} method.
     */
    @Test
    public void testSample() {
        double x = distribution.sample();
        Assert.assertTrue(x >= 0 && x < 1);
    }
}