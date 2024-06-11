package org.apache.commons.math.distribution;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0.5);
        assertEquals(0.1204393181186829, density, 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double result = dist.cumulativeProbability(0.5);
        assertEquals(p, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double result = dist.inverseCumulativeProbability(p);
        double expected = 0.5;
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double p = -1;
        dist.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double x = Double.NEGATIVE_INFINITY;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        double mean = dist.getMean();
        double stddev = dist.getStandardDeviation();
        double expected = mean + stddev * RandomData.nextGaussian();
        assertEquals(expected, sample, 0.001);
    }
}