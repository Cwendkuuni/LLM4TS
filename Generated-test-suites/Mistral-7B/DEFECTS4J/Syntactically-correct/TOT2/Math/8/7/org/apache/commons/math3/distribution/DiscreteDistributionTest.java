import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
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
    public void setUp() {
        rng = new Well19937c();
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.3),
                new Pair<Integer, Double>(2, 0.4),
                new Pair<Integer, Double>(3, 0.3)
        );
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.3, distribution.probability(1), 0.001);
        Assert.assertEquals(0.4, distribution.probability(2), 0.001);
        Assert.assertEquals(0.3, distribution.probability(3), 0.001);
        Assert.assertEquals(0, distribution.probability(4), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(3, samples.size());
        Assert.assertNotNull(samples.get(0));
        Assert.assertEquals(1, samples.get(0).getKey());
        Assert.assertEquals(0.3, samples.get(0).getValue(), 0.001);
        Assert.assertNotNull(samples.get(1));
        Assert.assertEquals(2, samples.get(1).getKey());
        Assert.assertEquals(0.4, samples.get(1).getValue(), 0.001);
        Assert.assertNotNull(samples.get(2));
        Assert.assertEquals(3, samples.get(2).getKey());
        Assert.assertEquals(0.3, samples.get(2).getValue(), 0.001);
    }

    @Test
    public void testSample() {
        Assert.assertEquals(1, distribution.sample());
        Assert.assertEquals(2, distribution.sample());
        Assert.assertEquals(3, distribution.sample());
        Assert.assertEquals(1, distribution.sample());
        // Test for the final value when all probabilities are exhausted
        Assert.assertEquals(3, distribution.sample());
    }

    @Test
    public void testSampleArray() {
        int sampleSize = 5;
        T[] expected = (T[]) Arrays.copyOf(distribution.singletons.toArray(), sampleSize);
        T[] result = distribution.sample(sampleSize);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        distribution.sample(-1);
    }

}