import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(4, distribution.singletons.size());
            assertEquals(0.1, distribution.probability(1), 0.001);
            assertEquals(0.3, distribution.probability(2), 0.001);
            assertEquals(0.4, distribution.probability(3), 0.001);
            assertEquals(0.2, distribution.probability(4), 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testDiscreteDistributionWithInvalidSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        distribution.sample(-1);
    }

    @Test
    public void testDiscreteDistributionSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        for (int i = 0; i < 10000; i++) {
            assertEquals("Unexpected value: " + distribution.sample(),
                         distribution.singletons.get((int) (Math.random() * distribution.singletons.size())),
                         distribution.sample());
        }
    }

    @Test
    public void testDiscreteDistributionSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        T[] sample = distribution.sample(5);
        for (int i = 0; i < sample.length; i++) {
            assertEquals("Unexpected value: " + sample[i],
                         distribution.singletons.get((int) (Math.random() * distribution.singletons.size())),
                         sample[i]);
        }
    }

    @Test
    public void testDiscreteDistributionProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        assertEquals(0.1, distribution.probability(1), 0.001);
        assertEquals(0.3, distribution.probability(2), 0.001);
        assertEquals(0.4, distribution.probability(3), 0.001);
        assertEquals(0.2, distribution.probability(4), 0.001);
        for (int i = 5; i < 10; i++) {
            assertEquals(0, distribution.probability((Integer) i), 0.001);
        }
    }

    @Test
    public void testDiscreteDistributionGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }
}