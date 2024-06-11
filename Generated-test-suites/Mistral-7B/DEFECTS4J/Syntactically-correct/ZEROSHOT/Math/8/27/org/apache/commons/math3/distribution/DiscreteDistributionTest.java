import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.Pair;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            // Test valid input
            int validInput = 2;
            double expectedProbability = 0.3;
            double actualProbability = distribution.probability(validInput);
            assertEquals("Invalid probability for valid input", expectedProbability, actualProbability, 0.001);

            // Test invalid input (null)
            Integer nullInput = null;
            double expectedProbabilityNull = 0.0;
            double actualProbabilityNull = distribution.probability(nullInput);
            assertEquals("Invalid probability for null input", expectedProbabilityNull, actualProbabilityNull);

            // Test invalid input (non-singleton value)
            int invalidInput = 4;
            double expectedProbabilityInvalid = 0.0;
            double actualProbabilityInvalid = distribution.probability(invalidInput);
            assertEquals("Invalid probability for invalid input", expectedProbabilityInvalid, actualProbabilityInvalid);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            // Test valid sampling
            int expectedSample = 2;
            int actualSample = distribution.sample().intValue();
            assertEquals("Invalid sample", expectedSample, actualSample);

            // Test multiple valid sampling
            int[] expectedSamples = {2, 1, 3, 2};
            int[] actualSamples = new int[4];
            for (int i = 0; i < 4; i++) {
                actualSamples[i] = distribution.sample().intValue();
            }
            assertArrayEquals("Invalid multiple samples", expectedSamples, actualSamples);
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            List<Pair<Integer, Double>> expectedSamples = samples;
            List<Pair<Integer, Double>> actualSamples = distribution.getSamples();
            assertEquals("Invalid samples", expectedSamples, actualSamples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReSeedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Well19937c rng = distribution.random;

            // Test reSeedRandomGenerator with valid seed
            long seed = 12345;
            distribution.reseedRandomGenerator(seed);
            long expectedNextInt = 148468530; // calculated using given seed and RNG algorithm
            long actualNextInt = rng.nextLong();
            assertEquals("Invalid nextInt after reSeeding", expectedNextInt, actualNextInt);

            // Test reSeedRandomGenerator with null seed
            distribution.reseedRandomGenerator(null);
            fail("ReSeeding with null seed should throw an IllegalArgumentException");

            // Test reSeedRandomGenerator with negative seed
            distribution.reseedRandomGenerator(-12345);
            fail("ReSeeding with negative seed should throw a NotStrictlyPositiveException");
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}