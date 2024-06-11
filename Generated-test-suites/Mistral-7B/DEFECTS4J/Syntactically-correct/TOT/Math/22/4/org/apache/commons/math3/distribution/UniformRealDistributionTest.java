package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test the constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test the constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(3.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(2.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.64, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test the constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(3.0, 1.0);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 3.0);
        for (double x = 1.0; x <= 4.0; x += 0.1) {
            double density = dist.density(x);
            if (x >= 3.0) {
                assertEquals(0.0, density, 1e-6);
            } else {
                assertEquals(1.0 / (3.0 - 1.0), density, 1e-6);
            }
        }
    }

    /**
     * Test the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 3.0);
        for (double x = 1.0; x <= 4.0; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            if (x >= 3.0) {
                assertEquals(1.0, p, 1e-6);
            } else {
                assertEquals(x - 1.0, p, 1e-6);
            }
        }
    }

    /**
     * Test the sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 1.0, 3.0);
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1.0 && sample <= 3.0);
        }
    }

    /**
     * Test the sample method with default bounds.
     */
    @Test
    public void testSampleDefault() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }
}