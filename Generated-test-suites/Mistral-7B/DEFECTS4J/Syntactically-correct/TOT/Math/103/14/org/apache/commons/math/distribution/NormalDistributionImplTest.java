package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test instance creation with valid parameters.
     */
    @Test
    public void testInstanceCreation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, distribution.getMean(), 0.001);
        assertEquals(1.2, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test instance creation with default parameters.
     */
    @Test
    public void testInstanceCreationDefault() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test mean getter and setter.
     */
    @Test
    public void testMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
        distribution.setMean(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    /**
     * Test standard deviation getter and setter.
     */
    @Test
    public void testStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
        distribution.setStandardDeviation(2.5);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test invalid standard deviation setter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStandardDeviationSetter() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulative probability method with valid parameters.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double expected = 0.5 * (1.0 + Erf.erf(-2.5 / (1.0 * Math.sqrt(2.0))));
        assertEquals(expected, distribution.cumulativeProbability(-2.5), 0.001);
        expected = 0.5 * (1.0 + Erf.erf(0.5 / (1.0 * Math.sqrt(2.0))));
        assertEquals(expected, distribution.cumulativeProbability(0.5), 0.001);
        expected = 1.0;
        assertEquals(expected, distribution.cumulativeProbability(3.5), 0.001);
    }

    /**
     * Test inverse cumulative probability method with valid parameters.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException,
            IllegalArgumentException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.123456789;
        double expected = Erf.erfInverse(p / 2.0) * (1.0 / Math.sqrt(2.0)) +
                distribution.getMean();
        assertEquals(expected, distribution.inverseCumulativeProbability(p),
                0.001);
    }

    /**
     * Test inverse cumulative probability method with edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.0);
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.0);
    }

    /**
     * Test inverse cumulative probability method with invalid probability.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability()
            throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 2.0;
        distribution.inverseCumulativeProbability(p);
    }
}