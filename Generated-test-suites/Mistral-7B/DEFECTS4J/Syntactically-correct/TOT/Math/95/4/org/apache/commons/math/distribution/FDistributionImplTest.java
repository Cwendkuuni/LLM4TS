package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FTestable;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test of {@link FDistributionImpl#FDistributionImpl(double, double)}.
     */
    @Test
    public void testFDistributionImpl() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        assertEquals("degrees of freedom not set", 2.0, fDist.getNumeratorDegreesOfFreedom(), 1e-14);
        assertEquals("degrees of freedom not set", 4.0, fDist.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test of {@link FDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        double[] expected = {0.0, 0.00218691, 0.0214225, 0.1, 0.2037925, 0.3276144, 0.4749264, 0.6412205, 0.8152282, 0.9639494, 1.0};
        FTestable fTestable = (FTestable) fDist;
        for (double x : expected) {
            double result = fDist.cumulativeProbability(x);
            assertEquals("wrong CDF value for x = " + x, result, expected[indexOf(x, expected)], 1e-12);
        }
    }

    /**
     * Test of {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        double[] expected = {0.0, 0.3821573, 0.5778244, 1.0};
        for (int i = 0; i < expected.length; i++) {
            double p = i / 10.0;
            double x = fDist.inverseCumulativeProbability(p);
            assertEquals("wrong inverse CDF value for p = " + p, x, expected[i], 1e-12);
        }
    }

    /**
     * Test of {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with negative probability.
     *
     * @throws Exception if test fails.
     */
    @Test(expected = MathException.class)
    public void testNegativeProbability() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        fDist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test of {@link FDistributionImpl#getMean()}.
     */
    @Test
    public void testMean() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        double result = fDist.getMean();
        double expected = Beta.regularizedBeta(2.0, 2.0, 4.0) / (4.0 - 2.0);
        assertEquals("wrong mean", result, expected, 1e-14);
    }

    /**
     * Test of {@link FDistributionImpl#getStandardDeviation()}.
     */
    @Test
    public void testStandardDeviation() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        StandardDeviation sd = new StandardDeviation();
        double[] x = new double[10000];
        for (int i = 0; i < x.length; i++) {
            x[i] = fDist.inverseCumulativeProbability(i / (double) x.length);
        }
        double result = sd.evaluate(x);
        double expected = Math.sqrt(Beta.regularizedBeta(2.5, 2.5, 4.0) / (4.0 - 2.5)
                - Beta.regularizedBeta(2.0, 2.0, 4.0) / (4.0 - 2.0));
        assertEquals("wrong standard deviation", result, expected, 1e-12);
    }
}