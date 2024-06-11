import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(3, distribution.singletons.size());
            Assert.assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)), distribution.singletons);
            Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.0);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionWithNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            new DiscreteDistribution<>(samples);
        } catch (NotStrictlyPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            throw new RuntimeException("Should not be here", e);
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionWithSumOfProbabilitiesZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), -0.1));

        try {
            new DiscreteDistribution<>(samples);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Should not be here", e);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionWithInfProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            throw new RuntimeException("Should not be here", e);
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.001);
            Assert.assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.001);
            Assert.assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.001);
            Assert.assertEquals(0.0, distribution.probability(Integer.valueOf(4)), 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            List<Pair<Integer, Double>> result = distribution.getSamples();
            Assert.assertEquals(3, result.size());
            Assert.assertEquals(samples, result);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(Integer.valueOf(1), distribution.sample());
            Assert.assertEquals(Integer.valueOf(2), distribution.sample());
            Assert.assertEquals(Integer.valueOf(3), distribution.sample());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Integer[] result = distribution.sample(5);
            Arrays.sort(result);
            Assert.assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)}, result);
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Should not throw an exception");
        }
    }
}