package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test construction with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test construction with mean equal to zero and standard deviation equal to one.
     */
    @Test
    public void testNormalDistributionImplDefaultConstruction() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test {@link NormalDistributionImpl#cumulativeProbability(double)} method.
     * Positive case.
     */
    @Test
    public void testCumulativeProbabilityPositiveCase() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 0.75;
        double expectedResult = Erf.erf(probability);
        double result = distribution.cumulativeProbability(probability);
        assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test {@link NormalDistributionImpl#cumulativeProbability(double)} method.
     * Edge case: negative mean.
     */
    @Test
    public void testCumulativeProbabilityNegativeMeanEdgeCase()
            throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(-1.0, 1.0);
        double probability = 0.75;
        double expectedResult = Erf.erf(probability) + 0.5;
        double result = distribution.cumulativeProbability(probability);
        assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test {@link NormalDistributionImpl#cumulativeProbability(double)} method.
     * Error case: mean is not set.
     */
    @Test(expected = IllegalStateException.class)
    public void testCumulativeProbabilityMeanNotSetErrorCase() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.cumulativeProbability(0.0);
    }

    /**
     * Test {@link NormalDistributionImpl#inverseCumulativeProbability(double)} method.
     * Positive case.
     */
    @Test
    public void testInverseCumulativeProbabilityPositiveCase()
            throws MathException, IllegalArgumentException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 0.75;
        double expectedResult = Erf.erfInverse(probability);
        double result = distribution.inverseCumulativeProbability(probability);
        assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test {@link NormalDistributionImpl#inverseCumulativeProbability(double)} method.
     * Edge case: p is equal to zero.
     */
    @Test
    public void testInverseCumulativeProbabilityZeroEdgeCase()
            throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 0.0;
        double expectedResult = Double.NEGATIVE_INFINITY;
        double result = distribution.inverseCumulativeProbability(probability);
        assertEquals(expectedResult, result);
    }

    /**
     * Test {@link NormalDistributionImpl#inverseCumulativeProbability(double)} method.
     * Error case: p is greater than one.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityGreaterThanOneErrorCase()
            throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 1.1;
        distribution.inverseCumulativeProbability(probability);
    }
}