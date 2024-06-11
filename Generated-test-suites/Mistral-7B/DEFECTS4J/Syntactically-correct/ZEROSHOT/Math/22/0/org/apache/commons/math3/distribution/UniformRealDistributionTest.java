import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertNotNull(dist);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 3.0);
        assertNotNull(dist);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(3.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(2.0, dist.getNumericalMean(), 0.0);
        assertEquals(1.4821373115839955, dist.getNumericalVariance(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidBounds() {
        new UniformRealDistribution(3.0, 2.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Well19937c rng = new Well19937c();
        dist.setSeed(rng.nextLong());
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound());
            assertTrue(sample < dist.getSupportUpperBound());
        }
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            assertEquals(x, cumProb, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionNegativeValues() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = -0.1; x <= 0.0; x += -0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 0.001);
            double cumProb = dist.cumulativeProbability(x);
            assertEquals(0.0, cumProb, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionSupportOutOfBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double lowerBound = -1.0;
        double upperBound = 2.0;
        double density = dist.density(lowerBound);
        assertEquals(0.0, density, 0.001);
        double cumProb = dist.cumulativeProbability(lowerBound);
        assertEquals(0.0, cumProb, 0.001);
        density = dist.density(upperBound);
        assertEquals(0.0, density, 0.001);
        cumProb = dist.cumulativeProbability(upperBound);
        assertEquals(1.0, cumProb, 0.001);
    }
}