package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.5, 2.0);
        assertNotNull(dist);
        assertEquals(1.5, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with mean=0 and stdDev=1.
     */
    @Test
    public void testDefaultConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertNotNull(dist);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test mean getter and setter.
     */
    @Test
    public void testMeanGetterSetter() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        dist.setMean(3.5);
        assertEquals(3.5, dist.getMean(), 0.0);
    }

    /**
     * Test standard deviation getter and setter.
     */
    @Test
    public void testStandardDeviationGetterSetter() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        dist.setStandardDeviation(2.5);
        assertEquals(2.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid argument.
     */
    @Test
    public void testCumulativeProbabilityValidArgument() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = dist.cumulativeProbability(0.5);
        assertEquals(0.682689462137, p, 0.0001);
    }

    /**
     * Test cumulativeProbability method with argument far from mean.
     */
    @Test
    public void testCumulativeProbabilityArgumentFarFromMean() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = dist.cumulativeProbability(10.0);
        assertEquals(0.5, p, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbabilityValidArguments() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.5;
        double expectedValue = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, expectedValue, 0.0001);

        p = 0.841344746; // Approximately the 84th percentile.
        expectedValue = dist.inverseCumulativeProbability(p);
        assertEquals(1.058183015, expectedValue, 0.0001);

        p = 0.15865525; // Approximately the 15.87th percentile.
        expectedValue = dist.inverseCumulativeProbability(p);
        assertEquals(-1.061496357, expectedValue, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method with invalid arguments.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidArguments() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverseCumulativeProbability method with p=0.
     */
    @Test
    public void testInverseCumulativeProbabilityPZero() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.0;
        double expectedValue = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, expectedValue, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with p=1.
     */
    @Test
    public void testInverseCumulativeProbabilityPLimit() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 1.0;
        double expectedValue = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, expectedValue, 0.0);
    }

    /**
     * Test Erf method used by cumulativeProbability method.
     */
    @Test
    public void testErfMethod() {
        Erf erf = new Erf();
        double x = 0.0;
        double expectedResult = 0.5;
        double result = erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0)));
        assertEquals(expectedResult, result, 0.0001);
    }
}