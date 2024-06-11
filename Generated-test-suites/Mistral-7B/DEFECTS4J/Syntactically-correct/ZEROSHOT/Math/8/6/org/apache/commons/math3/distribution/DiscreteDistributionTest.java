import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testEmptyConstructor() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        Assert.assertNull(distribution.random);
        Assert.assertNotNull(distribution.singletons);
        Assert.assertNotNull(distribution.probabilities);
        Assert.assertEquals(0, distribution.singletons.size());
        Assert.assertEquals(0, distribution.probabilities.length);
    }

    @Test
    public void testConstructorNullValues() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.1));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        Assert.assertNull(distribution.random);
        Assert.assertNotNull(distribution.singletons);
        Assert.assertEquals(1, distribution.singletons.size());
        Assert.assertEquals(null, distribution.singletons.get(0));
    }

    @Test
    public void testConstructorNullProbabilities() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), null));
        try {
            new DiscreteDistribution<>(samples);
            Assert.fail("IllegalArgumentException should be thrown.");
        } catch (MathIllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void testConstructorEmptyProbabilities() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.0));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        Assert.assertNull(distribution.random);
        Assert.assertNotNull(distribution.singletons);
        Assert.assertEquals(1, distribution.singletons.size());
        Assert.assertEquals(Integer.valueOf(1), distribution.singletons.get(0));
        Assert.assertNotNull(distribution.probabilities);
        Assert.assertEquals(0, distribution.probabilities.length);
    }

    @Test
    public void testConstructorValid() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.6));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        Assert.assertNotNull(distribution.random);
        Assert.assertNotNull(distribution.singletons);
        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(Integer.valueOf(1), distribution.singletons.get(0));
        Assert.assertEquals(Integer.valueOf(2), distribution.singletons.get(1));
        Assert.assertEquals(Integer.valueOf(3), distribution.singletons.get(2));
        Assert.assertNotNull(distribution.probabilities);
        Assert.assertEquals(3, distribution.probabilities.length);
        Assert.assertEquals(0.1, distribution.probabilities[0], 0.001);
        Assert.assertEquals(0.3, distribution.probabilities[1], 0.001);
        Assert.assertEquals(0.6, distribution.probabilities[2], 0.001);
        Assert.assertEquals(0.1 + 0.3 + 0.6, distribution.probability(null), 0.001);
    }

    @Test
    public void testProbabilityNullValue() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        Assert.assertEquals(1.0, distribution.probability(Integer.valueOf(1)), 0.001);
        Assert.assertEquals(0.0, distribution.probability(null), 0.001);
    }

    @Test
    public void testProbabilityValid() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        Assert.assertEquals(1.0, distribution.probability(Integer.valueOf(1)), 0.001);
    }

    @Test
    public void testProbabilityOutOfRange() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        Assert.assertEquals(0.0, distribution.probability(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        Assert.assertEquals(Integer.valueOf(1), distribution.sample());
    }

    @Test
    public void testSampleMultiple() throws NotStrictlyPositiveException {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        Integer[] samples = distribution.sample(5);
        Assert.assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, samples);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws NotStrictlyPositiveException {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        distribution.sample(-1);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(1, samples.size());
        Assert.assertEquals(Integer.valueOf(1), samples.get(0).getKey());
        Assert.assertEquals(1.0, samples.get(0).getValue(), 0.001);
    }

    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonList(new Pair<>(Integer.valueOf(1), 1.0)));
        long seed = 123;
        distribution.reseedRandomGenerator(seed);
        // Cannot test distribution directly, because the test relies on the
        // internal random number generator, which is not accessible.
    }

    private static List<Pair<Integer, Double>> singletonList(Pair<Integer, Double> pair) {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(pair);
        return list;
    }

}