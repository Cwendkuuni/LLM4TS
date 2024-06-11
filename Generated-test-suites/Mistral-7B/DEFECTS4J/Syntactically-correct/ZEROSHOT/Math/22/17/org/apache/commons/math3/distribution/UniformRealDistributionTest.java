import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3, 0.0001);
        assertEquals(0.1, dist.getLowerBound(), 0.0001);
        assertEquals(0.3, dist.getUpperBound(), 0.0001);
        assertEquals(0.2, dist.sample(), 0.0001);
    }

    @Test
    public void testUniformRealDistributionCustomConstructionWithRNG() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.1, 0.3, 0.0001);
        assertEquals(0.1, dist.getLowerBound(), 0.0001);
        assertEquals(0.3, dist.getUpperBound(), 0.0001);
        assertEquals(0.2, dist.sample(), 0.0001);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double x = 0.2;
        double expected = 1.0 / (0.3 - 0.1);
        assertEquals(expected, dist.density(x), 0.0);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double x = 0.2;
        double expected = (x - 0.1) / (0.3 - 0.1);
        assertEquals(expected, dist.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testUniformRealDistributionNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double expected = 0.2;
        assertEquals(expected, dist.getNumericalMean(), 0.0);
    }

    @Test
    public void testUniformRealDistributionNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double expected = ((0.3 - 0.1) * (0.3 - 0.1)) / 12;
        assertEquals(expected, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double expected = 0.1;
        assertEquals(expected, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double expected = 0.3;
        assertEquals(expected, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertTrue(dist.isSupportConnected());
    }
}