package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStddev() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with mean equal to zero and standard deviation equal to one.
     */
    @Test
    public void testNormalDistributionImplWithDefault() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test mean accessor.
     */
    @Test
    public void testMeanAccessor() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        dist.setMean(3.0);
        assertEquals(3.0, dist.getMean(), 0.001);
    }

    /**
     * Test mean modifier.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMeanModifierNegative() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.setMean(-1.0);
    }

    /**
     * Test standard deviation accessor.
     */
    @Test
    public void testStandardDeviationAccessor() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
        dist.setStandardDeviation(3.0);
        assertEquals(3.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test standard deviation modifier.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardDeviationModifierZeroOrNegative() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.setStandardDeviation(0.0);
        dist.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            double expected = Erf.erf(x / (dist.getStandardDeviation() * Math.sqrt(2.0))) / 2.0 + 0.5;
            assertEquals(expected, p, 0.001);
        }
    }

    /**
     * Test inverse cumulative probability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = dist.getMean() + dist.getStandardDeviation() * Erf.erfinv(2 * p - 1);
            assertEquals(expected, x, 0.01);
        }
    }

    /**
     * Test inverse cumulative probability method with mean and standard deviation different from zero.
     */
    @Test
    public void testInverseCumulativeProbabilityWithNonZeroMeanAndStddev()
            throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = dist.getMean() + dist.getStandardDeviation() * Erf.erfinv(2 * p - 1);
            assertEquals(expected, x, 0.01);
        }
    }

    /**
     * Test inverse cumulative probability method with extreme values of p.
     */
    @Test
    public void testInverseCumulativeProbabilityExtremeValues()
            throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double pMin = 0.0;
        double pMax = 1.0;
        double eps = 0.001;
        double xMin = dist.inverseCumulativeProbability(pMin);
        double xMax = dist.inverseCumulativeProbability(pMax);
        assertEquals(Double.NEGATIVE_INFINITY, xMin, eps);
        assertEquals(Double.POSITIVE_INFINITY, xMax, eps);
    }

    /**
     * Test inverse cumulative probability method with negative argument.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeArgument()
            throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverse cumulative probability method with p > 1.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityPGreaterThanOne()
            throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test generation of random numbers from distribution.
     */
    @Test
    public void testRandomNumbersGeneration() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double[] values = new double[1000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.inverseCumulativeProbability(StatUtils.uniform(0.0, 1.0));
        }
        for (int i = 0; i < values.length; i++) {
            double expected = dist.getMean() + dist.getStandardDeviation() * Erf.erfinv(2 * StatUtils.uniform(0.0, 1.0) - 1);
            assertEquals(expected, values[i], 0.01);
        }
    }
}