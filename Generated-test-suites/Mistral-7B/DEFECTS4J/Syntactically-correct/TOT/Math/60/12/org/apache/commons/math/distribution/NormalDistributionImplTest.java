package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NormalDistributionImplTest {

    /**
     * Test case for the constructor with valid mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplValidConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test case for the constructor with negative standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    /**
     * Test case for the density method.
     */
    @Test
    public void testDensityNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double densityVal = dist.density(1.0);
        double expectedDensity = 0.398942280401;
        assertEquals(expectedDensity, densityVal, 0.0001);
    }

    /**
     * Test case for the cumulativeProbability method with valid x.
     */
    @Test
    public void testCumulativeProbabilityNormalDistributionImplValidX() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double cumulativeProbabilityVal = dist.cumulativeProbability(1.0);
        double expectedCumulativeProbability = 0.841344746;
        assertEquals(expectedCumulativeProbability, cumulativeProbabilityVal, 0.001);
    }

    /**
     * Test case for the cumulativeProbability method with x out of range.
     */
    @Test
    public void testCumulativeProbabilityNormalDistributionImplOutOfRangeX() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        try {
            dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
        try {
            dist.cumulativeProbability(Double.POSITIVE_INFINITY);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test case for the inverseCumulativeProbability method with valid p.
     */
    @Test
    public void testInverseCumulativeProbabilityNormalDistributionImplValidP() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double inverseCumulativeProbabilityVal = dist.inverseCumulativeProbability(0.5);
        double expectedInverseCumulativeProbability = 0.0;
        assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbabilityVal, 0.001);
    }

    /**
     * Test case for the inverseCumulativeProbability method with p < 0.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInverseCumulativeProbabilityNormalDistributionImplPLessThanZero() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(0.0);
    }

    /**
     * Test case for the inverseCumulativeProbability method with p > 1.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInverseCumulativeProbabilityNormalDistributionImplPGreaterThanOne() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test case for the inverseCumulativeProbability method with p = 0.
     */
    @Test
    public void testInverseCumulativeProbabilityNormalDistributionImplPZero() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double inverseCumulativeProbabilityVal = dist.inverseCumulativeProbability(0.0);
        double expectedInverseCumulativeProbability = Double.NEGATIVE_INFINITY;
        assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbabilityVal, 0.0);
    }

    /**
     * Test case for the inverseCumulativeProbability method with p = 1.
     */
    @Test
    public void testInverseCumulativeProbabilityNormalDistributionImplPOne() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double inverseCumulativeProbabilityVal = dist.inverseCumulativeProbability(1.0);
        double expectedInverseCumulativeProbability = Double.POSITIVE_INFINITY;
        assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbabilityVal, 0.0);
    }

    /**
     * Test case for the sample method.
     */
    @Test
    public void testSampleNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double sampleVal = dist.sample();
        double expectedSampleVal = Erf.erfcInv(Math.random());
        double expectedSampleValAbs = Math.abs(expectedSampleVal);
        assertTrue(Precision.isClose(expectedSampleValAbs, sampleVal, 0.01));
    }
}