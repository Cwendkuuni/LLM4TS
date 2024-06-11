package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        try {
            FDistribution dist = new FDistribution(2.0, 3.0);
            assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution dist = new FDistribution(new Well19937c(), 2.0, 3.0);
            assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getNumericalMean().
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double mean = dist.getNumericalMean();
        assertEquals(3.125, mean, 0.001);
    }

    /**
     * Test getNumericalVariance().
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double variance = dist.getNumericalVariance();
        assertEquals(1.8202922483476336, variance, 0.001);
    }

    /**
     * Test getNumericalVariance when denominator degrees of freedom is 2.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNumericalVarianceDenominatorDegreesOfFreedomTwo() {
        FDistribution dist = new FDistribution(3.0, 2.0);
        dist.getNumericalVariance();
    }

    /**
     * Test density().
     */
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double x = 2.0;
        double expected = 0.12130673831732296;
        double result = dist.density(x);
        assertEquals(expected, result, 0.00000001);
    }

    /**
     * Test density for negative x.
     */
    @Test
    public void testDensityNegativeX() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double x = -1.0;
        double result = dist.density(x);
        assertEquals(0.0, result, 0.0);
    }

    /**
     * Test cumulativeProbability().
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double x = 1.5;
        double expected = 0.5383761471613536;
        double result = dist.cumulativeProbability(x);
        assertEquals(expected, result, 0.00000001);
    }

    /**
     * Test cumulativeProbability for x < 0.
     */
    @Test
    public void testCumulativeProbabilityNegativeX() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double x = -1.0;
        double result = dist.cumulativeProbability(x);
        assertEquals(0.0, result, 0.0);
    }

    /**
     * Test cumulativeProbability for x > supportUpperBound.
     */
    @Test
    public void testCumulativeProbabilityUpperBound() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double x = Double.POSITIVE_INFINITY;
        double result = dist.cumulativeProbability(x);
        assertEquals(1.0, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability().
     */
    @Test
    public void testInverseCumulativeProbability() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double p = 0.75;
        double expected = 1.927268286468365;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.00000001);
    }

    /**
     * Test inverseCumulativeProbability for p < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityNegativeP() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double p = -0.1;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test inverseCumulativeProbability for p > 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityGreaterThanOne() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double p = 1.1;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test getSolverAbsoluteAccuracy().
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double solverAbsoluteAccuracy = dist.getSolverAbsoluteAccuracy();
        assertEquals(1e-9, solverAbsoluteAccuracy, 0.0);
    }

    /**
     * Test getSupportLowerBound().
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double lowerBound = dist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.0);
    }

    /**
     * Test getSupportUpperBound().
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double upperBound = dist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive().
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        boolean inclusive = dist.isSupportLowerBoundInclusive();
        assertTrue(inclusive);
    }

    /**
     * Test isSupportUpperBoundInclusive().
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        boolean inclusive = dist.isSupportUpperBoundInclusive();
        assertFalse(inclusive);
    }

    /**
     * Test isSupportConnected().
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        boolean connected = dist.isSupportConnected();
        assertTrue(connected);
    }
}