package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Unexpected number of singletons", 3, distribution.singletons.size());
        assertEquals("Unexpected number of probabilities", 3, distribution.probabilities.length);
        assertEquals("Unexpected value of first singleton", (Integer)samples.get(0).getKey(), distribution.singletons.get(0));
        assertEquals("Unexpected value of first probability", 0.3, distribution.probabilities[0], 0.01);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionNegativeProbability() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = MathArithmeticException.class)
    public void testInvalidConstructionSumToZeroProbabilities() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));
        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidConstructionInfiniteProbability() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbabilityValid() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Unexpected value of probability for null singleton", 0, distribution.probability(null));
        assertEquals("Unexpected value of probability for first singleton", 0.3, distribution.probability((Integer)distribution.singletons.get(0)), 0.01);
        assertEquals("Unexpected value of probability for second singleton", 0.4, distribution.probability((Integer)distribution.singletons.get(1)), 0.01);
        assertEquals("Unexpected value of probability for third singleton", 0.3, distribution.probability((Integer)distribution.singletons.get(2)), 0.01);
    }

    /**
     * Test probability method with invalid arguments.
     */
    @Test
    public void testProbabilityInvalid() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNull("Unexpected value of probability for null", distribution.probability(null));
        assertNotEquals("Unexpected value of probability for non-singleton", 0.3, distribution.probability(2), 0.01);
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();

        assertEquals("Unexpected size of samples", 3, expectedSamples.size());

        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals("Unexpected singleton in samples", distribution.singletons.get(i), expectedSamples.get(i).getKey());
            assertEquals("Unexpected probability in samples", distribution.probabilities[i], expectedSamples.get(i).getValue(), 0.01);
        }
    }

    /**
     * Test sample method with valid arguments.
     */
    @Test
    public void testSampleValid() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Integer sample = distribution.sample();

        assertTrue("Unexpected value of sample", samples.contains(new Pair<>(sample, null)));
    }

    /**
     * Test sample method with invalid arguments.
     */
    @Test
    public void testSampleInvalid() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        distribution.reseedRandomGenerator(-1);

        // This is not really a test, but just a sanity check to ensure that the sample method is returning a valid object.
        Integer sample = distribution.sample();
        assertNotNull(sample);
    }

    /**
     * Test sample method with valid arguments multiple times.
     */
    @Test
    public void testSampleValidMultiple() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Set<Integer> samplesSet = new HashSet<>(50);

        for (int i = 0; i < 50; i++) {
            samplesSet.add(distribution.sample());
        }

        assertEquals("Unexpected size of sample set", 3, samplesSet.size());

        for (int singleton : distribution.singletons) {
            assertTrue("Unexpected absence of singleton in sample set", samplesSet.contains(singleton));
        }
    }

    /**
     * Test sample method with valid arguments and check if probabilities match.
     */
    @Test
    public void testSampleProbabilitiesMatch() {
        final int sampleSize = 100000;
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        int[] occurrences = new int[3];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample().intValue();
            occurrences[sample]++;
        }

        double total = sampleSize * 1.0;
        double sum = 0;

        for (int singleton : distribution.singletons) {
            sum += distribution.probability(singleton) * total;
            assertEquals("Unexpected number of occurrences for " + singleton, occurrences[singleton], (int)Math.round(sum), 1);
            total -= occurrences[singleton];
        }

        // Check if total occurrences match total samples
        assertEquals("Unexpected total occurrences", sampleSize, Precision.round(sum, 100000 * 1.0e-6));
    }

    /**
     * Test sample method with large sample size and check if probabilities match.
     */
    @Test
    public void testSampleProbabilitiesMatchLargeSampleSize() {
        final int sampleSize = 1000000;
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        int[] occurrences = new int[3];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample().intValue();
            occurrences[sample]++;
        }

        double total = sampleSize * 1.0;
        double sum = 0;

        for (int singleton : distribution.singletons) {
            sum += distribution.probability(singleton) * total;
            assertEquals("Unexpected number of occurrences for " + singleton, occurrences[singleton], (int)Math.round(sum), 1000000);
            total -= occurrences[singleton];
        }

        // Check if total occurrences match total samples
        assertEquals("Unexpected total occurrences", sampleSize, Precision.round(sum, 1000000 * 1.0e-6));
    }

    /**
     * Test sample method with invalid arguments (negative sample size).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidNegativeSampleSize() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // This is expected to fail with NotStrictlyPositiveException
        distribution.sample(-1);
    }

    /**
     * Test sample method with valid arguments and check if sampling from reseeded distribution matches original distribution.
     */
    @Test
    public void testSampleReseeded() {
        final int sampleSize = 1000;
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        int[] occurrences = new int[3];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample().intValue();
            occurrences[sample]++;
        }

        double total = sampleSize * 1.0;
        double sum = 0;

        for (int singleton : distribution.singletons) {
            sum += distribution.probability(singleton) * total;
            assertEquals("Unexpected number of occurrences for " + singleton, occurrences[singleton], (int)Math.round(sum), 1);
            total -= occurrences[singleton];
        }

        distribution.reseedRandomGenerator(12345);

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample().intValue();
            occurrences[sample]++;
        }

        sum = 0;

        for (int singleton : distribution.singletons) {
            sum += distribution.probability(singleton) * total;
            assertEquals("Unexpected number of occurrences for " + singleton, occurrences[singleton], (int)Math.round(sum), 1);
            total -= occurrences[singleton];
        }

        // Check if total occurrences match total samples
        assertEquals("Unexpected total occurrences", sampleSize, Precision.round(sum, sampleSize * 1.0e-6));
    }
}