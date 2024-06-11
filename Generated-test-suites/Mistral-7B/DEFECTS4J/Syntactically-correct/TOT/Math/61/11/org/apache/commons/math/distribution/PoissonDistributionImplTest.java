package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for the {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {

    /**
     * Test construction with valid mean.
     */
    @Test
    public void testValidConstruction() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals("Mean not set correctly", 2.5, dist.getMean(), 0.001);
    }

    /**
     * Test construction with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidConstructionNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals("Probability not correct for x = 0", 0.119, dist.probability(0), 0.001);
        assertEquals("Probability not correct for x = 1", 0.215, dist.probability(1), 0.001);
        assertEquals("Probability not correct for x = 10", 0.006, dist.probability(10), 0.001);
        assertEquals("Probability not correct for x = 100", 0.0, dist.probability(100), 0.001);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals("Cumulative probability not correct for x = 0", 0.073, dist.cumulativeProbability(0), 0.001);
        assertEquals("Cumulative probability not correct for x = 1", 0.203, dist.cumulativeProbability(1), 0.001);
        assertEquals("Cumulative probability not correct for x = 10", 0.988, dist.cumulativeProbability(10), 0.001);
        assertEquals("Cumulative probability not correct for x = 100", 1.0, dist.cumulativeProbability(100), 0.001);
    }

    /**
     * Test normal approximation probability method.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        for (int x = 0; x <= 10; x++) {
            double normalApprox = dist.normalApproximateProbability(x);
            double exact = dist.probability(x);
            double maxError = Math.max(Math.abs(normalApprox - exact), 1.0E-6);
            assertEquals("Normal approximation probability not close enough to exact probability for x = " + x,
                         exact, normalApprox, maxError);
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample;
        for (int i = 0; i < 10; i++) {
            sample = dist.sample();
            assertEquals("Sample not in expected range", 0, sample, Integer.MAX_VALUE);
            assertEquals("Probability of sample not close enough to expected", 2.5, dist.cumulativeProbability(sample), 0.01);
        }
    }
}