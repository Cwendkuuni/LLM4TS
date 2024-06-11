package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructionWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -3, 2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructionWithNonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 3, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructionWithNonPositiveSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 2, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructionWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 4, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructionWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 2, 6);
    }

    @Test
    public void testHypergeometricDistributionConstruction() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        assertEquals(3, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(5, hypergeometricDistribution.getSampleSize());
        assertEquals(8, hypergeometricDistribution.getPopulationSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        assertEquals(0, (int) hypergeometricDistribution.cumulativeProbability(0));
        assertEquals(1, (int) hypergeometricDistribution.cumulativeProbability(domain[0] - 1));
        assertEquals(0.3333333333333333, hypergeometricDistribution.cumulativeProbability(domain[0]), 0.000000001);
        assertEquals(0.6666666666666667, hypergeometricDistribution.cumulativeProbability(domain[1] - 1), 0.000000001);
        assertEquals(1, (int) hypergeometricDistribution.cumulativeProbability(domain[1]));
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        assertEquals(3, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(5, hypergeometricDistribution.getSampleSize());
        assertEquals(8, hypergeometricDistribution.getPopulationSize());
        assertEquals(4, domain[0]);
        assertEquals(5, domain[1]);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        assertEquals(0, (int) hypergeometricDistribution.probability(0), 0.000000001);
        assertEquals(0.3333333333333333, hypergeometricDistribution.probability(domain[0] - 1), 0.000000001);
        assertEquals(0.4166666666666666, hypergeometricDistribution.probability(domain[0]), 0.000000001);
        assertEquals(0.2777777777777778, hypergeometricDistribution.probability(domain[1] - 1), 0.000000001);
        assertEquals(0.2222222222222222, hypergeometricDistribution.probability(domain[1]), 0.000000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        assertEquals(1, (int) hypergeometricDistribution.upperCumulativeProbability(domain[1]));
        assertEquals(0, (int) hypergeometricDistribution.upperCumulativeProbability(domain[1] + 1));
        assertEquals(0.3333333333333333, hypergeometricDistribution.upperCumulativeProbability(domain[1] - 1), 0.000000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        assertEquals(2.6, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        double numericalVariance = hypergeometricDistribution.getNumericalVariance();
        assertEquals(0.2302585092994128, numericalVariance, 0.000001);
        assertEquals(0.2302585092994128, hypergeometricDistribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        assertEquals(-1, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        assertEquals(2, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 3, 2, 5);
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }
}