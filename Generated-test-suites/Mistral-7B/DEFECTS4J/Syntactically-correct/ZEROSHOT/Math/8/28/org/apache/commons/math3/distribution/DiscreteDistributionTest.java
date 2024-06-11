import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test method for DiscreteDistribution constructor.
     */
    @Test
    public void testDiscreteDistributionConstruction() {
        // Test empty list
        try {
            new DiscreteDistribution<>(Collections.emptyList());
            Assert.fail("Expected NotPositiveException for empty list");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test list with negative probabilities
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -1.0));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail("Expected NotPositiveException for negative probability");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test list with zero probabilities sum
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail("Expected MathArithmeticException for zero probabilities sum");
        } catch (MathArithmeticException e) {
            // Expected
        }

        // Test list with infinite probabilities
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail("Expected MathIllegalArgumentException for infinite probability");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test valid list
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Assert.assertEquals(3, distribution.singletons.size());
        Arrays.sort(distribution.singletons.toArray());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
        Assert.assertArrayEquals(new double[]{0.25, 0.5, 0.25}, distribution.probabilities, 0.0);
    }

    /**
     * Test method for reseedRandomGenerator().
     */
    @Test
    public void testReseedRandomGenerator() {
        final long seed = System.currentTimeMillis();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));
        distribution.reseedRandomGenerator(seed);
        // Cannot test result directly, as it depends on the RNG state
    }

    /**
     * Test method for probability().
     */
    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));

        // Test with valid values
        for (int i = 1; i <= 3; i++) {
            double probability = distribution.probability((Integer) i);
            Assert.assertTrue(Precision.isClose(probability, distribution.probabilities[i - 1], 0.001));
        }

        // Test with null value
        Assert.assertEquals(0.0, distribution.probability(null), 0.0);

        // Test with value not in the distribution
        Assert.assertEquals(0.0, distribution.probability(4), 0.0);
    }

    /**
     * Test method for getSamples().
     */
    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(3, samples.size());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(singletons(i), samples.get(i).getKey());
            Assert.assertEquals(distribution.probabilities[i], samples.get(i).getValue(), 0.001);
        }
    }

    /**
     * Test method for sample().
     */
    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));

        // Test multiple samples with same RNG state
        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samples.add(distribution.sample());
        }
        Assert.assertEquals(3, samples.size());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(singletons(i), samples.get(Collections.frequency(samples, singletons(i))));
        }
    }

    /**
     * Test method for sample().
     */
    @Test
    public void testSampleDifferentRNGstates() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));

        List<Integer> samples1 = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samples1.add(distribution.sample());
        }
        List<Integer> samples2 = new ArrayList<>();
        DiscreteDistribution<Integer> distribution2 = new DiscreteDistribution<>(new Well19937c(1), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));
        for (int i = 0; i < 1000; i++) {
            samples2.add(distribution2.sample());
        }

        Assert.assertEquals(3, samples1.size());
        Assert.assertEquals(3, samples2.size());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(singletons(i), samples1.get(Collections.frequency(samples1, singletons(i))));
            Assert.assertEquals(singletons(i), samples2.get(Collections.frequency(samples2, singletons(i))));
        }
        // Samples should be different, as RNG states are different
        Assert.assertNotSame(samples1, samples2);
    }

    /**
     * Test method for sample(int).
     */
    @Test
    public void testSampleInt() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.25),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.25)
        ));

        // Test with valid sample size
        T[] sample = distribution.sample(3);
        Assert.assertArrayEquals(Arrays.asList(1, 1, 2).toArray(new Integer[0]), Arrays.asList(sample[0], sample[1], sample[2]));

        // Test with zero sample size
        sample = distribution.sample(0);
        Assert.assertArrayEquals(new T[0], sample);

        // Test with negative sample size
        try {
            distribution.sample(-1);
            Assert.fail("Expected NotStrictlyPositiveException for negative sample size");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    private Integer singletons(int index) {
        return index + 1;
    }
}