import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        NormalDistribution distMean2 = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(2.0, distMean2.getMean(), 0.0);

        NormalDistribution distStdDev2 = new NormalDistributionImpl(0.0, 2.0);
        assertEquals(0.0, distStdDev2.getMean(), 0.0);
        assertEquals(2.0, distStdDev2.getStandardDeviation(), 0.0);

        NormalDistribution distMean2StdDev3 = new NormalDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distMean2StdDev3.getMean(), 0.0);
        assertEquals(3.0, distMean2StdDev3.getStandardDeviation(), 0.0);

        try {
            NormalDistribution distNegativeSD = new NormalDistributionImpl(0.0, -1.0);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDistributionFunction() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(0.5, dist.density(0.0), 1e-12);
        assertEquals(0.398942280401433, dist.density(0.5), 1e-12);

        double p = 0.1587;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.679465656417362, x, 1e-8);
        assertEquals(p, dist.cumulativeProbability(x), 1e-12);

        x = dist.inverseCumulativeProbability(0.999);
        assertEquals(3.097394776423735E-05, x, 1e-8);

        p = 0.999;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(2.326968450403865E-05, x, 1e-8);
        assertEquals(p, dist.cumulativeProbability(x), 1e-12);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        for (int i = 0; i < 10000; i++) {
            double x = dist.sample();
            assertEquals(true, dist.isSupportPoint(x));
            assertEquals(true, dist.isProbabilityDensity(x));
            assertEquals(0.0, dist.mean, x - 0.0);
            assertEquals(1.0, dist.standardDeviation, Math.abs(x - 0.0) / Math.sqrt(12.0), 0.01);
        }
    }

    @Test(expected = MathException.class)
    public void testSampleMathException() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0 / 0.0);
        dist.sample();
    }

    @Test
    public void testCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        try {
            dist.cumulativeProbability(-1.0);
            fail("Expected exception");
        } catch (MathException e) {
            // expected
        }

        try {
            dist.cumulativeProbability(3.0);
            fail("Expected exception");
        } catch (MathException e) {
            // expected
        }
    }
}