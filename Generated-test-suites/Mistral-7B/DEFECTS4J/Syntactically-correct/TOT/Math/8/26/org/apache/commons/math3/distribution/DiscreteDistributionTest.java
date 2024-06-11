package org.apache.commons.math3.distribution;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            // Test probability for valid inputs
            Assert.assertEquals(0.3, distribution.probability(1), 0.001);
            Assert.assertEquals(0.4, distribution.probability(2), 0.001);
            Assert.assertEquals(0.3, distribution.probability(3), 0.001);

            // Test probability for invalid inputs
            Assert.assertEquals(0.0, distribution.probability(null), 0.0);
            Assert.assertEquals(0.0, distribution.probability(4), 0.0);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exceptions
            Assert.assertEquals(NotPositiveException.class, e.getClass());
        }
    }

    @Test
    public void testSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

            // Test samples are as expected
            Assert.assertEquals(3, actualSamples.size());
            Assert.assertArrayEquals(new Integer[]{1, 2, 3},
                    Arrays.stream(actualSamples.toArray(new Pair[0])).mapToInt(p -> p.getKey().intValue()).toArray());

            // Test probabilities sum to one
            double sum = 0.0;
            for (Pair<Integer, Double> sample : actualSamples) {
                sum += sample.getValue();
            }
            Assert.assertEquals(1.0, sum, 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exceptions
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Integer actualSample = distribution.sample();

            // Test sample is as expected
            List<Integer> expectedSamples = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                expectedSamples.add(distribution.sample().intValue());
            }
            int[] expectedSampleArray = expectedSamples.stream().mapToInt(i -> i).toArray();
            Assert.assertArrayEquals(expectedSampleArray, Arrays.stream(Collections.nCopies(1000, actualSample).toArray()).mapToInt(i -> i.intValue()).toArray());
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exceptions
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Integer[] actualSamples = distribution.sample(10);

            // Test samples are as expected
            List<Integer> expectedSamples = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                expectedSamples.add(distribution.sample().intValue());
            }
            Assert.assertArrayEquals(expectedSamples.toArray(new Integer[0]), actualSamples);
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exceptions
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            // Test reseeding with the same seed does not change the distribution
            long seed = 123456789;
            distribution.reseedRandomGenerator(seed);
            List<Integer> samples1 = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                samples1.add(distribution.sample().intValue());
            }

            distribution.reseedRandomGenerator(seed);
            List<Integer> samples2 = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                samples2.add(distribution.sample().intValue());
            }

            Assert.assertEquals(samples1.size(), samples2.size());
            Assert.assertTrue(samples1.containsAll(samples2));
            Assert.assertTrue(samples2.containsAll(samples1));

            // Test reseeding with a different seed changes the distribution
            long newSeed = 987654321;
            distribution.reseedRandomGenerator(newSeed);
            List<Integer> samples3 = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                samples3.add(distribution.sample().intValue());
            }

            Assert.assertNotEquals(samples1, samples3);
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exceptions
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}