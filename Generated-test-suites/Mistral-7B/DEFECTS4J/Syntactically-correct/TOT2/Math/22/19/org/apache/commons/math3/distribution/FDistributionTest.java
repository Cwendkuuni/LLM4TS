package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionTest;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Tests for {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test constructor with correct arguments.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testConstruction() throws NotStrictlyPositiveException {
        FDistribution dist = new FDistribution(3, 5);
        assertEquals(3, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test constructor with incorrect argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-3, 5);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.density(1.5);
        assertTrue(result > 0);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.cumulativeProbability(1.5);
        result = dist.cumulativeProbability(2.5);
        assertTrue(result > result);
    }

    /**
     * Test numericalMean method.
     */
    @Test
    public void testNumericalMean() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.getNumericalMean();
        assertTrue(result > 0);
    }

    /**
     * Test numericalVariance method.
     */
    @Test
    public void testNumericalVariance() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.getNumericalVariance();
        assertTrue(result > 0);
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.getSolverAbsoluteAccuracy();
        assertTrue(result > 0);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.getSupportLowerBound();
        assertEquals(0.0, result, 0.0);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(3, 5);
        double result = dist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(3, 5);
        boolean result = dist.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(3, 5);
        boolean result = dist.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(3, 5);
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }

    /**
     * Test distribution with default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        FDistribution dist = new FDistribution();
        assertEquals(Double.NaN, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(Double.NaN, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(Double.NaN, dist.getNumericalVariance(), 0.0);
        assertEquals(Double.NaN, dist.getNumericalMean(), 0.0);
        assertEquals(Double.NaN, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test distribution with random generator constructor.
     */
    @Test
    public void testRandomGeneratorConstructor() {
        FDistribution dist = new FDistribution(new Well19937c(), 3, 5);
        assertEquals(3, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }
}