package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link FDistribution} class.
 */
public class FDistributionTest {

    private FDistribution fd;

    /**
     * Sets up the test by creating a new instance of FDistribution.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Before
    public void setUp() throws NotStrictlyPositiveException {
        RandomGenerator rng = new Well19937c();
        fd = new FDistribution(rng, 3.0, 5.0);
    }

    /**
     * Tests density method.
     */
    @Test
    public void testDensity() {
        double value = 1.5;
        double expected = 0.07236957842134058;
        assertEquals(expected, fd.density(value), 1e-4);
    }

    /**
     * Tests cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        double value = 1.5;
        double expected = 0.8426127493262192;
        assertEquals(expected, fd.cumulativeProbability(value), 1e-6);
    }

    /**
     * Tests getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expected = 3.0;
        assertEquals(expected, fd.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    /**
     * Tests getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expected = 5.0;
        assertEquals(expected, fd.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    /**
     * Tests getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        double expected = 3.0 / 2.0;
        assertEquals(expected, fd.getNumericalMean(), 1e-9);
    }

    /**
     * Tests getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        double expected = 4.5 / 12.0;
        assertEquals(expected, fd.getNumericalVariance(), 1e-9);
    }

    /**
     * Tests getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        double expected = 0.0;
        assertEquals(expected, fd.getSupportLowerBound(), 1e-9);
    }

    /**
     * Tests getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, fd.getSupportUpperBound(), 1e-9);
    }

    /**
     * Tests isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean expected = true;
        assertEquals(expected, fd.isSupportLowerBoundInclusive());
    }

    /**
     * Tests isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean expected = false;
        assertEquals(expected, fd.isSupportUpperBoundInclusive());
    }

    /**
     * Tests isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        boolean expected = true;
        assertEquals(expected, fd.isSupportConnected());
    }

    /**
     * Tests constructor with invalid parameters.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() throws NotStrictlyPositiveException {
        RandomGenerator rng = new Well19937c();
        FDistribution fdNegativeNumDegreesOfFreedom = new FDistribution(rng, -1.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction2() throws NotStrictlyPositiveException {
        RandomGenerator rng = new Well19937c();
        FDistribution fdZeroDenominatorDegreesOfFreedom = new FDistribution(rng, 3.0, 0.0);
    }
}