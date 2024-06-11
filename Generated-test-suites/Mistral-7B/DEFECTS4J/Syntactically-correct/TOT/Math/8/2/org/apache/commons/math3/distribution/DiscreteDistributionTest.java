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
    public void testDiscreteDistributionCreationWithValidParameters() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.4));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(3, discreteDistribution.singletons.size());
        Assert.assertEquals(3, discreteDistribution.probabilities.length);
        Assert.assertArrayEquals(new double[]{0.2, 0.4, 0.4}, discreteDistribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionCreationWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, -0.1));
        samples.add(new Pair<>(3, 0.4));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            Assert.assertEquals(-0.1, e.getValue(), 0.0);
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionCreationWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, -0.1));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionCreationWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(3, 0.4));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDiscreteDistributionSampleMethod() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.4)
        ));

        int sampledValue = discreteDistribution.sample().intValue();
        int expectedValue = 0;
        double probability = 0.2;

        for (int i = 0; i < 100000; i++) {
            int sampledValue = discreteDistribution.sample().intValue();
            if (sampledValue == 1) {
                probability += 0.2;
            }
        }

        Assert.assertNotSame(-1, sampledValue);
        Assert.assertEquals(1, probability, 0.001);
    }

    @Test
    public void testDiscreteDistributionSampleMultipleValuesMethod() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.4)
        ));

        Integer[] sampledValues = discreteDistribution.sample(10);

        int[] expectedValues = {1, 1, 2, 2, 2, 2, 2, 2, 2, 3};

        Assert.assertArrayEquals(expectedValues, sampledValues);
    }
}