package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.descript.Mean;
import org.apache.commons.math3.stat.descript.StandardDeviation;
import org.junit.Test;

/**
 * Test cases for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        try {
            new FDistribution(-1, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        try {
            new FDistribution(1, -2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        new FDistribution(2, 3);
    }

    /**
     * Test density.
     */
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2, 3);
        for (double x = 0; x <= 5; x += 0.1) {
            double density = dist.density(x);
            double expected = 0.212766 / (x * (3 + 2 * x));
            assertEquals(expected, density, 1e-5);
        }
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(2, 3);
        for (double x = 0; x <= 4; x += 0.1) {
            double prob = dist.cumulativeProbability(x);
            double expected = Beta.regularizedBeta((2 * x) / (3 + 2 * x), 1.5, 1.5);
            assertEquals(expected, prob, 1e-5);
        }
    }

    /**
     * Test mean.
     */
    @Test
    public void testMean() {
        FDistribution dist = new FDistribution(2.5, 3.5);
        double mean = new Mean().evaluate(dist, 100000);
        double expected = 3.5 / (3.5 - 2.5);
        assertEquals(expected, mean, 1e-5);
    }

    /**
     * Test variance.
     */
    @Test
    public void testVariance() {
        FDistribution dist = new FDistribution(2.5, 3.5);
        double variance = new StandardDeviation().evaluate(dist, 100000);
        double expected = (2 * 3.5 * 3.5 * (2.5 + 3.5 - 2)) / (2 * 3.5 * 3.5 * (3.5 - 4));
        assertEquals(expected, variance, 1e-5);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() {
        FDistribution dist = new FDistribution(2, 3);
        for (double p = 0; p <= 1; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected;
            if (p == 0) {
                expected = 0;
            } else if (p == 1) {
                expected = Double.POSITIVE_INFINITY;
            } else {
                expected = Beta.incompleteBeta(1.5, 1.5, p) / (1.5 * (1.5 + p));
            }
            assertEquals(expected, x, 1e-5);
        }
    }

    /**
     * Test numerical variance.
     */
    @Test
    public void testNumericalVariance() {
        FDistribution dist = new FDistribution(2.5, 3.5);
        double variance = dist.getNumericalVariance();
        double expected = (2 * 3.5 * 3.5 * (2.5 + 3.5 - 2)) / (2 * 3.5 * 3.5 * (3.5 - 4));
        assertEquals(expected, variance, 1e-5);
    }
}