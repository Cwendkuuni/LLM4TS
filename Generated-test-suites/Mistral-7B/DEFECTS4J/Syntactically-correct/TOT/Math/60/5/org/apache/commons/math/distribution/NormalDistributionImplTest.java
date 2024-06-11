package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomDataGenerator;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /** Test data generator. */
    private static final RandomDataGenerator rdg = new RandomDataGenerator();

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        for (int i = 0; i < 100; i++) {
            double value = dist.sample();
            assertTrue(value > -3 && value < 3);
        }
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double, double)}.
     */
    @Test
    public void testNormalDistributionImplWithAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2, 0.001);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);

        double p = 0.6827; // Approximate value for Gaussian distribution
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        double x = dist.cumulativeProbability(inverseCumulativeProbability);
        assertEquals(p, dist.cumulativeProbability(x), 0.001);
        assertEquals(x, dist.inverseCumulativeProbability(p), 0.001);

        try {
            dist.inverseCumulativeProbability(1.1);
        } catch (NoRealSolutionException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(-0.1);
        } catch (NoRealSolutionException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(-1.0);
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(5.0);
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 1.0;
        double density = dist.density(x);
        assertEquals(0.2419707917251777, density, 1e-4);

        x = 3.0;
        density = dist.density(x);
        assertEquals(0.001297434252845114, density, 1e-4);

        x = -3.0;
        density = dist.density(x);
        assertEquals(0.001297434252845114, density, 1e-4);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double x = 0.0;
        double probability = dist.cumulativeProbability(x);
        assertEquals(0.5, probability, 1e-6);

        x = 1.0;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.8413447460680221, probability, 1e-6);

        x = 2.0;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.9772497356154804, probability, 1e-6);

        x = 3.0;
        probability = dist.cumulativeProbability(x);
        assertEquals(1.0, probability, 1e-6);

        x = -3.0;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.0, probability, 1e-6);

        x = -5.0;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.0, probability, 1e-6);

        x = 5.0;
        probability = dist.cumulativeProbability(x);
        assertEquals(1.0, probability, 1e-6);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 1e-6);

        p = 0.8413447460680221;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1.0, x, 1e-6);

        p = 0.9772497356154804;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(2.0, x, 1e-6);

        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(3.0, x, 1e-6);

        p = 0.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-3.0, x, 1e-6);

        p = -0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-3.0, x, 1e-6);

        p = -1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-5.0, x, 1e-6);

        p = 1.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(rdg.nextGaussian(0, 1), x, 0.01);
    }

    /**
     * Test method for {@link NormalDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (int i = 0; i < 100; i++) {
            double value = dist.sample();
            assertTrue(value > -3 && value < 3);
        }
    }
}