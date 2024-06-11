import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        Well19937c rng = new Well19937c();
        distribution = new DiscreteDistribution<>(rng, samples);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            throw new IllegalArgumentException("Unexpected exception", e);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (MathArithmeticException | MathIllegalArgumentException e) {
            throw new NotPositiveException("Probability of value is not positive", 0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            throw new IllegalArgumentException("Unexpected exception", e);
        }
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        List<Integer> expectedSamples = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedSamples.add(sample);
            sample = distribution.sample();
        }

        Integer[] expectedSampleArray = expectedSamples.toArray(new Integer[expectedSamples.size()]);
        Arrays.sort(expectedSampleArray);

        T[] samples = distribution.sample(10);
        Arrays.sort((T[]) samples);

        Assert.assertArrayEquals(expectedSampleArray, samples);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);

        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();

        Assert.assertNotEquals(sample1, sample2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        int sampleSize = -1;
        try {
            distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}