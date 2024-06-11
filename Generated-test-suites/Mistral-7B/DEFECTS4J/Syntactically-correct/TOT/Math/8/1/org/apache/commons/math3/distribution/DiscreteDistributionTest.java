import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(3, distribution.singletons.size());
            assertEquals(0.3, distribution.probabilities[0], 0.001);
            assertEquals(0.4, distribution.probabilities[1], 0.001);
            assertEquals(0.3, distribution.probabilities[2], 0.001);
        } catch (MathIllegalArgumentException | NotPositiveException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityValuesSumToOne() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(1.0, distribution.probabilities.length, 0.0);
            assertEquals(1.0, MathArrays.sum(distribution.probabilities), 0.001);
        } catch (MathIllegalArgumentException | NotPositiveException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityNegativeValuesThrowsException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            fail("Expected an exception for negative probabilities.");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test
    public void testProbabilityInfValuesThrowsException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            fail("Expected an exception for infinite probabilities.");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testProbabilityNullValuesAreIgnored() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, null));
        samples.add(new Pair<>(4, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(4, distribution.singletons.size());
            assertEquals(0.3, distribution.probabilities[3], 0.001);
        } catch (MathIllegalArgumentException | NotPositiveException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSampleFromDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            int sample = 0;
            for (int i = 0; i < 1000; i++) {
                sample = distribution.sample().intValue();
            }

            double expectedProbability = 0.3;
            int numberOfOccurrences = 0;

            for (int i = 0; i < distribution.singletons.size(); i++) {
                if (distribution.singletons.get(i).equals(sample)) {
                    numberOfOccurrences++;
                }
            }

            assertEquals(expectedProbability, (double) numberOfOccurrences / 1000, 0.001);
        } catch (NotStrictlyPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            List<Pair<Integer, Double>> result = distribution.getSamples();

            assertEquals(3, result.size());
            for (int i = 0; i < 3; i++) {
                assertEquals(distribution.singletons.get(i), result.get(i).getKey());
                assertEquals(distribution.probabilities[i], result.get(i).getValue(), 0.001);
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples, new Well19937c());

            long seed = 123456;
            distribution.reseedRandomGenerator(seed);

            for (int i = 0; i < 10; i++) {
                int sample = distribution.sample().intValue();
                System.out.println(sample);
            }

            seed = 7891011;
            distribution.reseedRandomGenerator(seed);

            for (int i = 0; i < 10; i++) {
                int sample = distribution.sample().intValue();
                System.out.println(sample);
            }
        } catch (NotPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}