
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0d, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(1d, dist.getSupportUpperBound(), 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        try {
            new UniformRealDistribution(1, 1);
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensityEdgeCases() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0d, dist.density(0d), 0.0001);
        Assert.assertEquals(1d / (1 - 0), dist.density(0.5), 0.0001);
        Assert.assertEquals(0d, dist.density(1d), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityEdgeCases() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0d, dist.cumulativeProbability(0d), 0.0001);
        Assert.assertEquals(0.5d, dist.cumulativeProbability(0.5d), 0.0001);
        Assert.assertEquals(1d, dist.cumulativeProbability(1d), 0.0001);
    }

    @Test
    public void testSampleEdgeCases() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double sample = dist.sample();
        Assert.assertTrue(sample >= 0d && sample < 1d);
    }

    @Test
    public void testGetters() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0d, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(1d, dist.getSupportUpperBound(), 0);
        Assert.assertEquals(1d / (1 - 0), dist.density(0.5), 0.0001);
        Assert.assertEquals(0.5d, dist.cumulativeProbability(0.5d), 0.0001);
        Assert.assertEquals(0.5d, dist.getNumericalMean(), 0.0001);
        Assert.assertEquals((1d - 0) * (1d - 0) / 12, dist.getNumericalVariance(), 0.0001);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }
}