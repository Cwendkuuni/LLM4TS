import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {

    @org.junit.Before
    public void setUp() {
        random = new Well19937c();
    }

    private Well19937c random;

    @org.junit.Test
    public void testConstructorDefault() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorLowerThanUpper() {
        UniformRealDistribution dist = new UniformRealDistribution(5, 3);
    }

    @org.junit.Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        for (double x = 3; x <= 5; x++) {
            double density = dist.density(x);
            assertEquals(1 / (5 - 3), density, 1e-9);
        }
        double densityOutside = dist.density(-1);
        assertEquals(0, densityOutside, 0);
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        for (double x = 3; x <= 5; x++) {
            double cdf = dist.cumulativeProbability(x);
            assertEquals(x / (5 - 3), cdf, 1e-9);
        }
        double cdfOutside = dist.cumulativeProbability(-1);
        assertEquals(0, cdfOutside, 0);
    }

    @org.junit.Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0);
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertEquals(4.5, dist.getNumericalMean(), 1e-9);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertEquals(0.25, dist.getNumericalVariance(), 1e-9);
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertEquals(3, dist.getSupportLowerBound(), 0);
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertEquals(5, dist.getSupportUpperBound(), 0);
    }

    @org.junit.Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @org.junit.Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        assertTrue(dist.isSupportConnected());
    }

    @org.junit.Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(3, 5);
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 3 && sample < 5);
        }
    }
}