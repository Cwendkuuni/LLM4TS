package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);

        dist = new UniformRealDistribution(0, 1);
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);

        try {
            dist = new UniformRealDistribution(-1, 0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            dist = new UniformRealDistribution(0, 0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0; x < 1; x += 0.01) {
            double density = dist.density(x);
            double expected = 1 / (1 - 0);
            assertEquals(expected, density, 1e-7);
        }

        dist = new UniformRealDistribution(0, 2);
        for (double x = 0; x < 2; x += 0.01) {
            double density = dist.density(x);
            double expected = 1 / (2 - 0);
            assertEquals(expected, density, 1e-7);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0; x < 1; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            assertEquals(x, cdf, 1e-7);
        }

        dist = new UniformRealDistribution(0, 2);
        for (double x = 0; x < 2; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            double expected = x / (2 - 0);
            assertEquals(expected, cdf, 1e-7);
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0 && sample < 1);
        }

        dist = new UniformRealDistribution(0, 2);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0 && sample < 2);
        }
    }

    /**
     * Test numericalMean method.
     */
    @Test
    public void testNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double mean = dist.getNumericalMean();
        assertEquals(0.5, mean, 1e-7);

        dist = new UniformRealDistribution(0, 2);
        mean = dist.getNumericalMean();
        assertEquals(1, mean, 1e-7);
    }

    /**
     * Test numericalVariance method.
     */
    @Test
    public void testNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double variance = dist.getNumericalVariance();
        assertEquals(0.000244948974278317, variance, 1e-11);

        dist = new UniformRealDistribution(0, 2);
        variance = dist.getNumericalVariance();
        assertEquals(0.005, variance, 1e-7);
    }
}