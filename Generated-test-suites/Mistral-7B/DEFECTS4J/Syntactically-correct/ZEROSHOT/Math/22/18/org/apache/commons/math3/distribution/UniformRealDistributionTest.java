import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private RandomGenerator random;

    @Before
    public void setUp() {
        random = new Well19937c();
        try {
            distribution = new UniformRealDistribution(random, 0, 1);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConstructor() {
        UniformRealDistribution uniformRealDistribution = new UniformRealDistribution();
        Assert.assertEquals(0, uniformRealDistribution.getSupportLowerBound(), 0.0);
        Assert.assertEquals(1, uniformRealDistribution.getSupportUpperBound(), 0.0);

        UniformRealDistribution uniformRealDistribution1 = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0, uniformRealDistribution1.getSupportLowerBound(), 0.0);
        Assert.assertEquals(1, uniformRealDistribution1.getSupportUpperBound(), 0.0);

        UniformRealDistribution uniformRealDistribution2 = new UniformRealDistribution(0.1, 0.2, 0.001);
        Assert.assertEquals(0.1, uniformRealDistribution2.getSupportLowerBound(), 0.0);
        Assert.assertEquals(0.2, uniformRealDistribution2.getSupportUpperBound(), 0.0);
        Assert.assertEquals(0.001, uniformRealDistribution2.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testDensity() {
        double result = distribution.density(0.5);
        Assert.assertEquals(1.0 / (1.0 - 0.0), result, 0.0);

        result = distribution.density(0.1);
        Assert.assertEquals(1.0 / (1.0 - 0.0), result, 0.0);

        result = distribution.density(1.0);
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        double result = distribution.cumulativeProbability(0.2);
        Assert.assertEquals(0.2, result, 0.0);

        result = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, result, 0.0);

        result = distribution.cumulativeProbability(0.8);
        Assert.assertEquals(0.8, result, 0.0);

        result = distribution.cumulativeProbability(1.0);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        try {
            new UniformRealDistribution(random, 1, 0);
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals("lower bound (1.0) is greater than or equal to upper bound (0.0)", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGetNumericalMean() {
        double result = distribution.getNumericalMean();
        Assert.assertEquals(0.5, result, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        double result = distribution.getNumericalVariance();
        Assert.assertEquals(0.005555555555555555, result, 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        double result = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double result = distribution.getSupportUpperBound();
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = distribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = distribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsSupportConnected() {
        boolean result = distribution.isSupportConnected();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testSample() {
        double result = distribution.sample();
        Assert.assertTrue(result >= 0.0 && result < 1.0);
    }
}