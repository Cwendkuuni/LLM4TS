package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionPositiveParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        int[] domain = distribution.getDomain();
        assertEquals(3, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getSampleSize());
        assertEquals(3, domain[0]);
        assertEquals(5, domain[1]);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.001);
        assertEquals(0.003928672472162, distribution.cumulativeProbability(1), 0.001);
        assertEquals(0.047136596470991, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.115570562411172, distribution.cumulativeProbability(3), 0.001);
        assertEquals(0.265872795128868, distribution.cumulativeProbability(4), 0.001);
        assertEquals(0.57888643191421, distribution.cumulativeProbability(5), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.001);
        assertEquals(0.047136596470991, distribution.probability(1), 0.001);
        assertEquals(0.072901248192025, distribution.probability(2), 0.001);
        assertEquals(0.115570562411172, distribution.probability(3), 0.001);
        assertEquals(0.166666666666667, distribution.probability(4), 0.001);
        assertEquals(0.230769230769231, distribution.probability(5), 0.001);
        assertEquals(0.230769230769231, distribution.probability(6), 0.001);
        assertEquals(-1.0, distribution.upperCumulativeProbability(0), 0.001);
        assertEquals(0.047136596470991, distribution.upperCumulativeProbability(1), 0.001);
        assertEquals(0.094273192941983, distribution.upperCumulativeProbability(2), 0.001);
        assertEquals(0.141414141414141, distribution.upperCumulativeProbability(3), 0.001);
        assertEquals(0.188888888888889, distribution.upperCumulativeProbability(4), 0.001);
        assertEquals(0.230769230769231, distribution.upperCumulativeProbability(5), 0.001);
        assertEquals(0.230769230769231, distribution.upperCumulativeProbability(6), 0.001);
        assertEquals(3.0, distribution.getNumericalMean(), 0.001);
        assertEquals(0.187485646931384, distribution.getNumericalVariance(), 0.001);
        assertEquals(0, distribution.getSupportLowerBound());
        assertEquals(5, distribution.getSupportUpperBound());
        assertTrue(distribution.isSupportConnected());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        new HypergeometricDistribution(10, 3, 5); // should throw NotPositiveException
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        new HypergeometricDistribution(0, 3, 5); // should throw NotStrictlyPositiveException
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -3, 5); // should throw NotPositiveException
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5); // should throw NotStrictlyPositiveException
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        new HypergeometricDistribution(10, 3, -5); // should throw NotPositiveException
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5); // should throw NumberIsTooLargeException
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 3, 11); // should throw NumberIsTooLargeException
    }
}