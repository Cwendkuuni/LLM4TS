import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractTests;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest extends AbstractTests {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        int[] domain = dist.getDomain();
        assertEquals(2, domain.length);
        assertEquals(domain[0], getLowerDomain(populationSize, numberOfSuccesses, sampleSize));
        assertEquals(domain[1], getUpperDomain(numberOfSuccesses, sampleSize));

        // check bounds are correct
        int lowerBound = domain[0];
        int upperBound = domain[1];
        for (int i = lowerBound; i <= upperBound; i++) {
            double prob = dist.probability(i);
            double cumProb = dist.cumulativeProbability(i);
            double upperCumProb = dist.upperCumulativeProbability(i);

            // check probability and cumulative probability agree
            assertEquals(cumProb, prob + cumProb, 1e-13);

            // check upper and lower cumulative probabilities agree
            assertEquals(upperCumProb, cumProb, 1e-13);
        }

        // test getSupportLowerBound and getSupportUpperBound
        int lower = dist.getSupportLowerBound();
        int upper = dist.getSupportUpperBound();
        assertEquals(lower, getLowerDomain(populationSize, numberOfSuccesses, sampleSize));
        assertEquals(upper, getUpperDomain(numberOfSuccesses, sampleSize));

        // test isSupportConnected
        assertTrue(dist.isSupportConnected());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 4;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = null;

        try {
            dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException e) {
            dist = e.getCause();
        }

        fail("Expected NotPositiveException for negative number of successes.");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = null;

        try {
            dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            dist = e.getCause();
        }

        fail("Expected NotStrictlyPositiveException for zero population size.");
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 4;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = null;

        try {
            dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            dist = e.getCause();
        }

        fail("Expected NumberIsTooLargeException for number of successes greater than population size.");
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = null;

        try {
            dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            dist = e.getCause();
        }

        fail("Expected NumberIsTooLargeException for sample size greater than population size.");
    }

    @Test
    public void testHypergeometricDistributionMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);

        double expectedMean = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        assertEquals(expectedMean, dist.getNumericalMean(), 1e-13);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);

        double expectedVariance = (double) sampleSize * numberOfSuccesses * (double) (populationSize - sampleSize) * (double) (populationSize - numberOfSuccesses)
                / ((double) (populationSize * populationSize) * (double) (populationSize - 1));
        assertEquals(expectedVariance, dist.getNumericalVariance(), 1e-13);
    }

    private static int getLowerDomain(int n, int m, int k) {
        return Math.max(0, m - (n - k));
    }

    private static int getUpperDomain(int m, int k) {
        return Math.min(k, m);
    }
}