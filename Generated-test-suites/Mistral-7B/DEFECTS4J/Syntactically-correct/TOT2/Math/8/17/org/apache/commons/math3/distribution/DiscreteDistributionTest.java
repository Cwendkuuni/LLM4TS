import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    private RandomGenerator rng;
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbabilityMethod() {
        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
        Assert.assertEquals(0, distribution.probability((Integer) 4), 0.001);
    }

    @Test
    public void testSampleMethod() {
        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            samples.add(distribution.sample());
        }
        int[] frequencies = new int[4];
        for (Integer s : samples) {
            frequencies[s]++;
        }
        Assert.assertArrayEquals(new int[]{35, 43, 22}, frequencies);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeNegative() {
        distribution.sample(-1);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(expectedSamples, samples);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNullValue() {
        distribution.probability(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleNullValue() {
        distribution.sample();
        distribution.sample((Integer) null);
    }
}