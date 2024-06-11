import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            RandomGenerator rng = new Well19937c();
            distribution.reseedRandomGenerator(12345L);
            int value = distribution.sample();
            int expectedValue = 1;
            assertEquals(expectedValue, value);
            double probability = distribution.probability(expectedValue);
            assertEquals(0.3, probability, 0.001);
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.0));
        samples.add(new Pair<>(2, 0.0));
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
        } catch (MathIllegalArgumentException e) {
            assertEquals("Probability must be finite.", e.getMessage());
        }
    }

    @Test
    public void testDiscreteDistributionSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            RandomGenerator rng = new Well19937c();
            distribution.reseedRandomGenerator(12345L);
            int value1 = distribution.sample();
            int expectedValue1 = 1;
            assertEquals(expectedValue1, value1);
            int value2 = distribution.sample();
            int expectedValue2 = 2;
            assertEquals(expectedValue2, value2);
            int value3 = distribution.sample();
            int expectedValue3 = 3;
            assertEquals(expectedValue3, value3);
        } catch (NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDiscreteDistributionSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            int sampleSize = 3;
            T[] sample = distribution.sample(sampleSize);
            int expectedValue1 = 1;
            int expectedValue2 = 1;
            int expectedValue3 = 1;

            for (int i = 0; i < sampleSize; i++) {
                int value = ((Integer) sample[i]).intValue();
                assertEquals(expectedValue1, value);
                expectedValue1 = (expectedValue1 == expectedValue2) ? expectedValue3 : expectedValue1;
                expectedValue3 = (expectedValue1 == expectedValue2) ? expectedValue1 : expectedValue3;
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}