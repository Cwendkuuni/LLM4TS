package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class NormalDistributionImplTest {

    /**
     * Test NormalDistributionImpl construction with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.5);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(3.5, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test NormalDistributionImpl construction with default mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplDefaultConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test cumulative probability with given mean and standard deviation.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.5);
        double p = 0.8413; // value from NormalDistributionTest
        double cdf = dist.cumulativeProbability(2.542);
        assertEquals(p, cdf, 0.001);
    }

    /**
     * Test inverse cumulative probability with given mean and standard deviation.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.5);
        double p = 0.1596; // value from NormalDistributionTest
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(-1.225, x, 0.001);
    }

    /**
     * Test mean and standard deviation getters and setters.
     */
    @Test
    public void testMeanAndStandardDeviationGettersAndSetters() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertEquals(2.5, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test setting standard deviation with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulative probability with mean equal to x and standard deviation equal to 0.
     */
    @Test
    public void testCumulativeProbabilityZeroVariance() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double x = 0.0;
        double p = 0.5;
        double cdf = dist.cumulativeProbability(x);
        assertEquals(p, cdf, 0.001);
    }

    /**
     * Test inverse cumulative probability with p equal to 0 or 1.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test inverse cumulative probability with mean and standard deviation.
     */
    @Test
    public void testInverseCumulativeProbabilityWithMeanAndStdDev()
            throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.5);
        double p = 0.6827; // value from NormalDistributionTest
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(1.206, x, 0.001);
    }

    /**
     * Test mean and standard deviation with large values.
     */
    @Test
    public void testLargeValues() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1e6, 1e6);
        StandardDeviation stdDev = new StandardDeviation();
        double mean = dist.getMean();
        double std = dist.getStandardDeviation();
        assertEquals(1e6, mean, 0.0);
        assertEquals(1e6, std, 0.0);
        double sd = stdDev.evaluate(dist.createSamples(10000));
        assertEquals(std, sd, 0.001);
    }
}