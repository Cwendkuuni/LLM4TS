import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test cases for DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    private List<Pair<Integer, Double>> samples;
    private DiscreteDistribution<Integer> distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));
        rng = new Well19937c();
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbability() {
        // Test probability for valid input
        assertEquals(0.3, distribution.probability((Integer) 1), 0.01);

        // Test probability for null input
        assertNull(distribution.probability(null));

        // Test probability for invalid input
        assertNull(distribution.probability((Integer) 4));
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
        assertEquals(samples.size(), samplesFromDistribution.size());

        for (int i = 0; i < samplesFromDistribution.size(); i++) {
            assertEquals(samples.get(i).getKey(), samplesFromDistribution.get(i).getKey());
            assertEquals(samples.get(i).getValue(), samplesFromDistribution.get(i).getValue(), 0.01);
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        // Test sample method with default random generator
        Integer sample = distribution.sample();
        assertTrue(samples.contains(new Pair<>(sample, 1.0)));

        // Test sample method with custom random generator
        MersenneTwister customRng = new MersenneTwister();
        DiscreteDistribution<Integer> distributionWithCustomRng = new DiscreteDistribution<>(customRng, samples);
        Integer sampleFromCustomRng = distributionWithCustomRng.sample();
        assertTrue(samples.contains(new Pair<>(sampleFromCustomRng, 1.0)));

        // Test sample method with empty list
        List<Pair<Integer, Double>> emptySamples = new ArrayList<>();
        try {
            new DiscreteDistribution<>(rng, emptySamples);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test sample method with negative sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        distribution.sample(-1);
    }

    /**
     * Test sample method with zero sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleZeroSampleSize() {
        distribution.sample(0);
    }

    /**
     * Test reseedRandomGenerator method.
     */
    @Test
    public void testReseedRandomGenerator() {
        long seed = 12345;
        distribution.reseedRandomGenerator(seed);

        // Generate a sample and check it against the original one
        Integer sample = distribution.sample();
        Integer originalSample = distribution.sample();
        assertEquals(originalSample, sample);

        // Check if the new seed was set correctly
        assertEquals(seed, distribution.random.getSeed());
    }
}