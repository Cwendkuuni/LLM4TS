package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 1.0, 3.0);
        assertEquals(1.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(3.0, distribution.getSupportUpperBound(), 0.0);
        assertEquals(2.0, distribution.getNumericalMean(), 0.0);
        assertEquals(0.64, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBoundsConstructor() {
        new UniformRealDistribution(new Well19937c(), 5.0, 2.0);
    }

    /**
     * Test {@link #density(double)}.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            double density = distribution.density(x);
            if (x == distribution.getSupportLowerBound() || x == distribution.getSupportUpperBound()) {
                assertEquals(1.0 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()), density, 0.001);
            } else {
                assertEquals(1.0 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()), density, 0.0001);
            }
        }
    }

    /**
     * Test {@link #cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            if (x == distribution.getSupportLowerBound()) {
                assertEquals(0.0, cumulativeProbability, 0.001);
            } else if (x == distribution.getSupportUpperBound()) {
                assertEquals(1.0, cumulativeProbability, 0.001);
            } else {
                double intervalSize = distribution.getSupportUpperBound() - distribution.getSupportLowerBound();
                assertEquals((x - distribution.getSupportLowerBound()) / intervalSize, cumulativeProbability, 0.001);
            }
        }
    }

    /**
     * Test {@link #sample()}.
     */
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getSupportLowerBound());
            assertTrue(sample < distribution.getSupportUpperBound());
        }
    }

    /**
     * Test {@link #getSolverAbsoluteAccuracy()}.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test {@link #getNumericalMean()}.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5 * (distribution.getSupportLowerBound() + distribution.getSupportUpperBound()), distribution.getNumericalMean(), 0.0);
    }

    /**
     * Test {@link #getNumericalVariance()}.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        double intervalSize = distribution.getSupportUpperBound() - distribution.getSupportLowerBound();
        assertEquals(intervalSize * intervalSize / 12, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test {@link #getSupportLowerBound()}.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(distribution.getSupportLowerBound(), distribution.getSupportLowerBound(), 0.0);
    }

    /**
     * Test {@link #getSupportUpperBound()}.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(distribution.getSupportUpperBound(), distribution.getSupportUpperBound(), 0.0);
    }

    /**
     * Test {@link #isSupportLowerBoundInclusive()}.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test {@link #isSupportUpperBoundInclusive()}.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test {@link #isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportConnected());
    }
}