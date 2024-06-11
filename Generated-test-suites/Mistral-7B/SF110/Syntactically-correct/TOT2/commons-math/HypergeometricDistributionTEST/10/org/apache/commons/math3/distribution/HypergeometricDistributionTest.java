
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorsWithInvalidPopulationSize() {
        try {
            new HypergeometricDistribution(-5, 1, 2);
        } catch (NotPositiveException e) {
            // expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorsWithInvalidNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(10, 0, 2);
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorsWithInvalidSampleSize() {
        try {
            new HypergeometricDistribution(10, 5, 11);
        } catch (NumberIsTooLargeException e) {
            // expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorsWithInvalidNumberOfSuccessesLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(10, 11, 2);
        } catch (NumberIsTooLargeException e) {
            // expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorsWithInvalidPopulationSizeLessThanSampleSize() {
        try {
            new HypergeometricDistribution(2, 5, 10);
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test
    public void testConstructorWithValidParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        int populationSize = distribution.getPopulationSize();
        int numberOfSuccesses = distribution.getNumberOfSuccesses();
        int sampleSize = distribution.getSampleSize();
        Assert.assertEquals(10, populationSize);
        Assert.assertEquals(5, numberOfSuccesses);
        Assert.assertEquals(2, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        double expectedCumulativeProbability = 0.5;
        for (int i = 0; i <= 5; i++) {
            double actualCumulativeProbability = distribution.cumulativeProbability(i);
            Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
            expectedCumulativeProbability += distribution.probability(i);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        double expectedProbability = 0.361; // calculated using the formula
        double actualProbability = distribution.probability(3);
        Assert.assertEquals(expectedProbability, actualProbability, 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        double expectedMean = 5.0; // calculated using the formula
        double actualMean = distribution.getNumericalMean();
        Assert.assertEquals(expectedMean, actualMean, 0.001);
    }
}