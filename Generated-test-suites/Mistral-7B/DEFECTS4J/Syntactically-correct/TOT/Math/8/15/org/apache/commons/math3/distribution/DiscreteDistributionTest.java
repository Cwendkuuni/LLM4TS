import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test method for constructor DiscreteDistribution(List<Pair<T, Double>>).
     */
    @Test
    public void testConstructorList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Check singletons
        assertEquals(1, distribution.singletons.get(0));
        assertEquals(2, distribution.singletons.get(1));
        assertEquals(3, distribution.singletons.get(2));

        // Check probabilities
        double[] probabilities = distribution.probabilities;
        assertEquals(0.3, probabilities[0], 0.001);
        assertEquals(0.4, probabilities[1], 0.001);
        assertEquals(0.3, probabilities[2], 0.001);
    }

    /**
     * Test method for constructor DiscreteDistribution(RandomGenerator, List<Pair<T, Double>>).
     */
    @Test
    public void testConstructorRandomGeneratorList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        Well19937c rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        // Check singletons
        assertEquals(1, distribution.singletons.get(0));
        assertEquals(2, distribution.singletons.get(1));
        assertEquals(3, distribution.singletons.get(2));

        // Check probabilities
        double[] probabilities = distribution.probabilities;
        assertEquals(0.3, probabilities[0], 0.001);
        assertEquals(0.4, probabilities[1], 0.001);
        assertEquals(0.3, probabilities[2], 0.001);
    }

    /**
     * Test method for method probability(T).
     */
    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test with valid input
        assertEquals(0.3, distribution.probability((Integer) 1), 0.001);

        // Test with null input
        assertNull(distribution.probability(null));

        // Test with invalid input
        assertNull(distribution.probability((Integer) 4));
    }

    /**
     * Test method for method getSamples().
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();

        // Check size
        assertEquals(samples.size(), expectedSamples.size());

        // Check elements
        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> sample = samples.get(i);
            Pair<Integer, Double> expectedSample = expectedSamples.get(i);
            assertEquals(sample.getKey(), expectedSample.getKey());
            assertEquals(sample.getValue(), expectedSample.getValue(), 0.001);
        }
    }

    /**
     * Test method for method sample().
     */
    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test multiple calls to sample() with same seed
        int sample = (Integer) distribution.sample();
        int sample2 = (Integer) distribution.sample();
        assertEquals(sample, sample2);

        // Test with reseedRandomGenerator()
        distribution.reseedRandomGenerator(123);
        sample = (Integer) distribution.sample();
        int sample3 = (Integer) distribution.sample();
        assertNotEquals(sample, sample3);

        // Test with null input
        assertNull(distribution.sample(null));

        // Test with negative sample size
        try {
            distribution.sample(-1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test method for method sample(int).
     */
    @Test
    public void testSampleInt() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        T[] samplesArray = distribution.sample(5);

        // Check size
        assertEquals(5, samplesArray.length);

        // Check elements
        for (int i = 0; i < samplesArray.length; i++) {
            Integer sample = (Integer) samplesArray[i];
            assertTrue(distribution.singletons.contains(sample));
        }
    }
}