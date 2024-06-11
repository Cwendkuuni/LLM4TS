package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test for the {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        assertEquals(2.5, poissonDistImpl.getMean(), 0.001);
    }

    /**
     * Test the constructor with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstruction() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test the probability method with valid arguments.
     */
    @Test
    public void testProbability() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistImpl.probability(i);
            assertEquals(FastMath.exp(-2.5) * Gamma.factorial(i) / Math.pow(Math.E, 2.5), probability, 0.001);
        }
    }

    /**
     * Test the cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistImpl.cumulativeProbability(i);
            double probability = poissonDistImpl.probability(i);
            assertTrue(Precision.isClose(cumulativeProbability, Gamma.regularizedGammaQ(i + 1, 2.5), 0.001));
            assertTrue(Precision.isClose(cumulativeProbability, probability, 0.001));
        }
    }

    /**
     * Test the normalApproximateProbability method with valid arguments.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, FastMath.sqrt(2.5));
        NormalDistributionImpl normalDistImpl = (NormalDistributionImpl) normalDist;
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDistImpl.normalApproximateProbability(i);
            double probability = normalDistImpl.cumulativeProbability(i);
            assertTrue(Precision.isClose(normalApproximateProbability, probability, 0.001));
        }
    }

    /**
     * Test the sample method with valid arguments.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        for (int i = 0; i < 10; i++) {
            int sample = poissonDistImpl.sample();
            assertEquals(i, sample); // for small means, use simulation
            int expected = (int) (2.5 * Math.random()); // for large means, should be close to mean
            assertEquals(expected, sample); // for large means, should be close to mean
        }
    }

    /**
     * Test the inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        for (int i = 1; i <= 10; i++) {
            int inverseCumulativeProbability = poissonDistImpl.inverseCumulativeProbability(
                    poissonDistImpl.cumulativeProbability(i));
            assertEquals(i, inverseCumulativeProbability);
        }
    }
}