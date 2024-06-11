package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    private FDistribution distribution;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        Well19937c rng = new Well19937c();
        distribution = new FDistribution(rng, 3, 5);
    }

    @Test
    public void testProbabilityDensityFunction() {
        double density = distribution.density(1.5);
        assertEquals(0.1984234783843315, density, 0.00001);

        density = distribution.density(0.5);
        assertEquals(0.02951476547642931, density, 0.00001);

        density = distribution.density(0.1);
        assertEquals(0.004510316134164745, density, 0.00001);

        density = distribution.density(100.0);
        assertEquals(1.178284664364417e-23, density, 0.00001);
    }

    @Test
    public void testCumulativeDistributionFunction() {
        double cumulativeProbability = distribution.cumulativeProbability(0.25);
        assertEquals(0.10943058959526538, cumulativeProbability, 0.00001);

        cumulativeProbability = distribution.cumulativeProbability(2.5);
        assertEquals(0.9993613852172774, cumulativeProbability, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeDistributionFunctionNegativeArgument() {
        distribution.cumulativeProbability(-1.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(3, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(5, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 1e-12);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(3.0 / 2.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        assertEquals(13.120412488671137, variance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}