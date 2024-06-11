import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;
    private RandomGenerator random;

    /**
     * Initialize random generator and samples for testing.
     */
    @Before
    public void setUp() {
        random = new Well19937c();
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
    }

    /**
     * Test constructor with valid samples.
     */
    @Test
    public void testConstructorValidSamples() {
        try {
            distribution = new DiscreteDistribution<>(samples);
            Assert.assertNotNull(distribution);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test constructor with invalid samples (negative probabilities).
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSamplesNegativeProbabilities() {
        samples.get(0).setValue(-0.1);
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotStrictlyPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test constructor with invalid samples (sum of probabilities equals to zero).
     */
    @Test(expected = MathArithmeticException.class)
    public void testConstructorInvalidSamplesZeroProbabilities() {
        samples.get(0).setValue(0.0);
        samples.get(1).setValue(1.0);
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotStrictlyPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test constructor with invalid samples (infinitive probability).
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInvalidSamplesInfProbability() {
        samples.get(0).setValue(Double.POSITIVE_INFINITY);
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotStrictlyPositiveException | MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        try {
            distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.3, distribution.probability(1), 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test probability method with invalid input (null input).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNullInput() {
        try {
            distribution.probability(null);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException | IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test probability method with invalid input (not existing value).
     */
    @Test
    public void testProbabilityNotExistingValue() {
        try {
            distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.0, distribution.probability(4), 0.001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        try {
            distribution = new DiscreteDistribution<>(samples);
            List<Pair<Integer, Double>> result = distribution.getSamples();
            Assert.assertEquals(samples, result);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test sample method with valid input (single sample).
     */
    @Test
    public void testSampleValidInputSingleSample() {
        try {
            distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(1, distribution.sample().intValue());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test sample method with valid input (multiple samples).
     */
    @Test
    public void testSampleValidInputMultipleSamples() {
        try {
            distribution = new DiscreteDistribution<>(samples);
            T[] result = distribution.sample(5);
            for (int i = 0; i < result.length; i++) {
                Assert.assertEquals(1 + i, result[i].intValue());
            }
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test sample method with invalid input (negative sample size).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidInputNegativeSampleSize() {
        try {
            distribution.sample(-1);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}