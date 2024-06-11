import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link DiscreteDistribution} class.
 */
public class DiscreteDistributionTest {

    /**
     * Test method for {@link DiscreteDistribution#DiscreteDistribution(List)} constructor.
     */
    @Test
    public void testDiscreteDistributionListConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            int[] sample = distribution.sample(10);
            Assert.assertArrayEquals(new int[]{1, 1, 2, 2, 2, 2, 3, 3, 3, 3}, sample);
            Assert.assertEquals(0.25, distribution.probability(1), 0.001);
            Assert.assertEquals(0.5, distribution.probability(2), 0.001);
            Assert.assertEquals(0.25, distribution.probability(3), 0.001);
        } catch (NotPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link DiscreteDistribution#DiscreteDistribution(RandomGenerator, List)} constructor.
     */
    @Test
    public void testDiscreteDistributionRandomGeneratorListConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        RandomDataGenerator random = new RandomDataGenerator();
        DiscreteDistribution<Integer> distribution;

        try {
            distribution = new DiscreteDistribution<>(random, samples);
            int[] sample = distribution.sample(10);
            Assert.assertArrayEquals(new int[]{1, 1, 2, 2, 2, 2, 3, 3, 3, 3}, sample);
            Assert.assertEquals(0.25, distribution.probability(1), 0.001);
            Assert.assertEquals(0.5, distribution.probability(2), 0.001);
            Assert.assertEquals(0.25, distribution.probability(3), 0.001);
        } catch (NotPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link DiscreteDistribution#sample()} method.
     */
    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        DiscreteDistribution<Integer> distribution;

        try {
            distribution = new DiscreteDistribution<>(samples);
            int sample = distribution.sample();
            List<Integer> samplesList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                samplesList.add(distribution.sample());
            }
            Assert.assertTrue(samplesList.contains(1));
            Assert.assertTrue(samplesList.contains(2));
            Assert.assertTrue(samplesList.contains(3));
            Assert.assertEquals(1, distribution.probability(sample), 0.001);
        } catch (NotPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link DiscreteDistribution#sample(int)} method.
     */
    @Test
    public void testSampleIntMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        DiscreteDistribution<Integer> distribution;

        try {
            distribution = new DiscreteDistribution<>(samples);
            int[] sample = distribution.sample(10);
            Assert.assertArrayEquals(new int[]{1, 1, 1, 2, 2, 2, 2, 2, 2, 3}, sample);
            for (int i = 0; i < sample.length; i++) {
                Assert.assertTrue(Arrays.asList(singletonList(1), singletonList(2), singletonList(3)).contains(sample[i]));
                Assert.assertEquals(1.0 / 3.0, distribution.probability(sample[i]), 0.001);
            }
        } catch (NotPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link DiscreteDistribution#probability(Object)} method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNullInput() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new ArrayList<>());
        distribution.probability(null);
    }

    /**
     * Test method for {@link DiscreteDistribution#probability(Object)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityInvalidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        distribution.probability(new Object() {});
    }

    /**
     * Test method for {@link DiscreteDistribution#probability(Object)} method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        int sample = distribution.sample();
        Assert.assertEquals(distribution.probability(sample), 1.0 / 3.0, 0.001);
    }

    /**
     * Test method for {@link DiscreteDistribution#reseedRandomGenerator(long)} method.
     */
    @Test
    public void testReseedRandomGeneratorMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);

        int sample = distribution.sample();
        Assert.assertNotSame(distribution.sample(), sample);
    }

    /**
     * Test method for {@link DiscreteDistribution#getSamples()} method.
     */
    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }
}