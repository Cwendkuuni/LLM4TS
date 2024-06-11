package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private RandomGenerator rng;
    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0, urd.getSupportLowerBound(), 0);
        assertEquals(1, urd.getSupportUpperBound(), 0);
        assertEquals(0.5, urd.getNumericalMean(), 0);
        assertEquals(0.25, urd.getNumericalVariance(), 0);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testConstructorWithCustomBounds() {
        UniformRealDistribution urd = new UniformRealDistribution(rng, 2, 5);
        assertEquals(2, urd.getSupportLowerBound(), 0);
        assertEquals(5, urd.getSupportUpperBound(), 0);
        assertEquals(3, urd.getNumericalMean(), 0);
        assertEquals(2.25, urd.getNumericalVariance(), 0);
    }

    /**
     * Test constructor with custom bounds and accuracy.
     */
    @Test
    public void testConstructorWithCustomBoundsAndAccuracy() {
        UniformRealDistribution urd = new UniformRealDistribution(rng, 2, 5, 1e-5);
        assertEquals(2, urd.getSupportLowerBound(), 0);
        assertEquals(5, urd.getSupportUpperBound(), 0);
        assertEquals(3, urd.getNumericalMean(), 0);
        assertEquals(2.25, urd.getNumericalVariance(), 1e-5);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        double x = 0.25;
        double density = distribution.density(x);
        assertEquals(0.0039269908169872416, density, 1e-7);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        double x = 0.3;
        double probability = distribution.cumulativeProbability(x);
        assertEquals(0.3, probability, 1e-7);
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        double probability = 0.5;
        double expectedX = 0.5;
        double x = distribution.inverseCumulativeProbability(probability);
        assertEquals(expectedX, x, solverAbsoluteAccuracy);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        double x = distribution.sample();
        assertTrue(x >= 0 && x < 1);
    }

    /**
     * Test sample method with custom generator.
     */
    @Test
    public void testSampleWithCustomGenerator() {
        UniformRealDistribution urd = new UniformRealDistribution(rng, 2, 5);
        double x = urd.sample();
        assertTrue(x >= 2 && x < 5);
    }

    /**
     * Test sample method with custom accuracy.
     */
    @Test
    public void testSampleWithCustomAccuracy() {
        UniformRealDistribution urd = new UniformRealDistribution(rng, 2, 5, 1e-6);
        double x = urd.sample();
        assertTrue(Math.abs(x - 3) <= 0.000001);
    }

    /**
     * Test sample method with lower bound equals to upper bound.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleWithLowerBoundEqualsToUpperBound() {
        UniformRealDistribution urd = new UniformRealDistribution(rng, 1, 1);
        urd.sample();
    }
}