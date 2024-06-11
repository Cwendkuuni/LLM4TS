import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 3;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 13;
        int sampleSize = 3;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 13;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
        }
    }

    @Test
    public void testConstructorValidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = dist.getDomain();

        // For x < lower bound
        double x = domain[0] - 1;
        double expected = 0.0;
        double actual = dist.cumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x = lower bound
        x = domain[0];
        expected = 0.0;
        actual = dist.cumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x = upper bound
        x = domain[1];
        expected = 1.0;
        actual = dist.cumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x > upper bound
        x = domain[1] + 1;
        expected = 1.0;
        actual = dist.cumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = dist.getDomain();

        // For x < lower bound
        double x = domain[0] - 1;
        double expected = 0.0;
        double actual = dist.probability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x = lower bound
        x = domain[0];
        expected = dist.probability(domain[0]);
        actual = dist.probability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x = upper bound
        x = domain[1];
        expected = dist.probability(domain[1]);
        actual = dist.probability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x > upper bound
        x = domain[1] + 1;
        expected = 0.0;
        actual = dist.probability((int) x);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = dist.getDomain();

        // For x < lower bound
        double x = domain[0] - 1;
        double expected = 1.0;
        double actual = dist.upperCumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x = lower bound
        x = domain[0];
        expected = 1.0 - dist.cumulativeProbability(domain[0]);
        actual = dist.upperCumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x = upper bound
        x = domain[1];
        expected = 0.0;
        actual = dist.upperCumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);

        // For x > upper bound
        x = domain[1] + 1;
        expected = 0.0;
        actual = dist.upperCumulativeProbability((int) x);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testProbabilityDistribution() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 100000;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] observedFrequencies = new int[dist.getSupportUpperBound() + 1];
        for (int i = 0; i < sampleSize; i++) {
            int x = dist.nextInt();
            observedFrequencies[x]++;
        }

        int[] expectedFrequencies = new int[dist.getSupportUpperBound() + 1];
        for (int x = 0; x <= dist.getSupportUpperBound(); x++) {
            expectedFrequencies[x] = (int) (sampleSize * dist.probability(x));
        }

        for (int x = 0; x <= dist.getSupportUpperBound(); x++) {
            int diff = Math.abs(expectedFrequencies[x] - observedFrequencies[x]);
            if (diff > 10) {
                fail("Expected frequency " + expectedFrequencies[x] + ", observed frequency " + observedFrequencies[x]);
            }
        }
    }

    @Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 10;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 10;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (double) (populationSize * populationSize * (populationSize - 1));
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 0.001);
    }
}