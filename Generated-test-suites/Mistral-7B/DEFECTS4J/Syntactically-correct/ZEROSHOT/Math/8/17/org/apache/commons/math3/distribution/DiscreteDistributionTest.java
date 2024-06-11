import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            assertEquals(3, distribution.singletons.size());
            assertEquals(0.3, distribution.probabilities[0], 0.001);
            assertEquals(0.4, distribution.probabilities[1], 0.001);
            assertEquals(0.3, distribution.probabilities[2], 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));

        try {
            new DiscreteDistribution<>(rng, samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testSumOfProbabilitiesZero() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), -0.4));

        try {
            new DiscreteDistribution<>(rng, samples);
            fail("Expected MathArithmeticException");
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));

        try {
            new DiscreteDistribution<>(rng, samples);
            fail("Expected MathIllegalArgumentException");
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testProbability() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.001);
            assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.001);
            assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.001);
            assertEquals(0, distribution.probability(Integer.valueOf(4)), 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSamples() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            List<Pair<Integer, Double>> result = distribution.getSamples();
            assertEquals(samples.size(), result.size());
            for (int i = 0; i < samples.size(); i++) {
                assertEquals(samples.get(i).getKey(), result.get(i).getKey());
                assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            Integer result = distribution.sample();
            List<Integer> expectedValues = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                expectedValues.add(distribution.sample().intValue());
            }
            assertArrayEquals(expectedValues.stream().mapToInt(i -> i).toArray(), new int[expectedValues.size()]
                    , "Samples do not match expected values");
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSampleArray() {
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            Integer[] result = distribution.sample(1000);
            List<Integer> expectedValues = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                expectedValues.add(distribution.sample().intValue());
            }
            assertArrayEquals(expectedValues.toArray(new Integer[expectedValues.size()]), result,
                    "Samples do not match expected values");
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}