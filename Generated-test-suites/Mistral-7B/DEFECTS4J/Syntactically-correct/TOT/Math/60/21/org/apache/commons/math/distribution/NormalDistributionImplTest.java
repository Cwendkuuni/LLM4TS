package org.apache.commons.math.distribution.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertNotNull(dist);
    }

    /**
     * Test construction with negative standard deviation.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructionNegativeSD() {
        new NormalDistributionImpl(0, -1);
    }

    /**
     * Test mean and standard deviation.
     */
    @Test
    public void testMeanAndSD() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 1e-9);
        assertEquals(1, dist.getStandardDeviation(), 1e-9);
    }

    /**
     * Test density value.
     */
    @Test
    public void testDensityValue() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals(0.398942280401433, density, 1e-9);
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double cumProb = dist.cumulativeProbability(0);
        assertEquals(0.5, cumProb, 1e-9);
        p = 0.25;
        cumProb = dist.cumulativeProbability(1.5);
        assertEquals(p, cumProb, 1e-9);
        p = 0.95;
        cumProb = dist.cumulativeProbability(1.58);
        assertEquals(p, cumProb, 1e-9);
        p = 0.01;
        cumProb = dist.cumulativeProbability(-3.1);
        assertEquals(p, cumProb, 1e-9);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.15;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(-0.6745, x, 1e-9);
        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0, x, 1e-9);
        p = 0.95;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1.6449, x, 1e-9);
    }

    /**
     * Test sample value.
     */
    @Test
    public void testSampleValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = dist.sample();
        assertEquals(0, x, 1e-9);
    }

    /**
     * Test mean and standard deviation of distribution.
     */
    @Test
    public void testMeanAndSDFromSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        int n = 100000;
        double[] samples = new double[n];
        for (int i = 0; i < n; i++) {
            samples[i] = dist.sample();
        }
        Mean mean = new Mean();
        StandardDeviation sd = new StandardDeviation();
        assertEquals(0, mean.value(samples), 1e-9);
        assertEquals(1, sd.value(samples), 1e-9);
    }
}