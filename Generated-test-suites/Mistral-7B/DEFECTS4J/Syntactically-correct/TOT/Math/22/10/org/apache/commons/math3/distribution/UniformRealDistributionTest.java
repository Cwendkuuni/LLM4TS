package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test creating a standard uniform real distribution.
     */
    @Test
    public void testStandardUniform() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getNumericalMean(), 0.001);
        assertEquals(0, dist.getNumericalVariance(), 0.001);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0 && sample <= 1);
        }
    }

    /**
     * Test creating a uniform real distribution with custom bounds.
     */
    @Test
    public void testCustomUniform() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3);
        assertEquals(2, dist.getNumericalMean(), 0.001);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1 && sample <= 3);
        }
    }

    /**
     * Test creating a uniform real distribution with a random number generator.
     */
    @Test
    public void testCustomUniformWithRng() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 1, 3);
        assertEquals(2, dist.getNumericalMean(), 0.001);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1 && sample <= 3);
        }
    }

    /**
     * Test creating a uniform real distribution with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidUniform() throws NumberIsTooLargeException {
        new UniformRealDistribution(3, 2);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0, dist.inverseCumulativeProbability(0), 0.001);
        assertEquals(1, dist.inverseCumulativeProbability(1), 0.001);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.001);
        assertNaN(dist.inverseCumulativeProbability(-1));
        assertNaN(dist.inverseCumulativeProbability(2));
    }
}