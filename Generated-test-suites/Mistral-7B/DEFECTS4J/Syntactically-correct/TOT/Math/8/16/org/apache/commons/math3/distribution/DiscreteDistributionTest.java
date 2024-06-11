import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class DiscreteDistributionTest {

    private List<Pair<Integer, Double>> samples;
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5),
                new Pair<>(4, 0.1),
                new Pair<>(5, 0.05)
        );
        distribution = new DiscreteDistribution<>(new Well19937c(), samples.stream().map(p -> new Pair<>(p.getKey(), p.getValue())).collect(Collectors.toList()));
    }

    @Test
    public void testProbability() {
        for (int value : Arrays.asList(1, 2, 3, 4, 5)) {
            assertEquals("Probability for " + value, distribution.probability(value), 0.01, 0.0);
        }
        assertNull("Probability for null", distribution.probability(null));
    }

    @Test
    public void testSample() {
        final List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            samples.add(distribution.sample());
        }

        int[] frequencies = new int[5];
        for (Integer sample : samples) {
            frequencies[sample]++;
        }

        assertEquals("Sample frequencies", Arrays.stream(frequencies).mapToDouble(i -> (double) i / samples.size()).toArray(), new double[]{0.2, 0.3, 0.5, 0.1, 0.05});
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> negativeSamples = Arrays.asList(new Pair<>(1, -0.1), new Pair<>(2, -0.2));
        new DiscreteDistribution<>(new Well19937c(), negativeSamples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilities() {
        List<Pair<Integer, Double>> zeroProbabilitiesSamples = Arrays.asList(new Pair<>(1, 0.0), new Pair<>(2, 0.0));
        new DiscreteDistribution<>(new Well19937c(), zeroProbabilitiesSamples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> infiniteProbabilitiesSamples = Arrays.asList(new Pair<>(1, Double.POSITIVE_INFINITY));
        new DiscreteDistribution<>(new Well19937c(), infiniteProbabilitiesSamples);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> testSamples = distribution.getSamples();
        assertEquals("Samples size", testSamples.size(), samples.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals("Sample " + i + " value", samples.get(i).getKey(), testSamples.get(i).getKey());
            assertEquals("Sample " + i + " probability", samples.get(i).getValue(), testSamples.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 42;
        distribution.reseedRandomGenerator(seed);
        //Seeding test is hard to test as it's not possible to verify the state of a random number generator
    }
}