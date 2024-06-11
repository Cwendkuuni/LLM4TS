import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private Well19937c random;

    @Before
    public void setUp() {
        random = new Well19937c();
        try {
            distribution = new UniformRealDistribution(random, 0, 1);
        } catch (NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreation() {
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testDensity() {
        for (double x = 0.0; x < 1.1; x += 0.01) {
            double density = distribution.density(x);
            Assert.assertEquals(1.0 / (1.0 - 0.0), density, 1e-5);
        }
    }

    @Test
    public void testCumulativeProbability() {
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            if (x == 0.0) {
                Assert.assertEquals(0.0, cumulativeProbability, 1e-9);
            } else {
                Assert.assertEquals(x / (1.0 - 0.0), cumulativeProbability, 1e-5);
            }
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        try {
            new UniformRealDistribution(random, 1, 0);
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0.0);
            Assert.assertTrue(sample <= 1.0);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        Assert.assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 1e-5);
    }

    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(0.0008333333333333333, distribution.getNumericalVariance(), 1e-5);
    }
}