import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionCreation() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.assertNotNull(distribution);
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            Assert.fail("Exception should not be thrown during creation: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        double probability = distribution.probability(1);
        Assert.assertEquals(0.3, probability, 0.001);

        probability = distribution.probability(4);
        Assert.assertEquals(0.0, probability, 0.001);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        int result = 0;
        for (int i = 0; i < 100; i++) {
            result = distribution.sample().intValue();
        }

        double probability = 0;
        for (int value : samples.stream().map(Pair::getKey).mapToInt(Integer::intValue).toArray()) {
            probability += samples.get(samples.indexOf(new Pair<>(value, 1.0))).getValue();
        }

        Assert.assertEquals(probability, 1.0, 0.01);
    }

    @Test
    public void testSampleSizeMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        try {
            distribution.sample(0);
            Assert.fail("Exception should be thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception.
        }

        T[] result = distribution.sample(3);
        for (int i = 0; i < 3; i++) {
            Assert.assertNotNull(result[i]);
        }
    }

    @Test
    public void testReseedRandomGeneratorMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        long seed = 123456789;
        distribution.reseedRandomGenerator(seed);

        int result = 0;
        for (int i = 0; i < 10; i++) {
            result = distribution.sample().intValue();
        }

        Well19937c rng = new Well19937c(seed);
        int expected = 0;
        for (int i = 0; i < 10; i++) {
            expected = rng.nextInt(3);
            rng.nextInt(); // consume remaining bits
        }

        Assert.assertEquals(expected, result);
    }
}