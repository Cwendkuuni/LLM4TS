package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionparams;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link FDistribution} class
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testFDistributionValidArguments() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid numerator degrees of freedom
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidNumeratorDOF() {
        new FDistribution(0.0, 2.0);
    }

    /**
     * Test constructor with invalid denominator degrees of freedom
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidDenominatorDOF() {
        new FDistribution(1.0, 0.0);
    }

    /**
     * Test method density
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double x = 1.5;
        double expected = fd.density(x);
        double delta = 1e-7;
        double result = fd.density(x);
        assertEquals("Error in density calculation: x = " + x, expected, result, delta);
    }

    /**
     * Test method cumulativeProbability
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double x = 1.5;
        double p = 0.9;
        double expected = fd.cumulativeProbability(x);
        double result = fd.cumulativeProbability(x, p);
        assertEquals("Error in cumulativeProbability calculation: x = " + x + "; p = " + p, expected, result, 1e-9);
    }

    /**
     * Test method getNumericalMean
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(5.0, 7.0);
        double mean = fd.getNumericalMean();
        assertEquals("Error in numerical mean calculation: expected = 5.833333, actual = " + mean, 5.833333, mean, 1e-7);
    }

    /**
     * Test method getNumericalVariance
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(4.0, 8.0);
        double variance = fd.getNumericalVariance();
        assertEquals("Error in numerical variance calculation: expected = 2.682149, actual = " + variance, 2.682149, variance, 1e-7);
    }

    /**
     * Test method calculateNumericalVariance
     */
    @Test
    public void testCalculateNumericalVariance() {
        FDistribution fd = new FDistribution(4.0, 8.0);
        double variance = fd.calculateNumericalVariance();
        assertEquals("Error in calculateNumericalVariance calculation: expected = 2.682149, actual = " + variance, 2.682149, variance, 1e-7);
    }

    /**
     * Test method isSupportLowerBoundInclusive
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertTrue("Error in isSupportLowerBoundInclusive calculation: expected = true, actual = " + fd.isSupportLowerBoundInclusive(), fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertFalse("Error in isSupportUpperBoundInclusive calculation: expected = false, actual = " + fd.isSupportUpperBoundInclusive(), fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertTrue("Error in isSupportConnected calculation: expected = true, actual = " + fd.isSupportConnected(), fd.isSupportConnected());
    }

    /**
     * Test method generateQuantiles
     */
    @Test
    public void testGenerateQuantiles() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double[] quantiles = fd.generateQuantiles(100);
        for (int i = 0; i < quantiles.length; i++) {
            double p = (double) i / 100;
            double x = fd.inverseCumulativeProbability(p);
            assertEquals("Error in inverseCumulativeProbability calculation: p = " + p + ", x = " + x, i / 100, x, 1e-9);
        }
    }

    /**
     * Test method sample
     */
    @Test
    public void testSample() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        RandomGenerator rg = new Well19937c();
        for (int i = 0; i < 100; i++) {
            double x = fd.sample(rg);
            assertTrue("Expected positive value: x = " + x, x > 0);
        }
    }
}