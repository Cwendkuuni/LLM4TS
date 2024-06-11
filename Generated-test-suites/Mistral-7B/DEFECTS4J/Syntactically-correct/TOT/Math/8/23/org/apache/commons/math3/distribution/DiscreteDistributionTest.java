import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityMethod() {
        // Test case 1: Probability of a value in the distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        int valueToTest = 2;
        double expectedProbability = 0.4;
        double actualProbability = distribution.probability((Integer) valueToTest);

        Assert.assertEquals(expectedProbability, actualProbability, 0.001);

        // Test case 2: Probability of a null value
        valueToTest = null;
        expectedProbability = 0.0;
        actualProbability = distribution.probability(valueToTest);

        Assert.assertEquals(expectedProbability, actualProbability);

        // Test case 3: Probability of a value not in the distribution
        int valueNotInDistribution = 4;
        expectedProbability = 0.0;
        actualProbability = distribution.probability((Integer) valueNotInDistribution);

        Assert.assertEquals(expectedProbability, actualProbability);
    }

    @Test
    public void testSampleMethod() {
        // Test case 1: Sample a single value from the distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        int expectedValue = 2;
        int actualValue = distribution.sample().intValue();

        Assert.assertEquals(expectedValue, actualValue);

        // Test case 2: Sample multiple values from the distribution
        int sampleSize = 3;
        T[] expectedValues = new Object[sampleSize];
        T[] actualValues = (T[]) java.lang.reflect.Array.newInstance(expectedValues[0].getClass(), sampleSize);

        for (int i = 0; i < sampleSize; i++) {
            expectedValues[i] = distribution.sample();
        }

        distribution.sample(sampleSize); // Sample the required number of values

        for (int i = 0; i < sampleSize; i++) {
            actualValues[i] = distribution.sample();
        }

        for (int i = 0; i < sampleSize; i++) {
            Assert.assertEquals(expectedValues[i], actualValues[i]);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        // Test case 1: Sample with a negative sample size
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        int sampleSize = -1;
        distribution.sample(sampleSize);
    }

    @Test(expected = NullPointerException.class)
    public void testProbabilityMethodWithNullValue() {
        // Test case 1: Probability of a null value with a null distribution
        List<Pair<Integer, Double>> samples = null;
        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        Integer nullValue = null;
        double probability = distribution.probability(nullValue);
    }

    @Test(expected = NotPositiveException.class)
    public void testProbabilityMethodWithNegativeProbability() {
        // Test case 1: Probability of a value with a negative probability
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        int valueToTest = 1;
        double probability = distribution.probability((Integer) valueToTest);
    }

    @Test(expected = MathArithmeticException.class)
    public void testProbabilityMethodWithSumZeroProbabilities() {
        // Test case 1: Probability of a value with sum of probabilities equals to zero
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.4));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        int valueToTest = 1;
        double probability = distribution.probability((Integer) valueToTest);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testProbabilityMethodWithInfiniteProbability() {
        // Test case 1: Probability of a value with infinite probability
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        int valueToTest = 1;
        double probability = distribution.probability((Integer) valueToTest);
    }
}