import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionCreationWithValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(Arrays.asList(1, 2, 3), distribution.singletons);
        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionCreationWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, -0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            Assert.assertEquals("-0.4", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionCreationWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, -0.1));
        samples.add(new Pair<>(4, 0.1));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (MathArithmeticException e) {
            Assert.assertEquals("Probabilities do not sum to 1.0: 0.3 + 0.2 + (-0.1) + 0.1 = 0.2", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionCreationWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (MathIllegalArgumentException e) {
            Assert.assertEquals("Probability must be finite", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testProbabilityMethodWithValidInput() {
        DiscreteDistribution<Integer> distribution = createSampleDistribution();
        int testValue = 3;

        double probability = distribution.probability(testValue);
        Assert.assertEquals(0.3, probability, 0.001);
    }

    @Test
    public void testProbabilityMethodWithOutOfRangeInput() {
        DiscreteDistribution<Integer> distribution = createSampleDistribution();
        int testValue = 4;

        double probability = distribution.probability(testValue);
        Assert.assertEquals(0.0, probability, 0.001);
    }

    @Test
    public void testSampleMethodWithValidInput() {
        DiscreteDistribution<Integer> distribution = createSampleDistribution();

        int sample = distribution.sample();
        Assert.assertTrue(distribution.singletons.contains(sample));
    }

    @Test
    public void testSampleMethodWithMultipleValidCalls() {
        DiscreteDistribution<Integer> distribution = createSampleDistribution();

        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            samples.add(distribution.sample());
        }

        Assert.assertEquals(10, samples.size());
        Assert.assertTrue(new ArrayList<>(samples).containsAll(distribution.singletons));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        DiscreteDistribution<Integer> distribution = createSampleDistribution();

        int sampleSize = -5;

        try {
            distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("Number of samples must be positive", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSampleMethodWithValidSampleSize() {
        DiscreteDistribution<Integer> distribution = createSampleDistribution();

        int[] samples = distribution.sample(5);

        Assert.assertArrayEquals(Arrays.asList(distribution.singletons.toArray()), Arrays.asList(samples));
    }

    private DiscreteDistribution<Integer> createSampleDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        return new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

}