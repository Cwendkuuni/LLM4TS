package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.Statistics;
import org.junit.Test;

/**
 * Test cases for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 4.0;
        final FDistributionImpl distribution = new FDistributionImpl(
                numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 4.0;
        final FDistributionImpl distribution = new FDistributionImpl(
                numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test for edge cases
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 1e-10);
        assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 1e-10);

        // Test for typical values
        double x = 1.5;
        double p = distribution.cumulativeProbability(x);
        double expected = Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom);
        assertEquals(expected, p, 1e-10);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 4.0;
        final FDistributionImpl distribution = new FDistributionImpl(
                numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test for edge cases
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 1e-10);
        assertTrue(Double.isInfinite(distribution.inverseCumulativeProbability(1.0)));

        // Test for typical values
        double p = 0.75;
        double x = distribution.inverseCumulativeProbability(p);
        double expected = (denominatorDegreesOfFreedom * x) / (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom * x);
        double mean = (denominatorDegreesOfFreedom) / (denominatorDegreesOfFreedom - 2.0);
        double variance = (denominatorDegreesOfFreedom * (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom)) / ((denominatorDegreesOfFreedom - 2.0) * (denominatorDegreesOfFreedom - 3.0) * denominatorDegreesOfFreedom);
        double stddev = Statistics.standardDeviation(new double[] { mean, expected }, true);
        assertEquals(mean, x, variance * stddev);
    }
}