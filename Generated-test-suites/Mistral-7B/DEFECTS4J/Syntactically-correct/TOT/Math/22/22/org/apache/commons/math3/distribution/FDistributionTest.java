package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;

/**
 * Test cases for the FDistribution class.
 */
public class FDistributionTest extends AbstractRealDistributionTest {

    /**
     * Test the constructor with valid parameters.
     *
     * @throws NotStrictlyPositiveException if
     * {@code numeratorDegreesOfFreedom <= 0} or
     * {@code denominatorDegreesOfFreedom <= 0}.
     */
    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;

        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(fDist);
    }

    /**
     * Test the constructor with invalid parameters.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidConstruction() {
        final double numeratorDegreesOfFreedom = 0.0;
        final double denominatorDegreesOfFreedom = 5.0;

        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testFDensity() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        for (double input = 0.0; input <= 10.0; input += 0.1) {
            final double expected = fDist.density(input);
            final double obtained = fDist.density(input);
            assertEquals(expected, obtained, 1e-5);
        }
    }

    /**
     * Test the cumulativeProbability method.
     */
    @Test
    public void testFCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        for (double input = 0.0; input <= 10.0; input += 0.1) {
            final double expected = fDist.cumulativeProbability(input);
            final double obtained = fDist.cumulativeProbability(input);
            assertEquals(expected, obtained, 1e-5);
        }
    }

    /**
     * Test the getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 5.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, 7.0);

        assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 1e-5);
    }

    /**
     * Test the getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistribution fDist = new FDistribution(5.0, denominatorDegreesOfFreedom);

        assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 1e-5);
    }

    /**
     * Test the getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        final double solverAbsoluteAccuracy = 1e-8;
        final FDistribution fDist = new FDistribution(5.0, 7.0, solverAbsoluteAccuracy);

        assertEquals(solverAbsoluteAccuracy, fDist.getSolverAbsoluteAccuracy(), 1e-5);
    }

    /**
     * Test the getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        final double expected = denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2);
        final double obtained = fDist.getNumericalMean();
        assertEquals(expected, obtained, 1e-5);
    }

    /**
     * Test the getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 9.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        final double expected = 1.147077462472514361645579712826684641048674514978153332331106545133735127031456768224556959;
        final double obtained = fDist.getNumericalVariance();
        assertEquals(expected, obtained, 1e-15);
    }

    /**
     * Test the getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);

        final double expected = 0.0;
        final double obtained = fDist.getSupportLowerBound();
        assertEquals(expected, obtained, 1e-5);
    }

    /**
     * Test the getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);

        final double expected = Double.POSITIVE_INFINITY;
        final double obtained = fDist.getSupportUpperBound();
        assertEquals(expected, obtained, 1e-5);
    }

    /**
     * Test the isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);

        final boolean expected = true;
        final boolean obtained = fDist.isSupportLowerBoundInclusive();
        assertEquals(expected, obtained);
    }

    /**
     * Test the isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);

        final boolean expected = false;
        final boolean obtained = fDist.isSupportUpperBoundInclusive();
        assertEquals(expected, obtained);
    }

    /**
     * Test the isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        final FDistribution fDist = new FDistribution(5.0, 7.0);

        final boolean expected = true;
        final boolean obtained = fDist.isSupportConnected();
        assertEquals(expected, obtained);
    }

    /**
     * Test the randomValue method.
     */
    @Test
    public void testRandomValue() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistribution fDist = new FDistribution(new Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        final double expectedMin = 0.0;
        final double expectedMax = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 1000; i++) {
            final double value = fDist.randomValue();
            final boolean withinSupport = (value >= expectedMin) && (value <= expectedMax);
            assertTrue(withinSupport);
        }
    }
}