package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for FDistribution class.
 */
public class FDistributionTest {

    /**
     * Test instance creation with positive numerator and denominator degrees of freedom.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testCreateInstance() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test calculation of the density with valid inputs.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        // test with valid values
        double x = 1.5;
        double density = fDist.density(x);
        double expectedDensity = 0.1252534229168538;
        assertEquals(expectedDensity, density, 0.0001);
    }

    /**
     * Test calculation of the cumulative probability with valid inputs.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        // test with valid values
        double x = 1.5;
        double cumulativeProbability = fDist.cumulativeProbability(x);
        double expectedCumulativeProbability = 0.9182932366220933;
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
    }

    /**
     * Test creation of an FDistribution instance with negative degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testCreateInstanceNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = -1.0;
        final double denominatorDegreesOfFreedom = 3.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test calculation of the density with negative inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDensityNegativeInput() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        // test with negative values
        double x = -1.0;
        fDist.density(x);
    }

    /**
     * Test calculation of the cumulative probability with negative inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeInput() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        // test with negative values
        double x = -1.0;
        fDist.cumulativeProbability(x);
    }

    /**
     * Test calculation of the numerical mean with valid inputs.
     */
    @Test
    public void testNumericalMean() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        double numericalMean = fDist.getNumericalMean();
        double expectedNumericalMean = 3.0 / 2.0;
        assertEquals(expectedNumericalMean, numericalMean, 0.001);
    }

    /**
     * Test calculation of the numerical variance with valid inputs.
     */
    @Test
    public void testNumericalVariance() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        double numericalVariance = fDist.getNumericalVariance();
        double expectedNumericalVariance = 9.0 / 12.0;
        assertEquals(expectedNumericalVariance, numericalVariance, 0.001);
    }

    /**
     * Test calculation of the numerical mean with invalid inputs.
     */
    @Test(expected = IllegalStateException.class)
    public void testNumericalMeanInvalidInput() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 2.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);
        fDist.getNumericalMean();
    }
}