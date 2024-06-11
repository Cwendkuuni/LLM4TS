import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() throws Exception {
        // Test case 1: Basic construction
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.3, distribution.probabilities[0], 0.001);
        assertEquals(0.4, distribution.probabilities[1], 0.001);
        assertEquals(0.3, distribution.probabilities[2], 0.001);

        // Test case 2: Reseed random generator
        long seed = 12345;
        Well19937c rng = new Well19937c(seed);
        DiscreteDistribution<Integer> distribution2 = new DiscreteDistribution<>(rng, samples);
        distribution2.reseedRandomGenerator(123456);

        // Test case 3: Probability calculation
        int x = 1;
        assertEquals(0.3, distribution.probability(x), 0.001);

        // Test case 4: Get samples
        List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
        assertEquals(3, samplesFromDistribution.size());
        assertEquals(0.3, samplesFromDistribution.get(0).getValue(), 0.001);
        assertEquals(0.4, samplesFromDistribution.get(1).getValue(), 0.001);
        assertEquals(0.3, samplesFromDistribution.get(2).getValue(), 0.001);

        // Test case 5: Sample
        int sample = distribution.sample();
        assertTrue(contains(distribution.singletons, sample));

        // Test case 6: Sample array
        int sampleSize = 5;
        T[] sampleArray = distribution.sample(sampleSize);
        for (int i = 0; i < sampleSize; i++) {
            assertTrue(contains(distribution.singletons, sampleArray[i]));
        }
    }

    private boolean contains(List<Integer> list, int value) {
        for (Integer integer : list) {
            if (integer.equals(value)) {
                return true;
            }
        }
        return false;
    }
}