package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Unit tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.01);
        assertEquals(1.0, dist.getStandardDeviation(), 0.01);
    }

    /**
     * Test constructor with specified parameters.
     */
    @Test
    public void testConstructorWithParameters() {
        double mean = 2.5;
        double standardDeviation = 0.5;
        NormalDistribution dist = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals(mean, dist.getMean(), 0.01);
        assertEquals(standardDeviation, dist.getStandardDeviation(), 0.01);
    }

    /**
     * Test mean getter and setter.
     */
    @Test
    public void testMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.01);
        dist.setMean(1.5);
        assertEquals(1.5, dist.getMean(), 0.01);
    }

    /**
     * Test standard deviation getter and setter.
     */
    @Test
    public void testStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(1.0, dist.getStandardDeviation(), 0.01);
        dist.setStandardDeviation(2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.01);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
        double x = 0.5;
        double p = dist.cumulativeProbability(x);
        assertEquals(0.5, p, 0.001);
    }

    /**
     * Test inverse cumulative probability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
        double p = 0.75;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.674495783554557, x, 0.001);
    }

    /**
     * Test inverse cumulative probability method with extreme probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityExtremeProbabilities() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x);

        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x);
    }

    /**
     * Test moments.
     */
    @Test
    public void testMoments() {
        NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
        Mean mean = new Mean();
        StandardDeviation stddev = new StandardDeviation();
        mean.setData(dist.sample(1000000));
        stddev.setData(dist.sample(1000000));
        assertEquals(0.5, mean.getResult(), 0.01);
        assertEquals(1.0, stddev.getResult(), 0.01);
    }
}