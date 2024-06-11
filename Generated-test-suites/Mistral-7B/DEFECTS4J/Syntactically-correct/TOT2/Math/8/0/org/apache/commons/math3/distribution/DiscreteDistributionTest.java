import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testCreateDiscreteDistribution() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
        Assert.assertArrayEquals(new double[]{0.1, 0.3, 0.6}, distribution.probabilities, 0.001);
    }

    @Test
    public void testProbability() {
        DiscreteDistribution distribution = new DiscreteDistribution<>( Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        ) );

        Assert.assertEquals(0.1, distribution.probability(1), 0.001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.001);
        Assert.assertEquals(0.6, distribution.probability(3), 0.001);

        Assert.assertEquals(0.0, distribution.probability(null), 0.001);
        Assert.assertEquals(0.0, distribution.probability(4), 0.001);
    }

    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        DiscreteDistribution distribution = new DiscreteDistribution<>( Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        ) );

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        Assert.assertArrayEquals(new Pair[] {new Pair<>(1, 0.1), new Pair<>(2, 0.3), new Pair<>(3, 0.6)}, samples.toArray());
    }

    @Test
    public void testSampleSingleValue() {
        DiscreteDistribution distribution = new DiscreteDistribution<>( Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        ) );

        Assert.assertEquals(1, distribution.sample());
        Assert.assertEquals(1, distribution.sample());
        Assert.assertEquals(2, distribution.sample());
        Assert.assertEquals(3, distribution.sample());
        Assert.assertEquals(3, distribution.sample());
    }

    @Test
    public void testSampleMultipleValues() throws NotStrictlyPositiveException {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        ));

        int sampleSize = 5;
        T[] sample = distribution.sample(sampleSize);

        Assert.assertArrayEquals(new Object[]{1, 1, 2, 3, 3}, sample);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        ));

        distribution.sample(-1);
    }

    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        ));
        long seed = 12345;

        distribution.reseedRandomGenerator(seed);

        T sample = distribution.sample();
        Assert.assertNotSame(sample, distribution.sample());
    }

}