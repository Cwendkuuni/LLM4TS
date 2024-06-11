import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, dist.getPopulationSize());
            assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
            assertEquals(sampleSize, dist.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 7);
        assertEquals(0.21875, dist.probability(0), 0.0001);
        assertEquals(0.0, dist.probability(-1));
        assertEquals(0.3125, dist.probability(1), 0.0001);
        assertEquals(0.15625, dist.probability(2), 0.0001);
        assertEquals(0.15625, dist.probability(3), 0.0001);
        assertEquals(0.125, dist.probability(4), 0.0001);
        assertEquals(0.0625, dist.probability(5), 0.0001);
        assertEquals(0.0, dist.probability(6), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 7);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.21875, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.3125, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.46875, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0.625, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.75, dist.cumulativeProbability(4), 0.0001);
        assertEquals(0.875, dist.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 7);
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        assertEquals(0.79125, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.6875, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.5625, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.44375, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.34375, dist.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.25, dist.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.125, dist.upperCumulativeProbability(6), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionSample() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 7);
        Well19937c rng = new Well19937c();
        for (int i = 0; i < 10000; i++) {
            int x = dist.sample(rng.nextInt());
            assertTrue(dist.cumulativeProbability(x) >= dist.cumulativeProbability(x - 1));
        }
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double numericalVariance = dist.getNumericalVariance();
        double expectedVariance = (5.0 * 5.0 * (10.0 - 5.0) * (10.0 - 5.0)) / (10.0 * 10.0 * (10.0 - 1.0));
        assertEquals(expectedVariance, numericalVariance, 0.0001);
    }
}