package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionWithValidParameters() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        FDistribution distribution = new FDistribution(new Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);

        // Test density
        double x = 1.5;
        double expectedDensity = 0.088284271247462;
        double actualDensity = distribution.density(x);
        assertEquals("Incorrect density for x = " + x, expectedDensity, actualDensity, 1e-6);

        // Test cumulative probability
        double expectedCumulativeProbability = 0.95682265159356;
        double actualCumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals("Incorrect cumulative probability for x = " + x, expectedCumulativeProbability, actualCumulativeProbability, 1e-6);

        // Test inverse cumulative probability
        double inverseCumulativeProbability = 0.75;
        double expectedValue = 1.8917455422543;
        double actualValue = distribution.inverseCumulativeProbability(inverseCumulativeProbability);
        assertEquals("Incorrect value for inverse cumulative probability = " + inverseCumulativeProbability, expectedValue, actualValue, 1e-6);

        // Test mean and variance
        double expectedMean = 3.0;
        double expectedVariance = 4.2632136544035;
        assertEquals("Incorrect mean", expectedMean, distribution.getNumericalMean(), 1e-6);
        assertEquals("Incorrect variance", expectedVariance, distribution.getNumericalVariance(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 0.0;
        double denominatorDegreesOfFreedom = 10.0;
        try {
            new FDistribution(new Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 0.0;
        try {
            new FDistribution(new Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}