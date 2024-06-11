package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Tests the constructor.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @org.junit.Test
    public void testConstructor() throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the density method.
     */
    @org.junit.Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double x = 1.5;
        double density = fDistribution.density(x);
        double expectedDensity = FastMath.exp(3.0 * FastMath.log(x) -
                                              FastMath.log(x) +
                                              5.0 * FastMath.log(5.0) -
                                              3.0 * FastMath.log(5.0 * x + 3.0 * x) -
                                              5.0 * FastMath.log(5.0 * x + 3.0 * x) -
                                              Beta.logBeta(3.0, 5.0));
        assertEquals(expectedDensity, density, 1e-7);
    }

    /**
     * Tests the cumulativeProbability method.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double x = 1.5;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        double expectedCumulativeProbability = Beta.regularizedBeta(1.5 * 3.0 / (5.0 + 1.5 * 3.0), 1.5, 1.5);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-7);
    }

    /**
     * Tests the getNumeratorDegreesOfFreedom and getDenominatorDegreesOfFreedom methods.
     */
    @org.junit.Test
    public void testGetNumeratorDegreesOfFreedomAndGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the getSolverAbsoluteAccuracy method.
     */
    @org.junit.Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, fDistribution.getSolverAbsoluteAccuracy(), 1e-15);
    }

    /**
     * Tests the getNumericalMean method.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double numericalMean = fDistribution.getNumericalMean();
        assertTrue(Double.isNaN(numericalMean));
    }

    /**
     * Tests the getNumericalVariance method.
     */
    @org.junit.Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double numericalVariance = fDistribution.getNumericalVariance();
        assertTrue(Double.isNaN(numericalVariance));
    }

    /**
     * Tests the getSupportLowerBound, getSupportUpperBound, isSupportLowerBoundInclusive,
     * and isSupportUpperBoundInclusive methods.
     */
    @org.junit.Test
    public void testGetSupportBoundsAndIsInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound());
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    /**
     * Tests the isSupportConnected method.
     */
    @org.junit.Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertTrue(fDistribution.isSupportConnected());
    }

    /**
     * Tests the exception thrown when creating a new instance with non-positive degrees of freedom.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws NotStrictlyPositiveException {
        new FDistribution(-3.0, 5.0);
    }

    /**
     * Tests the exception thrown when creating a new instance with non-positive denominator degrees of freedom.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionDenominator() throws NotStrictlyPositiveException {
        new FDistribution(3.0, -5.0);
    }

    /**
     * Tests the generation of random numbers.
     */
    @org.junit.Test
    public void testRandomNumbers() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 10; i++) {
            double x = fDistribution.inverseCumulativeProbability(rng.nextDouble());
            double y = fDistribution.cumulativeProbability(x);
            assertTrue(y <= x);
        }
    }
}