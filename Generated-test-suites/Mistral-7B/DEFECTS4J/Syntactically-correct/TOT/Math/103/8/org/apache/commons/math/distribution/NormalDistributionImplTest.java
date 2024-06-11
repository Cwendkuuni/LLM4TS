package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test instance creation.
     */
    @Test
    public void testCreation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test creation with default parameters.
     */
    @Test
    public void testDefaultCreation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting and getting mean.
     */
    @Test
    public void testMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.setMean(3.0);
        assertEquals(3.0, dist.getMean(), 0.0);
        dist.setMean(1.0);
        assertEquals(1.0, dist.getMean(), 0.0);
    }

    /**
     * Test setting and getting standard deviation.
     */
    @Test
    public void testStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.setStandardDeviation(3.0);
        assertEquals(3.0, dist.getStandardDeviation(), 0.0);
        dist.setStandardDeviation(1.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test creating distribution with negative standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(1.0, -1.0);
    }

    /**
     * Test cumulative probability calculation.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(0.154430669, dist.cumulativeProbability(0.0), 0.00000001);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 0.00000001);
        assertEquals(0.841344341, dist.cumulativeProbability(2.0), 0.00000001);
    }

    /**
     * Test inverse cumulative probability calculation.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(-0.5241578235, dist.inverseCumulativeProbability(0.154430669), 0.00000001);
        assertEquals(1.0, dist.inverseCumulativeProbability(0.5), 0.00000001);
        assertEquals(1.9565212955, dist.inverseCumulativeProbability(0.841344341), 0.00000001);
    }

    /**
     * Test calculating probabilities using the static methods of {@link StatUtils}.
     */
    @Test
    public void testProbabilities() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 1.0);
        double[] probabilities = new double[100];
        double[] xValues = new double[100];
        for (int i = 0; i < 100; i++) {
            xValues[i] = i / 100.0;
            probabilities[i] = StatUtils.calculateProbability(dist, xValues[i]);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(xValues[i], dist.inverseCumulativeProbability(probabilities[i]), 0.00000001);
        }
    }

    /**
     * Test cumulative probability calculation with extreme values.
     */
    @Test
    public void testExtremeValues() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(0.0, dist.cumulativeProbability(-100.0), 0.00000001);
        assertEquals(1.0, dist.cumulativeProbability(100.0), 0.00000001);
    }

    /**
     * Test exception handling in cumulative probability calculation.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 1.0);
        dist.cumulativeProbability(1000.0);
    }
}