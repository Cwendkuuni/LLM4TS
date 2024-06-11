import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for UniformRealDistribution.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0.0, dist.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, dist.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with custom bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testCustomConstructor() {
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper, 1e-10);
        Assert.assertEquals(lower, dist.getLowerBound(), 0.0);
        Assert.assertEquals(upper, dist.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with custom bounds and default inverse cumulative probability accuracy.
     */
    @Test
    public void testCustomConstructorWithDefaultAccuracy() {
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        Assert.assertEquals(lower, dist.getLowerBound(), 0.0);
        Assert.assertEquals(upper, dist.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        double lower = 5.0;
        double upper = 4.0;
        new UniformRealDistribution(lower, upper);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.5, dist.getNumericalMean(), 0.0);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Assert.assertTrue(dist.isSupportConnected());
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        Well19937c rng = new Well19937c();
        dist.setSeed(rng.nextLong());
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample();
            Assert.assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }
}