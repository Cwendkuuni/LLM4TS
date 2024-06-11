import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the {@link DiscreteDistribution} class.
 */
public class DiscreteDistributionTest {

    private List<Pair<Integer, Double>> samples;
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        samples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3));
        RandomGenerator rng = new Well19937c();
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // failure is expected in the constructor
        }
    }

    /**
     * Tests that the distribution correctly returns the probability of a sample.
     */
    @Test
    public void testProbability() {
        for (Pair<Integer, Double> sample : samples) {
            double probability = distribution.probability(sample.getKey());
            Assert.assertEquals(sample.getValue(), probability, 0.0001);
        }

        Assert.assertEquals(0.0, distribution.probability(null), 0.0);
        Assert.assertEquals(0.0, distribution.probability(4), 0.0);
    }

    /**
     * Tests that the distribution correctly generates samples.
     */
    @Test
    public void testSample() {
        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samples.add(distribution.sample());
        }

        double[] histogram = new double[samples.size()];
        for (Integer sample : samples) {
            histogram[samples.indexOf(sample)]++;
        }

        for (Pair<Integer, Double> sample : samples) {
            double expectedProbability = sample.getValue();
            double observedProbability = histogram[samples.indexOf(sample.getKey())] / histogram.length;
            Assert.assertEquals(expectedProbability, observedProbability, 0.001);
        }
    }

    /**
     * Tests that the distribution correctly generates a random sample of a given size.
     */
    @Test
    public void testSampleSize() throws NotStrictlyPositiveException {
        int[] expectedSamples = {1, 1, 2, 2, 3, 3};
        int[] samples = distribution.sample(6);

        Assert.assertArrayEquals(expectedSamples, samples);
    }

    /**
     * Tests that an exception is thrown if the number of samples is not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        distribution.sample(-1);
    }

}