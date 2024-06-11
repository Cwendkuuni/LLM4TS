import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4, 0.01);
        assertEquals(0.1, distCustom.getLowerBound(), 0.0);
        assertEquals(0.4, distCustom.getUpperBound(), 0.0);
        assertEquals(0.01, distCustom.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionLowerThanUpperBoundException() {
        new UniformRealDistribution(2.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x < 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            double expectedCumProb = x;
            assertEquals(expectedCumProb, cumProb, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4, 0.001);
        assertEquals(0.001, distCustom.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.255, distCustom.getNumericalMean(), 0.001);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.008333333333333333, dist.getNumericalVariance(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.00016666666666666666, distCustom.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, distCustom.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.4, distCustom.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertTrue(distCustom.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertFalse(distCustom.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.4);
        assertTrue(distCustom.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Well19937c rng = new Well19937c();
        dist.setSeed(rng);
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound() && sample < dist.getUpperBound());
        }
    }
}