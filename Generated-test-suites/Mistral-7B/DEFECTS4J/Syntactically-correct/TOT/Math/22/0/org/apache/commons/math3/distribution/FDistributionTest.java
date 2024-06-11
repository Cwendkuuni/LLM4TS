package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest extends AbstractRealDistributionTest {

    /**
     * Tests the construction of an F distribution with valid degrees of freedom.
     */
    @Test
    public void testFDistributionConstructionValidDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(distribution);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the construction of an F distribution with invalid degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructionInvalidDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 0.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Tests the creation of an F distribution using a random number generator.
     */
    @Test
    public void testFDistributionCreationWithRandomNumberGenerator() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final RandomGenerator rng = new Well19937c();
        FDistribution distribution = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(distribution);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the density method of the F distribution.
     */
    @Test
    public void testFDensity() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        final double expected = FastMath.exp(3.0 * Math.log(2.0) + 1.5 * Math.log(3.0) -
                Math.log(2.0) + 1.5 * Math.log(5.0) - 1.5 * Math.log(7.0) - 1.5 * Math.log(7.0) -
                Beta.logBeta(1.5, 1.5));

        double result = distribution.density(2.0);
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Tests the cumulative probability method of the F distribution.
     */
    @Test
    public void testFCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        final double expected = Beta.regularizedBeta((3.0 * 2.0) / (5.0 + 3.0 * 2.0), 1.5, 1.5);

        double result = distribution.cumulativeProbability(0.5);
        assertEquals(expected, result, 1e-12);

        result = distribution.cumulativeProbability(1.0);
        assertEquals(expected + 0.156249568596896, result, 1e-12);
    }

    /**
     * Tests the inverse cumulative probability method of the F distribution.
     */
    @Test
    public void testFInverseCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        final double inverseCumAbsoluteAccuracy = 1e-6;

        double result = distribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, result, inverseCumAbsoluteAccuracy);

        result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.5, result, inverseCumAbsoluteAccuracy);

        result = distribution.inverseCumulativeProbability(1.0);
        assertEquals(1.0, result, inverseCumAbsoluteAccuracy);
    }

    /**
     * Tests the mean method of the F distribution.
     */
    @Test
    public void testFMean() {
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistribution(3.0, denominatorDegreesOfFreedom);
        double expected = denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2);
        double result = distribution.getNumericalMean();
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Tests the variance method of the F distribution.
     */
    @Test
    public void testFVariance() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        final double expected = (2.0 * Math.pow(5.0, 2.0) * (3.0 + 5.0 - 2.0)) /
                (3.0 * Math.pow(5.0 - 2.0, 2.0) * (5.0 - 4.0));
        double result = distribution.getNumericalVariance();
        assertEquals(expected, result, 1e-12);
    }
}