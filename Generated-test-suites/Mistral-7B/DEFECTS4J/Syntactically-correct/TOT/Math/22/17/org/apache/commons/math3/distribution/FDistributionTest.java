package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Tests for the {@link FDistribution} class.
 */
public class FDistributionTest {
    /**
     * Tests creation of an F distribution with valid arguments.
     */
    @Test
    public void testCreation() {
        FDistribution fd = new FDistribution(1.0, 2.0);
    }

    /**
     * Tests creation of an F distribution with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidCreation() {
        new FDistribution(-1.0, 2.0);
    }

    /**
     * Tests the density function.
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        RealDistribution rd = new FDistribution(1.0, 2.0);
        double[] xValues = { 0.0, 0.5, 1.0, 2.0, 5.0 };
        for (double x : xValues) {
            double densityExpected = rd.density(x);
            double densityActual = fd.density(x);
            assertEquals(densityExpected, densityActual, 1e-6);
        }
    }

    /**
     * Tests the cumulative probability function.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        RealDistribution rd = new FDistribution(1.0, 2.0);
        double[] xValues = { 0.0, 0.1, 0.5, 1.0, 2.0, 5.0 };
        for (double x : xValues) {
            double cdfExpected = rd.cumulativeProbability(x);
            double cdfActual = fd.cumulativeProbability(x);
            assertEquals(cdfExpected, cdfActual, 1e-6);
        }
    }

    /**
     * Tests the getNumeratorDegreesOfFreedom() method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Tests the getDenominatorDegreesOfFreedom() method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Tests the getSolverAbsoluteAccuracy() method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
                fd.getSolverAbsoluteAccuracy(), 1e-14);
    }

    /**
     * Tests the getNumericalMean() method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double numericalMean = fd.getNumericalMean();
        assertEquals(1.5, numericalMean, 1e-6);
    }

    /**
     * Tests the getNumericalVariance() method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double numericalVariance = fd.getNumericalVariance();
        double expectedVariance = 1.5 / (2.0 - 2);
        assertEquals(expectedVariance, numericalVariance, 1e-6);
    }

    /**
     * Tests the getSupportLowerBound() method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 1e-14);
    }

    /**
     * Tests the getSupportUpperBound() method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 1e-14);
    }

    /**
     * Tests the isSupportLowerBoundInclusive() method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Tests the isSupportUpperBoundInclusive() method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Tests the isSupportConnected() method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        assertTrue(fd.isSupportConnected());
    }

    /**
     * Tests creation of an F distribution using a {@link Well19937c} random number generator.
     */
    @Test
    public void testCreationWithRandomGenerator() {
        FDistribution fd = new FDistribution(new Well19937c(), 1.0, 2.0);
    }
}