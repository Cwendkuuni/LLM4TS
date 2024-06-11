package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), -5, 2, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, -2, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, 2, -5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 0, 2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, 2, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, 2, 6);
    }

    @Test
    public void testHypergeometricDistributionSampleSizeLargerThanNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 5, 2, 6);
        int x = distribution.sample();
        assertTrue(x >= 2 && x <= 5);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        double[] expected = { 0.0, 0.00634, 0.02416, 0.05178, 0.08158, 0.11464, 0.15092, 0.18942, 0.22998, 0.27225, 0.31612, 0.36151, 0.40825, 0.45635, 0.50575, 0.55635, 0.60825, 0.66151, 0.7158, 0.76987, 0.82463, 0.88001, 0.93605, 0.98335, 1.0 };
        for (int i = 0; i < expected.length; i++) {
            double result = distribution.cumulativeProbability(i);
            assertEquals(expected[i], result, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        double[] expected = { 0.0, 0.00095, 0.00386, 0.01008, 0.01731, 0.02638, 0.03673, 0.04838, 0.06127, 0.07549, 0.09104, 0.10786, 0.12603, 0.14552, 0.16629, 0.18834, 0.21163, 0.23621, 0.26202, 0.28898, 0.31708, 0.34633, 0.37677, 0.40836, 0.43985, 0.47214, 0.50511, 0.53958, 0.57539, 1.0 };
        for (int i = 0; i < expected.length; i++) {
            double result = distribution.probability(i);
            assertEquals(expected[i], result, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        double[] expected = { 1.0, 0.99905, 0.99714, 0.99424, 0.99137, 0.98852, 0.98568, 0.98285, 0.97999, 0.97715, 0.97435, 0.97152, 0.96865, 0.96575, 0.96282, 0.95987, 0.95691, 0.95391, 0.95089, 0.94783, 0.94473, 0.94160, 0.93843, 0.93522, 0.93200, 0.92875, 0.92547, 0.92216, 0.91883, 0.91547, 0.91207, 0.90864, 0.90517, 0.90166, 0.89811, 0.89452, 0.89088, 0.88721, 0.88350, 0.87975, 0.87597, 0.87215, 0.86830, 0.86442, 0.86049, 0.85653, 0.85253, 0.84849, 0.84441, 0.84030, 0.83615, 0.83196, 0.82773, 0.82346, 0.81915, 0.81480, 0.81041, 0.80600, 0.80155, 0.79606, 0.79053, 0.78496, 0.77935, 0.77371, 0.76802, 0.76229, 0.75652, 0.75071, 0.74485, 0.73894, 0.73300, 0.72702, 0.72099, 0.71483, 0.70853, 0.70218, 0.69578, 0.68934, 0.68285, 0.67631, 0.66962, 0.66278, 0.65578, 0.64863, 0.64133, 0.63396, 0.62643, 0.61875, 0.61081, 0.60269, 0.59430, 0.58563, 0.57668, 0.56745, 0.55783, 0.54802, 0.53802, 0.52773, 0.51715, 0.50627, 0.49501, 0.48335, 0.47130, 0.45876, 0.44568, 0.43201, 0.41767, 0.40257, 0.38668, 0.37003, 0.35237, 0.33359, 0.31359, 0.29233, 0.27060, 0.24734, 0.22237, 0.19533, 0.16606, 0.13442, 0.09909, 0.06054, 0.02726, 0.0 };
        for (int i = 0; i < expected.length; i++) {
            double result = distribution.upperCumulativeProbability(i);
            assertEquals(expected[i], result, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(3, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testHypergeometricDistributionGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testHypergeometricDistributionGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(3.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(1.2057459255309676D, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(2, distribution.getSupportLowerBound());
    }

    @Test
    public void testHypergeometricDistributionGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testHypergeometricDistributionSample() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        for (int i = 0; i < 100; i++) {
            int x = distribution.sample();
            assertTrue(x >= 2 && x <= 5);
        }
    }
}