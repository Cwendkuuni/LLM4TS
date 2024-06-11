import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 5;
        int numberOfSuccesses = -2;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 6;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expected = 0.2521;
        double actual = hd.probability(x);
        assertEquals(expected, actual, Precision.epsilon(1e-5));
    }

    @Test
    public void testHypergeometricDistributionProbabilityEdgeCases() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int xMin = hd.getSupportLowerBound();
        double expectedMin = 0.0;
        double actualMin = hd.probability(xMin);
        assertEquals(expectedMin, actualMin, Precision.epsilon(1e-5));

        int xMax = hd.getSupportUpperBound();
        double expectedMax = 0.5;
        double actualMax = hd.probability(xMax);
        assertEquals(expectedMax, actualMax, Precision.epsilon(1e-5));

        int xPopulationSize = populationSize;
        double expectedPopulationSize = 1.0;
        double actualPopulationSize = hd.probability(xPopulationSize);
        assertEquals(expectedPopulationSize, actualPopulationSize, Precision.epsilon(1e-5));
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expected = 0.2708;
        double actual = hd.cumulativeProbability(x);
        assertEquals(expected, actual, Precision.epsilon(1e-5));
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbabilityEdgeCases() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int xMin = hd.getSupportLowerBound();
        double expectedMin = 0.0;
        double actualMin = hd.cumulativeProbability(xMin);
        assertEquals(expectedMin, actualMin, Precision.epsilon(1e-5));

        int xMax = hd.getSupportUpperBound();
        double expectedMax = 0.5;
        double actualMax = hd.cumulativeProbability(xMax);
        assertEquals(expectedMax, actualMax, Precision.epsilon(1e-5));

        int xPopulationSize = populationSize;
        double expectedPopulationSize = 0.5;
        double actualPopulationSize = hd.cumulativeProbability(xPopulationSize);
        assertEquals(expectedPopulationSize, actualPopulationSize, Precision.epsilon(1e-5));
    }

    @Test
    public void testHypergeometricDistributionSample() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        int expectedCount = 1;

        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 10000; i++) {
            int sample = hd.sample(rng);
            if (sample == x) {
                expectedCount++;
            }
        }

        int actualCount = (int) Math.round(expectedCount * 100.0 / 10000.0);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testHypergeometricDistributionSampleEdgeCases() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int xMin = hd.getSupportLowerBound();
        int expectedMin = 0;

        RandomGenerator rng = RandomUtils.createNonThreadSafeGenerator();
        for (int i = 0; i < 10000; i++) {
            int sample = hd.sample(rng);
            if (sample == xMin) {
                expectedMin++;
            }
        }

        int actualMin = hd.getProbability(xMin) * 10000;
        assertEquals(expectedMin, actualMin);

        int xMax = hd.getSupportUpperBound();
        int expectedMax = populationSize;

        for (int i = 0; i < 10000; i++) {
            int sample = hd.sample(rng);
            if (sample == xMax) {
                expectedMax++;
            }
        }

        int actualMax = hd.getProbability(xMax) * 10000;
        assertEquals(expectedMax, actualMax);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedVariance = 0.1111;
        double actualVariance = hd.getNumericalVariance();
        assertEquals(expectedVariance, actualVariance, Precision.epsilon(1e-5));
    }
}