package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for {@code cumulativeProbability} method with valid inputs.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test with probability 0
        double p = 0.0;
        double expected = 0.0;
        double result = distribution.cumulativeProbability(p);
        assertEquals(expected, result, 1e-10);

        // Test with probability 0.5
        p = 0.5;
        expected = 0.32699166358565215;
        result = distribution.cumulativeProbability(p);
        assertEquals(expected, result, 1e-6);

        // Test with probability 0.95
        p = 0.95;
        expected = 0.994450396232727;
        result = distribution.cumulativeProbability(p);
        assertEquals(expected, result, 1e-6);

        // Test with probability 1
        p = 1.0;
        expected = 1.0;
        result = distribution.cumulativeProbability(p);
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test for {@code inverseCumulativeProbability} method with valid inputs.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test with probability 0
        double p = 0.0;
        double expected = 0.0;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 1e-10);

        // Test with probability 0.5
        p = 0.5;
        expected = 1.4587745457682316;
        result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 1e-6);

        // Test with probability 0.95
        p = 0.95;
        expected = 2.540204920627585;
        result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 1e-6);

        // Test with probability 1
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result);
    }

    /**
     * Test for {@code cumulativeProbability} method with invalid inputs.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test with negative probability
        double p = -1.0;
        distribution.cumulativeProbability(p);
    }

    /**
     * Test for {@code inverseCumulativeProbability} method with invalid inputs.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test with probability < 0
        double p = -1.0;
        distribution.inverseCumulativeProbability(p);

        // Test with probability > 1
        p = 2.0;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test for {@code setNumeratorDegreesOfFreedom} method.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test with valid input
        double degreesOfFreedom = 7.0;
        distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);

        // Test with invalid input
        degreesOfFreedom = -2.0;
        assertThrows(IllegalArgumentException.class,
                () -> distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom));
    }

    /**
     * Test for {@code setDenominatorDegreesOfFreedom} method.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test with valid input
        double degreesOfFreedom = 12.0;
        distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        // Test with invalid input
        degreesOfFreedom = -3.0;
        assertThrows(IllegalArgumentException.class,
                () -> distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom));
    }

    /**
     * Test for distribution properties using statistics.
     */
    @Test
    public void testDistributionProperties() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);

        // Test mean
        double expectedMean = 5.0 / (5.0 - 2.0) * (5.0 / (5.0 - 2.0) + 1.0 / (10.0 - 2.0));
        double mean = StatUtils.mean(10000, distribution::cumulativeProbability, 0.5);
        assertEquals(expectedMean, mean, 1e-6);

        // Test variance
        double expectedVariance = 5.0 / (5.0 - 2.0) * (5.0 / (5.0 - 2.0) * 5.0 / (5.0 - 2.0)
                + 1.0 / (10.0 - 2.0) * 1.0 / (10.0 - 2.0) - (expectedMean * expectedMean));
        double variance = StatUtils.variance(10000, distribution::cumulativeProbability, 0.5);
        assertEquals(expectedVariance, variance, 1e-6);

        // Test standard deviation
        double standardDeviation = Math.sqrt(expectedVariance);
        double resultStandardDeviation = StatUtils.standardDeviation(10000, distribution::cumulativeProbability, 0.5);
        assertEquals(standardDeviation, resultStandardDeviation, 1e-6);
    }
}