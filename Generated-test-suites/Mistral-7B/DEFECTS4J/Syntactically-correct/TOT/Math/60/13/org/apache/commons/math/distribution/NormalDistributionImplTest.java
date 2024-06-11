package org.apache.commons.math.distribution;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        // Test creating a normal distribution with valid parameters
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals("Mean is not set correctly", 0.0, normalDist.getMean(), 1e-9);
        Assert.assertEquals("Standard deviation is not set correctly", 1.0, normalDist.getStandardDeviation(), 1e-9);

        // Test creating a normal distribution with negative standard deviation
        try {
            new NormalDistributionImpl(0, -1);
            Assert.fail("Expected MathIllegalArgumentException with negative standard deviation");
        } catch (MathIllegalArgumentException e) {
            Assert.assertEquals("Expected specific error message", "Standard deviation must be non-negative", e.getMessage());
        }

        // Test creating a normal distribution with zero standard deviation
        try {
            new NormalDistributionImpl(0, 0);
            Assert.fail("Expected MathIllegalArgumentException with zero standard deviation");
        } catch (MathIllegalArgumentException e) {
            Assert.assertEquals("Expected specific error message", "Standard deviation must be non-zero", e.getMessage());
        }
    }

    @Test
    public void testNormalDistributionDensity() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        // Test density for x = mean
        double densityMean = normalDist.density(0);
        Assert.assertEquals("Density for x = mean is not correct", 0.39894228040143271, densityMean, 1e-8);

        // Test density for x = mean + 3 * standard deviation
        double densityAboveMean = normalDist.density(3);
        Assert.assertEquals("Density for x = mean + 3 * standard deviation is not correct", 9.2103696320473658e-12, densityAboveMean, 1e-15);

        // Test density for x = mean - 3 * standard deviation
        double densityBelowMean = normalDist.density(-3);
        Assert.assertEquals("Density for x = mean - 3 * standard deviation is not correct", 9.2103696320473658e-12, densityBelowMean, 1e-15);
    }

    @Test
    public void testNormalDistributionCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        // Test cumulative probability for x = mean
        double cumProbMean = normalDist.cumulativeProbability(0);
        Assert.assertEquals("Cumulative probability for x = mean is not correct", 0.5, cumProbMean, 1e-9);

        // Test cumulative probability for x = mean + 3 * standard deviation
        double cumProbAboveMean = normalDist.cumulativeProbability(3);
        Assert.assertEquals("Cumulative probability for x = mean + 3 * standard deviation is not correct", 0.9973302575109864, cumProbAboveMean, 1e-9);

        // Test cumulative probability for x = mean - 3 * standard deviation
        double cumProbBelowMean = normalDist.cumulativeProbability(-3);
        Assert.assertEquals("Cumulative probability for x = mean - 3 * standard deviation is not correct", 0.0026697424890136, cumProbBelowMean, 1e-9);

        // Test cumulative probability for x = -10 * standard deviation
        double cumProbNegative = normalDist.cumulativeProbability(-10);
        Assert.assertEquals("Cumulative probability for x = -10 * standard deviation is not correct", 4.55e-307, cumProbNegative, 1e-307);
    }

    @Test
    public void testNormalDistributionInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        // Test inverse cumulative probability for p = 0.25
        double inverseCumProb25 = normalDist.inverseCumulativeProbability(0.25);
        Assert.assertEquals("Inverse cumulative probability for p = 0.25 is not correct", -0.6794661082831903, inverseCumProb25, 1e-9);

        // Test inverse cumulative probability for p = 0.5
        double inverseCumProb50 = normalDist.inverseCumulativeProbability(0.5);
        Assert.assertEquals("Inverse cumulative probability for p = 0.5 is not correct", 0.0, inverseCumProb50, 1e-9);

        // Test inverse cumulative probability for p = 0.75
        double inverseCumProb75 = normalDist.inverseCumulativeProbability(0.75);
        Assert.assertEquals("Inverse cumulative probability for p = 0.75 is not correct", 0.6794661082831903, inverseCumProb75, 1e-9);

        // Test inverse cumulative probability for p < 0
        try {
            normalDist.inverseCumulativeProbability(-1);
            Assert.fail("Expected MathException with p < 0");
        } catch (MathException e) {
            Assert.assertEquals("Expected specific error message", "p must be between 0 and 1", e.getMessage());
        }

        // Test inverse cumulative probability for p > 1
        try {
            normalDist.inverseCumulativeProbability(1.2);
            Assert.fail("Expected MathException with p > 1");
        } catch (MathException e) {
            Assert.assertEquals("Expected specific error message", "p must be between 0 and 1", e.getMessage());
        }
    }

    @Test
    public void testNormalDistributionSample() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        // Test sampling from the distribution
        for (int i = 0; i < 10000; i++) {
            double sample = normalDist.sample();
            Assert.assertTrue("Sample value is out of bounds", sample >= -10 && sample <= 10);
        }

        // Test sampling from the distribution for large number of iterations
        for (int i = 0; i < 1000000; i++) {
            double sample = normalDist.sample();
            double meanSample = normalDist.getMean();
            double standardDeviationSample = normalDist.getStandardDeviation();
            Assert.assertTrue("Sample value is too far from mean", Math.abs(sample - meanSample) <= 3 * standardDeviationSample);
        }
    }
}