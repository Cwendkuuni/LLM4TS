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

        UniformRealDistribution distLowerUpper = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, distLowerUpper.getLowerBound(), 0.0);
        assertEquals(0.4, distLowerUpper.getUpperBound(), 0.0);

        try {
            new UniformRealDistribution(1.0, 0.5);
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.01) {
            double density = dist.density(x);
            double expectedDensity = 1 / (dist.getUpperBound() - dist.getLowerBound());
            assertEquals(expectedDensity, density, 0.0001);
        }

        UniformRealDistribution distLowerUpper = new UniformRealDistribution(0.1, 0.4);

        for (double x = distLowerUpper.getLowerBound(); x <= distLowerUpper.getUpperBound(); x += 0.01) {
            double density = distLowerUpper.density(x);
            double expectedDensity = 1 / (distLowerUpper.getUpperBound() - distLowerUpper.getLowerBound());
            assertEquals(expectedDensity, density, 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.01) {
            double cumulativeProbability = dist.cumulativeProbability(x);
            double expectedCumulativeProbability = x / (dist.getUpperBound() - dist.getLowerBound());
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
        }

        UniformRealDistribution distLowerUpper = new UniformRealDistribution(0.1, 0.4);

        for (double x = distLowerUpper.getLowerBound(); x <= distLowerUpper.getUpperBound(); x += 0.01) {
            double cumulativeProbability = distLowerUpper.cumulativeProbability(x);
            double expectedCumulativeProbability = (x - distLowerUpper.getLowerBound()) / (distLowerUpper.getUpperBound() - distLowerUpper.getLowerBound());
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Well19937c rng = new Well19937c();
        dist.setSeed(rng.nextLong());

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }

        UniformRealDistribution distLowerUpper = new UniformRealDistribution(0.1, 0.4);
        distLowerUpper.setSeed(rng.nextLong());

        for (int i = 0; i < 1000; i++) {
            double sample = distLowerUpper.sample();
            assertTrue(sample >= distLowerUpper.getLowerBound());
            assertTrue(sample < distLowerUpper.getUpperBound());
        }
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double numericalMean = dist.getNumericalMean();
        assertEquals(0.5, numericalMean, 0.0001);

        UniformRealDistribution distLowerUpper = new UniformRealDistribution(0.1, 0.4);
        double numericalMean = distLowerUpper.getNumericalMean();
        assertEquals(0.255, numericalMean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(0.000625, numericalVariance, 0.000001);

        UniformRealDistribution distLowerUpper = new UniformRealDistribution(0.1, 0.4);
        double numericalVariance = distLowerUpper.getNumericalVariance();
        assertEquals(0.000111, numericalVariance, 0.000001);
    }
}