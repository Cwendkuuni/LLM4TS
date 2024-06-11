package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link PoissonDistributionImpl}
 */
public class PoissonDistributionImplTest {

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double)}
     */
    @Test
    public void testPoissonDistributionImpl() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertThrows(MathRuntimeException.class, () -> new PoissonDistributionImpl(-1));
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}
     */
    @Test
    public void testPoissonDistributionImplWithOptions() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5, 1e-15, 1_000_000);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertThrows(MathIllegalArgumentException.class, () -> new PoissonDistributionImpl(-1, 0, 1000000));
    }

    /**
     * Test method for {@link PoissonDistributionImpl#probability(int)}
     */
    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(0.1206, dist.probability(1), 0.0001);
        assertEquals(0.0498, dist.probability(2), 0.0001);
        assertEquals(0.1131, dist.probability(3), 0.0001);
        assertEquals(0.2262, dist.probability(4), 0.0001);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#cumulativeProbability(int)}
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(0.0498, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.1795, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0.3833, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.6537, dist.cumulativeProbability(4), 0.0001);
        assertEquals(0.9993, dist.cumulativeProbability(10), 0.0001);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#normalApproximateProbability(int)}
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(0.1219, dist.normalApproximateProbability(1), 0.0001);
        assertEquals(0.0503, dist.normalApproximateProbability(2), 0.0001);
        assertEquals(0.1135, dist.normalApproximateProbability(3), 0.0001);
        assertEquals(0.2257, dist.normalApproximateProbability(4), 0.0001);
        assertEquals(0.4169, dist.normalApproximateProbability(5), 0.0001);
        assertEquals(0.6726, dist.normalApproximateProbability(6), 0.0001);
        assertEquals(0.9333, dist.normalApproximateProbability(7), 0.0001);
        assertEquals(1.0, dist.normalApproximateProbability(8), 0.0001);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#sample()}
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        for (int i = 0; i < 10000; i++) {
            int x = dist.sample();
            assertEquals(Precision.isClose(3.0, dist.cumulativeProbability(x), 0.0001), true);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#inverseCumulativeProbability(double)}
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(1, dist.inverseCumulativeProbability(0.3833));
        assertEquals(2, dist.inverseCumulativeProbability(0.6537));
        assertEquals(3, dist.inverseCumulativeProbability(0.9993));
        assertEquals(4, dist.inverseCumulativeProbability(0.99999), 1);
        assertThrows(MathRuntimeException.class, () -> dist.inverseCumulativeProbability(0.9999999));
    }
}