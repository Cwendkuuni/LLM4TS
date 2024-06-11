package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for {@link NormalDistributionImpl}
 */
public class NormalDistributionImplTest {

    @Test
    public void testConstructionWithMeanAndStddev() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(1.2, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testConstructionWithMeanAndStddev_MeanZeroStddevOne() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability_MeanZeroStddevOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        for (double x = -10; x <= 10; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            assertTrue(cdf >= 0.0 && cdf <= 1.0);
            if (x < -3.0 || x > 3.0) {
                assertEquals(cdf, 0.5, 0.001);
            } else {
                double expected = Erf.erf(x / Math.sqrt(2.0));
                assertEquals(cdf, expected / 2.0, 0.001);
            }
        }
    }

    @Test
    public void testCumulativeProbability_MeanTwoStddevOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(2.0, 1.0);
        for (double x = -5; x <= 5; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            assertTrue(cdf >= 0.0 && cdf <= 1.0);
            if (x < -3.0 || x > 3.0) {
                assertEquals(cdf, 0.5, 0.001);
            } else {
                double mean = dist.getMean();
                double stddev = dist.getStandardDeviation();
                double expected = Erf.erf((x - mean) / (stddev * Math.sqrt(2.0)));
                assertEquals(cdf, (expected + 0.5) / 2.0, 0.001);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviation_NegativeValue() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double cdf = dist.cumulativeProbability(x);
            assertEquals(p, cdf, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_MaxIterationsExceeded() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMaxIterations(5);
        dist.inverseCumulativeProbability(0.5);
    }
}