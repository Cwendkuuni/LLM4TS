import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(new Integer(1), distribution.singletons.get(0));
        Assert.assertEquals(new Integer(2), distribution.singletons.get(1));
        Assert.assertEquals(new Integer(3), distribution.singletons.get(2));

        Assert.assertArrayEquals(new double[]{0.3, 0.5, 0.2}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionZeroProbabilitySum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.2));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testDiscreteDistributionProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.3, distribution.probability(1), 0.001);
        Assert.assertEquals(0.5, distribution.probability(2), 0.001);
        Assert.assertEquals(0.2, distribution.probability(3), 0.001);

        Assert.assertEquals(0.0, distribution.probability(4), 0.001);
    }

    @Test
    public void testDiscreteDistributionSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(3, result.size());
        Assert.assertEquals(new Pair<>(1, 0.3), result.get(0));
        Assert.assertEquals(new Pair<>(2, 0.5), result.get(1));
        Assert.assertEquals(new Pair<>(3, 0.2), result.get(2));
    }

    @Test
    public void testDiscreteDistributionSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer sample = distribution.sample();

        Assert.assertEquals(1, sample.intValue());
        Assert.assertEquals(0.3, distribution.probability(sample.intValue()), 0.001);
    }

    @Test
    public void testDiscreteDistributionSampleArray() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] sampleArray = distribution.sample(5);

        Assert.assertArrayEquals(new Integer[]{1, 1, 2, 3, 3}, sampleArray);
    }

    @Test
    public void testDiscreteDistributionSeed() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Random(), samples);

        Integer[] sampleArray1 = distribution.sample(5);
        Integer[] sampleArray2 = distribution.sample(5);

        Assert.assertNotSame(sampleArray1, sampleArray2);

        Random seededRandom = new Random();
        distribution.reseedRandomGenerator(seededRandom.nextLong());

        Integer[] sampleArray3 = distribution.sample(5);
        Integer[] sampleArray4 = distribution.sample(5);

        Assert.assertNotSame(sampleArray3, sampleArray4);
        Assert.assertArrayEquals(sampleArray1, sampleArray3);
        Assert.assertArrayEquals(sampleArray1, sampleArray4);
    }
}