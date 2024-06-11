import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        Assert.assertNotNull(distribution);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        double probability = 0;

        for (int i = 0; i < 10000; i++) {
            probability += distribution.probability(Integer.valueOf(1));
        }

        Assert.assertEquals("Probability should be close to expected value", 0.3, probability, 0.01);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        int sample = 0;
        for (int i = 0; i < 1000; i++) {
            sample = distribution.sample().intValue();
        }

        Assert.assertEquals("Sample should be one of the possible values", 1, sample);
    }

    @Test
    public void testSampleSizeMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        int[] samplesArray = distribution.sample(10);

        for (int i = 0; i < 10; i++) {
            Assert.assertArrayContains("Sample should be one of the possible values", samples.get(i).getKey().intValue(), samplesArray);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        distribution.sample(-1);

        // This assertion will be thrown due to the exception in test method
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        int sample = 0;

        for (int i = 0; i < 1000; i++) {
            sample = distribution.sample().intValue();
        }

        long seed = System.currentTimeMillis();
        distribution.reseedRandomGenerator(seed);

        for (int i = 0; i < 1000; i++) {
            int newSample = distribution.sample().intValue();
            Assert.assertNotEquals("Reseeded generator should produce different samples", sample, newSample);
            sample = newSample;
        }
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception during test: " + e.getMessage());
        }

        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();

        for (int i = 0; i < expectedSamples.size(); i++) {
            Assert.assertEquals("Expected samples should match the original samples", samples.get(i).getKey(), expectedSamples.get(i).getKey());
            Assert.assertEquals("Expected probabilities should match the original probabilities", samples.get(i).getValue(), expectedSamples.get(i).getValue(), 0.01);
        }
    }
}