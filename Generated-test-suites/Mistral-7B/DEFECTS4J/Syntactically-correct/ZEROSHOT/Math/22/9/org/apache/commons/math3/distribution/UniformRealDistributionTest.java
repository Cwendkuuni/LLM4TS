import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0.001);
        assertEquals(1, dist.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1, 5);
        assertEquals(1, dist.getSupportLowerBound(), 0.001);
        assertEquals(5, dist.getSupportUpperBound(), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstructor() throws NumberIsTooLargeException {
        new UniformRealDistribution(5, 1);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double density;
        for (double x = 0; x <= 1; x += 0.01) {
            density = dist.density(x);
            assertEquals(1, density, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            if (x <= 0.5) {
                assertEquals(x / 1, cumProb, 0.001);
            } else {
                assertEquals(1 - (1 - x / 1), cumProb, 0.001);
            }
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Well19937c rng = new Well19937c();
        UniformRealDistribution copyDist = new UniformRealDistribution(rng, 0, 1);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            double sampleCopy = copyDist.sample();
            assertEquals(sample, sampleCopy, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double numericalMean = dist.getNumericalMean();
        assertEquals(0.5, numericalMean, 0.001);
    }

    @Test
    public void testUniformRealDistributionNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(0.001666666666667, numericalVariance, 0.0000001);
    }

    @Test
    public void testUniformRealDistributionSupport() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0, dist.getSupportLowerBound(), 0.001);
        assertEquals(1, dist.getSupportUpperBound(), 0.001);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }
}