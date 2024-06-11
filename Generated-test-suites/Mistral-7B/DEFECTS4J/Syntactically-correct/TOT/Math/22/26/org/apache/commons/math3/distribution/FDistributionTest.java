package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Tests creation of F distribution.
     */
    @Test
    public void testCreation() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom);
    }

    /**
     * Tests creation of F distribution with custom inverse cumulative probability accuracy.
     */
    @Test
    public void testCreationWithCustomInverseCumAccuracy() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final double inverseCumAccuracy = 1e-7;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom,
                                                      inverseCumAccuracy);
    }

    /**
     * Tests creation of F distribution with random number generator.
     */
    @Test
    public void testCreationWithRandomGenerator() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final double inverseCumAccuracy = 1e-7;
        final Well19937c rng = new Well19937c();
        final FDistribution fDist = new FDistribution(rng,
                                                      numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom,
                                                      inverseCumAccuracy);
    }

    /**
     * Tests error handling when creating F distribution with invalid degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testErrorHandlingCreationWithNegativeDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = -1.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Tests methods for numerator degrees of freedom.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 3.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, 5.0);
        assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests methods for denominator degrees of freedom.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(3.0, denominatorDegreesOfFreedom);
        assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests methods for inverse cumulative probability accuracy.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        final double inverseCumAccuracy = 1e-7;
        final FDistribution fDist = new FDistribution(3.0, 5.0, inverseCumAccuracy);
        assertEquals(inverseCumAccuracy, fDist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Tests method for numerical mean.
     */
    @Test
    public void testGetNumericalMean() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);
        assertEquals(5.0 / 2.0, fDist.getNumericalMean(), 0.0);
        final FDistribution fDist2 = new FDistribution(2.0, 4.0);
        // NaN since the variance is undefined for this case
        assertEquals(Double.NaN, fDist2.getNumericalMean(), 0.0);
    }

    /**
     * Tests method for numerical variance.
     */
    @Test
    public void testGetNumericalVariance() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);
        final double expected = (2 * (7.0 * 7.0) * (5.0 + 7.0 - 2)) /
                               ((5.0 * (7.0 - 2.0) * (7.0 - 4.0)) );
        assertEquals(expected, fDist.getNumericalVariance(), 0.0);
        final FDistribution fDist2 = new FDistribution(2.0, 4.0);
        assertEquals(Double.NaN, fDist2.getNumericalVariance(), 0.0);
    }

    /**
     * Tests method for density.
     */
    @Test
    public void testDensity() {
        final double x = 1.0;
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom);
        final double expectedDensity = 0.01136083318835267;
        assertEquals(expectedDensity, fDist.density(x), 0.0);
    }

    /**
     * Tests method for cumulative probability.
     */
    @Test
    public void testCumulativeProbability() {
        final double x = 1.0;
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom);
        final double expectedCumProb = 0.1599234622947457;
        assertEquals(expectedCumProb, fDist.cumulativeProbability(x), 0.0);
    }

    /**
     * Tests method for inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() {
        final double p = 0.95;
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom);
        final double expectedX = 2.937123212878403;
        assertEquals(expectedX, fDist.inverseCumulativeProbability(p), 0.0);
    }

    /**
     * Tests method for support lower and upper bounds.
     */
    @Test
    public void testSupportBounds() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                                                      denominatorDegreesOfFreedom);
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertTrue(fDist.isSupportUpperBoundInclusive());
        assertTrue(fDist.isSupportConnected());
        assertEquals(0.0, fDist.getSupportLowerBound());
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound());
    }
}