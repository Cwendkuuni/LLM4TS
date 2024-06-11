package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution normDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, normDist.getMean(), 0.0);
        assertEquals(1.0, normDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test method {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        NormalDistribution normDist = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid x
        double x = 1.0;
        double expectedDensity = 0.398942280401353;
        double density = normDist.density(x);
        assertEquals(expectedDensity, density, 1e-6);

        // Test with x outside of the distribution range
        x = Double.NEGATIVE_INFINITY;
        density = normDist.density(x);
        assertEquals(0.0, density, 1e-6);

        x = Double.POSITIVE_INFINITY;
        density = normDist.density(x);
        assertEquals(0.0, density, 1e-6);
    }

    /**
     * Test method {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normDist = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid x
        double x = 0.5;
        double expectedProbability = 0.682689462137;
        double probability = normDist.cumulativeProbability(x);
        assertEquals(expectedProbability, probability, 1e-6);

        // Test with x outside of the distribution range
        x = Double.NEGATIVE_INFINITY;
        probability = normDist.cumulativeProbability(x);
        assertEquals(0.0, probability, 1e-6);

        x = Double.POSITIVE_INFINITY;
        probability = normDist.cumulativeProbability(x);
        assertEquals(1.0, probability, 1e-6);

        // Test with x close to a boundary
        x = Precision.safeRound(0.0, 5) - 1.0e-5;
        probability = normDist.cumulativeProbability(x);
        assertEquals(Precision.safeRound(0.1587, 5), probability, 1e-6);
    }

    /**
     * Test method {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normDist = new NormalDistributionImpl(0.0, 1.0);

        // Test with p = 0.0
        double probability = 0.0;
        double expectedX = Double.NEGATIVE_INFINITY;
        double x = normDist.inverseCumulativeProbability(probability);
        assertEquals(expectedX, x);

        // Test with p = 1.0
        probability = 1.0;
        expectedX = Double.POSITIVE_INFINITY;
        x = normDist.inverseCumulativeProbability(probability);
        assertEquals(expectedX, x);

        // Test with p between 0.0 and 1.0
        probability = 0.5;
        expectedX = 0.0;
        x = normDist.inverseCumulativeProbability(probability);
        assertEquals(expectedX, x, 1e-6);

        // Test with p outside of the range [0.0, 1.0]
        probability = 1.1;
        assertThrows(MathException.class, () -> normDist.inverseCumulativeProbability(probability));
    }

    /**
     * Test method {@link NormalDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution normDist = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid distribution
        double x = normDist.sample();
        double mean = normDist.getMean();
        double standardDeviation = normDist.getStandardDeviation();
        assertEquals(true, x < mean + 3.0 * standardDeviation);
        assertEquals(true, x > mean - 3.0 * standardDeviation);

        // Test with mean = 0.0 and standard deviation = 0.0
        normDist = new NormalDistributionImpl();
        x = normDist.sample();
        assertEquals(0.0, x, 1e-6);
    }
}