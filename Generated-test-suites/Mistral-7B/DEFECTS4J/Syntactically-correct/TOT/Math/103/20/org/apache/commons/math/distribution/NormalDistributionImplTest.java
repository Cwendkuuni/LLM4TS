package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with specified mean and standard deviation.
     */
    @Test
    public void testConstructorWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 3.5);
        assertEquals(2.5, distribution.getMean(), 0.0);
        assertEquals(3.5, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test getMean method.
     */
    @Test
    public void testGetMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(1.2);
        assertEquals(1.2, distribution.getMean(), 0.0);
    }

    /**
     * Test setMean method with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanWithNegativeValue() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(-1.5);
    }

    /**
     * Test getStandardDeviation method.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.7);
        assertEquals(2.7, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test setStandardDeviation method with non-positive value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNonPositiveValue() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.5);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        double result;

        result = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, result, 0.0001);

        distribution.setMean(1.0);
        distribution.setStandardDeviation(1.5);

        result = distribution.cumulativeProbability(1.0);
        assertEquals(0.841344746, result, 0.0001);

        result = distribution.cumulativeProbability(2.5);
        assertTrue(result > 0.999);

        result = distribution.cumulativeProbability(-1.0);
        assertTrue(result < 0.001);
    }

    /**
     * Test inverseCumulativeProbability method with valid probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidProbabilities()
            throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        double result;

        distribution.setMean(1.0);
        distribution.setStandardDeviation(1.5);

        result = distribution.inverseCumulativeProbability(0.1);
        assertEquals(-1.051622121, result, 0.01);

        result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.0);

        result = distribution.inverseCumulativeProbability(0.9);
        assertEquals(2.041622121, result, 0.01);
    }

    /**
     * Test inverseCumulativeProbability method with invalid probabilities.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbabilities()
            throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        distribution.setMean(1.0);
        distribution.setStandardDeviation(1.5);

        distribution.inverseCumulativeProbability(0.0);

        distribution.inverseCumulativeProbability(1.2);
    }
}