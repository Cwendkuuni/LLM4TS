package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.statistics.Percentile;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     * @throws MathException if test fails
     */
    @Test
    public void testNormalDistributionImpl() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals("Mean not initialized correctly.", 5.0, dist.getMean(), 1e-6);
        assertEquals("Standard deviation not initialized correctly.", 2.0, dist.getStandardDeviation(), 1e-6);
    }

    /**
     * Test default constructor.
     * @throws MathException if test fails
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals("Mean not initialized correctly.", 0.0, dist.getMean(), 1e-6);
        assertEquals("Standard deviation not initialized correctly.", 1.0, dist.getStandardDeviation(), 1e-6);
    }

    /**
     * Test mean and standard deviation setters.
     * @throws MathException if test fails
     */
    @Test
    public void testMeanAndStandardDeviationSetters() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(5.0);
        dist.setStandardDeviation(2.0);
        assertEquals("Mean not set correctly.", 5.0, dist.getMean(), 1e-6);
        assertEquals("Standard deviation not set correctly.", 2.0, dist.getStandardDeviation(), 1e-6);
    }

    /**
     * Test mean and standard deviation getters.
     * @throws MathException if test fails
     */
    @Test
    public void testMeanAndStandardDeviationGetters() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean not retrieved correctly.", 1.0, dist.getMean(), 1e-6);
        assertEquals("Standard deviation not retrieved correctly.", 2.0, dist.getStandardDeviation(), 1e-6);
    }

    /**
     * Test method cumulativeProbability with mean = 0 and standard deviation = 1.
     * @throws MathException if test fails
     */
    @Test
    public void testCumulativeProbabilityZeroMeanOneStandardDeviation() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals("CDF not correct for x = 0.", 0.5, dist.cumulativeProbability(0.0), 1e-6);
        assertEquals("CDF not correct for x = 0.5.", 0.682689492137, dist.cumulativeProbability(0.5), 1e-6);
        assertEquals("CDF not correct for x = 1.", 0.841344746069, dist.cumulativeProbability(1.0), 1e-6);
    }

    /**
     * Test method cumulativeProbability with mean = 5 and standard deviation = 2.
     * @throws MathException if test fails
     */
    @Test
    public void testCumulativeProbabilityFiveMeanTwoStandardDeviation() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals("CDF not correct for x = 0.", 0.00688849555969, dist.cumulativeProbability(0.0), 1e-6);
        assertEquals("CDF not correct for x = 2.5.", 0.993158554184, dist.cumulativeProbability(2.5), 1e-6);
        assertEquals("CDF not correct for x = 7.5.", 1.0, dist.cumulativeProbability(7.5), 1e-6);
    }

    /**
     * Test method inverseCumulativeProbability with p = 0.25.
     * @throws MathException if test fails
     */
    @Test
    public void testInverseCumulativeProbabilityPTwentyFive() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        double x = dist.inverseCumulativeProbability(0.25);
        assertEquals("x not correct for p = 0.25.", -0.674489753484, x, 1e-6);
    }

    /**
     * Test method inverseCumulativeProbability with p = 0.75.
     * @throws MathException if test fails
     */
    @Test
    public void testInverseCumulativeProbabilityPSeventyFive() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        double x = dist.inverseCumulativeProbability(0.75);
        assertEquals("x not correct for p = 0.75.", 0.674489753484, x, 1e-6);
    }

    /**
     * Test method inverseCumulativeProbability with p = 0.
     * @throws MathException if test fails
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityPPZero() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        dist.inverseCumulativeProbability(0.0);
    }

    /**
     * Test method inverseCumulativeProbability with p = 1.
     * @throws MathException if test fails
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityPPOne() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        dist.inverseCumulativeProbability(1.0);
    }

    /**
     * Test method percentPoint with p = 0.25.
     * @throws MathException if test fails
     */
    @Test
    public void testPercentPointPTwentyFive() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        double x = ((Percentile) new Percentile()).inverse(0.25, dist);
        assertEquals("x not correct for p = 0.25.", -0.674489753484, x, 1e-6);
    }

    /**
     * Test method percentPoint with p = 0.75.
     * @throws MathException if test fails
     */
    @Test
    public void testPercentPointPSeventyFive() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        double x = ((Percentile) new Percentile()).inverse(0.75, dist);
        assertEquals("x not correct for p = 0.75.", 0.674489753484, x, 1e-6);
    }

    /**
     * Test method percentPoint with p = 0.
     * @throws MathException if test fails
     */
    @Test(expected = MathException.class)
    public void testPercentPointPPZero() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        ((Percentile) new Percentile()).inverse(0.0, dist);
    }

    /**
     * Test method percentPoint with p = 1.
     * @throws MathException if test fails
     */
    @Test(expected = MathException.class)
    public void testPercentPointPPOne() 
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        ((Percentile) new Percentile()).inverse(1.0, dist);
    }

    /**
     * Test method mean.
     * @throws MathException if test fails
     */
    @Test
    public void testMean() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        Mean mean = new Mean();
        mean.setData(dist.sample(10000));
        assertEquals("Mean not correct.", 1.0, mean.getResult(), 1e-6);
    }

    /**
     * Test method standard deviation.
     * @throws MathException if test fails
     */
    @Test
    public void testStandardDeviation() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        StandardDeviation std = new StandardDeviation();
        std.setData(dist.sample(10000));
        assertEquals("Standard deviation not correct.", 2.0, std.getResult(), 1e-6);
    }
}