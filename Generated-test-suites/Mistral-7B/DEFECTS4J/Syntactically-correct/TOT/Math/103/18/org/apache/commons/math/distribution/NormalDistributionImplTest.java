package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

/**
 * Test for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid mean and standard deviation.
     */
    @Test
    public void testValidConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 3.5);
        assertEquals("Mean not initialized correctly", 2.5, dist.getMean(), 0.001);
        assertEquals("Standard deviation not initialized correctly", 3.5, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with mean equals to zero and standard deviation equals to one.
     */
    @Test
    public void testDefaultConstruction() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals("Mean not initialized correctly", 0.0, dist.getMean(), 0.001);
        assertEquals("Standard deviation not initialized correctly", 1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test method getMean.
     */
    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Mean not initialized correctly", 0.0, dist.getMean(), 0.001);
        NormalDistribution dist1 = new NormalDistributionImpl(1.0, 1.0);
        assertEquals("Mean not initialized correctly", 1.0, dist1.getMean(), 0.001);
    }

    /**
     * Test method setMean.
     */
    @Test
    public void testSetMean() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(3.5);
        assertEquals("Mean not set correctly", 3.5, dist.getMean(), 0.001);
    }

    /**
     * Test method getStandardDeviation.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Standard deviation not initialized correctly", 1.0, dist.getStandardDeviation(), 0.001);
        NormalDistribution dist1 = new NormalDistributionImpl(0.0, 2.0);
        assertEquals("Standard deviation not initialized correctly", 2.0, dist1.getStandardDeviation(), 0.001);
    }

    /**
     * Test method setStandardDeviation.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(3.5);
        assertEquals("Standard deviation not set correctly", 3.5, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test method cumulativeProbability.
     * @throws MathException if the algorithm fails to converge.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Probability of negative value not correct", 0.0, dist.cumulativeProbability(-1.0), 0.001);
        assertEquals("Probability of mean not correct", 0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals("Probability of positive value not correct", 0.9999999, dist.cumulativeProbability(3.5), 0.001);
        NormalDistribution dist1 = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Probability of negative value not correct", 0.0, dist1.cumulativeProbability(-5.0), 0.001);
        assertEquals("Probability of mean not correct", 0.5, dist1.cumulativeProbability(1.0), 0.001);
        assertEquals("Probability of positive value not correct", 0.9999999, dist1.cumulativeProbability(5.0), 0.001);
    }

    /**
     * Test method inverseCumulativeProbability.
     * @throws MathException if the inverse cumulative probability can not be computed due to convergence or other numerical errors.
     * @throws IllegalArgumentException if p is not a valid probability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, IllegalArgumentException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Inverse cumulative probability of 0.0 not correct", -1.2589254, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals("Inverse cumulative probability of 0.5 not correct", 0.0, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals("Inverse cumulative probability of 0.9 not correct", 1.2589254, dist.inverseCumulativeProbability(0.9), 0.001);
        NormalDistribution dist1 = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Inverse cumulative probability of 0.0 not correct", -3.4926425, dist1.inverseCumulativeProbability(0.0), 0.001);
        assertEquals("Inverse cumulative probability of 0.5 not correct", 1.0, dist1.inverseCumulativeProbability(0.5), 0.001);
        assertEquals("Inverse cumulative probability of 0.95 not correct", 3.4926425, dist1.inverseCumulativeProbability(0.95), 0.001);
    }

    /**
     * Test method for boundary cases.
     */
    @Test
    public void testBoundaryCases() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        try {
            dist.setStandardDeviation(-1.0);
            fail("Standard deviation must be positive.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        NormalDistribution dist1 = new NormalDistributionImpl(0.0, 0.0);
        try {
            dist1.cumulativeProbability(0.0);
            fail("Probability of mean must not be 0.0.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method for zero variance.
     */
    @Test
    public void testZeroVariance() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 0.0);
        try {
            dist.cumulativeProbability(0.0);
            fail("Probability of mean must not be 0.0.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method for large values.
     */
    @Test
    public void testLargeValues() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        NormalDistribution dist1 = new NormalDistributionImpl(mean + 20 * stdDev, stdDev);
        double prob = dist1.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertTrue("Probability must be 1.0 for large positive values", prob > 0.9999999);
        NormalDistribution dist2 = new NormalDistributionImpl(mean - 20 * stdDev, stdDev);
        prob = dist2.cumulativeProbability(Double.NEGATIVE_INFINITY);
        assertTrue("Probability must be 0.0 for large negative values", prob < 0.0000001);
    }

    /**
     * Test method for standard deviation larger than mean.
     */
    @Test
    public void testStdDevLargerThanMean() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 2.0);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean + 3 * stdDev;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be less than 0.5 for standard deviation larger than mean", prob < 0.5);
    }

    /**
     * Test method for standard deviation smaller than mean.
     */
    @Test
    public void testStdDevSmallerThanMean() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.5);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean - 1.5 * stdDev;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be greater than 0.5 for standard deviation smaller than mean", prob > 0.5);
    }

    /**
     * Test method for mean and standard deviation equal to zero.
     */
    @Test
    public void testMeanAndStandardDeviationZero() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 0.0);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be 0.5 for mean and standard deviation equal to zero", prob == 0.5);
    }

    /**
     * Test method for mean and standard deviation equal.
     */
    @Test
    public void testMeanAndStandardDeviationEqual() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 2.5);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be 0.5 for mean and standard deviation equal", prob == 0.5);
    }

    /**
     * Test method for mean and standard deviation not equal.
     */
    @Test
    public void testMeanAndStandardDeviationNotEqual() {
        NormalDistribution dist = new NormalDistributionImpl(1.5, 2.5);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean + 1.5 * stdDev;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be less than 0.5 for mean and standard deviation not equal", prob < 0.5);
    }

    /**
     * Test method for negative mean and standard deviation.
     */
    @Test
    public void testNegativeMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(-1.0, 1.0);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean + stdDev;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be greater than 0.5 for negative mean and standard deviation", prob > 0.5);
    }

    /**
     * Test method for positive mean and negative standard deviation.
     */
    @Test
    public void testPositiveMeanAndNegativeStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, -1.0);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double x = mean - stdDev;
        double prob = dist.cumulativeProbability(x);
        assertTrue("Probability must be less than 0.5 for positive mean and negative standard deviation", prob < 0.5);
    }

    /**
     * Test method for mean equals to expected value.
     */
    @Test
    public void testMeanEqualsToExpectedValue() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 1.0);
        Double[] values = new Double[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.inverseCumulativeProbability(Math.random());
        }
        StandardDeviation sd = new StandardDeviation();
        double expectedValue = dist.getMean();
        double actualValue = sd.evaluate(values);
        assertEquals("Expected value does not match actual value", expectedValue, actualValue, 0.01);
    }
}