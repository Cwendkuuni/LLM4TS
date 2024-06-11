import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Probability test
        assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.01);
        assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.01);
        assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.01);

        // Sample test
        List<Integer> sampleList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            sampleList.add(distribution.sample().intValue());
        }

        double sum = 0;
        for (int value : sampleList) {
            sum += distribution.probability(Integer.valueOf(value));
        }

        assertEquals(1.0, sum, 0.01);

        // GetSamples test
        List<Pair<Integer, Double>> getSamples = distribution.getSamples();
        assertEquals(samples.size(), getSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), getSamples.get(i).getKey());
            assertEquals(samples.get(i).getValue(), getSamples.get(i).getValue(), 0.01);
        }

        // Sample with size test
        Integer[] sampleSize = new Integer[10];
        distribution.sample(sampleSize.length, sampleSize);

        sum = 0;
        for (Integer value : sampleSize) {
            sum += distribution.probability(value);
        }

        assertEquals(1.0, sum, 0.01);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.3));
        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitySum() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.1));
        samples.add(new Pair<>(Integer.valueOf(3), -0.2));
        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgmentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testEmptyList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        new DiscreteDistribution<>(new ArrayList<>());
    }

    @Test
    public void testNullSingleton() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.3));
        try {
            new DiscreteDistribution<>(samples);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("singleton"));
        }
    }

    @Test
    public void testNullSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        try {
            new DiscreteDistribution<>(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("samples"));
        }
    }

    @Test
    public void testReSeed() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        distribution.reseedRandomGenerator(123456789);

        List<Integer> sampleList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            sampleList.add(distribution.sample().intValue());
        }

        distribution.reseedRandomGenerator(987654321);
        List<Integer> sampleList2 = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            sampleList2.add(distribution.sample().intValue());
        }

        assertNotSame(sampleList, sampleList2);
    }
}