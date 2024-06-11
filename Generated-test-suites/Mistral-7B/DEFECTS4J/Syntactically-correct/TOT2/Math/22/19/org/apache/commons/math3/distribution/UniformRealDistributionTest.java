package org.apache.commons.math3.distribution;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.jupiter.api.Test;

public class UniformRealDistributionTest {

    // Test the default constructor
    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        // Testing the distribution bounds
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        // Testing the density at a point
        double x = 0.5;
        assertEquals(2.0 / (1.0 * 1.0), distribution.density(x), 0.01);
    }

    // Test lower bound is equal to upper bound
    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionLowerBoundEqualsUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 1.0);
    }

    // Test lower bound is greater than upper bound
    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionLowerBoundGreaterThanUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(5.0, 1.0);
    }

    // Test inverse cumulative probability accuracy is negative
    @Test(expected = IllegalArgumentException.class)
    public void testUniformRealDistributionNegativeInverseCumAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 2.0, -1.0);
    }

    // Test negative values for the lower and upper bounds
    @Test(expected = IllegalArgumentException.class)
    public void testUniformRealDistributionNegativeBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(-1.0, 1.0);
    }

    // Test null RandomGenerator
    @Test(expected = NullPointerException.class)
    public void testUniformRealDistributionNullRandomGenerator() {
        UniformRealDistribution distribution = new UniformRealDistribution(null, 1.0, 1.0);
    }

    // Test cumulative probability calculation for lower bound
    @Test
    public void testUniformRealDistributionCumulativeProbabilityLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double result = distribution.cumulativeProbability(0.0);
        assertEquals(0.0, result, 0.01);
    }

    // Test cumulative probability calculation for upper bound
    @Test
    public void testUniformRealDistributionCumulativeProbabilityUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double result = distribution.cumulativeProbability(1.0);
        assertEquals(1.0, result, 0.01);
    }

    // Test cumulative probability calculation for values outside the distribution range
    @Test
    public void testUniformRealDistributionCumulativeProbabilityOutsideRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double result = distribution.cumulativeProbability(2.0);
        assertEquals(1.0, result, 0.01);
    }

    // Test density calculation for values outside the distribution range
    @Test
    public void testUniformRealDistributionDensityOutsideRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double result = distribution.density(2.0);
        assertEquals(0.0, result, 0.01);
    }

    // Test checking support bounds inclusivity
    @Test
    public void testUniformRealDistributionSupportBoundsInclusivity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }

    // Test checking support bounds connection
    @Test
    public void testUniformRealDistributionSupportBoundsConnection() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(true, distribution.isSupportConnected());
    }

    // Test sample method
    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double result = distribution.sample();
        assertTrue(result >= 0.0 && result < 1.0);
    }
}

//