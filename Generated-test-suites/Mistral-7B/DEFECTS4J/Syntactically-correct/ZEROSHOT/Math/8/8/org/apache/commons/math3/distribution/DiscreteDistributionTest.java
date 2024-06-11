import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscreteDistributionTest {

    @org.junit.Test
    public void testConstructionWithNullList() {
        //Test case for passing null list to constructor
        try {
            new DiscreteDistribution<Integer>(null);
            fail("Expected exception, but none was thrown.");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @org.junit.Test
    public void testConstructionWithEmptyList() {
        //Test case for passing empty list to constructor
        try {
            new DiscreteDistribution<Integer>(Collections.emptyList());
            fail("Expected exception, but none was thrown.");
        } catch (NotPositiveException e) {
            //Expected exception for empty list, as probabilities sum to 0
        }
    }

    @org.junit.Test
    public void testConstructionWithInvalidProbability() {
        //Test case for passing list with invalid probability
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 1.5));
        try {
            new DiscreteDistribution<Integer>(new Well19937c(), list);
            fail("Expected exception, but none was thrown.");
        } catch (MathIllegalArgumentException e) {
            //Expected exception
        }
    }

    @org.junit.Test
    public void testConstructionWithInvalidNegativeProbability() {
        //Test case for passing list with negative probability
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, -1.0));
        try {
            new DiscreteDistribution<Integer>(new Well19937c(), list);
            fail("Expected exception, but none was thrown.");
        } catch (NotPositiveException e) {
            //Expected exception
        }
    }

    @org.junit.Test
    public void testConstructionWithZeroProbability() {
        //Test case for passing list with zero probability
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.0));
        try {
            new DiscreteDistribution<Integer>(new Well19937c(), list);
            fail("Expected exception, but none was thrown.");
        } catch (MathArithmeticException e) {
            //Expected exception
        }
    }

    @org.junit.Test
    public void testProbabilityWithValidInput() {
        //Test case for probability method with valid input
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        double actual = distribution.probability(1);
        double expected = 0.3;
        assertEquals(expected, actual, 0.001);
    }

    @org.junit.Test
    public void testProbabilityWithInvalidInput() {
        //Test case for probability method with invalid input
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        double actual = distribution.probability(4);
        assertEquals(0, actual, 0.001);
    }

    @org.junit.Test
    public void testGetSamples() {
        //Test case for getSamples method
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(3, samples.size());
        for (Pair<Integer, Double> pair : samples) {
            assertNotNull(pair.getKey());
            assertNotNull(pair.getValue());
        }
    }

    @org.junit.Test
    public void testSample() {
        //Test case for sample method
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        Integer actual = distribution.sample();
        Integer expected = 1;
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testSampleWithMultipleCalls() {
        //Test case for sample method with multiple calls
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();
        List<Integer> samplesList = new ArrayList<>();
        samplesList.add(sample1);
        samplesList.add(sample2);
        samplesList.add(sample3);

        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(samples.size(), samplesList.size());
        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> pair = samples.get(i);
            Integer expected = samplesList.get(i);
            assertEquals(expected, pair.getKey());
        }
    }

    @org.junit.Test
    public void testSampleWithSameElementMultipleTimes() {
        //Test case for sample method with same element multiple times
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(1, 0.4));
        list.add(new Pair<>(2, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();

        List<Integer> samplesList = new ArrayList<>();
        samplesList.add(sample1);
        samplesList.add(sample2);
        samplesList.add(sample3);

        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(samples.size(), samplesList.size());
        int count = 0;
        for (Pair<Integer, Double> pair : samples) {
            if (pair.getKey().equals(sample1)) {
                count++;
            }
        }
        assertEquals(2, count);
    }

    @org.junit.Test
    public void testSampleWithDifferentElements() {
        //Test case for sample method with different elements
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();
        Integer sample4 = distribution.sample();

        List<Integer> samplesList = new ArrayList<>();
        samplesList.add(sample1);
        samplesList.add(sample2);
        samplesList.add(sample3);
        samplesList.add(sample4);

        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(samples.size(), samplesList.size());
        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> pair = samples.get(i);
            boolean isSame = false;
            for (Integer sample : samplesList) {
                if (pair.getKey().equals(sample)) {
                    isSame = true;
                    break;
                }
            }
            assertFalse(isSame);
        }
    }

    @org.junit.Test
    public void testSampleSize() {
        //Test case for sample method with sample size
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        T[] actual = distribution.sample(3);
        T[] expected = (T[]) new Integer[3];
        for (int i = 0; i < 3; i++) {
            expected[i] = i + 1;
        }
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeLessThanZero() {
        //Test case for sample method with sample size less than zero
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        distribution.sample(-1);
    }

    @org.junit.Test
    public void testReseedRandomGenerator() {
        //Test case for reseedRandomGenerator method
        List<Pair<Integer, Double>> list = new ArrayList<>();
        list.add(new Pair<>(1, 0.3));
        list.add(new Pair<>(2, 0.2));
        list.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), list);
        long seed = 1234L;
        distribution.reseedRandomGenerator(seed);
        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();
        List<Integer> samplesList = new ArrayList<>();
        samplesList.add(sample1);
        samplesList.add(sample2);
        samplesList.add(sample3);

        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(samples.size(), samplesList.size());
        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> pair = samples.get(i);
            Integer expected = samplesList.get(i);
            assertEquals(expected, pair.getKey());
        }
    }
}