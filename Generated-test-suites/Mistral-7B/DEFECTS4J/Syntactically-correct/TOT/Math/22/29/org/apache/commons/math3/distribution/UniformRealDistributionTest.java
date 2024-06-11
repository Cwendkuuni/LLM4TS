package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private static final double DELTA = 1e-6;
    private static final double EPSILON = 1e-9;
    private UniformRealDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), EPSILON);
        assertEquals(1, dist.getSupportUpperBound(), EPSILON);
        assertEquals(0.5, dist.getNumericalMean(), EPSILON);
        assertEquals(0.25, dist.getNumericalVariance(), EPSILON);
    }

    /**
     * Test constructor with specified bounds.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 2, 5);
        assertEquals(2, dist.getSupportLowerBound(), EPSILON);
        assertEquals(5, dist.getSupportUpperBound(), EPSILON);
        assertEquals(3, dist.getNumericalMean(), EPSILON);
        assertEquals(2.8888888888888888, dist.getNumericalVariance(), DELTA);
    }

    /**
     * Test constructor with negative bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNegativeBounds() {
        new UniformRealDistribution(rng, 5, 2);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        double[] x = new double[] { 0.1, 0.5, 0.9 };
        double[] result = new double[] { 0.009765625, 0.125, 0.009765625 };
        for (int i = 0; i < x.length; i++) {
            assertEquals(result[i], dist.density(x[i]), EPSILON);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        double[] x = new double[] { 0.1, 0.5, 0.9 };
        double[] result = new double[] { 0.09682539682539683, 0.5, 0.9031746031746032 };
        for (int i = 0; i < x.length; i++) {
            assertEquals(result[i], dist.cumulativeProbability(x[i]), EPSILON);
        }
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        double[] p = new double[] { 0.1, 0.5, 0.9 };
        double[] result = new double[] { 0.1, 0.5, 0.9 };
        for (int i = 0; i < p.length; i++) {
            assertEquals(result[i], dist.inverseCumulativeProbability(p[i]), EPSILON);
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0 && sample < 1);
        }
    }
}