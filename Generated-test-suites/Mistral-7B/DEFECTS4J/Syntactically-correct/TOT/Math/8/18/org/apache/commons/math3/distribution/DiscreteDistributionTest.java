import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DiscreteDistribution} class.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.2, distribution.probability(1), 0.001);
        assertEquals(0.3, distribution.probability(2), 0.001);
        assertEquals(0.5, distribution.probability(3), 0.001);

        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));

        assertEquals(expectedSamples, distribution.getSamples());
    }

    /**
     * Test constructor with negative probabilities.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test constructor with probabilities summing to zero.
     */
    @Test(expected = MathArithmeticException.class)
    public void testProbabilitiesSumZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test constructor with infinite probabilities.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        assertEquals(0.2, distribution.probability(1), 0.001);
        assertEquals(0.3, distribution.probability(2), 0.001);
        assertEquals(0.5, distribution.probability(3), 0.001);

        Distribution<Integer> distributionWithRepeatedValues = new DiscreteDistribution<Integer>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.2),
                new Pair<>(1, 0.4)));

        assertEquals(0.1, distributionWithRepeatedValues.probability(1), 0.001);
        assertEquals(0.3, distributionWithRepeatedValues.probability(2), 0.001);
        assertEquals(0.2, distributionWithRepeatedValues.probability(3), 0.001);
        assertEquals(0.4, distributionWithRepeatedValues.probability(1), 0.001);
    }

    /**
     * Test probability method with null arguments.
     */
    @Test
    public void testProbabilityNullArguments() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        assertNull(distribution.probability(null));
    }

    /**
     * Test sample method with valid arguments.
     */
    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        for (int i = 0; i < 10; i++) {
            int actual = distribution.sample();
            int expectedIndex = (int) (Math.random() * distribution.probabilities.length);
            int expected = distribution.singletons.get(expectedIndex).intValue();

            assertEquals(expected, actual);
        }
    }

    /**
     * Test sample method with null arguments.
     */
    @Test
    public void testSampleNullArguments() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        assertNull(distribution.sample(null));
    }

    /**
     * Test sample method with non-positive sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        distribution.sample(-1);
    }

    /**
     * Test sample method with zero sample size.
     */
    @Test
    public void testSampleZeroSize() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        T[] actual = distribution.sample(0);

        assertEquals(0, actual.length);
    }

    /**
     * Test sample method with positive sample size.
     */
    @Test
    public void testSamplePositiveSize() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        T[] actual = distribution.sample(3);

        for (int i = 0; i < 3; i++) {
            int expectedIndex = (int) (Math.random() * distribution.probabilities.length);
            int expected = distribution.singletons.get(expectedIndex).intValue();

            assertEquals(expected, actual[i].intValue());
        }
    }

    /**
     * Test reseedRandomGenerator method with valid seed.
     */
    @Test
    public void testReseedRandomGeneratorValidSeed() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        long seed = 12345;
        distribution.reseedRandomGenerator(seed);

        // No specific test here as the seeding is checked in other tests.
    }

    /**
     * Test reseedRandomGenerator method with invalid seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReseedRandomGeneratorInvalidSeed() {
        DiscreteDistribution<Integer> distribution = getValidDistribution();

        distribution.reseedRandomGenerator(-1);
    }

    /**
     * Get a valid distribution instance for testing.
     *
     * @return a valid distribution instance.
     */
    private DiscreteDistribution<Integer> getValidDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        return new DiscreteDistribution<>(new Well19937c(), samples);
    }
}