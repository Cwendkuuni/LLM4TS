package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private static final double EPSILON = 1e-10;
    private RandomGenerator rng;
    private UniformRealDistribution dist;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0, dist.getSupportLowerBound(), EPSILON);
        Assert.assertEquals(1, dist.getSupportUpperBound(), EPSILON);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test constructor with specified lower and upper bounds.
     * @throws NumberIsTooLargeException if lower >= upper.
     */
    @Test
    public void testConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        Assert.assertEquals(0.5, dist.getSupportLowerBound(), EPSILON);
        Assert.assertEquals(1.5, dist.getSupportUpperBound(), EPSILON);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test constructor with specified lower bound, upper bound, and inverse cumulative probability accuracy.
     * @throws NumberIsTooLargeException if lower >= upper.
     */
    @Test
    public void testConstructorAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5, 1e-5);
        Assert.assertEquals(0.5, dist.getSupportLowerBound(), EPSILON);
        Assert.assertEquals(1.5, dist.getSupportUpperBound(), EPSILON);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test {@link #density(double)}.
     */
    @Test
    public void testDensity() {
        for (double x = 0; x <= 1; x += 0.1) {
            double density = dist.density(x);
            double expected = 1 / (1 - 0);
            Assert.assertEquals(expected, density, EPSILON);
        }
    }

    /**
     * Test {@link #cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        for (double x = 0; x <= 1; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            double expected = x;
            Assert.assertEquals(expected, p, EPSILON);
        }
    }

    /**
     * Test {@link #sample()}.
     */
    @Test
    public void testSample() {
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            Assert.assertTrue(sample >= dist.getSupportLowerBound());
            Assert.assertTrue(sample < dist.getSupportUpperBound());
        }
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() throws NumberIsTooLargeException {
        new UniformRealDistribution(1, 0);
    }
}