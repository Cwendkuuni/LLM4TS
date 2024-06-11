import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstruction() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBounds() {
        UniformRealDistribution distribution = null;
        try {
            distribution = new UniformRealDistribution(2.0, 1.0);
        } catch (NumberIsTooLargeException e) {
            // expected exception
        }
        assertNull(distribution);
    }

    @Test
    public void testUniformRealDistributionCustomConstruction() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, distribution.getLowerBound(), 0.0);
        assertEquals(1.5, distribution.getUpperBound(), 0.0);
        assertEquals(1.0, distribution.getNumericalMean(), 0.0);
        assertEquals(0.2246484775621495, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructionWithInverseCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5, 1e-6);
        assertEquals(0.5, distribution.getLowerBound(), 0.0);
        assertEquals(1.5, distribution.getUpperBound(), 0.0);
        assertEquals(1.0, distribution.getNumericalMean(), 0.0);
        assertEquals(0.2246484775621495, distribution.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionCustomConstructionWithRandomGenerator() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0.5, 1.5);
        assertEquals(0.5, distribution.getLowerBound(), 0.0);
        assertEquals(1.5, distribution.getUpperBound(), 0.0);
        assertEquals(1.0, distribution.getNumericalMean(), 0.0);
        assertEquals(0.2246484775621495, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.2, 0.6);
        double density;

        density = distribution.density(0.3);
        assertEquals(1.5 / (0.6 - 0.2), density, 0.0);

        density = distribution.density(0.5);
        assertEquals(3.0 / (0.6 - 0.2), density, 0.0);

        density = distribution.density(0.6);
        assertEquals(1.5 / (0.6 - 0.2), density, 0.0);

        density = distribution.density(0.1);
        assertEquals(3.0 / (0.6 - 0.2), density, 0.0);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.2, 0.6);
        double cumulativeProbability;

        cumulativeProbability = distribution.cumulativeProbability(0.1);
        assertEquals(0.0 / (0.6 - 0.2), cumulativeProbability, 0.0);

        cumulativeProbability = distribution.cumulativeProbability(0.3);
        assertEquals(0.16666666666666664 / (0.6 - 0.2), cumulativeProbability, 0.0);

        cumulativeProbability = distribution.cumulativeProbability(0.5);
        assertEquals(0.5 / (0.6 - 0.2), cumulativeProbability, 0.0);

        cumulativeProbability = distribution.cumulativeProbability(0.6);
        assertEquals(0.8333333333333333 / (0.6 - 0.2), cumulativeProbability, 0.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.2, 0.6);
        double sample;

        for (int i = 0; i < 1000; i++) {
            sample = distribution.sample();
            assertTrue(sample >= 0.2 && sample <= 0.6);
        }
    }

    @Test
    public void testUniformRealDistributionSampleWithInverseCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.2, 0.6, 1e-6);
        double inverseCumulativeProbability, sample;

        inverseCumulativeProbability = 0.4;
        sample = distribution.inverseCumulativeProbability(inverseCumulativeProbability);
        assertEquals(0.4444444444444445, sample, 1e-6);

        inverseCumulativeProbability = 0.5;
        sample = distribution.inverseCumulativeProbability(inverseCumulativeProbability);
        assertEquals(0.5, sample, 0.0);

        inverseCumulativeProbability = 0.55;
        sample = distribution.inverseCumulativeProbability(inverseCumulativeProbability);
        assertEquals(0.5555555555555556, sample, 1e-6);
    }
}