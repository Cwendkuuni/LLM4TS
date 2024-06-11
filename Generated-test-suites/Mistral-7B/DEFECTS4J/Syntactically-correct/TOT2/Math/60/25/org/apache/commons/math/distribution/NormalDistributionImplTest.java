package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid parameters.
     * @throws MathException if the constructor fails.
     */
    @Test
    public void testNormalDistributionImplConstructValid() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructInvalid() {
        new NormalDistributionImpl(0, -1);
    }

    /**
     * Test density method with valid input.
     */
    @Test
    public void testDensityValidInput() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.398942280401, dist.density(0.0), 0.0001);
    }

    /**
     * Test density method with edge cases.
     */
    @Test
    public void testDensityEdgeCases() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.density(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(0.0, dist.density(Double.POSITIVE_INFINITY), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, dist.cumulativeProbability(1.0), 0.001);
    }

    /**
     * Test cumulativeProbability method with edge cases.
     */
    @Test
    public void testCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(-1.281552027, dist.inverseCumulativeProbability(0.1587), 0.001);
    }

    /**
     * Test inverseCumulativeProbability method with edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample1 = dist.sample();
        double sample2 = dist.sample();
        assertEquals(0.0, sample1, 0.001);
        assertEquals(-0.302758158, sample2, 0.001);
    }

    /**
     * Test sample method with custom mean and standard deviation.
     */
    @Test
    public void testSampleCustomParameters() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        double sample = dist.sample();
        double expectedMean = 1.0;
        double expectedStdDev = 2.0;
        double sampleStdDev = Math.sqrt(Math.pow(sample - expectedMean, 2) / 2.0);
        assertEquals(expectedMean, sample, 0.001);
        assertEquals(expectedStdDev, sampleStdDev, 0.001);
    }

    /**
     * Test sample method with edge cases.
     */
    @Test
    public void testSampleEdgeCases() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sampleMin
                = dist.sample();
        double sampleMax = dist.sample();
        assertTrue(sampleMin <= sampleMax);
        assertEquals(Double.NEGATIVE_INFINITY, sampleMin, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, sampleMax, 0.0);
    }
}