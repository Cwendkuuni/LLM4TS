import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistributionTest;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscreteDistributionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertNotNull(distribution);
    }

    /**
     * Test constructor with negative probabilities.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test constructor with probabilities summing to zero.
     */
    @Test(expected = MathArithmeticException.class)
    public void testProbabilitiesSumToZero() {
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
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(0.2, distribution.probability((Integer) 1), 0.001);
    }

    /**
     * Test probability method with invalid input.
     */
    @Test
    public void testProbabilityInvalidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(0.0, distribution.probability((Integer) 4), 0.001);
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getKey());
        assertEquals(0.2, result.get(0).getValue(), 0.001);
        assertEquals(2, result.get(1).getKey());
        assertEquals(0.4, result.get(1).getValue(), 0.001);
        assertEquals(3, result.get(2).getKey());
        assertEquals(0.6, result.get(2).getValue(), 0.001);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer result = distribution.sample();

        assertEquals(1, result.intValue());
    }

    /**
     * Test sample method with multiple samples.
     */
    @Test
    public void testSampleMultiple() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer[] result = distribution.sample(5);

        assertEquals(5, result.length);

        for (Integer integer : result) {
            assertEquals(1, integer.intValue());
            assertEquals(1, distribution.probability((Integer) 1), 0.001);
        }
    }

    /**
     * Test reseedRandomGenerator method.
     */
    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        long seed = 12345;
        distribution.reseedRandomGenerator(seed);

        Integer result = distribution.sample();

        assertEquals(1, result.intValue());
        assertEquals(1, distribution.probability((Integer) 1), 0.001);
    }
}