import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstructor() {
        new UniformRealDistribution(1.0, 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, dist.getLowerBound(), 0.0);
        assertEquals(1.5, dist.getUpperBound(), 0.0);
        assertEquals(1.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.225, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            double expected = 1.0 / (1.0 - 0.0);
            assertEquals(expected, density, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double p = dist.cumulativeProbability(x);
            double expected = x;
            assertEquals(expected, p, 1e-6);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.5, 1.5);
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.5 && sample <= 1.5);
        }
    }

    @Test
    public void testMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double mean = dist.getNumericalMean();
        assertEquals(0.5, mean, 0.0);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double variance = dist.getNumericalVariance();
        assertEquals(0.25, variance, 0.0);
    }

    @Test
    public void testSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportConnected());
    }
}