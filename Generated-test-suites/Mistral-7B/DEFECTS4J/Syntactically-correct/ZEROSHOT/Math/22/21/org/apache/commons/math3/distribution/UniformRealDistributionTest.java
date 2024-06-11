import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(1.0, 1.0);
    }

    @Test
    public void testConstructorWithCustomBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, distribution.getLowerBound(), 0.0);
        assertEquals(3.0, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithCustomBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0, 0.0001);
        assertEquals(1.0, distribution.getLowerBound(), 0.0001);
        assertEquals(3.0, distribution.getUpperBound(), 0.0001);
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 1.0, 3.0);
        assertEquals(1.0, distribution.getLowerBound(), 0.0);
        assertEquals(3.0, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            double eps = 1e-5;
            assertEquals("Expected density to be approximately " + (1.0 / (1.0 - 0.0)) + " at " + x, (1.0 / (1.0 - 0.0)) * eps, density, eps);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = distribution.cumulativeProbability(x);
            double eps = 1e-5;

            if (x == 0.0) {
                assertEquals("Expected cumulative probability to be 0.0 at " + x, 0.0, cdf, eps);
            } else if (x == 1.0) {
                assertEquals("Expected cumulative probability to be 1.0 at " + x, 1.0, cdf, eps);
            } else {
                double expectedCdf = (x - 0.0) / (1.0 - 0.0);
                assertEquals("Expected cumulative probability to be approximately " + expectedCdf + " at " + x, expectedCdf, cdf, eps);
            }
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double eps = 1e-5;
            assertEquals("Expected inverse cumulative probability to be approximately " + (p * (1.0 - 0.0) + 0.0) + " at " + p, p * (1.0 - 0.0) + 0.0, x, eps);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        distribution.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue("Expected sample to be within bounds", sample >= 0.0 && sample < 1.0);
        }
    }

    @Test
    public void testNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double numericalMean = distribution.getNumericalMean();
        assertEquals(0.5, numericalMean, 0.0);
    }

    @Test
    public void testNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(0.005555555555555556, numericalVariance, 0.0);
    }

    @Test
    public void testSupport() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

}