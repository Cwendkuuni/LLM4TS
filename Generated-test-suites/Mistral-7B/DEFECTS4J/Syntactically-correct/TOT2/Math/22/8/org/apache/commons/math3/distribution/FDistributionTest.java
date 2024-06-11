package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for FDistribution.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertTrue(distribution instanceof FDistribution);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid numerator degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeNumerator() {
        new FDistribution(-1.0, 4.0);
    }

    /**
     * Test constructor with invalid denominator degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDenominator() {
        new FDistribution(3.0, -1.0);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double density = distribution.density(1.5);
        assertEquals(3.777752089886326E-5, density, 1e-9);

        distribution = new FDistribution(7.0, 3.0);
        density = distribution.density(2.5);
        assertEquals(1.137348319801797E-6, density, 1e-9);
    }

    /**
     * Test the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double prob = distribution.cumulativeProbability(0.5);
        assertEquals(0.377964625744285, prob, 1e-9);

        distribution = new FDistribution(2.0, 2.0);
        prob = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, prob, 1e-9);

        distribution = new FDistribution(3.0, 3.0);
        prob = distribution.cumulativeProbability(1.0);
        assertEquals(0.851140258126838, prob, 1e-9);
    }

    /**
     * Test the getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 1e-9);
    }

    /**
     * Test the getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(7.0, 4.0);
        assertEquals(3.5, distribution.getNumericalMean(), 1e-9);
    }

    /**
     * Test the getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(7.0, 4.0);
        assertEquals(11.32419253858334, distribution.getNumericalVariance(), 1e-9);
    }

    /**
     * Test the getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 1e-9);
    }

    /**
     * Test the getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 1e-9);
    }

    /**
     * Test the isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test the isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test the isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertTrue(distribution.isSupportConnected());
    }
}