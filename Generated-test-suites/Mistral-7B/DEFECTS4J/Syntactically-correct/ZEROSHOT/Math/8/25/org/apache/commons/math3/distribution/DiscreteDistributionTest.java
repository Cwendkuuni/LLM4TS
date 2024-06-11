import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;
    private Well19937c rng;

    @Before
    public void setUp() {
        samples = new ArrayList<>();
        rng = new Well19937c();

        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Initialization should not throw an exception.");
        }
    }

    @Test
    public void testProbability() {
        for (int i = 1; i <= 3; i++) {
            double probability = distribution.probability((Integer) i);
            double expectedProbability = 0.0;

            for (Pair<Integer, Double> sample : samples) {
                if (sample.getKey().equals(i)) {
                    expectedProbability = sample.getValue();
                    break;
                }
            }

            Assert.assertEquals(expectedProbability, probability, 0.001);
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();
        List<Pair<Integer, Double>> actualSamples = samples;

        Assert.assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            Assert.assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            Assert.assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        int expectedValue = 1;
        double probability = 0.0;

        for (Pair<Integer, Double> samplePair : samples) {
            if (samplePair.getValue() > probability) {
                expectedValue = samplePair.getKey();
                probability = samplePair.getValue();
            }
        }

        Assert.assertEquals(expectedValue, sample);
    }

    @Test
    public void testSampleArray() {
        int sampleSize = 3;
        Integer[] samples = new Integer[sampleSize];

        try {
            distribution.sample(sampleSize);

            for (int i = 0; i < sampleSize; i++) {
                Assert.assertNotNull(samples[i]);
                sample(samples[i]);
            }

        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Sample array generation should not throw an exception.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleArrayNegativeSize() {
        distribution.sample(-1);
    }
}