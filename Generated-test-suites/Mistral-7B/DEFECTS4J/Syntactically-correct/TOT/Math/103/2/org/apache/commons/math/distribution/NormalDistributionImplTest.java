package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionWithGivenMeanAndStdDev() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 0.5);
        assertEquals("Mean should be set correctly.", 2.5, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be set correctly.", 0.5, dist.getStandardDeviation(), 0.001);

        NormalDistribution expectedDist = new NormalDistribution(2.5, 0.5);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.inverseCumulativeProbability(i1.nextDouble());
            assertEquals("Samples should follow the expected distribution.", expectedDist.density(sample), dist.density(sample), 0.0001);
        }
    }

    /**
     * Test default constructor with mean = 0 and standard deviation = 1.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals("Mean should be set to 0.", 0.0, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be set to 1.", 1.0, dist.getStandardDeviation(), 0.001);

        NormalDistribution expectedDist = new NormalDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.inverseCumulativeProbability(i1.nextDouble());
            assertEquals("Samples should follow the expected distribution.", expectedDist.density(sample), dist.density(sample), 0.0001);
        }
    }

    /**
     * Test mean and standard deviation accessors and mutators.
     */
    @Test
    public void testMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        // Test getters
        assertEquals("Mean should be 0.", 0.0, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be 1.", 1.0, dist.getStandardDeviation(), 0.001);

        // Test setters
        dist.setMean(2.5);
        dist.setStandardDeviation(0.5);
        assertEquals("Mean should be set to 2.5.", 2.5, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be set to 0.5.", 0.5, dist.getStandardDeviation(), 0.001);

        // Test inverseCumulativeProbability method with new mean and standard deviation
        for (int i = 0; i < 1000; i++) {
            double sample = dist.inverseCumulativeProbability(i1.nextDouble());
            Mean mean = new Mean();
            StandardDeviation stdDev = new StandardDeviation();
            mean.addData(sample);
            stdDev.addData(sample);
            assertEquals("Samples should follow the new distribution.", mean.getResult(), dist.getMean(), 0.001);
            assertEquals("Samples should follow the new distribution.", stdDev.getResult(), dist.getStandardDeviation(), 0.001);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            double expectedCdf = Erf.erf((x - dist.getMean()) / (dist.getStandardDeviation() * Math.sqrt(2.0)));
            double error = Math.abs(cdf - expectedCdf);
            if (error > 0.001) {
                System.out.println(String.format("Error at x = %f: %f (expected %f).", x, cdf, expectedCdf));
            }
            assertTrue("CDF value should be >= 0 and <= 1.", cdf >= 0 && cdf <= 1);
        }
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double error = Math.abs(x - Erf.erfinv(p / 2.0));
            if (error > 0.001) {
                System.out.println(String.format("Error at p = %f: %f (expected %f).", p, x, Erf.erfinv(p / 2.0)));
            }
            assertTrue("Inverse CDF value should be >= " + dist.getMean() + " and <= " + (dist.getMean() + dist.getStandardDeviation()), x >= dist.getMean() - dist.getStandardDeviation() && x <= dist.getMean() + dist.getStandardDeviation());
        }
    }

    /**
     * Test exception handling when setting standard deviation to non-positive value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenSettingNegativeStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, -1.0);
    }
}