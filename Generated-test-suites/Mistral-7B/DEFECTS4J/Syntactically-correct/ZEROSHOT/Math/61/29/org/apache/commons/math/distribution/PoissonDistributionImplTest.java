import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        Assert.assertEquals(mean, distribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -2.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.probability(0);
        Assert.assertEquals(FastMath.exp(-0.5), probability, 1e-5);
    }

    @Test
    public void testProbabilityOne() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        double probability = distribution.probability(1);
        Assert.assertEquals(FastMath.exp(-2.5) * FastMath.sqrt(2.5) / MathUtils.SQRT_2_PI, probability, 1e-5);
    }

    @Test
    public void testProbabilityGreaterThanOne() {
        PoissonDistribution distribution = new PoissonDistributionImpl(5.0);
        double probability = distribution.probability(3);
        Assert.assertEquals(FastMath.exp(-5.0) * FastMath.exp(3.0) / MathUtils.SQRT_2_PI / MathUtils.SQRT_3_PI, probability, 1e-5);
    }

    @Test
    public void testProbabilityGreaterThanMaxInteger() {
        PoissonDistribution distribution = new PoissonDistributionImpl(100000.0);
        double probability = distribution.probability(Integer.MAX_VALUE);
        Assert.assertEquals(FastMath.exp(-100000.0), probability, 1e-5);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(0);
        } catch (MathException e) {
            Assert.fail();
        }
        Assert.assertEquals(0.0, cumulativeProbability);
    }

    @Test
    public void testCumulativeProbabilityOne() {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(1);
        } catch (MathException e) {
            Assert.fail();
        }
        Assert.assertEquals(FastMath.exp(-0.5), cumulativeProbability, 1e-5);
    }

    @Test
    public void testCumulativeProbabilityGreaterThanOne() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(3);
        } catch (MathException e) {
            Assert.fail();
        }
        double gamma = Gamma.regularizedGammaP(3.0, 2.5, DEFAULT_EPSILON, DEFAULT_MAX_ITERATIONS);
        Assert.assertEquals(gamma, cumulativeProbability, 1e-5);
    }

    @Test
    public void testNormalApproximateProbabilityZero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        double normalApproximateProbability = distribution.normalApproximateProbability(0);
        Assert.assertEquals(FastMath.exp(-0.5) / MathUtils.SQRT_2_PI, normalApproximateProbability, 1e-5);
    }

    @Test
    public void testNormalApproximateProbabilityOne() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = distribution.normalApproximateProbability(1);
        Assert.assertEquals(FastMath.exp(-2.5) / MathUtils.SQRT_2_PI, normalApproximateProbability, 1e-5);
    }

    @Test
    public void testNormalApproximateProbabilityGreaterThanOne() {
        PoissonDistribution distribution = new PoissonDistributionImpl(5.0);
        double normalApproximateProbability = distribution.normalApproximateProbability(3);
        Assert.assertEquals(FastMath.exp(-5.0 + 3.0) / (MathUtils.SQRT_2_PI * MathUtils.SQRT_3_PI), normalApproximateProbability, 1e-5);
    }
}