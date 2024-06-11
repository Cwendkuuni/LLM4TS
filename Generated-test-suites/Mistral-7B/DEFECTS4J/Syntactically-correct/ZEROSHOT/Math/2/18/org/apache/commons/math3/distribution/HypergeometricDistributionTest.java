import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionParameters;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() throws NotPositiveException {
        // Test with valid arguments
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test mean and variance
        double mean = hd.getNumericalMean();
        double variance = hd.getNumericalVariance();
        assertEquals(mean, (double) (sampleSize * numberOfSuccesses / (double) populationSize));
        assertEquals(variance, hd.calculateNumericalVariance());

        // Test probability and cumulative probability
        for (int i = 0; i <= sampleSize; i++) {
            double p = hd.probability(i);
            double cdf = hd.cumulativeProbability(i);
            assertEquals("Probability of i is not correct.", p, cdf, 1e-14);
        }

        // Test lower and upper bounds
        int lowerBound = hd.getSupportLowerBound();
        int upperBound = hd.getSupportUpperBound();
        assertEquals(lowerBound, 5);
        assertEquals(upperBound, 5);

        // Test support is connected
        assertTrue(hd.isSupportConnected());

        // Test random number generation
        HypergeometricDistributionParameters params = new HypergeometricDistributionParameters(populationSize, numberOfSuccesses, sampleSize);
        RandomGenerator rg = new Well19937c();
        HypergeometricDistribution hdGen = new HypergeometricDistribution(rg, populationSize, numberOfSuccesses, sampleSize);

        int expectedSampleMean = (int) Math.round(hd.getNumericalMean());
        int expectedSampleVariance = (int) Math.round(hd.calculateNumericalVariance() * 100);

        int sampleSizeForTest = 100000;
        int[] samples = new int[sampleSizeForTest];

        for (int i = 0; i < sampleSizeForTest; i++) {
            samples[i] = hdGen.nextInt();
        }

        double sampleMean = new StandardDeviation().evaluate(samples);
        int sampleVariance = (int) Math.round(new StandardDeviation().evaluate(samples.clone()) * 100);

        assertEquals(sampleMean, expectedSampleMean);
        assertEquals(sampleVariance, expectedSampleVariance);

        // Test tail probabilities
        double p0 = hd.probability(0);
        double pMax = hd.probability(hd.getSupportUpperBound());
        double p1 = hd.probability(1);

        double cdf0 = hd.cumulativeProbability(0);
        double cdfMax = hd.cumulativeProbability(hd.getSupportUpperBound());
        double cdf1 = hd.cumulativeProbability(1);

        double ucdf0 = hd.upperCumulativeProbability(0);
        double ucdfMax = hd.upperCumulativeProbability(hd.getSupportUpperBound());
        double ucdf1 = hd.upperCumulativeProbability(1);

        assertEquals(p0, cdf0, 1e-14);
        assertEquals(pMax, cdfMax, 1e-14);
        assertEquals(p1, cdf1 + p0 - cdfMax, 1e-14);
        assertEquals(p1, ucdf1, 1e-14);
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidPopulationSize() throws NotPositiveException {
        int populationSize = -1;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSizeOrZero() throws NotStrictlyPositiveException {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() throws NotPositiveException {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSampleSize() throws NotStrictlyPositiveException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 0;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccessesOrZero() throws NotPositiveException {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidPopulationSizeOrNumberOfSuccesses() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException ex) {
            assertEquals("The number of successes in the population cannot be greater than the population size.", ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSizeOrNumberOfSuccesses() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException ex) {
            assertEquals("The sample size cannot be greater than the population size.", ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidNumberOfSuccessesOrPopulationSize() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 5;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException ex) {
            assertEquals("The number of successes in the population cannot be greater than the population size.", ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSizeOrPopulationSize() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 11;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException ex) {
            assertEquals("The sample size cannot be greater than the population size.", ex.getMessage());
            throw ex;
        }
    }
}