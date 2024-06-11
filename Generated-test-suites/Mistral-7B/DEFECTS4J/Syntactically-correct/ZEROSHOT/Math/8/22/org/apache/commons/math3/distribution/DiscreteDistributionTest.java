import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
        assertNotNull(discreteDistribution);

        assertEquals(4, discreteDistribution.singletons.size());
        assertEquals(4, discreteDistribution.probabilities.length);

        for (int i = 0; i < discreteDistribution.singletons.size(); i++) {
            assertEquals(discreteDistribution.singletons.get(i), discreteDistribution.singletons.get(i));
            assertEquals(discreteDistribution.probabilities[i], discreteDistribution.probabilities[i], 0.001);
        }

        // Check probabilities sum to 1.
        double sum = 0.0;
        for (double probability : discreteDistribution.probabilities) {
            sum += probability;
        }
        assertEquals(1.0, sum, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructionNegativeProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructionZeroProbabilities() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructionInfiniteProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() throws Exception {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        // Test valid inputs.
        assertEquals(0.1, discreteDistribution.probability((Integer) 1), 0.01);
        assertEquals(0.3, discreteDistribution.probability((Integer) 2), 0.01);
        assertEquals(0.4, discreteDistribution.probability((Integer) 3), 0.01);
        assertEquals(0.2, discreteDistribution.probability((Integer) 4), 0.01);

        // Test invalid inputs.
        assertNull(discreteDistribution.probability(null));
    }

    @Test
    public void testGetSamples() throws Exception {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> samplesList = discreteDistribution.getSamples();

        assertEquals(4, samplesList.size());

        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> pair = samples.get(i);
            Pair<Integer, Double> pairInList = samplesList.get(i);

            assertEquals(pair.getKey(), pairInList.getKey());
            assertEquals(pair.getValue(), pairInList.getValue(), 0.01);
        }
    }

    @Test
    public void testSample() throws Exception {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Integer sample = discreteDistribution.sample();

        assertTrue(samples.contains(new Pair<>(sample, null)));
    }

    @Test
    public void testSampleArray() throws Exception {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Integer[] sampleArray = discreteDistribution.sample(3);

        assertEquals(3, sampleArray.length);

        for (Integer sample : sampleArray) {
            assertTrue(samples.contains(new Pair<>(sample, null)));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws Exception {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        discreteDistribution.sample(-1);
    }

    @Test
    public void testReseedRandomGenerator() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.2));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);

        long seed = 1234567890L;
        discreteDistribution.reseedRandomGenerator(seed);

        Integer sample = discreteDistribution.sample();
        assertNotNull(sample);

        // Test reseeding again with the same seed should produce the same sample.
        discreteDistribution.reseedRandomGenerator(seed);
        Integer sample2 = discreteDistribution.sample();
        assertEquals(sample, sample2);
    }
}