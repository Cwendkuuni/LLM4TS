package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests for the {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test construction with valid parameters.
     * @throws NotStrictlyPositiveException if construction fails
     */
    @org.junit.Test
    public void testConstruction() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);
    }

    /**
     * Test construction with invalid parameters.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        new FDistribution(-1.0, 2.5);
    }

    /**
     * Test the density method with valid parameters.
     */
    @org.junit.Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);

        final double expected = 0.03234131772165486;
        final double actual = distribution.density(1.5);
        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test the cumulative probability method with valid parameters.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);

        final double expected = 0.931636596257198;
        final double actual = distribution.cumulativeProbability(2.2);
        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test the cumulative probability method with edge cases.
     */
    @org.junit.Test
    public void testCumulativeProbabilityEdgeCases() {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);

        final double expectedZero = 0.0;
        final double actualZero = distribution.cumulativeProbability(0.0);
        assertEquals(expectedZero, actualZero, 1e-6);

        final double expectedOne = 1.0;
        final double actualOne = distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(expectedOne, actualOne, 1e-6);
    }

    /**
     * Test the numerical mean method with valid parameters.
     */
    @org.junit.Test
    public void testNumericalMean() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);

        final double expected = 3.0 / 2.0;
        final double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test the numerical mean method with invalid parameters.
     */
    @org.junit.Test
    public void testNumericalMeanInvalidParameters() {
        final FDistribution distribution = new FDistribution();
        final double actual = distribution.getNumericalMean();
        assertEquals(Double.NaN, actual);
    }

    /**
     * Test the numerical variance method with valid parameters.
     */
    @org.junit.Test
    public void testNumericalVariance() {
        final double numeratorDegreesOfFreedom = 4.0;
        final double denominatorDegreesOfFreedom = 6.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);

        final double expected = (2.0 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) *
                                 (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                               ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) *
                                 (denominatorDegreesOfFreedom - 4)));
        final double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test the numerical variance method with invalid parameters.
     */
    @org.junit.Test
    public void testNumericalVarianceInvalidParameters() {
        final FDistribution distribution = new FDistribution();
        final double actual = distribution.getNumericalVariance();
        assertEquals(Double.NaN, actual);
    }

    /**
     * Test the getSolverAbsoluteAccuracy method.
     */
    @org.junit.Test
    public void testSolverAbsoluteAccuracy() {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);

        final double expected = 1e-9;
        final double actual = distribution.getSolverAbsoluteAccuracy();
        assertEquals(expected, actual, 1e-9);
    }
}