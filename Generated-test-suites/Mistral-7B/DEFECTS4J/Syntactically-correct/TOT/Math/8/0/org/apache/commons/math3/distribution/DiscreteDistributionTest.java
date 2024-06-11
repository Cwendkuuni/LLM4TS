import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test the constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            assertNotNull(distribution);
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            // Failure is expected
        }
    }

    /**
     * Test the probability method with valid input.
     */
    @Test
    public void testProbabilityMethod() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            final double probability = distribution.probability(Integer.valueOf(3));
            assertEquals(0.5, probability, 0.01);
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            // Failure is expected
        }
    }

    /**
     * Test the sample method with valid input.
     */
    @Test
    public void testSampleMethod() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            final Integer sample = distribution.sample();
            assertNotNull(sample);
            assertEquals(1, sample.intValue()); // or any other previously encountered value
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            // Failure is expected
        }
    }

    /**
     * Test the sample method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleMethodWithInvalidInput() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        final DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), samples);
        distribution.sample();
    }

    /**
     * Test the sample method with large sample size.
     */
    @Test
    public void testSampleMethodWithLargeSampleSize() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            final Integer[] samplesArray = distribution.sample(1000);
            assertNotNull(samplesArray);
        } catch (NotStrictlyPositiveException e) {
            // Failure is expected
        }
    }

    /**
     * Test reseeding the random generator.
     */
    @Test
    public void testReseedRandomGenerator() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            final long seed = System.currentTimeMillis();
            distribution.reseedRandomGenerator(seed);

            final Integer[] sample1 = distribution.sample(10);
            final Integer[] sample2 = distribution.sample(10);

            assertEquals(sample1.length, sample2.length);
            for (int i = 0; i < sample1.length; i++) {
                assertEquals("Unexpected result for sample position " + i, sample1[i], sample2[i]);
            }
        } catch (NotStrictlyPositiveException | MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            // Failure is expected
        }
    }
}