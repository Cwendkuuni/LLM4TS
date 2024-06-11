import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            Assert.assertEquals(populationSize, dist.getPopulationSize());
            Assert.assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
            Assert.assertEquals(sampleSize, dist.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            Assert.fail("Failed to instantiate HypergeometricDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= populationSize; x++) {
            double prob = dist.cumulativeProbability(x);
            double expectedProb = getExpectedCumulativeProbability(populationSize, numberOfSuccesses, sampleSize, x);
            Assert.assertEquals(expectedProb, prob, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= populationSize; x++) {
            double prob = dist.probability(x);
            double expectedProb = getExpectedProbability(populationSize, numberOfSuccesses, sampleSize, x);
            Assert.assertEquals(expectedProb, prob, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= populationSize; x++) {
            double prob = dist.upperCumulativeProbability(x);
            double expectedProb = getExpectedUpperCumulativeProbability(populationSize, numberOfSuccesses, sampleSize, x);
            Assert.assertEquals(expectedProb, prob, 0.0001);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 5;
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SUCCESSES, e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(LocalizedFormats.POPULATION_SIZE, e.getMessage());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 5;
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, e.getMessage());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 6;
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, e.getMessage());
        }
    }

    private double getExpectedCumulativeProbability(int populationSize, int numberOfSuccesses, int sampleSize, int x) {
        int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getDomain();
        return (x >= domain[0] && x <= domain[1]) ? (double) numberOfSuccesses / populationSize : 0.0;
    }

    private double getExpectedProbability(int populationSize, int numberOfSuccesses, int sampleSize, int x) {
        int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getDomain();
        return (x >= domain[0] && x <= domain[1]) ? (double) FastMath.binomialProbability(numberOfSuccesses, populationSize, x / (double) sampleSize) : 0.0;
    }

    private double getExpectedUpperCumulativeProbability(int populationSize, int numberOfSuccesses, int sampleSize, int x) {
        int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getDomain();
        return (x >= domain[0] && x <= domain[1]) ? (double) numberOfSuccesses / populationSize : 1.0;
    }
}