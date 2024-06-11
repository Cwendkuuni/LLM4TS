package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Tests a valid PoissonDistributionImpl construction.
     */
    @Test
    public void testValidConstruction() {
        PoissonDistribution poisson = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, poisson.getMean(), 0.0);
        assertEquals(0.0, poisson.probability(0), 0.0);
        assertEquals(0.36787944115, poisson.probability(1), 0.0000001);
        assertEquals(0.36787944115, poisson.probability(1), 0.0000001);
        assertEquals(0.18343972058, poisson.probability(2), 0.0000001);
        assertEquals(0.12015639911, poisson.probability(3), 0.0000001);
        assertEquals(0.08267578414, poisson.probability(4), 0.0000001);
        assertEquals(0.05235987756, poisson.probability(5), 0.0000001);
        assertEquals(0.03437523738, poisson.probability(6), 0.0000001);
        assertEquals(0.02231343784, poisson.probability(7), 0.0000001);
        StandardDeviation sd = new StandardDeviation();
        double[] values = new double[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = poisson.sample();
        }
        double mean = 1.0;
        for (int i = 0; i < values.length; i++) {
            mean += values[i];
        }
        mean /= values.length;
        assertEquals(mean, poisson.getMean(), 0.01);
    }

    /**
     * Tests an invalid PoissonDistributionImpl construction with a negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidConstructionNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Tests the {@link #cumulativeProbability(int)} method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(2.0);
        assertEquals(0.0, poisson.cumulativeProbability(-1), 0.0);
        assertEquals(0.13533528323, poisson.cumulativeProbability(0), 0.0000001);
        assertEquals(0.54551113127, poisson.cumulativeProbability(1), 0.0000001);
        assertEquals(0.86466077154, poisson.cumulativeProbability(2), 0.0000001);
        assertEquals(0.98211813403, poisson.cumulativeProbability(3), 0.0000001);
        assertEquals(0.99929584166, poisson.cumulativeProbability(4), 0.0000001);
        assertEquals(1.0, poisson.cumulativeProbability(5), 0.0000001);
        assertEquals(1.0, poisson.cumulativeProbability(Integer.MAX_VALUE), 0.0000001);
    }

    /**
     * Tests the {@link #probability(int)} method.
     */
    @Test
    public void testProbability() {
        PoissonDistribution poisson = new PoissonDistributionImpl(2.0);
        assertEquals(0.13533528323, poisson.probability(0), 0.0000001);
        assertEquals(0.13533528323, poisson.probability(0), 0.0000001);
        assertEquals(0.27067056646, poisson.probability(1), 0.0000001);
        assertEquals(0.27067056646, poisson.probability(1), 0.0000001);
        assertEquals(0.21212121212, poisson.probability(2), 0.0000001);
        assertEquals(0.18208757165, poisson.probability(3), 0.0000001);
        assertEquals(0.15331824812, poisson.probability(4), 0.0000001);
        assertEquals(0.12886612528, poisson.probability(5), 0.0000001);
        assertEquals(0.10825646503, poisson.probability(6), 0.0000001);
        assertEquals(0.09048387435, poisson.probability(7), 0.0000001);
        assertEquals(0.07416553352, poisson.probability(8), 0.0000001);
        assertEquals(0.06109084734, poisson.probability(9), 0.0000001);
        assertEquals(0.05063425531, poisson.probability(10), 0.0000001);
    }

    /**
     * Tests the {@link #normalApproximateProbability(int)} method.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(2.0);
        assertEquals(0.54551113127, poisson.normalApproximateProbability(1), 0.0000001);
        assertEquals(0.86466077154, poisson.normalApproximateProbability(2), 0.0000001);
        assertEquals(0.98211813403, poisson.normalApproximateProbability(3), 0.0000001);
        assertEquals(1.0, poisson.normalApproximateProbability(4), 0.0000001);
    }

    /**
     * Tests the {@link #sample()} method.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(2.0);
        int value = poisson.sample();
        assertEquals(1, value, 0);
        value = poisson.sample();
        assertEquals(1, value, 0);
        value = poisson.sample();
        assertEquals(0, value, 0);
        value = poisson.sample();
        assertEquals(2, value, 0);
        value = poisson.sample();
        assertEquals(1, value, 0);
        value = poisson.sample();
        assertEquals(1, value, 0);
        value = poisson.sample();
        assertEquals(1, value, 0);
        value = poisson.sample();
        assertEquals(3, value, 0);
        value = poisson.sample();
        assertEquals(1, value, 0);
    }

    /**
     * Tests the {@link #getMean()} method.
     */
    @Test
    public void testGetMean() {
        PoissonDistribution poisson = new PoissonDistributionImpl(2.0);
        assertEquals(2.0, poisson.getMean(), 0.0);
    }

    /**
     * Tests the {@link #getStandardDeviation()} method.
     */
    @Test
    public void testGetStandardDeviation() {
        PoissonDistribution poisson = new PoissonDistributionImpl(2.0);
        assertEquals(Math.sqrt(2.0), poisson.getStandardDeviation(), 0.0);
    }
}