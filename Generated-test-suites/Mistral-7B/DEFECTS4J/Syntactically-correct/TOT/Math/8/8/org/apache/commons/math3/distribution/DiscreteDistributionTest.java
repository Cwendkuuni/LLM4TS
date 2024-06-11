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
import java.util.Random;

/**
 * Tests for {@link DiscreteDistribution} class.
 */
public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
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
        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        Assert.assertEquals(expectedSamples, samples);
    }

    @Test
    public void testSample() {
        Assert.assertEquals(1, distribution.sample());
        Assert.assertEquals(2, distribution.sample());
        Assert.assertEquals(3, distribution.sample());
        Assert.assertEquals(3, distribution.sample()); // same value can be returned multiple times
    }

    @Test
    public void testSampleArray() {
        int sampleSize = 10;
        T[] sample = distribution.sample(sampleSize);

        int[] expectedSample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            expectedSample[i] = i < 3 ? i + 1 : 3;
        }
        Assert.assertArrayEquals(expectedSample, Arrays.stream(sample).mapToInt(i -> ((Integer) i).intValue()).toArray());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        distribution.sample(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testProbabilityNegativeValue() {
        distribution.probability(-1);
    }
}