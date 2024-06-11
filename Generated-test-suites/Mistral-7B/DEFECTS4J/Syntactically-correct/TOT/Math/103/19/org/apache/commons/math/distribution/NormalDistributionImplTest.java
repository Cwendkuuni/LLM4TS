package org.apache.commons.math.distribution;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest extends TestCase {
    /** The mean of the normal distribution. */
    private static final double MEAN = 0.0;
    /** The standard deviation of the normal distribution. */
    private static final double STDDEV = 1.0;

    /** The normal distribution under test. */
    private NormalDistributionImpl distribution;

    /**
     * Sets up the test.
     */
    protected void setUp() {
        distribution = new NormalDistributionImpl(MEAN, STDDEV);
    }

    /**
     * Tests the constructor with valid mean and standard deviation.
     */
    public void testValidConstructor() {
        assertEquals(MEAN, distribution.getMean(), 0.0);
        assertEquals(STDDEV, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Tests the constructor with zero standard deviation.
     */
    public void testZeroStandardDeviation() {
        try {
            new NormalDistributionImpl(MEAN, 0.0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the cumulative probability method.
     */
    public void testCumulativeProbability() {
        double x;
        double p;
        double cdf;

        // test valid values
        for (x = -20.0; x <= 20.0; x += 1.0) {
            p = distribution.cumulativeProbability(x);
            cdf = 0.5 * (1.0 + Erf.erf((x - MEAN) / (STDDEV * Math.sqrt(2.0))));
            assertEquals(cdf, p, 1e-8);
        }

        // test extreme values
        x = Double.NEGATIVE_INFINITY;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.0, p, 1e-8);

        x = Double.POSITIVE_INFINITY;
        p = distribution.cumulativeProbability(x);
        assertEquals(1.0, p, 1e-8);

        // test mean and standard deviation
        x = MEAN;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.5, p, 1e-8);

        x = MEAN + 3.0 * STDDEV;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.6827, p, 1e-6);

        x = MEAN - 3.0 * STDDEV;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.3173, p, 1e-6);
    }

    /**
     * Tests the inverse cumulative probability method.
     */
    public void testInverseCumulativeProbability() {
        double p;
        double x;

        // test valid values
        for (p = 0.0; p <= 1.0; p += 0.01) {
            x = distribution.inverseCumulativeProbability(p);
            assertEquals(StatUtils.permutationMean(10000, StatUtils.rndn(10000))
                    + MEAN, x, 1e-5);
        }

        // test extreme values
        p = 0.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x);

        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x);
    }
}

/**
 * Suite of tests.
 */
public class NormalDistributionImplTestSuite extends TestSuite {
    public NormalDistributionImplTestSuite() {
        addTest(new NormalDistributionImplTest("testValidConstructor") {});
        addTest(new NormalDistributionImplTest("testZeroStandardDeviation") {});
        addTest(new NormalDistributionImplTest("testCumulativeProbability") {});
        addTest(new NormalDistributionImplTest("testInverseCumulativeProbability") {});
    }
}