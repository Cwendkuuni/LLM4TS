import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNullArray;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link DiscreteDistribution} class.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid samples.
     */
    @Test
    public void testDiscreteDistributionWithValidSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertNotNull(distribution);
        assertEquals(3, distribution.singletons.size());
        assertEquals(Arrays.asList(1, 2, 3), distribution.singletons);
        assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
    }

    /**
     * Test constructor with invalid samples (negative probabilities).
     */
    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionWithInvalidSamplesNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with invalid samples (zero probabilities sum).
     */
    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionWithInvalidSamplesZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with invalid samples (infinite probabilities).
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionWithInvalidSamplesInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(samples);
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbabilityMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)));

        assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
        assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
        assertEquals(0.0, distribution.probability((Integer) 4), 0.001);
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamplesMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)));

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(3, samples.size());
        assertEquals(Arrays.asList(1, 2, 3), samples);
    }

    /**
     * Test sample method with single value.
     */
    @Test
    public void testSampleMethodWithSingleValue() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)));

        assertEquals((Integer) 1, distribution.sample());
        assertEquals((Integer) 1, distribution.sample());
    }

    /**
     * Test sample method with multiple values
     */
    @Test
    public void testSampleMethodWithMultipleValues() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)));

        List<Integer> samples = new ArrayList<>();
        samples.add((Integer) distribution.sample());
        samples.add((Integer) distribution.sample());
        samples.add((Integer) distribution.sample());

        assertNotNullArray(samples.toArray(new Integer[samples.size()]));
        assertEquals(3, samples.size());
        assertTrue(Arrays.asList(1, 2, 3).containsAll(samples));
    }

    /**
     * Test sample method with empty list of samples.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSampleMethodWithEmptyListOfSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList());
        distribution.sample();
    }

    /**
     * Test reseedRandomGenerator method.
     */
    @Test
    public void testReseedRandomGeneratorMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)));

        long seed = 123456789;
        distribution.reseedRandomGenerator(seed);

        assertEquals(seed, distribution.random.getSeed());
    }

    /**
     * Test sample method with null values.
     */
    @Test
    public void testSampleMethodWithNullValues() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(null, 0.3),
                new Pair<>(null, 0.4),
                new Pair<>(null, 0.3)));

        assertEquals(null, distribution.sample());
        assertEquals(null, distribution.sample());
    }

    /**
     * Test sample method with multiple null values.
     */
    @Test
    public void testSampleMethodWithMultipleNullValues() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(Arrays.asList(
                new Pair<>(null, 0.3),
                new Pair<>(null, 0.4),
                new Pair<>(null, 0.3)));

        List<String> samples = new ArrayList<>();
        samples.add((String) distribution.sample());
        samples.add((String) distribution.sample());
        samples.add((String) distribution.sample());

        assertFalse(samples.contains(null));
    }

    /**
     * Test sample method with custom random generator.
     */
    @Test
    public void testSampleMethodWithCustomRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        final int seed = 123456789;
        Well19937c rng = new Well19937c(seed);

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        assertNotNull(distribution);
        assertEquals(3, distribution.singletons.size());
        assertEquals(Arrays.asList(1, 2, 3), distribution.singletons);
        assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);

        assertEquals(seed, distribution.random.getSeed());
    }

    /**
     * Test sample method with empty list of samples and custom random generator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSampleMethodWithEmptyListOfSamplesAndCustomRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        final int seed = 123456789;
        Well19937c rng = new Well19937c(seed);

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
        distribution.sample();
    }
}