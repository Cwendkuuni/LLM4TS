import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionCreation() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistribution.probability(i);
            double expectedProbability = Math.exp(-2.5) * Math.pow(2.5, i) / Math.factorial(i);
            Assert.assertEquals(expectedProbability, probability, 0.01);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, 2.5);
            Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.01);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = 0.5 * (1 + Math.erf((double) i / Math.sqrt(2.5)));
            Assert.assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 0.01);
        }
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        Assert.assertFalse(sample < 0);
        Assert.assertFalse(sample > 1000 * 2.5);
        int sample2 = poissonDistribution.sample();
        Assert.assertEquals(sample, sample2);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (double p = 0; p <= 1; p += 0.01) {
            int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(p);
            int expectedInverseCumulativeProbability = poissonDistribution.getDomainLowerBound()
                    + (int) Math.round((poissonDistribution.getDomainUpperBound() - poissonDistribution.getDomainLowerBound()) * p);
            Assert.assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability);
        }
    }
}