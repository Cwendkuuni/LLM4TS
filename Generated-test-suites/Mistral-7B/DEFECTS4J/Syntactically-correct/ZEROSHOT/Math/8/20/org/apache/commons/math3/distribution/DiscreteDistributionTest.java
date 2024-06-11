import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructionWithValidData() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), list);

        assertNotNull(distribution);
        assertEquals(3, distribution.singletons.size());
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructionWithNegativeProbability() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, -0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, 0.4));

        new DiscreteDistribution<>(new Well19937c(), list);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructionWithZeroProbabilities() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, 0.35));
        list.add(new Pair<>(4, 0));

        new DiscreteDistribution<>(new Well19937c(), list);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructionWithInfiniteProbability() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, Double.POSITIVE_INFINITY));

        new DiscreteDistribution<>(new Well19937c(), list);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), list);

        double probability = distribution.probability(1);
        assertEquals(0.25, probability, 0.0001);
        probability = distribution.probability(2);
        assertEquals(0.35, probability, 0.0001);
        probability = distribution.probability(3);
        assertEquals(0.4, probability, 0.0001);
        probability = distribution.probability(4);
        assertEquals(0, probability, 0.0001);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), list);

        int sample = distribution.sample().intValue();
        assertEquals(1, sample);
        sample = distribution.sample().intValue();
        assertEquals(2, sample);
        sample = distribution.sample().intValue();
        assertEquals(3, sample);
    }

    @Test
    public void testSampleArrayMethod() {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.25));
        list.add(new Pair<>(2, 0.35));
        list.add(new Pair<>(3, 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), list);

        int[] samples = distribution.sample(5);

        for (int i = 0; i < samples.length; i++) {
            int sample = samples[i];
            assertEquals(1, sample, i);
            sample = samples[i + 1];
            assertEquals(2, sample, i);
            sample = samples[i + 2];
            assertEquals(3, sample, i);
        }
    }

}