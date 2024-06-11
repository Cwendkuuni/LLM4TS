package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test class for {@link NormalDistributionImpl}
 */
public class NormalDistributionImplTest {

    /**
     * Test instance creation with valid arguments.
     */
    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test instance creation with default arguments.
     */
    @Test
    public void testNormalDistributionDefaultCreation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test calculation of cumulative probability for various input values.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        double p = 0.0;
        double expected = 0.5;
        double actual = distribution.cumulativeProbability(0.0);
        assertEquals(expected, actual, 0.001);

        p = 0.1;
        expected = 0.154554;
        actual = distribution.cumulativeProbability(0.0);
        assertEquals(expected, actual, 0.001);

        p = 0.5;
        expected = 0.5;
        actual = distribution.cumulativeProbability(0.0);
        assertEquals(expected, actual, 0.001);

        p = 0.9;
        expected = 0.841345;
        actual = distribution.cumulativeProbability(0.0);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test calculation of inverse cumulative probability for various input values.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual);

        p = 0.1;
        expected = -0.841345;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);

        p = 0.5;
        expected = 0.0;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);

        p = 0.9;
        expected = 0.841345;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test setting mean and standard deviation.
     */
    @Test
    public void testSettingProperties() {
        NormalDistribution distribution = new NormalDistributionImpl();

        // set mean
        distribution.setMean(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0);

        // set standard deviation
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting negative standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    /**
     * Test calculation of distribution function for various input values using
     * {@link Erf}.
     */
    @Test
    public void testDistributionFunction() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double x : new double[] { -5.0, -2.0, -1.0, 0.0, 1.0, 2.0, 5.0 }) {
            double pdf = Erf.erf((x - distribution.getMean()) / (distribution.getStandardDeviation() * Math.sqrt(2.0)));
            double actual = distribution.probabilityDensityFunction(x);
            assertEquals(pdf, actual, 0.001);
        }
    }

    /**
     * Test calculation of distribution function for extreme input values.
     */
    @Test
    public void testDistributionFunctionExtremeValues() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double actual = distribution.probabilityDensityFunction(Double.NEGATIVE_INFINITY);
        assertEquals(0.0, actual, 0.0);

        actual = distribution.probabilityDensityFunction(Double.POSITIVE_INFINITY);
        assertEquals(0.0, actual, 0.0);
    }

    /**
     * Test calculation of moments.
     */
    @Test
    public void testMoments() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double mean = distribution.getMean();
        double stdDev = distribution.getStandardDeviation();

        double expectedMean = mean;
        double expectedVariance = stdDev * stdDev;
        double expectedSkewness = 0.0;
        double expectedKurtosis = 3.0;

        double actualMean = distribution.moment(1.0);
        assertEquals(expectedMean, actualMean, 0.001);

        double actualVariance = distribution.moment(2.0);
        assertEquals(expectedVariance, actualVariance, 0.001);

        double actualSkewness = distribution.moment(3.0);
        assertEquals(expectedSkewness, actualSkewness, 0.001);

        double actualKurtosis = distribution.moment(4.0);
        assertEquals(expectedKurtosis, actualKurtosis, 0.001);
    }

    /**
     * Test calculation of moments for non-standard distributions.
     */
    @Test
    public void testNonStandardMoments() {
        NormalDistribution distribution = new NormalDistributionImpl(3.0, 2.0);
        double mean = distribution.getMean();
        double stdDev = distribution.getStandardDeviation();

        double expectedMean = mean;
        double expectedVariance = stdDev * stdDev;
        double expectedSkewness = -1.2;
        double expectedKurtosis = 6.24;

        double actualMean = distribution.moment(1.0);
        assertEquals(expectedMean, actualMean, 0.001);

        double actualVariance = distribution.moment(2.0);
        assertEquals(expectedVariance, actualVariance, 0.001);

        double actualSkewness = distribution.moment(3.0);
        assertEquals(expectedSkewness, actualSkewness, 0.001);

        double actualKurtosis = distribution.moment(4.0);
        assertEquals(expectedKurtosis, actualKurtosis, 0.001);
    }
}