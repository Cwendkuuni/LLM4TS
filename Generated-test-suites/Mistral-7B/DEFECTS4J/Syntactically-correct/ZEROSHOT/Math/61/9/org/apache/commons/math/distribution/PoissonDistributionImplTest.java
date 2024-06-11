import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, dist.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.135335283230664, dist.probability(0), 0.0001);
        Assert.assertEquals(0.240692026980063, dist.probability(1), 0.0001);
        Assert.assertEquals(0.142137751945233, dist.probability(2), 0.0001);
        Assert.assertEquals(0.081615353277835, dist.probability(3), 0.0001);
        Assert.assertEquals(0.031780487878982, dist.probability(4), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0, dist.cumulativeProbability(Integer.MIN_VALUE), 0.0);
        Assert.assertEquals(0.049804605881989, dist.cumulativeProbability(0), 0.001);
        Assert.assertEquals(0.135335283230664, dist.cumulativeProbability(1), 0.001);
        Assert.assertEquals(0.240692026980063, dist.cumulativeProbability(2), 0.001);
        Assert.assertEquals(0.343223899742491, dist.cumulativeProbability(3), 0.001);
        Assert.assertEquals(0.455488640136307, dist.cumulativeProbability(4), 0.001);
        Assert.assertEquals(0.577292612114113, dist.cumulativeProbability(5), 0.001);
        Assert.assertEquals(0.710378627661211, dist.cumulativeProbability(6), 0.001);
        Assert.assertEquals(0.846548779835806, dist.cumulativeProbability(7), 0.001);
        Assert.assertEquals(0.982124316039247, dist.cumulativeProbability(8), 0.001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 0.0);

        Assert.assertTrue(Precision.isClose(0.5, dist.cumulativeProbability(PoissonDistribution.mean(2.5)), 0.01));
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double p = dist.probability(i);
            double np = dist.normalApproximateProbability(i);
            Assert.assertTrue(Precision.isClose(p, np, 0.01));
        }
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            Assert.assertTrue(sample >= 0);
            Assert.assertTrue(sample <= 1000 * 2.5);
        }
    }

    @Test
    public void testCumulativeProbabilityLargeMean() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(100.0);
        for (int i = 0; i <= 10; i++) {
            double cdf = dist.cumulativeProbability(i);
            Assert.assertTrue(Precision.isClose(Gamma.regularizedGammaQ(i + 1, 100.0), cdf, 0.01));
        }
    }
}