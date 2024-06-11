package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveNumberException;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomDataGenerator;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid mean value.
     */
    @org.junit.Test
    public void testConstructionWithValidMean() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructionWithNegativeMean() {
        final double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    /**
     * Test probability function with zero argument.
     */
    @org.junit.Test
    public void testProbabilityFunctionWithZeroArgument() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(FastMath.exp(-mean), distribution.probability(0), 0.0);
    }

    /**
     * Test probability function with positive argument.
     */
    @org.junit.Test
    public void testProbabilityFunctionWithPositiveArgument() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double expectedProbability = FastMath.exp(-mean) / FastMath.sqrt(MathUtils.TWO_PI * 1);
        assertEquals(expectedProbability, distribution.probability(1), 0.001);
    }

    /**
     * Test probability function with negative argument.
     */
    @org.junit.Test
    public void testProbabilityFunctionWithNegativeArgument() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, distribution.probability(-1), 0.0);
    }

    /**
     * Test probability function with large argument.
     */
    @org.junit.Test
    public void testProbabilityFunctionWithLargeArgument() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double expectedProbability = FastMath.exp(-mean) / FastMath.sqrt(MathUtils.TWO_PI * 1000);
        assertEquals(expectedProbability, distribution.probability(1000), 0.001);
    }

    /**
     * Test cumulative probability function with zero argument.
     */
    @org.junit.Test
    public void testCumulativeProbabilityFunctionWithZeroArgument() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0);
    }

    /**
     * Test cumulative probability function with large argument.
     */
    @org.junit.Test
    public void testCumulativeProbabilityFunctionWithLargeArgument() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double expectedCumulativeProbability = Gamma.regularizedGammaP(1000.0 + 1.0, mean);
        assertEquals(expectedCumulativeProbability, distribution.cumulativeProbability(1000), 0.001);
    }

    /**
     * Test normalApproximateProbability function with zero argument.
     */
    @org.junit.Test
    public void testNormalApproximateProbabilityFunctionWithZeroArgument() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, distribution.normalApproximateProbability(0), 0.0);
    }

    /**
     * Test normalApproximateProbability function with positive argument.
     */
    @org.junit.Test
    public void testNormalApproximateProbabilityFunctionWithPositiveArgument() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double expectedProbability = FastMath.erf(FastMath.sqrt(mean) / 2) / 2;
        assertEquals(expectedProbability, distribution.normalApproximateProbability(1), 0.01);
    }

    /**
     * Test normalApproximateProbability function with large argument.
     */
    @org.junit.Test
    public void testNormalApproximateProbabilityFunctionWithLargeArgument() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double expectedProbability = FastMath.erf(FastMath.sqrt(mean) / 2 + 1.5) / 2;
        assertEquals(expectedProbability, distribution.normalApproximateProbability(1000), 0.01);
    }

    /**
     * Test sample function with small mean value.
     */
    @org.junit.Test
    public void testSampleFunctionWithSmallMeanValue() throws MathException {
        final double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1000 * mean);
    }

    /**
     * Test sample function with large mean value.
     */
    @org.junit.Test
    public void testSampleFunctionWithLargeMeanValue() throws MathException {
        final double mean = 10000.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int sample = distribution.sample();
        assertEquals(sample, distribution.sample(), 0);
    }

    /**
     * Test inverse cumulative probability function with 0 probability.
     */
    @org.junit.Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        distribution.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test inverse cumulative probability function with probability between 0 and 1.
     */
    @org.junit.Test
    public void testInverseCumulativeProbabilityWithProbabilityBetweenZeroAndOne() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int result = distribution.inverseCumulativeProbability(0.5);
        int expectedResult = (int) Math.round(FastMath.round(Math.log(2.0) / Math.log(2.0 + FastMath.exp(-mean)) * 1000.0));
        assertEquals(expectedResult, result);
    }

    /**
     * Test inverse cumulative probability function with probability greater than 1.
     */
    @org.junit.Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithProbabilityGreaterThanOne() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        distribution.inverseCumulativeProbability(1.1);
    }

    /**
     * Test inverse cumulative probability function with probability less than 0.
     */
    @org.junit.Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithProbabilityLessThanZero() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        distribution.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverse cumulative probability function with probability at boundary.
     */
    @org.junit.Test
    public void testInverseCumulativeProbabilityWithProbabilityAtBoundary() throws MathException {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int result = distribution.inverseCumulativeProbability(1.0);
        int expectedResult = (int) Math.round(Math.round(mean));
        assertEquals(expectedResult, result);
    }

    /**
     * Test inverse cumulative probability function with negative mean value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithNegativeMeanValue() throws MathException {
        final double mean = -1.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        distribution.inverseCumulativeProbability(0.5);
    }

    /**
     * Test distribution with random values.
     */
    @org.junit.Test
    public void testDistributionWithRandomValues() {
        final double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        MersenneTwister random = new MersenneTwister();
        RandomDataGenerator rdg = new RandomDataGenerator();

        for (int i = 0; i < 1000; i++) {
            int sample = (int) rdg.nextPoisson(mean, random);
            assertTrue(sample >= 0 && sample <= 1000 * mean);
            assertEquals(distribution.probability(sample), distribution.cumulativeProbability(sample), 0.01);
        }
    }
}