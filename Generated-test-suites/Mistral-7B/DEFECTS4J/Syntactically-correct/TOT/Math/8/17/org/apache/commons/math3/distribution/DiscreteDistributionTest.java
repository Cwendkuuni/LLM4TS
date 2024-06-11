import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testEmptyDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<>(samples);
            fail("Expected IllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        assertNull(dist);
    }

    @Test
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, -0.1));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<>(samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
        assertNull(dist);
    }

    @Test
    public void testZeroProbabilitySum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.1));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<>(samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
        assertNull(dist);
    }

    @Test
    public void testInfProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, Double.POSITIVE_INFINITY));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<>(samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        assertNull(dist);
    }

    @Test
    public void testEmptySingletons() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        assertEquals(0, dist.singletons.size());
    }

    @Test
    public void testSingletonDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 1.0));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        assertEquals(1, dist.singletons.size());
        assertEquals(1, dist.probabilities.length);
        assertEquals(1.0, dist.probabilities[0], 0.0);
        assertEquals(1, dist.probability(1));

        T[] sample = dist.sample(10);
        for (int i = 0; i < sample.length; i++) {
            assertEquals(1, sample[i].intValue());
        }
    }

    @Test
    public void testMultipleSingletons() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        assertEquals(3, dist.singletons.size());
        assertEquals(3, dist.probabilities.length);
        assertEquals(0.2, dist.probabilities[0], 0.001);
        assertEquals(0.3, dist.probabilities[1], 0.001);
        assertEquals(0.5, dist.probabilities[2], 0.001);

        assertEquals(0.2, dist.probability(1), 0.001);
        assertEquals(0.3, dist.probability(2), 0.001);
        assertEquals(0.5, dist.probability(3), 0.001);

        T[] sample = dist.sample(100);
        int[] sampleInt = new int[sample.length];
        for (int i = 0; i < sample.length; i++) {
            sampleInt[i] = sample[i].intValue();
        }
        int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 10 instances of 1
                1, 2, 1, 3, 1, 1, 1, 1, 2, 3}; // 10 instances of each of the other values
        assertArrayEquals(expected, sampleInt);
    }

    @Test
    public void testRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.4));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new Well19937c(), samples);

        T[] sample = dist.sample(10000);
        int freq[] = new int[2];
        for (int i = 0; i < sample.length; i++) {
            int value = sample[i].intValue();
            freq[value]++;
        }
        int total = sample.length;

        double relativeError = 0.05; // 5%
        for (int i = 0; i < 2; i++) {
            double expected = (double) freq[i] / total;
            assertEquals(expected, dist.probability(i), relativeError);
        }
    }

    @Test
    public void testReSeed() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.4));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new Well19937c(), samples);

        dist.reseedRandomGenerator(12345);
        T[] sample = dist.sample(10000);
        int freq[] = new int[2];
        for (int i = 0; i < sample.length; i++) {
            int value = sample[i].intValue();
            freq[value]++;
        }
        int total = sample.length;

        double relativeError = 0.05; // 5%
        for (int i = 0; i < 2; i++) {
            double expected = (double) freq[i] / total;
            assertEquals(expected, dist.probability(i), relativeError);
        }

        dist.reseedRandomGenerator(67890);
        sample = dist.sample(10000);
        freq = new int[2];
        for (int i = 0; i < sample.length; i++) {
            int value = sample[i].intValue();
            freq[value]++;
        }
        total = sample.length;

        for (int i = 0; i < 2; i++) {
            assertEquals(expected, dist.probability(i), relativeError);
        }
    }
}