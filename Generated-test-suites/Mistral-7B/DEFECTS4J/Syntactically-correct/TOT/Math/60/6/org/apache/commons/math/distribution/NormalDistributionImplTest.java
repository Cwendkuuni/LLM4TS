package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    private NormalDistribution dist;

    @Before
    public void setup() {
        dist = new NormalDistributionImpl(0, 1);
    }

    /**
     * Test for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)} constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplWithValidArguments() {
        NormalDistribution distExpected = new NormalDistribution();
        assertEquals(distExpected, dist);
    }

    /**
     * Test for {@link NormalDistributionImpl#NormalDistributionImpl(double, double, double)} constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplWithValidArgumentsAndAccuracy() {
        dist = new NormalDistributionImpl(0, 1, 1e-5);
        assertEquals(0, dist.getSolverAbsoluteAccuracy(), 1e-5);
        NormalDistribution distExpected = new NormalDistribution(0, 1);
        assertEquals(distExpected, dist);
    }

    /**
     * Test for {@link NormalDistributionImpl#NormalDistributionImpl()} constructor.
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution distExpected = new NormalDistribution();
        assertEquals(distExpected, dist);
    }

    /**
     * Test for {@link NormalDistributionImpl#getMean()} method.
     */
    @Test
    public void testGetMean() {
        assertEquals(0, dist.getMean(), 1e-9);
    }

    /**
     * Test for {@link NormalDistributionImpl#getStandardDeviation()} method.
     */
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, dist.getStandardDeviation(), 1e-9);
    }

    /**
     * Test for {@link NormalDistributionImpl#density(double)} method.
     */
    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, dist.density(0), 1e-9);
        assertEquals(0.3989422804014327, dist.density(1), 1e-9);
    }

    /**
     * Test for {@link NormalDistributionImpl#cumulativeProbability(double)} method.
     */
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.5, dist.cumulativeProbability(0), 1e-9);
        assertEquals(0.8413447460434737, dist.cumulativeProbability(1), 1e-9);
    }

    /**
     * Test for {@link NormalDistributionImpl#inverseCumulativeProbability(double)} method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidArguments() {
        double p = 0.25;
        double expected = -0.6745;
        double result;

        try {
            result = dist.inverseCumulativeProbability(p);
            assertEquals(expected, result, 1e-9);
        } catch (MathException e) {
            // This should not happen as the input is valid.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for {@link NormalDistributionImpl#inverseCumulativeProbability(double)} method with invalid arguments.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidArguments() {
        double p = -1;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test for {@link NormalDistributionImpl#sample()} method.
     */
    @Test
    public void testSample() {
        RandomData rd = new RandomData();
        double sample = dist.sample(rd);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();

        // Check if the sample is within the range of the normal distribution
        assertTrue(Precision.isClose(mean - 3 * stdDev, sample, 1e-6));
        assertTrue(Precision.isClose(mean + 3 * stdDev, sample, 1e-6));
    }
}