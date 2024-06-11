package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.Statistics;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0_sd1() {
        NormalDistributionImpl dist = new NormalDistributionImpl();

        // Mean and standard deviation tests
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);

        // Probability density function (PDF) tests
        assertEquals(0.0400965778509581, dist.density(0.0), 0.0001);
        assertEquals(0.398942280401433, dist.density(0.5), 0.0001);
        assertEquals(0.0400965778509581, dist.density(1.0), 0.0001);

        // Cumulative distribution function (CDF) tests
        assertEquals(0.154621614563876, dist.cumulativeProbability(0.5), 0.0001);
        assertEquals(0.841378385436124, dist.cumulativeProbability(1.5), 0.0001);

        // Inverse cumulative distribution function (ICDF) tests
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.001);

        // Moments tests
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
        assertEquals(1.0, dist.getVariance(), 0.001);
        assertEquals(1.0, dist.getSkewness(), 0.001);
        assertEquals(-3.0, dist.getKurtosis(), 0.001);

        // Generating numbers tests
        double[] numbers = new double[1000];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = dist.inverseCumulativeProbability(i / (double) numbers.length);
        }
        assertEquals(0.5, Statistics.mean(numbers), 0.001);
        assertEquals(1.0, Statistics.standardDeviation(numbers), 0.001);

        // Error handling tests
        try {
            dist.setMean(-1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.setStandardDeviation(0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(Double.NaN);
            fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(Double.POSITIVE_INFINITY);
            fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(-0.5);
            fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(1.5);
            fail();
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImpl_mean1_sd2() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);

        // Mean and standard deviation tests
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);

        // Probability density function (PDF) tests
        // ...

        // Cumulative distribution function (CDF) tests
        // ...

        // Inverse cumulative distribution function (ICDF) tests
        // ...

        // Moments tests
        // ...

        // Generating numbers tests
        // ...

        // Error handling tests
        // ...
    }
}